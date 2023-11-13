<template>
  <div class="data-register">
    <h2>SQL 테이블 생성</h2>
    <form @submit.prevent="registerTable">
      <div class="input-group">
        <label for="tableName">테이블 이름 입력:</label>
        <input type="text" id="tableName" v-model="tableName" />
      </div>

      <div class="columns">
        <div class="column" v-for="(column, index) in columns" :key="index">
          <input type="text" v-model="column.name" @input="searchColumnName(index)" placeholder="컬럼 이름" />
          <select v-model="column.type">
            <option value="INT">INT</option>
            <option value="VARCHAR(255)">VARCHAR(255)</option>
            <option value="TEXT">TEXT</option>
            <option value="DATE">DATE</option>
            <option value="CHAR(255)">CHAR(255)</option>
          </select>
          <button type="button" @click="removeColumn(index)">컬럼 제거</button>
          
          <ul class="search-results" v-if="searchResults[index]">
            <li v-for="result in searchResults[index]" :key="result.no" @click="selectColumnName(result, index)">
              {{ result.classificationName }} - {{ result.description }}, {{ result.dataType }}
            </li>
          </ul>
        </div>
      </div>

      <button type="button" @click="addColumn">컬럼 추가</button>

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
      tableName: '',
      columns: [{ name: '', type: 'INT' }],
      searchResults: {},
      serverData: [],
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
      this.columns.push({ name: '', type: 'INT' });
    },
    removeColumn(index) {
      this.columns.splice(index, 1);
    },
    searchColumnName(index) {
      const searchQuery = this.columns[index].name.toLowerCase();
      const filteredResults = this.serverData.filter(item =>
        item.standardTerminology.toLowerCase().includes(searchQuery) ||
        item.domain.classificationName.toLowerCase().includes(searchQuery)
      );
      this.searchResults = { ...this.searchResults, [index]: filteredResults };
    },

    selectColumnName(result, columnIndex) {
      this.columns[columnIndex].name = result.standardTerminology;
      this.columns[columnIndex].type = this.formatDataType(result.domain.dataType);
      this.searchResults = {};
    },
    formatDataType(dataType) {
      if (dataType.toUpperCase() === 'CHAR') {
        return 'CHAR(255)';
      }
      return dataType.toUpperCase();
    },
    generateCreateStatement() {
      let createStatement = `CREATE TABLE ${this.tableName} (`;
      this.columns.forEach((column, index) => {
        createStatement += `${column.name} ${column.type}`;
        if (index < this.columns.length - 1) createStatement += ', ';
      });
      createStatement += ');';
      console.log(createStatement);
    },
    resetForm() {
      this.tableName = '';
      this.columns = [{ name: '', type: 'INT' }];
      this.searchResults = [];
    }
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
