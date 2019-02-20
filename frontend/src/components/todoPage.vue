<template>
  <div class="container">
    <!-- <h2>Todo List</h2> -->
    <div class="input-group">
      <input class="form-control" placeholder="Write a new Todo"
        @keyup.enter="addTodo(name)" v-model="name">
      <span class="input-group-btn">
        <button class="btn btn-outline-primary ml10"
        @click="addTodo(name)"> Add </button>
      </span>
    </div>
    <ul class="list-group mt10">
      <li class="list-group-item"
          v-for="(todo, index) in todos" :key="index">
        {{todo.content}}
        <button class="btn btn-danger btn-sm fr"
          @click="delTodo(todo)"> X </button>
      </li>
    </ul>
  </div>
</template>

<script>

import sendAxios from '../resources/sendAxios.js';

// const TODOS_LS = 'loadedTodos'
const gTodoURL = 'http://localhost:8080/todo';

export default {
  name: 'todoPage',
  data () {
    return {
      name: null,
      todos: []
    };
  },
  methods: {
    delTodo (targetTodo) {
      const deleteURL = `${gTodoURL}/delete/${targetTodo.id}`;
      this.todos.map((_todo, i, obj) => {
        if (_todo.id === targetTodo.id) {
          sendAxios('DELETE', deleteURL).then(result => {
            if (result.status === 200) {
              obj.splice(i, 1);
            } else {
              alert('Fail Remove');
            }
          });
        }
      });
      // localSaveTodo(this.todos)
    },
    addTodo (content) {
      if (!isEmpty(content)) {
        const addURL = `${gTodoURL}/regist`;
        sendAxios('POST', addURL, { content: content }).then(result => {
          this.getTodos();
          // vm.todos.push(content) 빈 상자가 나와서 getTodos를 임시 호출
          // 서버에 요청시 415 Invalid mime type charset=UTF-8 추가 해결
          // vm.axios.defaults.headers.post['Content-type'] = 'application/json;charset=UTF-8';
          // localSaveTodo(this.todos)
        });
      }
      this.name = null;
    },
    getTodos () {
      const vm = this;
      const loadURL = `${gTodoURL}/list`;
      sendAxios('GET', loadURL).then(result => {
        vm.todos = result.data;
      });
    }
  },
  mounted () {
    // this.getTodos();
    // this.localLoadTodos();
  }
};

function isEmpty (value) {
  if (value) {
    return false;
  } else {
    return true;
  }
}

// function localSaveTodo (todo) {
//   localStorage.setItem(TODOS_LS, JSON.stringify(todo))
// }

// localLoadTodos () {
//   const localLoadTodo = localStorage.getItem(TODOS_LS)
//   if (!isEmpty(localLoadTodo)) {
//     const parsedTodos = JSON.parse(localLoadTodo)
//     parsedTodos.forEach(todo => {
//       this.todos.push({name: todo.name})
//     })
//   }
// },

</script>

<style>
  .fr {
    float: right;
  }

  .ml10 {
    left: 10px;
  }

  .mt10 {
    margin-top: 10px
  }
</style>
