import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

// Mutations: state 값을 변경하는 로직 (동기적)
// Actions 는 비동기적 로직을 정의

export const store = new Vuex.Store({
  state: {
    userId: '',
    isLogin: false,
    nullReply: true,
    isWrite: true
    // isNotNull: false //clear form, sign
  },
  mutations: {
    login (state, payload) {
      state.userId = payload.value;
      state.isLogin = true;
    }
  },
  actions: {

  }
});
