
<!--여기서 본격적으로 vuejs 개발-->
<!--가장 최상위 컴포넌트입니다.-->
<!--가장 먼저 마운팅 되는 App 컴포넌트 -->

<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'app',
  data () {
    return {
      message: 'Wow'
    }
  }
}
</script>

<style>
#app {
  text-align: center;
  margin-top: 60px;
}

</style>
