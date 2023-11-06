<template>
    <div>
      <div class="search-filter">
        <!-- 검색 및 필터링 섹션 -->
        <input type="date" v-model="startDate" />
        <input type="date" v-model="endDate" />
        <select v-model="selectedCategory">
          <option value="">카테고리</option>
          <!-- 카테고리 옵션 -->
        </select>
        <input type="text" v-model="searchQuery" placeholder="검색어" />
        <button @click="searchData">검색</button>
      </div>
  
      <h1>데이터 목록</h1>
      <button @click="addData">등록</button>
      <div class="button-group">
        <button @click="navigateToTable">테이블</button>
        <button @click="navigateToDataMap">데이터맵</button>
      </div>
      <table>
        <!-- 데이터 테이블 -->
        <tr>
          <th>No</th>
          <th>제목</th>
          <th>상황</th>
          <th>등록일</th>
          <th>카테고리</th>
        </tr>
        <tr v-for="(item, index) in dataList" :key="index">
          <td>{{ index + 1 }}</td>
          <td>{{ item.title }}</td>
          <td>{{ item.status }}</td>
          <td>{{ item.date }}</td>
          <td>{{ item.category }}</td>
        </tr>
      </table>
  
      <div class="pagination">
        <!-- 페이지네이션 컨트롤 -->
        <span @click="changePage(page)" v-for="page in pages" :key="page">{{ page }}</span>
      </div>
    </div>
  </template>
  

<script>
export default {
    name: 'MainPage',
    components: {
    },
  data() {
    return {
      startDate: '',
      endDate: '',
      selectedCategory: '',
      searchQuery: '',
      dataList: [],
      pages: [1, 2, 3, 4, 5, 6, 7] // 예시로 페이징 번호를 설정
    };
  },
  methods: {
    navigateToTable() {
    // '테이블' 버튼 로직
    //this.$router.push('/table');
  },
  navigateToDataMap() {
    // '데이터맵' 버튼 로직
    //this.$router.push('/data-map');
  },
    searchData() {
      // 검색 데이터 로직
    },
    addData() {
      // 데이터 추가 로직
    },
    changePage() {
    //changePage(pageNumber)
      // 페이지 변경 로직
    }
    
  }
};
</script>

<style scoped>
.search-filter {
  margin-bottom: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-filter input[type="date"],
.search-filter input[type="text"],
.search-filter select {
  margin-right: 0.5rem;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 0.25rem;
}

.search-filter button {
  padding: 0.5rem 1rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 0.25rem;
  cursor: pointer;
}

.search-filter button:hover {
  background-color: #0056b3;
}

h1 {
  display: inline-block;
  margin-right: 1rem;
}

button {
  padding: 0.5rem 1rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 0.25rem;
  cursor: pointer;
}

button:hover {
  background-color: #218838;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

table, th, td {
  border: 1px solid #ddd;
}

th, td {
  text-align: left;
  padding: 0.5rem;
}

th {
  background-color: #f8f9fa;
}

.pagination {
  margin-top: 1rem;
  display: flex;
  justify-content: center;
}

.pagination span {
  margin: 0 0.5rem;
  cursor: pointer;
  color: #007bff;
}

.pagination span:hover {
  text-decoration: underline;
}
.button-group button {
  margin-left: 0.5rem; /* 버튼 간격 조정 */
  padding: 0.5rem 1rem;
  border: 1px solid #ccc;
  background-color: white;
  color: #333;
  border-radius: 0.25rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.button-group button:hover {
  background-color: #f2f2f2; /* 호버 효과 */
}
</style>

