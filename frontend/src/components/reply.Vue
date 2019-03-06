<template>
  <div id="reply" v-show="!this.$store.state.isWrite">
    <hr>
    <div v-if="replys.length === 0">
      <p>등록된 댓글이 없습니다.</p>
    </div>
    <li v-else style="list-style:none;" v-for="(replyForm, index) in replys" :key="index">
      <div class="input-group col-10 ml-1">
        <b-input style="display: inline !important" v-bind:readonly="isReplyReadOnly"
            class=" r-15" v-model="replyForm.replyer" />
        <span class="fr ml-6">{{ replyForm.isEdit === true
              ? `Edited ${moment(replyForm.rdate).fromNow()}`
              : moment(replyForm.rdate).fromNow() }}</span>
      </div>
      <div class="input-group col-15">
        <b-input class="mb-2 ml-1"
            v-model="replyForm.rtext" v-bind:readonly="!isReplyReadOnly" />
        <b-btn size="sm" variant="light" class="mb-2 ml-3 fr" @click="replyEdit(replyForm)">✏️</b-btn>
        <b-btn size="sm" variant="light" class="mb-2 ml-1 fr" @click="replyDelete(replyForm.bno, replyForm.rno)">❌</b-btn>
      </div>
    </li>
    <div v-show="this.$store.state.isLogin">
      <input class="col-10" required placeholder="댓글 작성" v-model="replyForm.rtext"/>
      <b-btn size="sm mb-1" @click="replyAdd(replyForm.bno)">작성</b-btn>
    </div>
  </div>
</template>

<script>

import sendAxios from '../resources/sendAxios.js';

const gReplyURL = 'http://localhost:8080/reply';

export default {
  name: 'reply',
  data () {
    return {
      replys: [],
      replyForm: {
        bno: '',
        rno: '',
        rtext: '',
        replyer: '',
        isEdit: false
      },
      isReplyReadOnly: true,
      perPage: 5,
      totalRows: 0,
      currentPage: 0
    };
  },
  methods: {
    replyPage () {
    },
    replyAll (bno) {
      const vm = this;
      const getReplyAllURL = `${gReplyURL}/bno/${bno}`;
      vm.replyForm.bno = bno;
      vm.replyForm.rtext = null;
      sendAxios('GET', getReplyAllURL).then(result => {
        if (result.data.length === 0) {
          this.$store.state.nullReply = true;
        } else {
          this.$store.state.nullReply = false;
        }
        vm.replys = result.data;
      });
    },
    replyAdd (bno) {
      const vm = this;
      const ReplyAddURL = `${gReplyURL}/bno/${bno}`;
      vm.replyForm.replyer = this.$store.state.userId;
      sendAxios('POST', ReplyAddURL, vm.replyForm).then(result => {
        vm.replyForm.rtext = null;
        vm.replyAll(bno);
        this.$EventBus.$emit('updateReplycnt');
      });
    },
    replyEdit (replyform) {
      if (!confirm('수정 하시겠습니까?')) {
        return;
      }
      const vm = this;
      sendAxios('PATCH', gReplyURL, replyform).then(result => {
        vm.replyAll(replyform.bno);
        alert('수정이 완료 되었습니다.');
      });
    },
    replyDelete (bno, rno) {
      if (!confirm('삭제 하시겠습니까?')) {
        return;
      }
      const vm = this;
      const ReplyDelURL = `${gReplyURL}/bno/${bno}/rno/${rno}`;
      sendAxios('DELETE', ReplyDelURL).then(result => {
        vm.replyAll(bno);
        this.$EventBus.$emit('updateReplycnt');
        alert('삭제가 완료 되었습니다.');
      });
    }
  },
  created () {
    this.$EventBus.$on('replyAll', (bno) => {
      this.replyAll(bno);
    });
  }
};
</script>

<style>
.fr {
  float: right;
}
</style>
