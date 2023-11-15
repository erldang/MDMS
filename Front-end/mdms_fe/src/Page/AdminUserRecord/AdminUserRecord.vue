<template>
  <div class="admin-page">
    <h1>관리자 페이지</h1>
    <h2>사용자 이력 조회</h2>
    <div class="button-group">
      <button @click="navigateToMain">표준 용어</button>
      <button @click="navigateToDataMap">데이터 맵</button>
      <button :disabled="$route.path === '/admin-user-record'">사용자 이력 조회</button>
    </div>

    <div class="table-container">
      <table class="user-history-table">
        <thead>
          <tr>
            <th>번호</th>
            <th>물리명</th>
            <th>논리명</th>
            <th>사용자</th>
            <th>생성 일자</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in userHistory" :key="record.no">
            <td>{{ record.no }}</td>
            <td>{{ record.physicalTableName }}</td>
            <td>{{ record.logicalTableName }}</td>
            <td>{{ record.email }}</td>
            <td>{{ new Date(record.registrationDate).toLocaleString() }}</td>
          </tr>
        </tbody>
      </table>
    </div>

  </div>
</template>
<script>
import axios from 'axios';

export default {
  name: 'AdminUserRecordPage',
  data() {
    return {
      userHistory: [] 
    };
  },
  created() {
    this.fetchUserHistory();
  },
  methods: {
    navigateToMain() {
      this.$router.push('/admin-main');
    },
    navigateToDataMap() {
      this.$router.push('/admin-datamap');
    },
    async fetchUserHistory() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:3001/history', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
        this.userHistory = response.data.data;
      } catch (error) {
        console.error('Error fetching user history:', error);
      }
    }
  }
};
</script>

<style scoped>
body {
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f4f4f4;
}

h1, h2 {
  color: #333;
}

.button-group button {
  padding: 10px 15px;
  margin-right: 10px;
  border: none;
  color: #fff;
  background-color: #007bff;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.button-group button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.table-container {
  margin-top: 20px;
  overflow-x: auto;
}

.user-history-table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.user-history-table thead {
  background-color: #007bff;
  color: #fff;
}

.user-history-table th, 
.user-history-table td {
  padding: 12px 15px;
  border: 1px solid #ddd;
  text-align: left;
}

.user-history-table tbody tr:nth-child(even) {
  background-color: #f2f2f2;
}

.user-history-table tbody tr:hover {
  background-color: #ddd;
}

.user-history-table td {
  position: relative;
}

.user-history-table td::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 2px;
  background-color: #007bff;
  transform: scaleX(0);
  transform-origin: bottom right;
  transition: transform 0.3s ease-out;
}

.user-history-table td:hover::after {
  transform: scaleX(1);
  transform-origin: bottom left;
}

</style>