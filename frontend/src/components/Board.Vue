<template>
  <div id="container">
    <!-- <div id="todoModal">
      <b-btn v-b-modal.todo>TodoList</b-btn>
    </div>
    <b-modal id="todo" title="Todo List">
      <todoList></todoList>
    </b-modal> -->

    <transition name="fade">
      <div class="alert alert-success alert-dismissible fade show" id="board" role="alert" v-show="isSuccess">작업이 완료 되었습니다.</div>
    </transition>

    <div class="nav">
      <b-navbar toggleable="lg" type="dark" variant="secondary">
        <b-navbar-brand href="#">Home</b-navbar-brand>
        <b-navbar-nav>
          <b-nav-form>
            <b-form-input size="sm" class="mr-sm-2" type="text" placeholder="Search" />
            <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>
          </b-nav-form>
            <b-btn v-show="!this.$store.state.isLogin" variant="warning"
                v-b-modal.sign_modal @click="$EventBus.$emit('clearSign')">로그인</b-btn>
            <b-btn v-show="this.$store.state.isLogin" variant="warning"
                @click="$EventBus.$emit('logout')">로그아웃</b-btn>
        </b-navbar-nav>
      </b-navbar>
    </div>

    <login/>

    <b-table id="board" striped hover :items="boards" :fields="fields">
      <template slot="title" slot-scope="boards">
        <b-btn id="read_btn" v-b-modal.modal variant="link" @click="showModal = true, readBoard(boards.item.bno)">
          {{ `${boards.item.title} [${boards.item.replycnt}] `}}
        </b-btn>
      </template>
      <template slot="regdata" slot-scope="boards">
        {{ moment(boards.item.regdata).fromNow() }}
      </template>
    </b-table>

    <b-btn v-show="!this.$store.state.isLogin" id="write_btn" class="fl" @click="$EventBus.$emit('loginUserValid')">글 쓰기</b-btn>
    <b-btn v-show="this.$store.state.isLogin" id="write_btn" class="fl" v-b-modal.modal @click="resetFrom()">글 쓰기</b-btn>
    <span>
      <Paginate
        :pageCount="totalRows"
        :clickHandler="boardPaging"
        :prevText="'<'"
        :nextText="'>'"
        no-li-surround
      >
      </Paginate>
    </span>

    <b-modal id="modal" v-show="showModal" ref="modal">
      <div class="input-group input-group-sm">
        <div class="input-group-append mb-3">
          <span class="input-group-text">제목</span>
          <input type="text" class="form-control" v-model="form.title" v-bind:readonly="isReadOnly"/>
          <span v-show="!this.$store.state.isWrite" class="input-group-text">작성자</span>
          <input v-show="!this.$store.state.isWrite" type="text" class="form-control" v-model="form.writer" readonly/>
        </div>
          <textarea rows="5" cols="70" v-model="form.content" v-bind:readonly="isReadOnly"></textarea>
      </div>

      <reply/>

      <div slot="modal-footer">
        <b-button class="float-right ml-1" variant="primary" @click="createBorad()" v-show="!isNull && this.$store.state.isWrite">OK</b-button>
        <div v-show="!this.$store.state.isWrite && form.writer === this.$store.state.userId">
          <b-button class="float-right ml-1" variant="danger" @click="deleteBoard(form.bno)">삭제</b-button>
          <b-button class="float-right" variant="primary" @click="updateBoard(form.bno)">
          {{btnMessage = isReadOnly ? '수정' : 'OK'}}
        </b-button>
        </div>
      </div>
    </b-modal>

  </div>
</template>

<script>
import Paginate from 'vuejs-paginate';

// import todoList from './todoPage';
import login from './login.vue';
import reply from './reply.vue';
import sendAxios from '../resources/sendAxios.js';

const gBoardURL = 'http://localhost:8080/board';
let gPageNum;

