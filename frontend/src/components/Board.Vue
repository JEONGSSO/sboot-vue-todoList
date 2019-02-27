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

      <b-navbar-toggle target="nav_collapse" />

      <b-collapse is-nav id="nav_collapse">
        <!-- Right aligned nav items -->
        <b-navbar-nav>
          <b-nav-form>
            <b-form-input size="sm" class="mr-sm-2" type="text" placeholder="Search" />
            <b-button size="sm" class="my-2 my-sm-0" type="submit">Search</b-button>
          </b-nav-form>
            <b-btn v-show="!isLogin" variant="warning" v-b-modal.sign_modal @click="clearSign()">로그인</b-btn>
            <b-btn v-show="isLogin" variant="warning" @click="logout()">로그아웃</b-btn>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>

  <b-modal size="sm" id="sign_modal" ref="sign">
    <transition>
      <div class="alert alert-danger alert-dismissible fade show" role="alert" v-show="failLogin">아이디 또는 비밀번호가 올바르지 않습니다.</div>
    </transition>
    <transition>
      <div class="alert alert-primary alert-dismissible fade show" role="alert" v-show="notLogin">로그인이 필요한 서비스 입니다.</div>
    </transition>
    <div v-show="isNotSingUp">
      <p>회원이 아니신가요?</p>
      <b-button variant="success" class="mt-3" @click="isNotSingUp = false">회원가입</b-button>
      <hr>
   </div>
    <b-form>
      <div v-if="!isNotSingUp">
        <p>회원 가입</p>
        <b-input id="sign_id" placeholder="아이디는 4-12 이내로 써주세요."
                v-model="signForm.uid" :state="validationId" class="mb-2"/>
        <b-input type="password" id="sign_pw" placeholder="패스워드는 4-12 이내로 써주세요."
                v-model="signForm.upw" :state="validationPw"/>
      </div>
      <div v-else>
        <b-input id="sign_id" placeholder="ID" v-model="loginForm.uid" class="mb-2"/>
        <b-input type="password" id="sign_pw" placeholder="Password" v-model="loginForm.upw"/>
      </div>
    </b-form>
    <div slot="modal-footer">
      <b-button class="float-right ml-1" variant="primary" @click="sign()">OK</b-button>
    </div>
  </b-modal>

    <b-table id="board" striped hover :items="boards" :fields="fields" :busy.sync="isBusy">
      <template slot="title" slot-scope="boards">
        <b-btn id="read_btn" v-b-modal.modal variant="link" @click="showModal = true, readBoard(boards.item.bno)">
          {{ boards.item.title }}
        </b-btn>
      </template>
      <template slot="regdata" slot-scope="boards">
        {{ moment(boards.item.regdata).fromNow() }}
      </template>
    </b-table>

    <b-btn id="write_btn" class="fl" @click="userValid()">글 쓰기</b-btn>
    <!-- <b-btn v-show="isLogin" id="write_btn" class="fl" v-b-modal.modal @click="clearForm(), form.writer = userId">글 쓰기</b-btn> -->
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
          <input v-show="false" type="text" class="form-control" v-model="form.writer" readonly/>
          <span v-show="!isWrite" class="input-group-text">작성자</span>
          <input v-show="!isWrite" type="text" class="form-control" v-model="form.writer" readonly/>
        </div>
          <textarea rows="5" cols="70" v-model="form.content" v-bind:readonly="isReadOnly"></textarea>
      </div>
      <div id="reply" v-show="!isWrite">
        <hr>
        <div v-if="replys.length === 0">
          <p>등록된 댓글이 없습니다.</p>
        </div>
        <li style="list-style:none;" v-else v-for="(replyForm, index) in replys" :key="index">
          <div class="input-group col-5">
            <input style="display: inline !important" type="text" class="form-control r-15" v-model="replyForm.replyer">
          </div>
          <div class="input-group col-10">
            <input type="text" class="form-control mb-2 r-15 pr-3" v-model="replyForm.rtext">
            <b-btn variant="light" class="mb-2">✏️</b-btn>
            <b-btn variant="light" class="mb-2 ml-1">❌</b-btn>
          </div>
        </li>
        <b-input placeholder="댓글 등록" v-model="addreply"></b-input>
      </div>
        <div slot="modal-footer">
          <b-button class="float-right ml-1" variant="primary" @click="createBorad()" v-show="isNotNull && isWrite">OK</b-button>
          <div v-show="!isWrite && form.writer === userId">
            <b-button class="float-right ml-1" variant="danger" @click="deleteBoard(form.bno)">삭제</b-button>
            <b-button class="float-right" variant="primary" @click="updateBoard(form.bno)">
            {{btnMessage = isReadOnly ? '수정' : 'OK'}}
          </b-button>
          </div>
          <!-- <b-button class="float-right" variant="primary" @click="isReadOnly = false, updateBoard(form.bno)" v-show="!isWrite"> -->
        </div>

    </b-modal>

  </div>
</template>

<script>

// import todoList from './todoPage';
import sendAxios from '../resources/sendAxios.js';
import Paginate from 'vuejs-paginate';

const gBoardURL = 'http://localhost:8080';

