import ProfilePage from '@/Page/Profile/UserProfile.vue'
import { createRouter, createWebHistory } from 'vue-router';
import LoginPage from '@/Page/Login/Login.vue';
import RegisterPage from '@/Page/Register/Register.vue';
import MainPage from '@/Page/Main/Main.vue';
import ForgotPassword from '@/Page/ForgotPassword/ForgotPassword.vue';
import DataDetail from '@/Page/DataDetail/DataDetail.vue';
import DataRegister from '@/Page/DataRegister/DataRegister.vue'
import AdminMainPage from '@/Page/AdminMain/AdminMain.vue'
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
  // 로그인, 회원가입, 비밀번호 찾기는 제한 없이 접근 가능
  if (to.name === 'Login' || to.name === 'Register' || to.name === 'ForgotPassword') {
    next();
  } else {
    const token = localStorage.getItem('token');
    const isAdmin = localStorage.getItem('admin') === 'true'; // 문자열을 불리언으로 변환

    // 토큰이 있는 경우
    if (token) {
      // 관리자가 아니면서 관리자 전용 페이지 접근 시 메인 페이지로 리다이렉션
      if (to.path.startsWith('/admin') && !isAdmin) {
        if (from.name !== 'Main') {
          next({ name: 'Main' });
        } else {
          next(false);
        }
      } 
      // 관리자이면서 일반 사용자 전용 페이지 접근 시 관리자 메인 페이지로 리다이렉션
      // 하지만, 프로필과 프로필 수정 페이지는 예외로 처리
      else if (!to.path.startsWith('/admin') && isAdmin && to.name !== 'Profile' && to.name !== 'EditProfilePage') {
        if (from.name !== 'AdminMainPage') {
          next({ name: 'AdminMainPage' });
        } else {
          next(false);
        }
      } 
      // 그 외의 경우 정상적으로 페이지 접근 허용
      else {
        next();
      }
    } 
    // 토큰이 없는 경우 로그인 페이지로 리다이렉션
    else {
      if (from.name !== 'Login') {
        next({ name: 'Login' });
      } else {
        next(false);
      }
    }
  }
});



export default router;
