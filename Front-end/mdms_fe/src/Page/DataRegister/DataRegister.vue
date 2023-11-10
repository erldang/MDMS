<template>
  <div>
    <form @submit.prevent="generateCreateStatement">
      <label for="tableName">Enter Table Name (English Only):</label>
      <input type="text" id="tableName" v-model="tableName" required>

      <button type="button" @click="addDataType">Add Column</button>

      <div v-for="(dataType, index) in dataTypes" :key="dataType.id">
        <label for="dataType">Choose Data Type:</label>
        <select v-model="dataType.selected" required>
          <option disabled value="">Please Select</option>
          <option v-for="type in dataTypeOptions" :value="type" :key="type">
            {{ type }}
          </option>
        </select>

        <label for="search">Search Column Name:</label>
        <input type="search" v-model="dataType.search" @input="searchDataType(dataType)">

        <!-- 검색 결과를 리스트로 표시하고 선택 가능하게 합니다. -->
        <ul v-if="dataType.searchResults.length">
          <li v-for="result in dataType.searchResults" :key="result.no">
            <button type="button" @click="selectRecommendation(index, result.classificationName)">
              {{ result.classificationName }}
            </button>
            <label>{{ result.description }}</label>
            <label>{{ result.dataType }}</label>
          </li>
        </ul>
        
        <!-- 검색 결과가 5개 이하일 때, 그 아래에 사용자 정의 추가 버튼을 표시합니다. -->
        <div v-if="dataType.searchResults.length && dataType.searchResults.length <= 5">
          <button type="button" @click="customAdd(index)">
            Custom Add
          </button>
        </div>

        <button type="button" @click="removeDataType(index)">Remove Column</button>
      </div>
      
      <button type="submit">Generate CREATE Statement</button>
      <button type="button" @click="registerTable">Register</button>
      <button type="button" @click="cancel">Cancel</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      tableName: '',
      dataTypes: [],
      dataTypeOptions: ['INT', 'VARCHAR(255)', 'TEXT', 'DATE', 'CHAR(255)'],
      serverData: [
        {
          "no": 1,
          "classificationName": "코드",
          "domain": "코드C12",
          "description": "정보를 나타내기 위한 기호 체계로 고정된 데이터 길이로 표현한 것",
          "dataType": "CHAR",
          "dataLength": "12",
          "decimalPointLength": "0",
          "saveFormat": "12자리 문자",
          "expressionForm": "-",
          "unitName": "-",
          "tolerance": "-",
          "degree": "2"
        },
        {
          "no": 2,
          "classificationName": "이름",
          "domain": "이름N20",
          "description": "사람의 이름을 표기하는 데 사용되는 문자열",
          "dataType": "VARCHAR",
          "dataLength": "20",
          "decimalPointLength": "0",
          "saveFormat": "최대 20자리 문자",
          "expressionForm": "-",
          "unitName": "-",
          "tolerance": "-",
          "degree": "1"
        },
        {
          "no": 3,
          "classificationName": "주소",
          "domain": "주소A50",
          "description": "거주지를 표시하는 데 사용되는 문자열",
          "dataType": "VARCHAR",
          "dataLength": "50",
          "decimalPointLength": "0",
          "saveFormat": "최대 50자리 문자",
          "expressionForm": "-",
          "unitName": "-",
          "tolerance": "-",
          "degree": "1"
        }
      ],
    };
  },
  methods: {
    addDataType() {
      this.dataTypes.push({
        id: Date.now(),
        selected: '',
        search: '',
        searchResults: []
      });
    },
    searchDataType(dataType) {
      if (dataType.search) {
        let searchString = dataType.search.toLowerCase();
        dataType.searchResults = this.serverData.filter(item =>
          item.classificationName.toLowerCase().includes(searchString)
        );
      } else {
        // 검색어가 비어있을 때 처리할 내용 추가
        dataType.searchResults = [];
      }
    },
    selectRecommendation(index, recommendation) {
    // 선택된 추천 값을 해당 데이터 타입의 선택된 값으로 설정
    this.dataTypes[index].selected = recommendation;
    // 검색 결과를 비우고 검색 입력을 추천된 값으로 설정 (옵션)
    this.dataTypes[index].searchResults = [];
    this.dataTypes[index].search = recommendation;
    },
    removeDataType(index) {
      this.dataTypes.splice(index, 1);
    },
    generateCreateStatement() {
      // 테이블 이름 가져오기
      const tableName = this.tableName.trim();
      
      // 테이블 이름이 비어있을 경우 예외 처리
      if (!tableName) {
        alert("Please enter a table name.");
        return;
      }
      
      // CREATE TABLE 문 생성
      let createStatement = `CREATE TABLE ${tableName} (\n`;
      
      // ID 열 추가 (기본키, 자동 증가)
      createStatement += "  ID INT PRIMARY KEY AUTO_INCREMENT,\n";
      
      // DataType 열 추가
      for (const dataType of this.dataTypes) {
        const columnName = dataType.search.trim();
        const dataTypeValue = dataType.selected;
        
        // Column Name이 비어있을 경우 예외 처리
        if (!columnName) {
          alert("Please enter a column name for all data types.");
          return;
        }
        
        createStatement += `  ${columnName} ${dataTypeValue},\n`;
      }
      
      // 마지막 쉼표와 개행 문자 제거
      createStatement = createStatement.slice(0, -2) + "\n";
      
      // CREATE TABLE 문 마무리
      createStatement += ");";
      
      // 생성된 CREATE TABLE 문을 콘솔에 출력하거나 필요한 방식으로 사용할 수 있습니다.
      console.log("Generated CREATE TABLE statement:");
      console.log(createStatement);
    },

    registerTable() {
      // 여기에 테이블 등록 로직을 구현하세요.
    },
    cancel() {
      // 여기에 취소 로직을 구현하세요.
    },
    customAdd(){
      console.log("커스텀 버튼 눌림");
    }
  },
  watch: {
    // 검색어(dataType.search)의 변화를 감지
    'dataTypes': {
      handler: 'searchDataType',
      deep: true,
    },
  },
};
</script>

<style>
  /* 스타일을 수정하거나 추가하세요 */
  .container {
    margin: 20px;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #f8f8f8;
  }

  form {
    display: flex;
    flex-direction: column;
  }

  label {
    margin-top: 10px;
  }

  input[type="text"],
  input[type="search"],
  select {
    width: 100%;
    padding: 8px;
    margin-top: 5px;
    border: 1px solid #ccc;
    border-radius: 3px;
    box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
  }

  button {
    margin-top: 10px;
    padding: 8px 16px;
    background-color: #0074d9;
    color: #fff;
    border: none;
    border-radius: 3px;
    cursor: pointer;
  }

  ul {
    list-style: none;
    padding: 0;
  }

  li {
    display: flex;
    align-items: center;
    margin-top: 10px;
  }

  li button {
    background-color: #33cc33;
    margin-right: 10px;
  }
</style>
