<template>
  <div>
    <h1>테이블 상세 정보</h1>
    <p>테이블 명: {{ itemData.logicalTableName }}</p>
    <p>물리명: {{ itemData.physicalTableName }}</p>
    <p>테이블 고유 번호: {{ itemData.no }}</p>
    <p>생성일자: {{ formatDate(itemData.registrationDate) }}</p>
    <p>테이블 튜플 개수: {{ tableData.length }}</p>
    <p>데이터 테이블 용량: {{ tableDataSize }}</p>
    <h3>사용된 속성 표준 용어</h3>
    <ul v-if="tableHeaders.length > 0">
      <li v-for="header in tableHeaders" :key="header">
        {{ header }}: {{ getTerminologyInfo(header).description }}
      </li>
    </ul>
    <h2>테이블 데이터</h2>
    <div v-if="loading">데이터 로딩중...</div>
    <div v-if="error">{{ error }}</div>
    <table v-if="tableData.length > 0">
      <thead>
        <tr>
          <th v-for="header in tableHeaders" :key="header">
            {{ header }} ({{ getTerminologyInfo(header).dataType }})
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(row, rowIndex) in tableData" :key="rowIndex">
          <td v-for="(value, key) in row" :key="key">
            {{ value }}
          </td>
        </tr>
      </tbody>
    </table>
    
  </div>
</template>


<script>
import axios from 'axios';

export default {
  name: 'DataDetail',
  data() {
    return {
      itemData: null,
      tableData: [],
      terminologyData: [],
      loading: false,
      error: null,
      tableHeaders: [],
      tableDataSize: '10MB'
    };
  },
  created() {
    if (this.$route.query.itemData) {
      this.itemData = JSON.parse(this.$route.query.itemData);
    }
    this.fetchData();
    this.fetchTerminologyData();
  },
  methods: {
    fetchData() {
      this.loading = true;
      this.error = null;
      const token = localStorage.getItem('token');
      axios.get(`http://localhost:3001/table/physicalJoin?physicalName=${this.itemData.physicalTableName}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
      .then(response => {
        this.tableData = response.data.data;
        if (this.tableData.length > 0) {
          this.tableHeaders = Object.keys(this.tableData[0]).filter(key => key !== 'No');
        }
      })
      .catch(error => {
        this.error = '데이터 로드 중 오류 발생';
        console.error('Error fetching table data:', error);
      })
      .finally(() => {
        this.loading = false;
      });
    },
    fetchTerminologyData() {
      const token = localStorage.getItem('token');
      axios.get('http://localhost:3001/terminology', {
        headers: { 'Authorization': `Bearer ${token}` }
      })
      .then(response => {
        this.terminologyData = response.data.data;
      })
      .catch(error => {
        console.error('Error fetching terminology data:', error);
      });
    },
    getTerminologyInfo(attributeName) {
      const term = this.terminologyData.find(term => term.englishAbbreviation === attributeName);
      return term ? { dataType: term.domain.dataType, description: term.description } : { dataType: 'Unknown', description: 'No description available' };
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString();
    }
  },
};
</script>

<style scoped>
div {
  font-family: 'Arial', sans-serif;
  color: #333;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

h1, h2, h3 {
  color: #2C3E50;
}

p, li {
  font-size: 1rem;
  line-height: 1.6;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

tr:nth-child(even) {
  background-color: #f9f9f9;
}

tr:hover {
  background-color: #f1f1f1;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  background: #f9f9f9;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
}

.loading, .error {
  color: #e74c3c;
  font-weight: bold;
}

.error {
  background-color: #fce4e4;
  padding: 10px;
  border-radius: 5px;
}

</style>
