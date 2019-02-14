<template>
   <div id="container">
      <div id="todoModal">
         <b-btn v-b-modal.todo>TodoList</b-btn>
      </div>
      <b-modal id="todo" title="Todo List">
        <todoList></todoList>
      </b-modal>

      <transition name="fade">
        <div class="alert alert-success alert-dismissible fade show" id="board" role="alert" v-show="isSuccess">글이 추가되었습니다.</div>
      </transition>
      <b-table id="board" striped hover :items="boards" :fields="fields">
        <template slot="title" slot-scope="boards">
          <b-btn id="read_btn" v-b-modal.read_board variant="link" @click="readBoard(boards.item.bno)">
            {{ boards.item.title }}
          </b-btn>
        </template>
        <template slot="regdata" slot-scope="boards">
          {{ moment(boards.item.regdata).fromNow() }}
        </template>
      </b-table>

      <b-btn id="write_btn" v-b-modal.write_board >글 쓰기</b-btn>
      <b-modal id="write_board" title="글 쓰기" @ok="onSubmit()">
        <div class="input-group input-group-sm mb-3">
          <div class="input-group-prepend mb-3">
            <span class="input-group-text">제목</span>
            <input type="text" class="form-control" v-model="form.title">
            <span class="input-group-text">작성자</span>
            <input type="text" class="form-control" v-model="form.writer">
          </div>
          <textarea rows="5" cols="70" v-model="form.content"></textarea>
        </div>
      </b-modal>
      <!-- TODO -->
      <b-modal id="read_board" title="form.title" @ok="onSubmitt()">
        <div class="input-group input-group-sm mb-3">
          <div class="input-group-prepend mb-3">
            <span class="input-group-text">작성자</span>
            <input type="text" class="form-control" readonly v-model="form.writer">
          </div>
            <textarea rows="5" cols="70" readonly v-model="form.content"></textarea>
        </div>
      </b-modal>
   </div>
</template>

<script>

import todoList from './todoPage'

const gBoardURL = 'http://localhost:8080'

export default {
  name: 'board',
  components: {
    todoList
  },
  data () {
    return {
      fields: [ 'bno', 'writer', 'title', 'regdata' ],
      boards: [],
      form: {
        writer: '',
        title: '',
        content: ''
      },
      isSuccess: false
    }
  },
  methods: {
    getBoardList () {
      const vm = this
      vm.$http.get(`${gBoardURL}/list`).then(result => {
        vm.boards = result.data
      })
    },
    readBoard (bno) {
      const vm = this
      vm.$http.get(`${gBoardURL}/read/${bno}`).then(result => {
        vm.form.writer = result.data.writer
        vm.form.content = result.data.content
        vm.form.title = result.data.title
        // console.log(result);
      })
    },
    onSubmit () {
      const vm = this
      vm.$http.post(`${gBoardURL}/regist`, { writer: vm.form.writer, title: vm.form.title, content: vm.form.content }).then(result => {
        vm.isSuccess = true
        vm.getBoardList()
        vm.alertTimeout(vm.isSuccess)
      //   vm.boards.push(result.data) bno 가져오기
      })
    },
    alertTimeout (isSuccess) {
      if (isSuccess) {
        setTimeout(() => {
          this.isSuccess = false
        }, 2000)
      }
    }
  },
  mounted () {
    this.getBoardList()
  }
}

</script>

<style>
  #container {
    margin-left: 15%;
  }

  #write_btn {
    float: left;
  }

  .alert-dismissible {
    padding-right: 1rem;
  }

  #write_board {
    text-align: center;
  }

  #todoModal {
    float : right;
    padding-right: 50px;
    display: none;
  }

  #board {
    inline-size: 80%;
  }

  .show {
    display: block;
  }

  .hidden {
    display: none;
  }

  .fl {
    float: left;
  }

  .fade-enter-active, .fade-leave-active {
   transition: opacity .5s;
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
    opacity: 0;
  }
</style>
