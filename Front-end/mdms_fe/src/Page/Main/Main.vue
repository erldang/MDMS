<!-- 데이터 목록에 등록 버튼 디자인 적용 안됨 -->
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
      // DataDetail 컴포넌트로 보내기 전에 데이터를 콘솔에 출력합니다.
      console.log('Navigating to DataDetail with item:', item);
      this.$router.push({ name: 'DataDetail', query: { itemData: JSON.stringify(item) } });
    }
  },
  mounted() {
    this.fetchData();
  },
};
</script>

<style scoped>
.page_btn button {
  background-color: #007bff;
  /* 밝은 파란색 */
  color: white;
  padding: 10px 20px;
  margin: 5px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1.1rem;
  transition: background-color 0.3s;
}

.page_btn button.active {
  background-color: #0056b3;
  /* 어두운 파란색 */
}

.search-filter {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.search-filter input[type="date"],
.search-filter input[type="text"] {
  padding: 8px;
  margin-right: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-filter button {
  padding: 8px 15px;
  background-color: #27ae60;
  /* 녹색 계열 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

h1 {
  display: inline-block;
  margin-right: 20px;
  color: #35495E;
  /* 모던한 다크 블루 색상 */
}

button.addData {
  float: right;
  background-color: #f0ad4e;
  /* 오렌지 계열 */
  color: white;
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1.1rem;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

table th,
table td {
  padding: 12px;
  text-align: left;
  border: 1px solid #ddd;
}

table th {
  background-color: #f2f2f2;
}

.loading,
.error {
  color: #e74c3c;
  /* 경고, 오류 메시지에 적합한 색상 */
  margin: 10px 0;
}</style>

