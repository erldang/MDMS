import ProfilePage from '@/Page/Profile/Profile.vue'
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
  // 로그인, 회원가입, 비밀번호 찾기 페이지는 로그인 여부와 상관없이 이동 가능 //  to.name === 'AdminUserRecordPage' || to.name === 'AdminDataMapPage' 는 테스트를 위한 코드입니다 발견하면 지워주시길 바랍니다.
  if (to.name === 'Login' || to.name === 'Register' || to.name === 'ForgotPassword'|| to.name === 'AdminUserRecordPage' || to.name === 'AdminDataMapPage') {
    next();
  } else if (!store.state.isLoggedIn) {
    next({ name: 'Login' });
  } else {
    next();
  }
});


export default router;
