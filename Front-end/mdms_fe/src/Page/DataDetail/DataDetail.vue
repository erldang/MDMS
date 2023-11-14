<template>
  <div>
    <h1>테이블 상세 정보</h1>
    <p>테이블 명: {{ itemData.logicalTableName }}</p>
    <p>물리명: {{ itemData.physicalTableName }}</p>
    <p>테이블 고유 번호: {{ itemData.no }}</p>
    <p>생성일자: {{ formatDate(itemData.registrationDate) }}</p>

    <h2>테이블 데이터</h2>
    <div v-if="loading">데이터 로딩중...</div>
    <div v-if="error">{{ error }}</div>
    <div v-if="tableData.length === 0">데이터가 없습니다.</div>
    <table v-if="tableData.length > 0">
      <thead>
        <tr>
          <th v-for="(header, index) in tableHeaders" :key="index">
            {{ header }}
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
  props: ['itemData'],
  data() {
    return {
      tableData: [],
      loading: false,
      error: null,
      tableHeaders: [], // 테이블 헤더를 위한 데이터
    };
  },
  methods: {
    fetchData() {
      const itemData = JSON.parse(this.$route.query.itemData);
      console.log(itemData);
      this.loading = true;
      this.error = null;
      const token = localStorage.getItem('token');
      axios.get(`http://localhost:3001/table/physicalJoin?physicalName=${this.itemData.physicalTableName}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
      .then(response => {
        this.tableData = response.data.data;
        if (this.tableData.length > 0) {
          // 첫 번째 데이터 항목에서 키를 추출하여 헤더로 사용합니다.
          this.tableHeaders = Object.keys(this.tableData[0]);
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
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString();
    }
  },
  mounted() {
    console.log('itemData in mounted:', this.itemData);
    console.log('itemData in mounted:', this.itemData.no);
    this.fetchData();
  },
};
</script>