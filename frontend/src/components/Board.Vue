<template>
  <div id="container">
    <div id="todoModal">
        <b-btn v-b-modal.todo>TodoList</b-btn>
    </div>
    <b-modal id="todo" title="Todo List">
      <todoList></todoList>
    </b-modal>

    <transition name="fade">
      <div class="alert alert-success alert-dismissible fade show" id="board" role="alert" v-show="isSuccess">작업이 완료 되었습니다.</div>
    </transition>

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

    <b-btn id="write_btn" class="fl" v-b-modal.modal @click="clearForm()">글 쓰기</b-btn>
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
          <input type="text" class="form-control col-6" v-model="form.title" v-bind:readonly="isReadOnly"/>
          <span class="input-group-text">작성자</span>
          <input type="text" class="form-control" v-model="form.writer" v-bind:readonly="!isWrite"/>
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
            <b-btn variant="danger" class="mb-2">X</b-btn>
          </div>
        </li>
      </div>
        <div slot="modal-footer">
          <b-button class="float-right ml-1" variant="primary" @click="createBorad()" v-show="isNotNull && isWrite">OK</b-button>
          <b-button class="float-right ml-1" variant="danger" @click="deleteBoard(form.bno)" v-show="!isWrite">삭제</b-button>
          <b-button class="float-right" variant="primary" @click="isReadOnly = false, updateBoard(form.bno)" v-show="!isWrite">
            {{btnMessage = isReadOnly ? '수정' : 'OK'}}
          </b-button>
        </div>

    </b-modal>

  </div>
</template>

<script>

import todoList from './todoPage';
import sendAxios from '../resources/sendAxios.js';
import Paginate from 'vuejs-paginate';

const gBoardURL = 'http://localhost:8080';

export default {
  name: 'board',
  components: {
    todoList,
    Paginate
  },
  data () {
    return {
      fields: [ 'bno', 'writer', 'title', 'regdata' ],
      boards: [],
      replys: [],
      replyForm: {
        bno: '',
        rno: '',
        rtext: '',
        replyer: ''
      },
      form: {
        bno: '',
        writer: '',
        title: '',
        content: ''
      },
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
    // getBoardList () {
    //   const vm = this;
    //   const getListURL = `${gBoardURL}/list`;
    //   sendAxios('GET', getListURL).then(result => {
    //     vm.boards = result.data;
    //     vm.perPage = Math.ceil(result.data.length / 5);
    //   });
    // },
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
      vm.boardPaging(1);
    },
    boardPaging (pageNum) {
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
      vm.getBoardList();
      vm.isSuccess = doneSuccess;
      vm.showModal = isShow;

      if (doneSuccess) {
        setTimeout(() => {
          vm.isSuccess = false;
        }, 2000);
      }
    },
    clearForm () {
      const vm = this;
      vm.form.title = '';
      vm.form.writer = '';
      vm.form.content = '';
      vm.isNotNull = false;
      vm.isWrite = true;
      vm.isReadOnly = false;
    }
  },
  mounted () {
    // this.getBoardList();
    // this.boardPaging();
    this.boardCount();
  },
  watch: {
    form: {
      handler (val) {
        if (val.title && val.writer && val.content) {
          this.isNotNull = true;
        } else {
          this.isNotNull = false;
        }
        // this.isNotNull = val.title && val.writer && val.content ? true : false;
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
    position: absolute;
    bottom: 92.5%;
    left: 0px;
    top: 0px;
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
