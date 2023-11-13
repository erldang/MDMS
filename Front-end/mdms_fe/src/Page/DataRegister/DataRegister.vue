<template>
  <div class="data-register">
    <h2>SQL 테이블 생성</h2>
    <form @submit.prevent="generateAndRegisterTable">
      <div class="input-group">
        <label for="tableName">테이블 이름 입력:</label>
        <input type="text" id="tableName" v-model="tableName" @input="validateTableName" />
      </div>

      <div class="columns">
        <div class="column" v-for="(column, index) in columns" :key="index">
          <input type="text" v-model="column.name" @input="searchColumnName(index)" placeholder="컬럼 이름" />
          <div class="column-info" v-if="column.standardTerminology">
            <p>선택된 컬럼: {{ column.standardTerminology }}</p>
            <p>영어 약어: {{ column.name }}</p>
            <p>데이터 타입: {{ column.domain.dataType }}</p>
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

      <div class="actions">
        <button type="button" @click="generateCreateStatement" :disabled="!tableName">SQL DDL 생성</button>
        <button type="submit" :disabled="!tableName || columns.length === 0">등록하기</button>
      </div>

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
      columns: [],
      searchResults: {},
      serverData: [],
      createdSQL: '', // 생성된 SQL 쿼리문을 저장하는 변수
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
        name: result.englishAbbreviation,
        type: typeFormat,
        standardTerminology: result.standardTerminology,
        description: result.description
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

      let createStatement = `CREATE TABLE ${this.tableName} (\n`;
      createStatement += `No INT AUTO_INCREMENT PRIMARY KEY,\n`;
      // 컬럼 정의 로직
      this.createdSQL = createStatement; // 생성된 SQL 쿼리문을 저장
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
    // 입력 폼을 초기화하는 함수
    resetForm() {
      this.tableName = '';
      this.columns = [];
      this.searchResults = {};
    }
  }
};
</script>


<!-- 커밋 테스트 주석 -->
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

.input-group label,
.column label {
  display: block;
  margin-bottom: 10px;
  font-weight: 600;
  color: #34495e;
}

.input-group input[type="text"],
.column input[type="text"],
.column select {
  width: 100%;
  padding: 12px;
  margin-bottom: 15px;
  border: 1px solid #bdc3c7;
  border-radius: 5px;
  box-sizing: border-box;
  transition: border-color 0.3s;
}

.input-group input[type="text"]:focus,
.column input[type="text"]:focus,
.column select:focus {
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
}

.button:hover {
  background-color: #2980b9;
  transform: translateY(-2px);
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
}

.search-results li:hover {
  background-color: #d6dbdf;
}
</style>
