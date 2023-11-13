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
import EditProfilePage from '@/Page/EditProfilePage/EditProfilePage.vue'
// import store from '@/store';

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
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: ForgotPassword
  },
  {
    path: '/data-detail/:id', 
    name: 'DataDetail',
    component: DataDetail,
    props: true, 
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
  },
  {
    path: '/edit-profile',
    name: 'EditProfilePage',
    component: EditProfilePage
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

// 라우터 가드에서 관리자 여부를 체크하여 리다이렉션
router.beforeEach((to, from, next) => {
  if (to.name === 'Login' || to.name === 'Register' || to.name === 'ForgotPassword') {
    next();
  } else {
    // 토큰을 로컬 스토리지에서 확인
    const token = localStorage.getItem('token');
    const isAdmin = localStorage.getItem('admin'); // Vuex store에서 관리자 여부를 가져옵니다.

    if (token) {
      // 관리자 전용 페이지에 접근 시도 시 관리자 여부를 체크합니다.
      if (to.path.startsWith('/admin') && !isAdmin) {
        // 관리자가 아닌데 관리자 페이지에 접근하려 하면 메인 페이지로 리다이렉트합니다.
        next({ name: 'Main' });
      } else if (!to.path.startsWith('/admin') && isAdmin) {
        // 관리자인데 일반 사용자 페이지에 접근하려 하면 관리자 메인 페이지로 리다이렉트합니다.
        next({ name: 'AdminMainPage' });
      } else {
        // 그 외의 경우에는 요청한 페이지로 정상 이동합니다.
        next();
      }
    } else {
      // 토큰이 없다면 로그인 페이지로 리다이렉트합니다.
      next({ name: 'Login' });
    }
  }
});


export default router;
