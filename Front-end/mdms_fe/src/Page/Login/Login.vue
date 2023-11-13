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
import axios from 'axios';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import './Login.css'
export default {
  name: 'LoginPage',
  data() {
    return {
      email: '',
      password: '',
    };
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    return { store, router };
  },
  methods: {
    async handleLogin() {
      try {
        const response = await axios.post('http://localhost:3001/user/login', {
          email: this.email,
          password: this.password,
        });

        if (response.data.ok === 'ok') {
          const { token, admin, username } = response.data.data;

          localStorage.setItem('token', token);
          localStorage.setItem('username', username);
          localStorage.setItem('email', this.email);

          this.store.dispatch('login', {
            token: token,
            email: this.email,
            username: username, 
          });

          if (admin) {
            this.router.push('/admin-main');
          } else {
            this.router.push('/'); 
          }
        } else {
          alert('로그인 실패: ' + response.data.message);
        }
      } catch (error) {
        console.error('로그인 에러:', error);
        alert('로그인 에러');
      }
    },
    goToForgotPassword() {
      this.router.push('/forgot-password');
    },
  },
};
</script>