// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.

/*
  프로젝트의 Base 파일입니다.
  가장 먼저 실행되는 javascript 파일입니다.
  Vue 인스턴스를 새로 만들고 시작하는 부분입니다.
  전역으로 처리해야할 일이 있으면 이 곳에서 진행하시면 됩니다.
*/

import Vue from 'vue';
import BootstrapVue from 'bootstrap-vue';
import moment from 'moment';

import App from './App';
import router from './router';

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';

Vue.config.productionTip = false;
Vue.use(BootstrapVue);
Vue.prototype.moment = moment;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  BootstrapVue,
  components: { App },
  template: '<App/>'
});
