import { createStore } from 'vuex';

export default createStore({
  state: {
    isLoggedIn: false,
    token: ''
  },
  mutations: {
    setLoginState(state, payload) {
      state.isLoggedIn = payload.status;
      state.token = payload.token;
    }
  },
  actions: {
    login({ commit }, token) {
      commit('setLoginState', { status: true, token: token });
    },
    logout({ commit }) {
      commit('setLoginState', { status: false, token: '' });
    }
  },
  getters: {
    isLoggedIn: state => state.isLoggedIn,
    token: state => state.token
  }
});
