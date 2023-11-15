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
import DataMapPage from '@/Page/DataMap/DataMap.vue'
import RegisterCustomTermPage from '@/Page/RegisterCustomTerm/RegisterCustomTerm.vue'
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
    path: '/data-detail',
    name: 'DataDetail',
    component: DataDetail,
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
  {
    path: '/user-data-map',
    name: 'DataMapPage',
    component: DataMapPage
  },
  {
    path: '/register-custom-term',
    name: 'RegisterCustomTermPage',
    component: RegisterCustomTermPage
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

// 라우터 가드에서 관리자 여부를 체크하여 리다이렉션
router.beforeEach((to, from, next) => {
  if (to.name === 'Login' || to.name === 'Register' || to.name === 'ForgotPassword' || to.name === 'RegisterCustomTermPage') {
    next();
  } else {
    const token = localStorage.getItem('token');
    const isAdmin = localStorage.getItem('admin') === 'true'; // 문자열을 불리언으로 변환

    if (token) {
      if (to.path.startsWith('/admin') && !isAdmin) {
        if (from.name !== 'Main') { // 리다이렉션 루프 방지
          next({ name: 'Main' });
        } else {
          next(false); // 현재 위치 유지
        }
      } else if (!to.path.startsWith('/admin') && isAdmin) {
        if (from.name !== 'AdminMainPage') { // 리다이렉션 루프 방지
          next({ name: 'AdminMainPage' });
        } else {
          next(false); // 현재 위치 유지
        }
      } else {
        next();
      }
    } else {
      if (from.name !== 'Login') { // 리다이렉션 루프 방지
        next({ name: 'Login' });
      } else {
        next(false); // 현재 위치 유지
      }
    }
  }
});



export default router;
