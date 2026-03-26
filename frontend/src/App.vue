<template>
  <router-view />
</template>

<script setup>
import { useUserStore } from './store/user'

const userStore = useUserStore()

// 初始化时检查登录状态
if (userStore.token) {
  userStore.getUserInfo()
}
</script>

<style lang="scss">
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
</style>
