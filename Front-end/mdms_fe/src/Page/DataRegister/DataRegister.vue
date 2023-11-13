<template>
  <div class="data-register">
    <h2>SQL 테이블 생성</h2>
    <form @submit.prevent="generateAndRegisterTable">
      <!-- 물리적 테이블 이름 입력 -->
      <div class="input-group">
        <label for="physicalTableName">물리적 테이블 이름 입력:</label>
        <input type="text" id="physicalTableName" v-model="tableName" @input="validateTableName" />
      </div>

      <!-- 논리적 테이블 이름 입력 -->
      <div class="input-group">
        <label for="logicalTableName">논리적 테이블 이름 입력:</label>
        <input type="text" id="logicalTableName" v-model="logicalTableName" />
      </div>


      <div class="columns">
        <div class="column" v-for="(column, index) in columns" :key="index">
          <input type="text" v-model="column.name" @input="searchColumnName(index)" placeholder="컬럼 이름" />
          <div class="column-info" v-if="column.standardTerminology">
            <p>선택된 컬럼: {{ column.standardTerminology }}</p>
            <p>영어 약어: {{ column.name }}</p>
            <p>설명: {{ column.description }}</p>
          </div>
          <button type="button" @click="removeColumn(index)">컬럼 제거</button>
          <ul class="search-results" v-if="searchResults[index]">
            <li v-for="result in searchResults[index]" :key="result.no" @click="selectColumnName(result, index)">
              {{ result.domain.dataType }} - {{ result.standardTerminology }} - {{ result.englishAbbreviation }}: {{ result.description }}
            </li>
          </ul>
        </div>
      </div>

      <button type="button" @click="addColumn">컬럼 추가</button>

      <!-- 액션 버튼들 -->
      <div class="actions">
        <button type="button" @click="generateCreateStatement" :disabled="!tableName">SQL DDL 생성</button>
        <button type="submit" :disabled="!tableName || !logicalTableName || columns.length === 0">등록하기</button>
      </div>

      <!-- 생성된 SQL 쿼리 표시 -->
      <div v-if="createdSQL" class="created-sql">
        <h3>생성된 SQL 쿼리:</h3>
        <textarea v-model="createdSQL" readonly></textarea>
        <button type="button" @click="copyToClipboard">복사하기</button>
      </div>
    </form>
  </div>
</template>


<script>
import axios from 'axios';

