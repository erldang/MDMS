import { createStore } from 'vuex';

export default createStore({
  state: {
    isLoggedIn: false,
    token: '',
    email: '',
    isAdmin: false,
    username: '',
  },
  mutations: {
    setLoginState(state, payload) {
      state.isLoggedIn = payload.status;
      state.token = payload.token;
      state.email = payload.email;
      state.isAdmin = payload.isAdmin;
      state.username = payload.username;
    },
    setLogoutState(state) {
      state.isLoggedIn = false;
      state.token = '';
      state.email = '';
      state.isAdmin = false;
      state.username = '';
    }
  },
  actions: {
    login({ commit }, payload) {
      commit('setLoginState', payload);
    },
    logout({ commit }) {
      commit('setLogoutState');
      localStorage.removeItem('token'); 
      localStorage.removeItem('email'); 
      localStorage.removeItem('username');
    }
  },
  getters: {
    isLoggedIn: state => state.isLoggedIn,
    token: state => state.token,
    email: state => state.email,
    isAdmin: state => state.isAdmin,
    username: state => state.username,
  }
});
