import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/Page/Login/Login.vue'
import RegisterPage from '@/Page/Register/Register.vue'
import MainPage from '@/Page/Main/Main.vue'  
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
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
