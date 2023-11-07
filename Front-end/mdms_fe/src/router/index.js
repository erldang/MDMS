import ProfilePage from '@/Page/Profile/Profile.vue'
import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '@/Page/Login/Login.vue';
import RegisterPage from '@/Page/Register/Register.vue';
import MainPage from '@/Page/Main/Main.vue';
import ForgotPassword from '@/Page/ForgotPassword/ForgotPassword.vue';
import DataDetail from '@/Page/DataDetail/DataDetail.vue';
import DataRegister from '@/Page/DataRegister/DataRegister.vue'

import store from '@/store'; // Vuex store 임포트

const routes = [
  {
    path: '/',
    name: 'Main',
    component: MainPage
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterPage
  },
  {
    path: '/profile',
    name: 'Profile',
    component: ProfilePage
  },
  {
    path: '/forgot-password', // 비밀번호 찾기 경로 추가
    name: 'ForgotPassword',
    component: ForgotPassword
  },
  {
    path: '/data-detail/:id', // Assuming you want to have an ID parameter
    name: 'DataDetail',
    component: DataDetail,
    props: true, // This allows us to pass the route params as props to the component
  },
  {
    path: '/data-register',
    name: 'DataRegister',
    component: DataRegister,
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

router.beforeEach((to, from, next) => {
  // 로그인, 회원가입, 비밀번호 찾기 페이지는 로그인 여부와 상관없이 이동 가능
  if (to.name === 'Login' || to.name === 'Register' || to.name === 'ForgotPassword') {
    next();
  } else if (!store.state.isLoggedIn) {
    next({ name: 'Login' });
  } else {
    next();
  }
});


export default router;
