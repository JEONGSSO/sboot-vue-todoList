<template>
   <div id="home">
      <b-alert show dismissible>
        {{message}} Alert!  Click the close button over there <b>&rArr;</b>
        <input type="text" v-model="message">
      </b-alert>
      <span v-if="isVisible">{{visit}}</span>
      <button @click="visitCount()"> 방문</button>
    <router-link to='/'>
      <button class="btn btn-primary">Go to hello</button>
    </router-link>
    <!-- <test></test> -->

    <div v-if="hasResult">
      <div v-for="post in posts" v-bind:key="post.id">
        <h1>{{post.title}}</h1>
        <p>{{post.body}}</p>
      </div>
    </div>
    <button v-else @click="searchTerm()">불러오기</button>
   </div>
</template>

<script>
// 변수로 template 넣어서 재사용
// let test = {
//   template: '<button @click="this.visitCount() += 1"> {{visit}} 방문 횟수 </button>'
// }

export default {
  name: 'home',
  data () {
    return {
      message: 'home!!',
      visit: 0,
      isVisible: false,
      posts: []
    }
  },
  methods: {
    visitCount () {
      this.visit++
      this.isVisible = true
    },
    searchTerm () {
      const URI = 'https://jsonplaceholder.typicode.com'
      this.$http.get(`${URI}/posts`).then(result => {
        console.log(result)
        this.posts = result.data
      }).catch(err => {
        console.log(err)
        alert('오류가 발생했습니다.')
      })
    }
  },
  computed: {
    tvisit () {
      return this.home + this.visit
    },
    hasResult () {
      return this.posts.length > 0
    }
  },
  components: {
    // 'test': test
  }
}
</script>
