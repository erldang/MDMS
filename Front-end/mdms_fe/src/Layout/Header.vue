<template>
    <header class="header-container">
      <!-- Logo & Title Section -->
      <div class="left-section" @click="navigateToHome">
        <img src="@/assets/img/test-logo.png" alt="테스트로고" class="logo-img">
        <div class="title-section">
          <h1 class="title">MDMS</h1>
          <p class="subtitle">메타데이터 관리 서비스</p>
        </div>
      </div>
  
      <!-- Navbar -->
      <nav v-if="isLoggedIn" class="navbar-section">
        <span class="username-text">{{ username }}</span>
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
    data() {
      return {
        // 이 부분은 실제로 사용자 정보를 Vuex 스토어나 서버로부터 가져와서 설정해야 합니다.
        username: '유저이름'
      };
    },
    computed: {
      ...mapGetters(['isLoggedIn'])
    },
    methods: {
      ...mapActions(['logout']), // Vuex에서 logout 액션을 매핑합니다.
      navigateToProfile() {
        this.$router.push('/profile');
      },
      navigateToHome() {
        this.$router.push('/');
      },
      logoutAction() {
        this.logout().then(() => { // 로그아웃 액션을 실행하고, 성공적으로 로그아웃한 후에 홈으로 이동합니다.
          this.$router.push('/').then(() => {
            window.location.reload(); // 페이지를 새로고침하여 앱 상태를 초기화합니다.
          });
        });
      }
    }
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