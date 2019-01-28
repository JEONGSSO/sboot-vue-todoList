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
          @click="delTodo(index)"> X </button>
      </li>
    </ul>
  </div>
</template>

<script>

const TODOS_LS = 'loadedTodos'

export default {
  name: 'todoPage',
  data () {
    return {
      name: null,
      todos: []
    }
  },
  methods: {
    delTodo (i) {
      this.todos.splice(i, 1)
      saveTodo(this.todos)
    },
    addTodo (name) {
      if (!isEmpty(name)) {
        this.todos.push({name: name})
        saveTodo(this.todos)
      }
      this.name = null
    },
    loadTodos () {
      const getTodos = localStorage.getItem(TODOS_LS)
      if (!isEmpty(getTodos)) {
        const parsedTodos = JSON.parse(getTodos)
        parsedTodos.forEach(todo => {
          this.todos.push({name: todo.name})
        })
      }
    }
  },
  mounted () {
    this.loadTodos()
  }
}

function isEmpty (value) {
  if (value) {
    return false
  } else {
    return true
  }
}

function saveTodo (todo) {
  localStorage.setItem(TODOS_LS, JSON.stringify(todo))
}

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
