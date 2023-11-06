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
            username: '유저이름'
        };
    },
    computed: {
        ...mapGetters(['isLoggedIn'])
    },
    methods: {
<<<<<<< HEAD
        navigateToProfile() {
            this.$router.push('/profile');
=======
        ...mapActions(['login', 'logout']),
        navigateToProfile() {
            this.$router.push('/profile');
        },
        navigateToHome() {
            this.$router.push('/').then(() => {
                this.$router.go(); // 라우터가 재확인하도록 강제
            });
        },
        logoutAction() {
            this.logout();
            this.$router.push('/').then(() => {
                this.$router.go();
            });
>>>>>>> origin/main
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
<<<<<<< HEAD
    background-color: #333;
    color: white;
    border-bottom: 1px solid black;
    padding: 1.25rem;  
    margin: 0.9375rem; 
=======
    margin-bottom: 1rem;
    background-color: #2C3E50;
    color: #ecf0f1;
    border-bottom: 2px solid #34495e;
>>>>>>> origin/main
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