export default {
  data() {
    return {
      tableName: '',
      logicalTableName: '',
      columns: [],
      searchResults: {},
      serverData: [],
      createdSQL: '',
      token: localStorage.getItem('token'), // 토큰을 로컬 스토리지에서 미리 가져옵니다.
      email: localStorage.getItem('email'), // 이메일을 로컬 스토리지에서 미리 가져옵니다.
    };
  },
  created() {
    this.fetchServerData();
  },
  methods: {
    async fetchServerData() {
      try {
        const token = localStorage.getItem('token'); // 로컬 스토리지에서 토큰 가져오기
        const response = await axios.get('http://localhost:3001/terminology', {
          headers: { 'Authorization': `Bearer ${token}` } // 토큰을 헤더에 포함시켜 요청
        });
        this.serverData = response.data.data; // 응답으로 받은 데이터를 serverData에 저장
      } catch (error) {
        alert('데이터를 불러오는 중 에러가 발생했습니다.'); // 에러 처리
        this.goBack(); // 에러 시 이전 페이지로 돌아가기
      }
    },
    // 이전 페이지로 돌아가는 함수
    goBack() {
      window.history.back();
    },
    // 새 컬럼을 배열에 추가하는 함수
    addColumn() {
      this.columns.push({ name: '', type: '', standardTerminology: '', description: '' });
    },
    // 특정 인덱스의 컬럼을 배열에서 제거하는 함수
    removeColumn(index) {
      this.columns.splice(index, 1);
    },
    // 컬럼 이름으로 서버 데이터를 검색하는 함수
    searchColumnName(index) {
      const searchQuery = this.columns[index].name.toLowerCase();
      this.searchResults[index] = this.serverData.filter(item =>
        item.standardTerminology.toLowerCase().includes(searchQuery)
      );
    },
    // 검색 결과를 선택하여 컬럼 정보를 업데이트하는 함수
    selectColumnName(result, index) {
      const dataType = this.formatDataType(result.domain.dataType);
      const dataLength = result.domain.dataLength;
      const decimalPointLength = result.domain.decimalPointLength;
      let typeFormat = dataType;

      // 데이터 타입이 NUMERIC이고 소수점 길이가 '-'가 아니라면 해당 포맷을 적용
      if (dataType === 'NUMERIC' && decimalPointLength !== '-') {
        typeFormat = `NUMERIC(${dataLength}, ${decimalPointLength})`;
      } else if (dataType === 'CHAR' || dataType === 'VARCHAR') {
        typeFormat = `${dataType}(${dataLength})`;
      }

      // columns 배열의 해당 인덱스에 컬럼 정보 업데이트
      this.columns[index] = {
        ...this.columns[index],
        no: result.no,
        degree: result.degree,
        name: result.englishAbbreviation,
        type: typeFormat,
        standardTerminology: result.standardTerminology,
        description: result.description,
        standardCode: result.standardCode,
        relevantOrganization: result.relevantOrganization,
        isCustom: result.isCustom,
        domain: result.domain // 이렇게 domain 전체를 저장해야 합니다.
      };


      // 검색 결과 초기화
      this.searchResults = {};
    },
    // 데이터 타입을 대문자로 변환하는 함수
    formatDataType(dataType) {
      return dataType.toUpperCase();
    },
    // CREATE TABLE 문을 생성하는 함수
    generateCreateStatement() {
      if (!this.tableName.trim() || this.columns.length === 0) {
        alert('테이블 이름을 입력하거나, 컬럼을 추가해주세요.');
        return;
      }

      let createStatement = `CREATE TABLE ${this.tableName} (`;
      createStatement += `No INT AUTO_INCREMENT PRIMARY KEY,`; // 테이블 기본 키 설정

      // columns 배열을 순회하면서 각 컬럼의 SQL 문을 생성
      this.columns.forEach((column, index) => {
        const columnData = this.serverData.find(item => item.englishAbbreviation === column.name);
        if (columnData) {
          let dataTypeString = this.getDataTypeString(columnData.domain);

          // 컬럼 정의 추가
          createStatement += `${column.name} ${dataTypeString}`;
          if (index < this.columns.length - 1) {
            createStatement += ','; // 마지막 컬럼이 아니라면 쉼표를 추가
          }
        } else {
          // 일치하는 데이터가 없는 경우 경고 출력
          console.warn(`No matching data found for column ${column.name}`);
        }
      });

      createStatement += ');'; // SQL 문 마무리
      this.createdSQL = createStatement; // 생성된 SQL 쿼리문을 data 모델의 createdSQL에 저장
    },
    copyToClipboard() {
      if (this.createdSQL) {
        navigator.clipboard.writeText(this.createdSQL)
          .then(() => {
            alert('클립보드에 복사되었습니다.');
          })
          .catch(err => {
            console.error('복사에 실패했습니다.', err);
          });
      }
    },
    // 데이터 타입 문자열을 생성하는 함수
    getDataTypeString(domain) {
      let dataTypeString = '';
      const { dataType, dataLength, decimalPointLength } = domain;

      if (dataType === 'NUMERIC') {
        dataTypeString = decimalPointLength !== '-' ? `NUMERIC(${dataLength}, ${decimalPointLength})` : `NUMERIC(${dataLength})`;
      } else {
        dataTypeString = dataType === 'DATETIME' ? dataType : `${dataType}(${dataLength})`;
      }

      return dataTypeString;
    },
    // 등록하기 버튼 클릭 시 호출되는 메소드
    async generateAndRegisterTable() {
      if (!this.tableName.trim() || !this.logicalTableName.trim()) {
        alert('테이블 이름과 논리적 테이블 이름을 입력해주세요.');
        return;
      }
      if (!/^[A-Za-z_]+$/.test(this.tableName.trim())) {
        alert('물리적 테이블 이름은 영문과 밑줄(_)만 포함할 수 있습니다.');
        return;
      }
      if (this.columns.length === 0) {
        alert('컬럼을 하나 이상 추가해주세요.');
        return;
      }

      // 서버로 전송할 데이터 생성
      const payload = {
        "email": this.email,
        "physicalTableName": this.tableName.trim(),
        "logicalTableName": this.logicalTableName.trim(),
        "query": this.createdSQL,
        "standardTerminologyList": this.columns.map(column => {
          return {
            "no": column.no,
            "degree": column.degree,
            "standardTerminology": column.standardTerminology,
            "description": column.description,
            "englishAbbreviation": column.englishAbbreviation,
            "domain": column.domain,
            "standardCode":column.standardCode,
            "relevantOrganization":column.relevantOrganization,
            "isCustom":column.isCustom,
          };
        }),
      };

      try {
        // 서버로 POST 요청 보내기
        const response = await axios.post('http://localhost:3001/table/create', payload, {
          headers: { Authorization: `Bearer ${this.token}` },
        });
        console.log(payload);

        // 성공적으로 데이터가 전송되면 응답 처리
        if (response.data.ok === 'ok') {
          alert(response.data.message);
          // 폼 초기화 및 기타 작업 수행
          this.resetForm();
        } else {
          // 서버에서 에러 메시지가 올 경우 사용자에게 알림
          alert(response.data.message);
        }
      } catch (error) {
        console.error('서버 통신 중 오류가 발생했습니다:', error);
        alert('서버 통신 중 오류가 발생했습니다.');
      }
    },

    // 폼을 초기화하는 메소드
    resetForm() {
      this.tableName = '';
      this.logicalTableName = '';
      this.columns = [];
      this.searchResults = {};
      this.createdSQL = '';
    },
  }
};
</script>


