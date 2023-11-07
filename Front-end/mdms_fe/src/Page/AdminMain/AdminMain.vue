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
  
      <h1>관리자 페이지</h1>
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
  import './AdminMain.css';
  // 추가적인 의존성이 필요하면 여기에 임포트하세요
  
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
      navigateToTable() {
        // '표준 용어' 페이지로 이동하는 로직
        //this.$router.push('/standard-terms'); 
      },
      navigateToCustomStandard() {
        // '커스텀 표준 용어' 페이지로 이동하는 로직
        //this.$router.push('/custom-standard-terms'); 
      },
      navigateToDataMap() {
        // '데이터 맵' 페이지로 이동하는 로직
        //this.$router.push('/data-map'); 
      },
      searchData() {
        // 여기에 검색 로직을 구현하세요
        // 일반적으로 검색 조건에 따라 데이터를 가져오기 위한 API 호출을 포함합니다
        console.log('검색 버튼 클릭');
      },
      addData() {
        // 새로운 데이터를 추가하는 로직
        console.log('등록 버튼 클릭');
      },
      changePage(page) {
        // 현재 페이지를 변경하는 메소드
        this.currentPage = page;
      }
    },
    // 컴포넌트가 마운트될 때 데이터를 로드해야하는 경우
    mounted() {
      this.fetchData();
    },
    methods: {
      fetchData() {
        // dataList를 위한 초기 데이터를 가져오는 작업
      },
    }
  };
  </script>