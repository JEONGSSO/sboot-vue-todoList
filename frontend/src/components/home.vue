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
    <div v-if="hasResult">
      <div v-for="post in posts" v-bind:key="post.id">
        <h1>{{post.title}}</h1>
        <p>{{post.body}}</p>
      </div>
    </div>
    <button v-else @click="searchTerm()">불러오기</button>
    <div id="test">
      <ol>
        <test
          v-for="item in groceryList"
          v-bind:todo="item"
          v-bind:key="item.id">
        </test>
      </ol>
    </div>

   </div>
</template>

<script>

export default {
  name: 'home',
  data () {
    return {
      message: 'home!!',
      visit: 0,
      isVisible: false,
      posts: [],
      groceryList: [
        { id: 0, text: 'Vegetables' },
        { id: 1, text: 'Cheese' },
        { id: 2, text: 'Whatever else humans are supposed to eat' }
      ]
    };
  },
  methods: {
    visitCount () {
      this.visit++;
      this.isVisible = true;
    },
    searchTerm () {
      const URI = 'https://jsonplaceholder.typicode.com';
      this.$http.get(`${URI}/posts`).then(result => {
        console.log(result);
        this.posts = result.data;
      }).catch(err => {
        console.log(err);
        alert('오류가 발생했습니다.');
      });
    }
  },
  computed: {
    tvisit () {
      return this.home + this.visit;
    },
    hasResult () {
      return this.posts.length > 0;
    }
  },
  components: {
    'test': {
      props: ['todo'],
      template: '<li>{{ todo.text }}</li>'
    }
  }
};

</script>