<style scoped>
.data-register {
  max-width: 800px;
  margin: 40px auto;
  padding: 40px;
  background-color: #ffffff;
  border-radius: 15px;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  border: 1px solid #e0e0e0;
}

.data-register h2 {
  text-align: center;
  color: #2c3e50;
  font-size: 24px;
  margin-bottom: 30px;
  padding-bottom: 10px;
  border-bottom: 2px solid #ecf0f1;
}

.input-group {
  margin-bottom: 20px;
}

.input-group label {
  display: block;
  margin-bottom: 10px;
  font-weight: 600;
  color: #34495e;
  font-size: 18px;
}

.input-group input[type="text"] {
  width: 100%;
  padding: 12px;
  margin-bottom: 15px;
  border: 1px solid #bdc3c7;
  border-radius: 5px;
  box-sizing: border-box;
  transition: border-color 0.3s;
}

.input-group input[type="text"]:focus {
  border-color: #3498db;
  outline: none;
}

.button {
  padding: 12px 20px;
  font-size: 16px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  transition: background-color 0.3s, transform 0.2s;
  border: none;
  border-radius: 5px;
  margin-right: 10px;
  cursor: pointer;
}

.button:not(:disabled):hover {
  background-color: #2980b9;
  transform: translateY(-2px);
}

.button:disabled {
  background-color: #7f8c8d;
}

.button:active {
  transform: translateY(0);
}

.search-results ul {
  padding-left: 0;
  margin-top: 10px;
}

.search-results li {
  padding: 10px;
  margin-bottom: 8px;
  background-color: #ecf0f1;
  border-radius: 5px;
  transition: background-color 0.3s;
  cursor: pointer;
}

.search-results li:hover {
  background-color: #d6dbdf;
}

.column-info {
  background-color: #f2f3f4;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 15px;
}

.created-sql {
  margin-top: 20px;
  background-color: #f2f3f4;
  padding: 15px;
  border-radius: 5px;
}

.created-sql h3 {
  margin-bottom: 10px;
}

.created-sql textarea {
  width: 100%;
  height: 150px;
  padding: 10px;
  border: 1px solid #bdc3c7;
  border-radius: 5px;
  margin-bottom: 10px;
}

.created-sql button {
  padding: 10px 15px;
  background-color: #27ae60;
  color: white;
  border-radius: 5px;
  cursor: pointer;
}

.created-sql button:hover {
  background-color: #229954;
}
</style>
