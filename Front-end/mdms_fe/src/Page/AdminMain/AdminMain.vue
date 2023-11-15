<template>
  <div>
    <h1>관리자 페이지</h1>
    <h2>표준 용어 관리</h2>
    <div class="button-group">
      <button :disabled="$route.path === '/admin-main'">표준 용어</button>
      <button @click="navigateToDataMap">데이터 맵</button>
      <button @click="navigateToUserRecord">사용자 이력 조회</button>
    </div>

    <div class="search-section">
      <select v-model="selectedCategory">
        <option value="">전체</option>
        <option value="custom">커스텀</option>
        <option value="standard">표준</option>
      </select>
      <select v-model="searchType">
        <option value="standardTerminology">표준 용어명</option>
        <option value="englishAbbreviation">영어 약어명</option>
      </select>
      <input type="text" v-model="searchQuery" placeholder="검색">
      <button @click="searchData">검색</button>
    </div>

    <table> 
      <tr>
        <th>No</th>
        <th>차수</th>
        <th>표준 용어명</th>
        <th>영어 약어명</th>
        <th>커스텀 여부</th>
      </tr>
      <tr v-for="item in paginatedData" :key="item.no">
        <td>{{ item.no }}</td>
        <td>{{ item.degree }}</td>
        <td>{{ item.standardTerminology }}</td>
        <td>{{ item.englishAbbreviation }}</td>
        <td>{{ item.isCustom ? '커스텀' : '표준' }}</td>
      </tr>
    </table>

    <div class="pagination">
      <span @click="changePage(page)" v-for="page in pages" :key="page">{{ page }}</span>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'AdminMainPage',
  data() {
    return {
      originalDataList: [], // 원본 데이터 리스트
      dataList: [], // 화면에 표시될 데이터 리스트
      searchQuery: '',
      searchType: 'standardTerminology',
      selectedCategory: '',
      currentPage: 1,
      pageSize: 30,
    };
  },
  created() {
    this.fetchData();
  },
  computed: {
      pages() {
        // 데이터 길이와 페이지 크기를 기반으로 총 페이지 수를 계산
        const numberOfPages = Math.ceil(this.dataList.length / this.pageSize);
        return Array.from({ length: numberOfPages }, (v, k) => k + 1);
      },
      paginatedData() {
        // 현재 페이지에 표시할 데이터를 계산
        const start = (this.currentPage - 1) * this.pageSize;
        const end = start + this.pageSize;
        return this.dataList.slice(start, end);
      }
    },
    methods: {
    fetchData() {
      axios.get(`http://localhost:3001/terminology`, {
        headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
      })
      .then(response => {
        this.originalDataList = response.data.data;
        this.dataList = this.originalDataList; // 초기 데이터 설정
      })
      .catch(error => {
        console.error(error);
      });
    },
    searchData() {
      // 검색 로직 구현
      this.currentPage = 1; // 검색시 첫 페이지로 리셋
      let filteredData = this.originalDataList;

      if (this.selectedCategory === 'custom') {
        filteredData = filteredData.filter(item => item.isCustom);
      } else if (this.selectedCategory === 'standard') {
        filteredData = filteredData.filter(item => !item.isCustom);
      }

      if (this.searchQuery) {
        filteredData = filteredData.filter(item => item[this.searchType].toLowerCase().includes(this.searchQuery.toLowerCase()));
      }

      this.dataList = filteredData;
    },
    navigateToDataMap() {
      this.$router.push('/admin-datamap');
    },
    navigateToUserRecord() {
      this.$router.push('/admin-user-record');
    },
    changePage(page) {
      this.currentPage = page;
    },
  }
};
</script>

<style scoped>
/* 기본 스타일링 */
body {
  font-family: Arial, sans-serif;
  color: #333;
  background-color: #f4f4f4;
  line-height: 1.6;
  padding: 20px;
}

h1, h2 {
  color: #444;
}

/* 레이아웃 및 정렬 */
.button-group, .search-section, .pagination {
  margin-bottom: 20px;
}

.button-group button, .search-section select, .search-section input, .pagination span {
  padding: 10px;
  margin-right: 10px;
  border: 1px solid #ddd;
  background: #fff;
  cursor: pointer;
  border-radius: 4px;
}

.button-group button:disabled {
  color: #aaa;
  cursor: default;
}

/* 테이블 스타일 */
table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

th {
  background-color: #f0f0f0;
}

/* 페이지네이션 */
.pagination span {
  cursor: pointer;
}

/* 반응형 디자인 */
@media (max-width: 600px) {
  .button-group button, .search-section select, .search-section input {
    margin-bottom: 10px;
    width: 100%;
  }
}
</style>