export default {
  name: 'board',
  components: {
    Paginate,
    login,
    reply
  },
  data () {
    return {
      fields: [ 'bno', 'writer', 'title', 'regdata' ],
      boards: [],
      form: {
        bno: '',
        writer: '',
        title: '',
        content: ''
      },
      perPage: 5,
      totalRows: 0,
      btnMessage: '',
      isSuccess: false,
      isReadOnly: true,
      showModal: true,
      isNull: true
    };
  },
  methods: {
    boardCount () {
      const vm = this;
      const getCountURL = `${gBoardURL}/count`;
      sendAxios('GET', getCountURL).then(result => {
        vm.totalRows = Math.ceil(result.data / vm.perPage);
      });
      vm.boardPaging();
    },
    boardPaging (pageNum = 1) {
      gPageNum = pageNum;
      const getPerPageURL = `${gBoardURL}/page/${pageNum}`;
      sendAxios('GET', getPerPageURL).then(result => {
        this.boards = result.data;
      });
    },
    readBoard (bno) {
      const vm = this;
      const readURL = `${gBoardURL}/bno/${bno}`;
      vm.isReadOnly = true;
      this.$store.state.isWrite = false;
      sendAxios('GET', readURL).then(result => {
        vm.form = result.data;
      });
      this.$EventBus.$emit('replyAll', bno);
    },
    createBorad () {
      const vm = this;
      sendAxios('POST', gBoardURL, vm.form).then(() => {
        vm.$refs.modal.hide();
        vm.jobComplete(true);
      });
    },
    updateBoard (bno) {
      const vm = this;
      const updateURL = `${gBoardURL}/bno/${bno}`;
      vm.isReadOnly = false;
      if (vm.btnMessage === 'OK') {
        sendAxios('PATCH', updateURL, vm.form).then(() => {
          vm.jobComplete(true, false);
        });
      }
    },
    deleteBoard (bno) {
      if (!this.$store.state.nullReply) {
        return alert('댓글이 있는 게시물은 삭제할 수 없습니다.');
      }
      if (confirm('삭제 하시겠습니까?')) {
        const vm = this;
        const deleteURL = `${gBoardURL}/bno/${bno}`;
        sendAxios('DELETE', deleteURL).then(() => {
          vm.jobComplete(true, false);
        });
      }
    },
    jobComplete (doneSuccess, isShow) {
      const vm = this;
      vm.boardPaging();
      vm.isSuccess = doneSuccess;
      vm.showModal = isShow;

      if (doneSuccess) {
        setTimeout(() => {
          vm.isSuccess = false;
        }, 2000);
      }
    },
    resetFrom () {
      const vm = this;
      vm.form.title = '';
      vm.form.content = '';
      vm.form.writer = this.$store.state.userId;
      this.$store.state.isWrite = true;
      vm.isReadOnly = false;
      vm.isNull = true;
      vm.$refs.modal.show();
    }
  },
  mounted () {
    this.boardCount();
  },
  created () {
    this.$EventBus.$on('resetFrom', () => {
      this.resetFrom();
    });
    this.$EventBus.$on('updateReplycnt', () => {
      sendAxios('GET', `${gBoardURL}/page/${gPageNum}`).then(result => {
        this.boards = result.data;
      });
    });
  },
  watch: {
    form: {
      handler (val) {
        if (val.title && val.content && val.writer) {
          this.isNull = false;
        } else {
          this.isNull = true;
        }
        // this.isNull = val.title && val.writer && val.content ? false : true;
      },
      deep: true
    }
  }
};

</script>

<style>
  #container {
    margin-left: 15%;
  }

  /* .pagination {
    margin-left: 16%;
  } */

  .nav {
    /* position: absolute; */
    /* bottom: 92.5%; */
    /* left: 0px;
    top: 0px; */
    margin: 0;
    padding: 0;
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

  .fr {
    float: right;
  }

  .p100 {
    padding-right: 100%;
  }

  .r-15 {
    right: 15px;
  }

  .fade-enter-active, .fade-leave-active {
   transition: opacity .5s;
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
    opacity: 0;
  }
</style>

<!-- <b-modal id="write_board" title="글 쓰기" ref="modal">
  <div class="input-group input-group-sm mb-3">
    <div class="input-group-prepend mb-3">
      <span class="input-group-text">제목</span>
      <input type="text" class="form-control" v-model="form.title">
      <span class="input-group-text">작성자</span>
      <input type="text" class="form-control" v-model="form.writer">
    </div>
    <textarea rows="5" cols="70" v-model="form.content"></textarea>
  </div>
  <div slot="modal-footer">
    <b-button v-show="isNull" class="float-right ml-1" variant="primary" @click="createBorad()">OK</b-button>
  </div>
</b-modal>
<b-modal id="read_board" v-show="showModal">
  <div class="input-group input-group-sm mb-3">
    <div class="input-group-prepend mb-3">
      <span class="input-group-text">제목</span>
        <input type="text" class="form-control" v-model="form.title" v-bind:readonly="isReadOnly">
      <span class="input-group-text">작성자</span>
      <input type="text" class="form-control" readonly v-model="form.writer">
    </div>
      <textarea rows="5" cols="70" v-model="form.content" v-bind:readonly="isReadOnly"></textarea>
  </div>
    <div slot="modal-footer">
      <b-button class="float-right ml-1" variant="danger" @click="deleteBoard(form.bno)">삭제</b-button>
      <b-button class="float-right" variant="primary" @click="isReadOnly = false, updateBoard(form.bno)">
        {{btnMessage = isReadOnly ? '수정' : 'OK'}}
      </b-button>
    </div>
</b-modal> -->

<!--    getBoardList () {
      const vm = this;
      const getListURL = `${gBoardURL}/list`;
      sendAxios('GET', getListURL).then(result => {
        vm.boards = result.data;
        vm.perPage = Math.ceil(result.data.length / 5);
      });
    }, -->
