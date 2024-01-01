import router from './router';
<template>
  <div id="app">
    <!-- <div>
      <router-link to="/" style="margin-left: 50px;">SEARCH</router-link>
      <router-link to="/result" style="margin-left: 50px;">RESULT</router-link>
      <router-link to="/detail" style="margin-left: 50px;">DETAIL</router-link>
    </div> -->
    <router-view :key="$route.fullPath"></router-view>
  </div>
</template>
<script>
export default {
  beforeCreate() {
    document.title = '食香传世'
  }
}
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}


</style>
