<template>
  <div id="app">
    <Header />
    <router-view></router-view>
  </div>
</template>

<script>
import Header from '@/Layout/Header.vue'; 

export default {
  name: 'App',
  components: {
    Header 
  }
}
</script>

<style></style>
