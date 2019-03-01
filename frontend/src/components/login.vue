<template>
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
            <b-input placeholder="아이디는 4-12 이내로 써주세요."
                     v-model="signForm.uid" :state="validationId" class="mb-2"/>
            <b-input type="password" placeholder="패스워드는 4-12 이내로 써주세요."
                     v-model="signForm.upw" :state="validationPw"/>
         </div>
         <div v-else>
            <b-input required placeholder="ID" v-model="loginForm.uid"  class="mb-2"/>
            <b-input required type="password" placeholder="Password" v-model="loginForm.upw"/>
         </div>
      </b-form>
      <div slot="modal-footer">
         <b-button class="float-right ml-1" variant="primary" @click="sign()">OK</b-button>
      </div>
   </b-modal>

</template>

<script>

import sendAxios from '../resources/sendAxios.js';

const gBoardURL = 'http://localhost:8080';

export default {
  name: 'login',
  data () {
    return {
      signForm: {
        uid: '',
        upw: ''
      },
      loginForm: {
        uid: '',
        upw: ''
      },
      notLogin: false,
      isNotSingUp: true,
      failLogin: false
    };
  },
  methods: {
    logout () {
      this.$store.state.isLogin = false;
      this.$store.state.userId = '';
      alert('로그아웃 되었습니다!');
    },
    sign () {
      const vm = this;
      if (vm.loginForm.uid === '' || vm.loginForm.upw === '') {
        return alert('아이디 및 비밀번호를 입력해주세요.');
      }
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
    clearLogin () {
      const vm = this;
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
      this.failLogin = false;
      this.$store.state.isLogin = true;
      this.$store.state.userId = userid;
      this.$refs.sign.hide();
    },
    userValid () {
      const vm = this;
      let userid = this.$store.state.userId;
      vm.clearLogin();
      if (userid === '') {
        vm.isNotSingUp = true;
        vm.notLogin = true;
        vm.$refs.sign.show();
        return;
      }
      this.$EventBus.$emit('clearForm');
    }
  },
  created () {
    this.$EventBus.$on('clearSign', () => {
      this.clearSign();
    });
    this.$EventBus.$on('logout', () => {
      this.logout();
    });
    this.$EventBus.$on('userValid', () => {
      this.userValid();
    });
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
