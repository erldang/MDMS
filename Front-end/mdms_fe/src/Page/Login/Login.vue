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
      password: ''
    }
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    return { store, router };
  },
  methods: {
    async handleLogin() { //만약 이걸로 관리자 사용자 구분이 되지 않는다면 router 에 관리자 사용자 구분을 위해 store 에 관리자 유무를 저장하고 이를 가지고 판단하여 이동시키기
        try {
          const response = await axios.post('http://localhost:3001/user/login', {
            "email": this.email,
            "password": this.password
          });

          if (response.data.ok === 'ok') {
            // 로그인 성공, JWT 토큰을 로컬 스토리지에 저장
            localStorage.setItem('token', response.data.message);
            // Vuex Store에 상태 저장

            console.log('login시 token : ' , response.data.message);

            this.store.dispatch('login', response.data.message);

            // 관리자 여부에 따라 리다이렉션 경로 변경
            if (response.data.data.admin) {
              this.router.push('/admin-main');
            } else {
              this.router.push('/');
            }
          } else {
            // 로그인 실패 처리
            alert('로그인 실패: ' + response.data.message);
          }
        } catch (error) {
          console.error('로그인 에러:', error);
          alert('로그인 에러');
        }
      },
    goToForgotPassword() {
      this.router.push('/forgot-password');
    }
  }
}
</script>