import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/Page/Login/Login.vue'
import RegisterPage from '@/Page/Register/Register.vue'
import MainPage from '@/Page/Main/Main.vue'
import ProfilePage from '@/Page/Profile/Profile.vue'
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
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