export default {
  name: 'board',
  components: {
    Paginate
    // todoList
  },
  data () {
    return {
      fields: [ 'bno', 'writer', 'title', 'regdata' ],
      boards: [],
      signForm: {
        uid: '',
        upw: ''
      },
      loginForm: {
        uid: '',
        upw: ''
      },
      userId: '',
      replys: [],
      replyForm: {
        bno: '',
        rno: '',
        rtext: '',
        replyer: ''
      },
      addreply: '',
      form: {
        bno: '',
        writer: '',
        title: '',
        content: ''
      },
      notLogin: false,
      isLogin: false,
      isNotSingUp: true,
      failLogin: false,
      isBusy: false,
      currentPage: 10,
      perPage: 5,
      totalRows: 0,
      btnMessage: '',
      isSuccess: false,
      isReadOnly: true,
      showModal: true,
      isNotNull: false,
      isWrite: true
    };
  },
  methods: {
    logout () {
      const vm = this;
      vm.isLogin = false;
      vm.userId = '';
      alert('로그아웃 되었습니다!');
    },
    sign () {
      const vm = this;
      const signURL = `${gBoardURL}/sign`;
      if (vm.isNotSingUp === false) {
        let charEnough = vm.signForm.uid.length >= 4 && vm.signForm.uid.length <= 12 && vm.signForm.upw.length >= 4 && vm.signForm.upw.length <= 12;
        if (!charEnough) {
          return alert('아이디 및 비밀번호는 4-12자 이내로 입력해주세요.');
        }
        sendAxios('POST', signURL, vm.signForm).then(result => {
          vm.signUpComplete();
        });
      } else {
        sendAxios('POST', `${signURL}/login`, vm.loginForm).then(result => {
          let login = JSON.stringify(result.data);
          if (login === '1') {
            vm.loginSuccess(vm.loginForm.uid);
          };
        }).catch(() => {
          vm.failLogin = true;
          vm.$refs.sign.show();
        });
      };
    },
    replyAll (bno) {
      const vm = this;
      const getReplyAll = `${gBoardURL}/reply/${bno}`;
      sendAxios('GET', getReplyAll).then(result => {
        vm.replys = result.data;
        // console.log(vm.replys);
      });
      vm.isBusy = false;
    },
    boardCount () {
      const vm = this;
      const getCountURL = `${gBoardURL}/count`;
      this.isBusy = true;
      sendAxios('GET', getCountURL).then(result => {
        vm.totalRows = Math.ceil(result.data / vm.perPage);
      });
      vm.boardPaging();
    },
    boardPaging (pageNum = 1) {
      const getPerPageURL = `${gBoardURL}/page/${pageNum}`;
      sendAxios('GET', getPerPageURL).then(result => {
        this.boards = result.data;
      });
      this.isBusy = false;
    },
    readBoard (bno) {
      const vm = this;
      const readURL = `${gBoardURL}/read/${bno}`;
      vm.isReadOnly = true;
      vm.isWrite = false;
      vm.isBusy = true;
      sendAxios('GET', readURL).then(result => {
        vm.form = result.data;
      });
      vm.replyAll(bno);
    },
    createBorad () {
      const vm = this;
      const createURL = `${gBoardURL}/regist`;
      sendAxios('POST', createURL, vm.form).then(() => {
        vm.$refs.modal.hide();
        vm.jobComplete(true);
      });
    },
    updateBoard (bno) {
      const vm = this;
      const updateURL = `${gBoardURL}/update`;
      vm.isReadOnly = false;
      if (vm.btnMessage === 'OK') {
        sendAxios('PATCH', updateURL, vm.form).then(() => {
          vm.jobComplete(true, false);
        });
      }
    },
    deleteBoard (bno) {
      let confirmOK = confirm('삭제 하시겠습니까?');
      if (confirmOK) {
        const vm = this;
        const deleteURL = `${gBoardURL}/delete/${bno}`;
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
    userValid () {
      const vm = this;
      vm.clearForm();
      if (vm.userId === '') {
        vm.notLogin = true;
        vm.$refs.sign.show();
        return;
      }
      vm.form.writer = vm.userId;
      vm.$refs.modal.show();
    },
    clearForm () {
      const vm = this;
      vm.form.title = '';
      vm.form.writer = '';
      vm.form.content = '';
      vm.isNotNull = false;
      vm.isWrite = true;
      vm.isReadOnly = false;
      vm.failLogin = false;
      vm.loginForm.uid = '';
      vm.loginForm.upw = '';
    },
    clearSign () {
      const vm = this;
      vm.signForm.uid = '';
      vm.signForm.upw = '';
      vm.loginForm.uid = '';
      vm.loginForm.upw = '';
      vm.failLogin = false;
      vm.isNotSingUp = true;
      vm.isNotNull = false;
      vm.notLogin = false;
    },
    signUpComplete () {
      const vm = this;
      vm.isNotSingUp = true;
      vm.signForm.uid = '';
      vm.signForm.upw = '';
      vm.$refs.sign.show();
      alert(`가입이 완료되었습니다.`);
    },
    loginSuccess (userid) {
      const vm = this;
      vm.failLogin = false;
      vm.isLogin = true;
      vm.userId = userid;
      vm.$refs.sign.hide();
    },
    pushReply () {
      console.log(this.addreply);
    }//////////////////여기
  },
  mounted () {
    // this.getBoardList();
    // this.boardPaging();
    this.boardCount();
  },
  watch: {
    form: {
      handler (val) {
        if (val.title && val.content && val.writer) {
          this.isNotNull = true;
        } else {
          this.isNotNull = false;
        }
        // this.isNotNull = val.title && val.writer && val.content ? true : false;
      },
      deep: true
    }
  },
  computed: {
    validationId () {
      return this.signForm.uid.length > 3 && this.signForm.uid.length < 13;
    },
    validationPw () {
      return this.signForm.upw.length > 3 && this.signForm.upw.length < 13;
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
    <b-button v-show="isNotNull" class="float-right ml-1" variant="primary" @click="createBorad()">OK</b-button>
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
