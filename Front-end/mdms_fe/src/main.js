import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import axios from 'axios';

// 로컬 스토리지에서 토큰을 가져와서 스토어 상태를 초기화
const token = localStorage.getItem('token');
if (token) {
  store.dispatch('login', token);
}

// Axios 기본값 설정
axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

const app = createApp(App).use(router).use(store).mount('#app');
