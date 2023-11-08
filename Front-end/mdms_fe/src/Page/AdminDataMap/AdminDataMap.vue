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
      <h2>데이터맵</h2>
      <div class="button-group">
        <button @click="navigateToStandard">표준 용어</button>
        <button @click="navigateToCustomStandard">커스텀 표준 용어</button>
        <button @click="navigateToDataMap">데이터 맵</button>
        <button @click="navigateToUserRecord">사용자 이력 조회</button>
      </div>
      <p>데이터 맵이 들어올 공간입니다.</p>
    </div>
  </template>
  
<script>
  import './AdminDataMap.css';
  export default {
    name: 'AdminDataMapPage',
    data() {
    },
    computed: {
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
  