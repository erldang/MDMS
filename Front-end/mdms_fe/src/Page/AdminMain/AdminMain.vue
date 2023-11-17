<template>
  <div>
    <!-- 페이지 제목 -->
    <h1 class="page-title">관리자 페이지</h1>
    <h2 class="sub-title">표준 용어 관리</h2>

    <!-- 버튼 그룹 -->
    <div class="navigation-buttons">
      <button class="nav-btn" :disabled="$route.path === '/admin-main'">표준 용어</button>
      <button class="nav-btn" @click="navigateToDataMap">데이터 맵</button>
      <button class="nav-btn" @click="navigateToUserRecord">사용자 이력 조회</button>
    </div>

    <!-- 검색 섹션 -->
    <div class="search-section">
      <select v-model="selectedCategory" class="search-select">
        <option value="">전체</option>
        <option value="custom">커스텀</option>
        <option value="standard">표준</option>
      </select>
      <select v-model="searchType" class="search-select">
        <option value="standardTerminology">표준 용어명</option>
        <option value="englishAbbreviation">영어 약어명</option>
      </select>
      <input type="text" v-model="searchQuery" class="search-input" placeholder="검색">
      <button class="search-button" @click="searchData">검색</button>
    </div>

    <!-- 데이터 테이블 -->
    <table class="data-table">
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

    <!-- 페이지네이션 -->
    <div class="pagination">
      <span class="page-number" @click="changePage(page)" v-for="page in pages" :key="page">{{ page }}</span>
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
body {
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f4f4f4;
}

h1,
h2 {
  color: #35495E;
  /* 모던한 다크 블루 색상 */
  margin-bottom: 10px;
}

/* 버튼 및 검색 필드 스타일 */
.button-group button,
.search-button {
  padding: 10px 20px;
  margin: 5px;
  border: none;
  color: white;
  background-color: #007bff;
  /* 밝은 파란색 */
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s, box-shadow 0.3s;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  /* 버튼에 그림자 효과 추가 */
}

.button-group button:disabled,
.search-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.search-select,
.search-input {
  padding: 10px;
  margin-right: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
  /* 검색 필드에 그림자 효과 추가 */
}

/* 테이블 스타일 */
.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.data-table th,
.data-table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

.data-table th {
  background-color: #f0f0f0;
}

/* 페이지네이션 */
.pagination span {
  padding: 5px 10px;
  margin-right: 5px;
  border: 1px solid #ddd;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s, box-shadow 0.3s;
}

.pagination span:hover {
  background-color: #ddd;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.15);
  /* 호버 시 그림자 효과 강화 */
}</style>
