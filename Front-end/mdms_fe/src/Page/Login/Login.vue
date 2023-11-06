<template>
    <div class="login-container">
        <h1>로그인</h1>

        <form @submit.prevent="handleLogin">
            <!-- 이메일(ID) 입력 -->
            <div class="input-group">
                <label for="email">이메일(ID)</label>
                <input type="email" id="email" v-model="email" placeholder="이메일 입력" required />
            </div>

            <!-- 비밀번호 입력 -->
            <div class="input-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" v-model="password" placeholder="비밀번호 입력" required />
            </div>

            <!-- 로그인 버튼 -->
            <button type="submit">로그인</button>
        </form>

        <div class="links">
            <a href="#" @click.prevent="goToForgotPassword">아이디/비밀번호 찾기</a>
            <router-link to="/register">회원가입</router-link>
        </div>
    </div>
</template>

<script>
// import { useStore } from 'vuex';
// import { useRouter } from 'vue-router';

export default {
    name: 'LoginPage',
    data() {
        return {
            email: '',
            password: ''
        }
    },
    methods: {
        handleLogin() {
            // 현재는 별도의 확인 없이 바로 로그인 상태로 변경합니다.
            this.$store.dispatch('login');
            this.$router.push('/');
        },
        goToForgotPassword() {
            // 아이디/비밀번호 찾기 페이지로 이동
            this.$router.push('/forgot-password');
        }
    }
}
</script>

<style scoped>
.login-container {
    width: 300px;
    margin: 0 auto;
    padding: 2rem;
    border: 1px solid #eaeaea;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h1 {
    text-align: center;
    margin-bottom: 2rem;
}

.input-group {
    margin-bottom: 1rem;
}

label {
    display: block;
    margin-bottom: 0.5rem;
    font-weight: 600;
}

input {
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #eaeaea;
    border-radius: 4px;
}

button {
    width: 100%;
    padding: 0.75rem;
    background-color: #007BFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
}

button:hover {
    background-color: #0056b3;
}

.links {
    margin-top: 1rem;
    display: flex;
    justify-content: space-between;
}
</style>
