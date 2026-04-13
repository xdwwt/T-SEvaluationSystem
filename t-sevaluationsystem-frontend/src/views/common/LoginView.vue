<template>
  <div class="login-container">
    <h2>登录</h2>
    <input v-model="username" placeholder="账号" />
    <input v-model="password" type="password" placeholder="密码" />
    <button @click="handleLogin">登录</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { loginApi } from '@/api/login.js'

const username = ref('')
const password = ref('')

const handleLogin = async () => {
  try {
    const res = await loginApi(username.value, password.value)
    alert('Token: ' + res.data)
  } catch (error) {
    alert('登录失败: ' + error.message)
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-width: 300px;
  margin: 100px auto;
  padding: 20px;
}
input, button {
  padding: 10px;
}
</style>
