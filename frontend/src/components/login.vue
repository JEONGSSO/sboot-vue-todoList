<template>
   <b-modal size="sm" id="sign_modal" ref="sign">
      <transition>
         <div class="alert alert-danger alert-dismissible fade show"
              role="alert" v-show="failLogin">아이디 또는 비밀번호를 다시 확인하세요.</div>
      </transition>
      <transition>
         <div class="alert alert-primary alert-dismissible fade show"
              role="alert" v-show="notLoginAlert">로그인이 필요한 서비스 입니다.</div>
      </transition>
      <div v-show="!isSignUp">
         <p>회원이 아니신가요?</p>
         <b-button variant="success" class="mt-3"
            @click="isSignUp = true, failLogin = false, notLoginAlert = false">회원가입</b-button>
         <hr>
      </div>
      <b-form>
         <div v-if="isSignUp">
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

const gSignURL = 'http://localhost:8080/sign';

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
      notLoginAlert: false,
      isSignUp: false,
      failLogin: false
    };
  },
  methods: {
    logout () {
      this.$store.commit('logout');
    },
    sign () {
      const vm = this;
      if (vm.isSignUp === true) {
        if (!vm.charEnough()) {
          return alert('아이디 및 비밀번호는 4-12자 이내로 입력해주세요.');
        }
        sendAxios('POST', gSignURL, vm.signForm).then(result => {
          vm.signUpComplete();
        });
      } else {
        if (vm.loginForm.uid === '' || vm.loginForm.upw === '') {
          return alert('아이디 및 비밀번호를 입력해주세요.');
        }
        sendAxios('POST', `${gSignURL}/login`, vm.loginForm).then(result => {
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
      vm.isSignUp = false;
      vm.notLoginAlert = false;
    },
    signUpComplete () {
      const vm = this;
      vm.isSignUp = false;
      vm.signForm.uid = '';
      vm.signForm.upw = '';
      vm.$refs.sign.show();
      alert(`가입이 완료되었습니다.`);
    },
    loginSuccess (userid) {
      this.failLogin = false;
      this.$store.commit('login', userid);
      // this.$store.commit({
      //   type: 'login',
      //   payload: userid
      // });
      this.$refs.sign.hide();
    },
    loginUserValid () {
      const vm = this;
      let userid = this.$store.getters.userId;
      console.log(userid);
      vm.clearLogin();
      if (userid === '') {
        vm.isSignUp = false;
        vm.notLoginAlert = true;
        vm.$refs.sign.show();
        return;
      }
      this.$EventBus.$emit('resetFrom');
    },
    charEnough () {
      const vm = this;
      // return vm.signForm.uid.length >= 4 && vm.signForm.uid.length <= 12 && vm.signForm.upw.length >= 4 && vm.signForm.upw.length <= 12 ? true : false;
      if (vm.signForm.uid.length >= 4 && vm.signForm.uid.length <= 12 && vm.signForm.upw.length >= 4 && vm.signForm.upw.length <= 12) {
        return true;
      } else {
        return false;
      }
    }
  },
  created () {
    this.$EventBus.$on('clearSign', () => {
      this.clearSign();
    });
    this.$EventBus.$on('logout', () => {
      this.logout();
    });
    this.$EventBus.$on('loginUserValid', () => {
      this.loginUserValid();
    });
  },
  computed: {
    validationId () {
      return this.signForm.uid.length >= 4 && this.signForm.uid.length <= 12;
    },
    validationPw () {
      return this.signForm.upw.length >= 4 && this.signForm.upw.length <= 12;
    }
  }
};
</script>
