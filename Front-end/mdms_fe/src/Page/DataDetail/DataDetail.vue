<template>
  <div class="data-detail-container">
    <div class="top-section">
      <div class="table-info">
        <h1 class="title">테이블 상세 정보</h1>
        <p class="info">테이블 명: {{ itemData.logicalTableName }}</p>
        <p class="info">물리명: {{ itemData.physicalTableName }}</p>
        <p class="info">테이블 고유 번호: {{ itemData.no }}</p>
        <p class="info">생성일자: {{ formatDate(itemData.registrationDate) }}</p>
        <p class="info">테이블 튜플 개수: {{ tableData.length }}</p>
        <p class="info">데이터 테이블 용량: {{ tableDataSize }}</p>
      </div>
      <div class="terms-section">
        <h3 class="subtitle">사용된 속성 표준 용어</h3>
        <ul class="term-list" v-if="filteredTableHeaders.length > 0">
          <li class="term-item" v-for="header in filteredTableHeaders" :key="header">
            {{ header }}: {{ getTerminologyInfo(header).description }}
          </li>
        </ul>
      </div>
    </div>
    <h2 class="subtitle">테이블 데이터</h2>
    <div class="loading" v-if="loading">데이터 로딩중...</div>
    <div class="error" v-if="error">{{ error }}</div>
    <table class="data-table" v-if="tableData.length > 0">
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
      tableDataSize: ''
    };
  },
  created() {
    if (this.$route.query.itemData) {
      this.itemData = JSON.parse(this.$route.query.itemData);
    }
    this.fetchData();
    this.fetchTerminologyData();
  },
  computed: {
    filteredTableHeaders() {
      // 'No'를 제외한 tableHeaders 배열 반환
      return this.tableHeaders.filter(header => header !== 'No');
    }
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
          this.tableDataSize = response.data.message;
          console.log(response);
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
      if (attributeName === 'NO') {
        return { dataType: 'INT', description: '튜플에 대한 고유 번호입니다.' }; // 'NO'에 대한 특별 처리
      }
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
.data-detail-container {
  font-family: 'Arial', sans-serif;
  color: #333;
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  /* 밝은 배경색 */
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  /* 강화된 그림자 효과 */
}

.title,
.subtitle {
  color: #35495E;
  /* 모던한 다크 블루 */
  margin-bottom: 15px;
}

.info {
  font-size: 1.1rem;
  line-height: 1.8;
  padding: 5px 0;
  border-bottom: 1px solid #ddd;
  /* 약간 더 진한 회색 */
}

.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.data-table th,
.data-table td {
  border: 1px solid #ddd;
  /* 약간 더 진한 회색 */
  padding: 12px 15px;
  text-align: left;
}

.data-table th {
  background-color: #35495E;
  /* 모던한 다크 블루 */
  color: white;
  font-weight: bold;
}

.data-table tr:nth-child(even) {
  background-color: #f2f2f2;
  /* 약간 더 진한 회색 */
}

.data-table tr:hover {
  background-color: #ddd;
  /* 진한 회색 */
}

.term-list {
  list-style: none;
  padding: 0;
  margin-top: 20px;
}

.term-item {
  background: #f2f2f2;
  /* 약간 더 진한 회색 */
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 5px;
  font-size: 1.1rem;
}

.loading,
.error {
  font-size: 1.2rem;
  color: #dc3545;
  /* 강렬한 빨간색 */
  font-weight: bold;
  padding: 10px;
  text-align: center;
  border-radius: 5px;
}

.error {
  background-color: #f8d7da;
  /* 연한 빨간색 */
}

.top-section {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.table-info {
  width: 30%;
}

.terms-section {
  width: 70%;
}</style>
