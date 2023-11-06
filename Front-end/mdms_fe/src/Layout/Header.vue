<template>
    <header>
        <!-- 로고 (여기서는 간단한 텍스트로 표현) -->
        <div class="logo">로고</div>

        <!-- 홈페이지 제목 -->
        <div class="title">MDMS</div>

        <!-- Navbar -->
        <!-- isLoggedIn 변수는 사용자의 로그인 상태 -->
        <nav v-if="isLoggedIn">
            <ul>
                <li>{{ username }}</li> <!-- 사용자 이름 -->
<<<<<<< HEAD
                <li @click="profile">회원 정보</li>
=======
                <li @click="navigateToProfile">회원 정보</li>
>>>>>>> origin/main
                <li>이력 조회</li>
                <li>로그아웃</li>
            </ul>
        </nav>
    </header>
</template>

<script>

export default {
    name: 'PageHeader',
    data() {
        return {
            isLoggedIn: true,  // 사용자의 로그인 상태를 나타내는 변수
            username: '테스트이름' // 로그인한 사용자의 이름
        };
    },
    methods: {
<<<<<<< HEAD
            profile() {
            // 현재는 별도의 확인 없이 바로 로그인 상태로 변경합니다.
            this.$router.push('/profile');
        }
    }
=======
    navigateToProfile() {
        this.$router.push('/profile');
    }
}   
>>>>>>> origin/main
}
</script>

<style scoped>
header {
    display: flex;
    align-items: center;
    padding: 1rem 2rem;
    background-color: #333;
    color: white;
    border-bottom: 1px solid black;

    padding: 1.25rem;  
    margin: 0.9375rem; 
}

.logo {
    flex: 1;
    margin-right: 1rem;
    padding: 0.5rem;
}

.title {
    flex: 2;
    margin-right: 1rem;
    padding: 0.5rem;
}

nav {
    flex: 3;
    margin-right: 1rem;
    padding: 0.5rem;
}

nav ul {
    display: flex;
    gap: 1rem;
    list-style-type: none;
    /* 리스트 점 제거 */
    padding-left: 0;
    /* 리스트 패딩 제거 */
}</style>
