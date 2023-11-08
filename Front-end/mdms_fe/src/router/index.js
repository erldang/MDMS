import ProfilePage from '@/Page/Profile/UserProfile.vue'
import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '@/Page/Login/Login.vue';
import RegisterPage from '@/Page/Register/Register.vue';
import MainPage from '@/Page/Main/Main.vue';
import ForgotPassword from '@/Page/ForgotPassword/ForgotPassword.vue';
import DataDetail from '@/Page/DataDetail/DataDetail.vue';
import DataRegister from '@/Page/DataRegister/DataRegister.vue'
import AdminMainPage from '@/Page/AdminMain/AdminMain.vue'
import AdminCustomPage from '@/Page/AdminCustom/AdminCustom.vue'
import AdminDataMapPage from '@/Page/AdminDataMap/AdminDataMap.vue'
import AdminUserRecordPage from '@/Page/AdminUserRecord/AdminUserRecord.vue'
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
  },
  {
    path: '/admin-main',
    name: 'AdminMainPage',
    component: AdminMainPage,
  },
  {
    path: '/custom-standard-terms',
    name: 'AdminCustomPage',
    component: AdminCustomPage,
  },
  {
    path: '/admin-datamap',
    name: 'AdminDataMapPage',
    component: AdminDataMapPage,
  },
  {
    path: '/admin-user-record',
    name: 'AdminUserRecordPage',
    component: AdminUserRecordPage,
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
  } else {
    // 토큰을 로컬 스토리지에서 확인합니다.
    const token = localStorage.getItem('token');
    
    // 로그인이 필요한 페이지에 접근할 때 토큰 또는 Vuex 상태를 체크합니다.
    if (token || store.state.isLoggedIn) {
      // 토큰이 있거나, Vuex 상태가 로그인 상태라면 다음 페이지로 이동합니다.
      next();
    } else {
      // 토큰이 없거나, Vuex 상태가 로그인 상태가 아니라면 로그인 페이지로 리다이렉트합니다.
      next({ name: 'Login' });
    }
  }
});


export default router;
