<template>
  <header class="header-container">
    <!-- 로고와 타이틀 섹션 -->
    <div class="left-section" @click="navigateToHome">
      <img src="@/assets/img/test-logo.png" alt="테스트로고" class="logo-img">
      <div class="title-section">
        <h1 class="title">MDMS</h1>
        <p class="subtitle">메타데이터 관리 서비스</p>
      </div>
    </div>
    
    <!-- 네비게이션 바 -->
    <nav v-if="isLoggedIn" class="navbar-section">
      <span class="username-text">{{ username }}</span>
      <!-- 나머지 네비게이션 바 -->
      <button class="nav-btn" @click="navigateToProfile">회원 정보</button>
      <button class="nav-btn">이력 조회</button>
      <button class="nav-btn logout-btn" @click="logoutAction">로그아웃</button>
    </nav>
  </header>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'PageHeader',
  computed: {
    // Vuex 스토어의 로그인 상태 또는 로컬 스토리지의 토큰 존재 여부를 통해 로그인 여부를 확인
    isLoggedIn() {
      return this.$store.getters.isLoggedIn || !!localStorage.getItem('token');
    },
    ...mapGetters(['username']), // Vuex 스토어에서 username을 가져옴
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
    padding: 1rem 2rem;
    margin-bottom: 1rem;
    background-color: #2C3E50;
    color: #ecf0f1;
    border-bottom: 2px solid #34495e;
}

.logo-img {
    width: 40px;
    height: 40px;
    margin-right: 0.8rem;
}

.title-section {
    display: flex;
    flex-direction: column;
}

.title {
    font-size: 1.8rem;
    margin: 0;
}

.subtitle {
    font-size: 0.85rem;
    color: #bdc3c7;
}

.nav-btn {
    padding: 0.6rem 1.2rem;
    margin-left: 0.5rem;
    background-color: transparent;
    border: 1px solid #ecf0f1;
    border-radius: 3px;
    cursor: pointer;
    transition: background-color 0.3s;
    color:#bdc3c7;
}

.nav-btn:hover {
    background-color: #34495e;
}

.logout-btn {
    border-color: #e74c3c;
    color: #e74c3c;
}

.logout-btn:hover {
    background-color: #e74c3c;
    color: #ecf0f1;
}

.username-text {
    margin-right: 1rem;
    font-weight: 500;
}
</style>