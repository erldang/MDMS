<template>
  <header class="header-container">
    <!-- 로고와 타이틀 섹션 -->
    <div class="left-section" @click="navigateToHome">
      <img src="@/assets/img/pruning-logo.png" alt="테스트로고" class="logo-img">
      <div class="title-section">
        <h1 class="title">MDMS : 데이터 정원사</h1>
        <p class="subtitle">메타데이터 관리 서비스</p>
      </div>
    </div>
    
    <!-- 네비게이션 바 -->
    <!-- <nav v-if="isLoggedIn" class="navbar-section"> -->
      <!-- <span class="username-text">{{ username }}</span> -->
    <nav v-if="isLoggedIn" class="navbar-section">
      <span class="username-text">{{ username }}</span>
      <button class="nav-btn" @click="navigateToProfile">회원정보</button>
      <button class="nav-btn logout-btn" @click="logoutAction">로그아웃</button>
    </nav>
  </header>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  name: 'PageHeader',
  computed: {
    // 로그인 여부 확인
    isLoggedIn() {
      return this.$store.getters.isLoggedIn || !!localStorage.getItem('token');
    },
    // 사용자 이름 가져오기
    username() {
      // Vuex 스토어에서 username을 가져옴
      return this.$store.getters.username || localStorage.getItem('username');
    },
  },

  methods: {
    // 로그아웃 액션 실행
    logoutAction() {
      this.logout().then(() => {
        localStorage.removeItem('token'); // 로컬 스토리지의 토큰 삭제
        this.$router.push('/login').then(() => {
          window.location.reload(); // 페이지 새로고침으로 앱 상태 초기화
        });
      }).catch(error => {
        console.error('Logout failed', error);
      });
    },
    ...mapActions(['logout']),
    navigateToProfile() {
      this.$router.push('/profile');
    },
    navigateToHome() {
      this.$router.push('/');
    },
  },
}
</script>



<style scoped>
.header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 40px;
    background-color: #35495E; /* 모던한 다크 블루 */
    color: #FFFFFF;
    margin-bottom: 1rem;
}

.left-section {
    display: flex;
    align-items: center;
}

.logo-img {
    width: 60px; /* 로고 크기 조정 */
    height: auto;
    margin-right: 20px;
}

.title-section {
    display: flex;
    flex-direction: column;
}

.title {
    font-size: 24px; /* 적당한 크기의 제목 폰트 */
    font-weight: bold; /* 볼드체로 강조 */
    margin: 0;
    color:#FFFFFF;
}

.subtitle {
    font-size: 16px; /* 서브타이틀 폰트 크기 */
    opacity: 0.9; /* 약간 투명도 조정 */
    color:#FFFFFF;
}

.navbar-section {
    display: flex;
    align-items: center;
}

.username-text {
    margin-right: 20px;
    font-size: 18px; /* 사용자 이름 폰트 크기 */
    font-weight: medium;
    white-space: nowrap;
}

.nav-btn {
    padding: 10px 20px;
    margin-left: 10px;
    background-color: transparent;
    border: 2px solid #FFFFFF; /* 흰색 테두리 */
    border-radius: 5px;
    cursor: pointer;
    transition: all 0.3s ease;
    font-size: 16px;
    color: #FFFFFF;
}

.nav-btn:hover {
    background-color: #4A6FA5; /* 버튼 호버 색상 */
    color: #FFFFFF;
}

.logout-btn {
    border-color: #FF5252; /* 로그아웃 버튼 테두리 색상 */
    color: #FF5252;
}

.logout-btn:hover {
    background-color: #FF5252; /* 로그아웃 버튼 호버 색상 */
    color: #FFFFFF;
}
</style>

