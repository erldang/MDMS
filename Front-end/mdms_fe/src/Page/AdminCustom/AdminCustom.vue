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
      <h2>커스텀 표준 용어 관리</h2>
      <div class="button-group">
        <button @click="navigateToStandard">표준 용어</button>
        <button @click="navigateToCustomStandard">커스텀 표준 용어</button>
        <button @click="navigateToDataMap">데이터 맵</button>
        <button @click="navigateToUserRecord">사용자 이력 조회</button>
      </div>
      <table>
        <tr>
          <th>No</th>
          <th>커스텀표준용어명</th>
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
        <!-- 데이터 로우를 반복해서 추가하세요. -->
      </table>
  
      <div class="pagination">
        <span @click="changePage(page)" v-for="page in pages" :key="page">{{ page }}</span>
      </div>
    </div>
  </template>
  
<script>
  import './AdminCustom.css';
  export default {
    name: 'AdminCustomPage',
    data() {
      return {
        dataList: [],
        startDate: '',
        endDate: '',
        selectedCategory: '',
        searchQuery: '',
        currentPage: 1,
        pageSize: 10,
        // 기타 커스텀 페이지에 필요한 데이터
      };
    },
    computed: {
      // AdminMainPage에서 사용된 computed 속성들
      // 필요한 경우 여기에 추가적인 로직을 포함시킬 수 있습니다
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
        // 커스텀 용어에 대한 검색 로직 구현
        console.log('검색 버튼 클릭');
      },
      // 기타 필요한 메서드들 추가 
    },
  };
</script>
  