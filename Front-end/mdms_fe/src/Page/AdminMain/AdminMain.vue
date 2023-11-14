<template>
    <div>
      <div class="search-filter">
        <input type="date" v-model="startDate" />
        <input type="date" v-model="endDate" />
        <select v-model="searchtype">
          <option value="검색 타입">검색 타입</option>
          <option value="검색 타입1">타입1</option>
          <option value="검색 타입2">타입2</option>
        </select>
        <input type="text" v-model="searchQuery" placeholder="검색어" />
        <button @click="searchData">검색</button>
      </div>
  
      <h1>관리자 페이지</h1>
      <h2>표준 용어 관리</h2>
      <div class="button-group">
        <button @click="navigateToStandard">표준 용어</button>
        <button @click="navigateToCustomStandard">커스텀 표준 용어</button>
        <button @click="navigateToDataMap">데이터 맵</button>
        <button @click="navigateToUserRecord">사용자 이력 조회</button>
      </div>
      <table> 
        <tr>
          <th>No</th>
          <th>공통표준용어명</th>
          <th>생성자</th>
          <th>등록일</th>
          <th>영문약어명</th>
          <th>도메인명</th>
          <th>허용값</th>
          <th>저장 형식</th>
          <th>표현 방식</th>
          <th>행정 표준코드</th>
          <th>소관 기관 명</th>
        </tr>
      </table>

      <div class="pagination">
        <span @click="changePage(page)" v-for="page in pages" :key="page">{{ page }}</span>
      </div>
    </div>
  </template>
  

  <script>
  import './AdminMain.css';  
  export default {
    name: 'AdminMainPage',
    data() {
      return {
        dataList: [],
        startDate: '',
        endDate: '',
        selectedCategory: '',
        searchQuery: '',
        currentPage: 1,
        pageSize: 10,
      };
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
      navigateToStandard(){
            //'표준 용어' 페이지로 이동하는 로직
            this.$router.push('/admin-main');
        },

      navigateToCustomStandard() {
        //'커스텀 표준 용어' 페이지로 이동하는 로직
        this.$router.push('/custom-standard-terms');
      },
      navigateToDataMap() {
        //'데이터맵' 페이지로 이동하는 로직
      },
      navigateToUserRecord(){
        //'사용자 이력 조회' 페이지로 이동하는 로직
      },
      searchData() {
        // 여기에 검색 로직을 구현
        // 일반적으로 검색 조건에 따라 데이터를 가져오기 위한 API 호출을 포함
        console.log('검색 버튼 클릭');
      },
    },
  };
  </script>