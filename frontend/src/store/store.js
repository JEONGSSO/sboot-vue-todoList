import Vue from 'vue';
import Vuex from 'vuex';
import * as getters from '../getters';

Vue.use(Vuex);

// Mutations: state 값을 변경하는 로직 (동기적)
// Actions 는 비동기적 로직을 정의

export const store = new Vuex.Store({
  getters,
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
    },
    logout (state) {
      state.userId = '';
      state.isLogin = false;
      alert('로그아웃 되었습니다!');
    },
    increment (state, payload) {
      console.log(state);
      console.log(payload.payload);
    }
  },
  actions: {
    increment ({commit}, payload) {
      commit('increment', payload);
    }
  }
});
