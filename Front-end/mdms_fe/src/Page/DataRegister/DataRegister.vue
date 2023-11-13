<template>
  <div class="data-register">
    <h2>SQL 테이블 생성</h2>
    <form @submit.prevent="registerTable">
      <div class="input-group">
        <label for="tableName">테이블 이름 입력:</label>
        <input type="text" id="tableName" v-model="tableName" />
      </div>

      <!-- 컬럼을 동적으로 추가하는 부분 -->
      <div class="columns">
        <div class="column" v-for="(column, index) in columns" :key="index">
          <!-- 검색어 입력 필드 -->
          <input type="text" v-model="column.name" @input="searchColumnName(index)" placeholder="컬럼 이름" />
          <!-- 데이터 타입 선택 드롭다운, 서버에서 가져온 데이터 타입으로 채워짐 -->
          <select v-model="column.type">
            <option v-for="type in dataTypes" :key="type" :value="type">{{ type }}</option>
          </select>
          <!-- 컬럼 제거 버튼 -->
          <button type="button" @click="removeColumn(index)">컬럼 제거</button>
          
          <!-- 검색 결과 표시, 검색된 표준 용어와 해당 설명을 표시 -->
          <ul class="search-results" v-if="searchResults[index]">
            <li v-for="result in searchResults[index]" :key="result.no" @click="selectColumnName(result, index)">
              {{ result.standardTerminology }} - {{ result.description }}, {{ result.domain.dataType }}
            </li>
          </ul>
        </div>
      </div>

      <!-- 컬럼 추가 버튼 -->
      <button type="button" @click="addColumn">컬럼 추가</button>

      <!-- SQL DDL 생성 및 등록 버튼 -->
      <div class="actions">
        <button type="button" @click="generateCreateStatement">SQL DDL 생성</button>
        <button type="submit">등록하기</button>
      </div>
    </form>
  </div>
</template>



<script>
import axios from 'axios';

export default {
  data() {
    return {
      tableName: '', // 테이블 이름을 위한 반응형 데이터
      columns: [{ name: '', type: 'INT' }], // 사용자가 추가하는 컬럼들을 위한 배열
      searchResults: {}, // 각 컬럼 검색 결과를 저장하는 객체
      serverData: [], // 서버로부터 가져온 도메인 데이터
      dataTypes: [], // 드롭다운 메뉴에 사용될 데이터 타입 목록
    };
  },
  created() {
    this.fetchServerData(); // 컴포넌트 생성 시 서버 데이터를 가져옴
  },
  methods: {
    // 서버 데이터를 가져오는 메소드
    async fetchServerData() {
      try {
        const token = localStorage.getItem('token'); // 로컬 스토리지에서 토큰을 가져옴
        const response = await axios.get('http://localhost:3001/terminology', {
          headers: { 'Authorization': `Bearer ${token}` } // 토큰을 Authorization 헤더에 추가
        });
        this.serverData = response.data.data; // 서버 데이터를 저장
        this.extractDataTypes(); // 데이터 타입을 추출하여 dataTypes 배열을 생성
      } catch (error) {
        alert('데이터를 불러오는 중 에러가 발생했습니다.');
        this.goBack(); // 에러 발생 시 이전 페이지로 이동
      }
    },
    // 이전 페이지로 이동하는 메소드
    goBack() {
      window.history.back();
    },
    // 새 컬럼을 추가하는 메소드
    addColumn() {
      this.columns.push({ name: '', type: 'INT' });
    },
    // 특정 인덱스의 컬럼을 제거하는 메소드
    removeColumn(index) {
      this.columns.splice(index, 1);
    },
    // 컬럼 이름에 따라 서버 데이터를 검색하는 메소드
    searchColumnName(index) {
      const searchQuery = this.columns[index].name;
      const filteredResults = this.serverData.filter(item =>
        item.standardTerminology.includes(searchQuery)
      );
      this.searchResults = { ...this.searchResults, [index]: filteredResults };
    },
    // 검색된 결과를 선택하여 컬럼 이름과 타입을 설정하는 메소드
    selectColumnName(result, columnIndex) {
      this.columns[columnIndex].name = result.standardTerminology;
      this.columns[columnIndex].type = this.formatDataType(result.domain.dataType);
      this.searchResults = {}; // 선택 후 검색 결과를 초기화
    },
    // 서버 데이터에 따라 적절한 데이터 타입 문자열을 생성하는 메소드
    formatDataType(dataType) {
      if (dataType.toUpperCase() === 'CHAR') {
        return 'CHAR(255)';
      }
      return dataType.toUpperCase();
    },
    // SQL CREATE TABLE 문을 생성하는 메소드
    generateCreateStatement() {
      let createStatement = `CREATE TABLE ${this.tableName} (`;
      createStatement += `No INT AUTO_INCREMENT PRIMARY KEY, `; // 기본 키 추가

      this.columns.forEach((column, index) => {
        const columnData = this.serverData.find(item => item.englishAbbreviation === column.name);
        if (columnData) {
          let dataTypeString = columnData.domain.dataType.toUpperCase();
          if (dataTypeString === 'NUMERIC' && columnData.domain.decimalPointLength !== "-") {
            // NUMERIC 타입에 대해 소수점 길이를 포함하여 타입 문자열 생성
            dataTypeString = `NUMERIC(${columnData.domain.dataLength}, ${columnData.domain.decimalPointLength})`;
          } else if (dataTypeString !== 'DATETIME') {
            // DATETIME을 제외한 다른 타입에 대해서는 일반적으로 데이터 길이를 포함
            dataTypeString = `${dataTypeString}(${columnData.domain.dataLength})`;
          }
          
          createStatement += `${columnData.englishAbbreviation} ${dataTypeString}`;
          if (index < this.columns.length - 1) {
            createStatement += ', ';
          }
        }
      });

      createStatement += ');';
      console.log(createStatement);
    },
    // 폼을 초기화하는 메소드
    resetForm() {
      this.tableName = '';
      this.columns = [{ name: '', type: 'INT' }];
      this.searchResults = {};
    },
    // 서버 데이터로부터 데이터 타입을 추출하여 dataTypes 배열에 저장하는 메소드
    extractDataTypes() {
      const typesSet = new Set();
      this.serverData.forEach(item => {
        typesSet.add(item.domain.dataType); // dataType을 Set에 추가하여 고유한 값을 확보
      });
      this.dataTypes = Array.from(typesSet); // Set을 배열로 변환하여 dataTypes에 할당
    },
  },
};
</script>

<style scoped>
.data-register {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.data-register h2 {
  text-align: center;
  color: #333;
  margin-bottom: 20px;
}

.input-group,
.columns,
.actions {
  margin-bottom: 15px;
}

.input-group label,
.column label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.input-group input[type="text"],
.column input[type="text"] {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.column select {
  width: 100%;
  padding: 8px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
}

.button {
  display: inline-block;
  padding: 10px 15px;
  margin-right: 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.button:hover {
  background-color: #0056b3;
}

.button[type="submit"] {
  background-color: #28a745;
}

.button[type="submit"]:hover {
  background-color: #218838;
}

.button[type="button"] {
  background-color: #dc3545;
}

.button[type="button"]:hover {
  background-color: #c82333;
}

.search-results ul {
  list-style: none;
  padding-left: 0;
}

.search-results li {
  padding: 8px;
  margin-bottom: 5px;
  background-color: #e9ecef;
  border-radius: 4px;
  cursor: pointer;
}

.search-results li:hover {
  background-color: #dee2e6;
}

</style>
