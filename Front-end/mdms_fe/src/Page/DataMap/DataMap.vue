<template>
    <!-- 컴포넌트의 HTML 마크업 -->
    <div>
      <!-- 페이지 이동 버튼 그룹 -->
      <div class="page_btn">
        <!-- 데이터 목록 페이지로 이동하는 버튼 -->
        <button :class="{ active: isCurrentPage('/') }" @click="navigateTo('/')">데이터 목록</button>
        <!-- 데이터 맵 페이지로 이동하는 버튼 -->
        <button :class="{ active: isCurrentPage('/user-data-map') }" @click="navigateTo('/user-data-map')">데이터 맵</button>
      </div>
      <!-- 페이지 제목 -->
      <h2>데이터 맵 페이지</h2>
      <!-- 여기에 필요한 거 추가 하면 됩니다 제목도 지워도 됩니다 -->
    </div>
</template>
  
<script>
  export default {
    name: 'DataMap', // 컴포넌트 이름
    methods: {
      // 경로(route)를 인자로 받아 해당 경로로 라우팅하는 메소드
      navigateTo(route) {
        this.$router.push(route);
      },
      // 현재 페이지 경로가 주어진 경로(route)와 일치하는지 확인하는 메소드
      isCurrentPage(route) {
        return this.$route.path === route;
      }
    }
  }
</script>

<style scoped>
/* 페이지 내 버튼에 대한 스타일 */
.page_btn button {
  background-color: #f4f4f4; /* 기본 배경색 */
  border: 1px solid #ccc; /* 테두리 색상 */
  padding: 10px 15px; /* 내부 패딩 */
  margin-right: 5px; /* 오른쪽 마진 */
  border-radius: 5px; /* 둥근 모서리 */
  cursor: pointer; /* 마우스 커서 스타일 */
  transition: background-color 0.3s ease; /* 배경색 변화 애니메이션 */
}

/* 버튼에 마우스 호버 시의 스타일 */
.page_btn button:hover {
  background-color: #ddd; /* 호버 시 배경색 */
}

/* 현재 활성화된 페이지 버튼의 스타일 */
.page_btn .active {
  background-color: #bbb; /* 활성화된 배경색 */
  color: white; /* 글자 색상 */
  cursor: default; /* 마우스 커서 스타일 */
}

</style>
