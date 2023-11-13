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

          <!-- 데이터 타입 레이블, 검색을 통해 자동으로 설정됨 -->
          <label v-if="column.type">데이터 타입: {{ column.type }}</label>

          <!-- 컬럼 제거 버튼 -->
          <button type="button" @click="removeColumn(index)">컬럼 제거</button>
          
          <!-- 검색 결과 표시, 검색된 표준 용어와 해당 설명을 표시 -->
          <ul class="search-results" v-if="searchResults[index]">
            <li v-for="result in searchResults[index]" :key="result.no" @click="selectColumnName(result, index)">
              {{ result.domain.dataType }} - {{ result.standardTerminology }} - {{ result.englishAbbreviation }}: {{ result.description }}
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
      tableName: '', // 사용자가 입력하는 테이블 이름
      columns: [], // 테이블의 컬럼들
      searchResults: {}, // 검색 결과
      serverData: [], // 서버로부터 가져온 데이터
    };
  },
  created() {
    this.fetchServerData();
  },
  methods: {
    async fetchServerData() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:3001/terminology', {
          headers: { 'Authorization': `Bearer ${token}` }
        });
        this.serverData = response.data.data;
      } catch (error) {
        alert('데이터를 불러오는 중 에러가 발생했습니다.');
        this.goBack();
      }
    },
    goBack() {
      window.history.back();
    },
    addColumn() {
      this.columns.push({ name: '', type: '', standardTerminology: '', description: '' });
    },
    removeColumn(index) {
      this.columns.splice(index, 1);
    },
    searchColumnName(index) {
      const searchQuery = this.columns[index].name.toLowerCase();
      this.searchResults[index] = this.serverData.filter(item =>
        item.standardTerminology.includes(searchQuery)
      );
    },
    selectColumnName(result, index) {
      const dataType = this.formatDataType(result.domain.dataType);
      const dataLength = result.domain.dataLength;
      const decimalPointLength = result.domain.decimalPointLength;
      let typeFormat = dataType;

      if (dataType === 'NUMERIC' && decimalPointLength !== '-') {
        typeFormat = `NUMERIC(${dataLength}, ${decimalPointLength})`;
      } else if (dataType === 'CHAR' || dataType === 'VARCHAR') {
        typeFormat = `${dataType}(${dataLength})`;
      }

      this.columns[index] = {
        ...this.columns[index],
        name: result.englishAbbreviation,
        type: typeFormat,
        standardTerminology: result.standardTerminology,
        description: result.description
      };

      this.searchResults = {};
    },
    formatDataType(dataType) {
      return dataType.toUpperCase();
    },
    generateCreateStatement() {
      let createStatement = `CREATE TABLE ${this.tableName} (\n`;
      createStatement += `No INT AUTO_INCREMENT PRIMARY KEY,\n`;

      this.columns.forEach((column, index) => {
        createStatement += `${column.name} ${column.type}`;
        if (index !== this.columns.length - 1) {
          createStatement += ',\n';
        }
      });

      createStatement += '\n);';
      console.log(createStatement);
    },
    resetForm() {
      this.tableName = '';
      this.columns = [];
      this.searchResults = {};
    }
  }
};
</script>


<style scoped>
.data-register {
  max-width: 800px; /* 너비를 조정하여 가독성 향상 */
  margin: 40px auto; /* 상단과 하단의 마진을 추가하여 더 많은 공간을 확보 */
  padding: 40px; /* 내부 패딩을 늘려 요소들 간의 공간 확보 */
  background-color: #ffffff; /* 밝은 배경색을 사용하여 깔끔한 외관 제공 */
  border-radius: 15px; /* 모서리 둥글기를 적용하여 현대적인 느낌 제공 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15); /* 그림자를 추가하여 입체감 부여 */
  border: 1px solid #e0e0e0; /* 경계선을 추가하여 구조적 외관 강화 */
}

.data-register h2 {
  text-align: center;
  color: #2c3e50; /* 타이틀 색상 변경 */
  font-size: 24px; /* 타이틀 폰트 크기를 늘려 더 눈에 띄게 함 */
  margin-bottom: 30px; /* 타이틀과 내용 사이의 여백 증가 */
  padding-bottom: 10px; /* 타이틀 아래에 패딩 추가 */
  border-bottom: 2px solid #ecf0f1; /* 타이틀 아래 경계선 추가 */
}

.input-group label,
.column label {
  display: block;
  margin-bottom: 10px; /* 레이블과 입력 필드 사이의 여백 증가 */
  font-weight: 600; /* 레이블 폰트 가중치를 높여 중요성 강조 */
  color: #34495e; /* 레이블 색상 변경 */
}

.input-group input[type="text"],
.column input[type="text"],
.column select {
  width: 100%;
  padding: 12px; /* 입력 필드 패딩 증가 */
  margin-bottom: 15px; /* 입력 필드 간의 여백 증가 */
  border: 1px solid #bdc3c7; /* 입력 필드 경계선 색상 변경 */
  border-radius: 5px; /* 입력 필드 모서리 둥글기 적용 */
  box-sizing: border-box; /* 박스 크기 결정 방법 변경 */
  transition: border-color 0.3s; /* 입력 필드 포커스 시 부드러운 색상 변화 효과 */
}

.input-group input[type="text"]:focus,
.column input[type="text"]:focus,
.column select:focus {
  border-color: #3498db; /* 입력 필드 포커스 시 경계선 색상 변경 */
  outline: none; /* 기본 아웃라인 제거 */
}

.button {
  /* 버튼 스타일 변경 */
  padding: 12px 20px; /* 버튼 패딩 증가 */
  font-size: 16px; /* 버튼 폰트 크기 증가 */
  font-weight: 600; /* 버튼 폰트 가중치 증가 */
  text-transform: uppercase; /* 버튼 텍스트를 대문자로 변경 */
  letter-spacing: 1px; /* 버튼 글자 사이 간격 증가 */
  transition: background-color 0.3s, transform 0.2s; /* 배경색과 변형에 대한 전환 효과 추가 */
}

.button:hover {
  background-color: #2980b9; /* 버튼 호버 시 배경색 변경 */
  transform: translateY(-2px); /* 버튼 호버 시 약간 위로 이동하여 클릭감 향상 */
}

/* 버튼을 위한 추가적인 호버 효과와 활성화 효과 추가 */
.button:active {
  transform: translateY(0);
}

.search-results ul {
  /* 검색 결과 스타일 변경 */
  padding-left: 0;
  margin-top: 10px; /* 검색 결과 상단 여백 증가 */
}

.search-results li {
  padding: 10px; /* 검색 결과 항목 패딩 증가 */
  margin-bottom: 8px; /* 검색 결과 항목 간 여백 증가 */
  background-color: #ecf0f1; /* 검색 결과 배경색 변경 */
  border-radius: 5px; /* 검색 결과 모서리 둥글기 적용 */
  transition: background-color 0.3s; /* 검색 결과 호버 시 배경색 변화 효과 */
}

.search-results li:hover {
  background-color: #d6dbdf; /* 검색 결과 호버 시 배경색 변경 */
}
</style>
