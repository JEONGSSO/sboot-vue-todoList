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
   </div>
</template>

<script>
export default {
  name: 'home',
  data () {
    return {
      message: 'home!!',
      visit: 0,
      isVisible: false
    }
  },
  methods: {
    visitCount () {
      this.visit++
      this.isVisible = true
    }
  },
  computed: {
    tvisit () {
      return this.home + this.visit
    }
  }
}
</script>
