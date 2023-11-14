<template>
  <div>
    <div class="page_btn">
      <button :class="{ active: isCurrentPage('/') }" @click="navigateTo('/')">데이터 목록</button>
      <button :class="{ active: isCurrentPage('/user-data-map') }" @click="navigateTo('/user-data-map')">데이터 맵</button>
    </div>
    <div class="search-filter">
      <input type="date" v-model="startDate" />
      <input type="date" v-model="endDate" />
      <input type="text" v-model="searchQuery" placeholder="검색어" />
      <button @click="fetchData">검색</button>
    </div>
    <div v-if="loading">데이터 로딩중...</div>
    <div v-if="error">{{ error }}</div>
    <h1>데이터 목록</h1>
    <button @click="addData">등록</button>
    <table>
      <tr>
        <th>No</th>
        <th>테이블 물리명</th>
        <th>테이블 논리명</th>
        <th>등록일</th>
      </tr>
      <tr v-for="(item, index) in filteredData" :key="index" @click="navigateToDetail(item)">
        <td>{{ item.no }}</td>
        <td>{{ item.physicalTableName }}</td>
        <td>{{ item.logicalTableName }}</td>
        <td>{{ formatDate(item.registrationDate) }}</td>
      </tr>
    </table>
  </div>
</template>

<script>
import axios from 'axios';
import './Main.css';

export default {
  name: 'MainPage',
  data() {
    return {
      startDate: '',
      endDate: '',
      searchQuery: '',
      rawData: [], // 서버로부터 받은 원본 데이터
      loading: false, // 로딩 상태
      error: null, // 에러 메시지
    };
  },
  computed: {
    filteredData() {
      return this.rawData.filter(item => {
        const startDateMatch = this.startDate ? new Date(item.registrationDate) >= new Date(this.startDate) : true;
        const endDateMatch = this.endDate ? new Date(item.registrationDate) <= new Date(this.endDate) : true;
        const queryMatch = this.searchQuery ? item.logicalTableName.toLowerCase().includes(this.searchQuery.toLowerCase()) || item.physicalTableName.toLowerCase().includes(this.searchQuery.toLowerCase()) : true;
        return startDateMatch && endDateMatch && queryMatch;
      });
    }
  },
  methods: {
    fetchData() {
      this.loading = true;
      this.error = null;
      const token = localStorage.getItem('token');
      const userEmail = localStorage.getItem('email');

      axios.get(`http://localhost:3001/history/join?email=${userEmail}`, {
        headers: { 'Authorization': `Bearer ${token}` }
      })
      .then(response => {
        this.rawData = response.data.data;
      })
      .catch(error => {
        this.error = '데이터 로드 중 에러 발생';
        console.error('Error fetching data:', error);
      })
      .finally(() => {
        this.loading = false;
      });
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString();
    },
    navigateTo(route) {
      this.$router.push(route);
    },
    isCurrentPage(route) {
      return this.$route.path === route;
    },
    addData() {
      this.$router.push('/data-register');
    },
    navigateToDetail(item) {
    this.$router.push({ name: 'DataDetail', query: { itemData: JSON.stringify(item) } });
  }
  },
  mounted() {
    this.fetchData();
  },
};
</script>
