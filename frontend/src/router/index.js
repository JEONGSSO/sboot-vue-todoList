import Vue from 'vue';
import Router from 'vue-router';
import home from '@/components/home';
import todoPage from '@/components/todoPage';
// import board from '@/components/Board';
import Board from '../components/Board';

Vue.use(Router);

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/home',
      name: 'home',
      component: home
    },
    {
      path: '/todo',
      name: 'todo',
      component: todoPage
    },
    {
      path: '/',
      name: 'board',
      component: Board
    }
  ]
});
