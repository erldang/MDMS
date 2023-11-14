<template>
  <div class="data-detail-container">
    <h1 class="title">테이블 상세 정보</h1>
    <p class="info">테이블 명: {{ itemData.logicalTableName }}</p>
    <p class="info">물리명: {{ itemData.physicalTableName }}</p>
    <p class="info">테이블 고유 번호: {{ itemData.no }}</p>
    <p class="info">생성일자: {{ formatDate(itemData.registrationDate) }}</p>
    <p class="info">테이블 튜플 개수: {{ tableData.length }}</p>
    <p class="info">데이터 테이블 용량: {{ tableDataSize }}</p>
    <h3 class="subtitle">사용된 속성 표준 용어</h3>
    <ul class="term-list" v-if="filteredTableHeaders.length > 0">
      <li class="term-item" v-for="header in filteredTableHeaders" :key="header">
        {{ header }}: {{ getTerminologyInfo(header).description }}
      </li>
    </ul>
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
import './DataDetail.css';

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
