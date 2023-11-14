<template>
  <header class="header-container">
    <!-- 로고와 타이틀 섹션 -->
    <div class="left-section" @click="navigateToHome">
      <img src="@/assets/img/pruning-logo.png" alt="테스트로고" class="logo-img">
      <div class="title-section">
        <h1 class="title">MDMS : 데이터 정원사</h1>
        <p class="subtitle">메타데이터 관리 및 시각화 서비스</p>
      </div>
    </div>
    
    <!-- 네비게이션 바 -->
    <nav v-if="isLoggedIn" class="navbar-section">
      <span class="username-text">{{ username }}</span>
      <button class="nav-btn" @click="navigateToProfile">회원 정보</button>
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
    padding: 1.6rem 3.2rem; /* 기존 여백의 1.6배 */
    background-color: #2C3E50;
    color: #ecf0f1;
}

.left-section {
    display: flex;
    align-items: center;
}

.logo-img {
    width: 150px;
    height: auto; 
    margin-right: 1.6rem; 
}

.title-section {
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.title {
    font-size: 2.4rem; /* 제목 폰트 크기 1.6배 증가 */
    margin: 0;
    color: #ffffff;
}

.subtitle {
    font-size: 1.44rem; /* 서브타이틀 폰트 크기 1.6배 증가 */
    color: #bdc3c7;
}

.navbar-section {
    display: flex;
    align-items: center;
}

.username-text {
    margin-right: 1.6rem; /* 오른쪽 마진 1.6배 증가 */
    font-weight: 500;
    font-size: 1.28rem; /* 폰트 크기 1.6배 증가 */
    color: #ffffff;
}

.nav-btn {
    padding: 0.8rem 1.6rem; /* 버튼 내부 여백 1.6배 증가 */
    margin-left: 0.8rem; /* 왼쪽 마진 1.6배 증가 */
    background-color: transparent;
    border: 1px solid #ecf0f1;
    border-radius: 4.8px; /* 모서리 둥글기 1.6배 증가 */
    cursor: pointer;
    transition: background-color 0.3s;
    font-size: 1.28rem; /* 폰트 크기 1.6배 증가 */
    color: #bdc3c7;
}

.nav-btn:hover {
    background-color: #34495e;
    color: #ffffff;
}

.logout-btn {
    border-color: #e74c3c;
    color: #e74c3c;
}

.logout-btn:hover {
    background-color: #e74c3c;
    color: #ffffff;
}
</style>
