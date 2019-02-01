<template>
  <div class="container">
    <h2>Todo List</h2>
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
        {{todo.name}}
        <button class="btn btn-danger btn-sm fr"
          @click="delTodo(todo)"> X </button>
      </li>
    </ul>
  </div>
</template>

<script>

const TODOS_LS = 'loadedTodos'
const gURL = 'https://todos.garam.xyz/api/todos'

export default {
  name: 'todoPage',
  data () {
    return {
      name: null,
      todos: []
    }
  },
  methods: {
    delTodo (todo) {
      const vm = this

      this.todos.map((_todo, i, obj) => {
        if (_todo.id === todo.id) {
          vm.$http.delete(`${gURL}/${todo.id}`).then(result => {
            if (result.status === 200) {
              obj.splice(i, 1)
            } else {
              alert('Fail Remove')
            }
          })
        }
      })
      // localSaveTodo(this.todos)
    },
    addTodo (name) {
      if (!isEmpty(name)) {
        const vm = this

        vm.$http.defaults.headers.post['Content-type'] = 'application/json'
        vm.$http.post(gURL, { name: name }).then(result => {
          vm.todos.push(result.data)
        // localSaveTodo(this.todos)
        })
      }
      this.name = null
    },
    localLoadTodos () {
      const localLoadTodo = localStorage.getItem(TODOS_LS)
      if (!isEmpty(localLoadTodo)) {
        const parsedTodos = JSON.parse(localLoadTodo)
        parsedTodos.forEach(todo => {
          this.todos.push({name: todo.name})
        })
      }
    },
    getTodos () {
      const vm = this
      vm.$http.get(gURL).then(result => {
        // console.log(result.data.data)
        vm.todos = result.data.data
      })
    }
  },
  mounted () {
    // this.localLoadTodos()
    this.getTodos()
  }
}

function isEmpty (value) {
  if (value) {
    return false
  } else {
    return true
  }
}

// function localSaveTodo (todo) {
//   localStorage.setItem(TODOS_LS, JSON.stringify(todo))
// }

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
