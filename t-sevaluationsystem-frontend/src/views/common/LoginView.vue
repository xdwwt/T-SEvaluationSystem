<template>
  <div class="login-container">
    <h2>教学评价系统</h2>
    <input v-model="username" placeholder="账号" />
    <input v-model="password" type="password" placeholder="密码" @keyup.enter="handleLogin" />
    <button @click="handleLogin" :disabled="loading">
      {{ loading ? '登录中...' : '登录' }}
    </button>
    <div class="error-msg" v-if="errorMsg">{{ errorMsg }}</div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loginApi } from '@/api/login.js'
import { useUserStore } from '@/stores/user.js'

const router = useRouter()
const userStore = useUserStore()

const username = ref('')
const password = ref('')
const loading = ref(false)
const errorMsg = ref('')

const handleLogin = async () => {
  errorMsg.value = ''
  if (!username.value || !password.value) {
    errorMsg.value = '请输入账号和密码'
    return
  }

  loading.value = true
  try {
    const res = await loginApi(username.value, password.value)
    if (res.code === 1) {
      userStore.setToken(res.data)
      // 根据角色跳转到对应首页
      if (userStore.isAdmin()) {
        router.push('/admin')
      } else if (userStore.isTeacher()) {
        router.push('/teacher')
      } else if (userStore.isStudent()) {
        router.push('/student')
      }
    } else {
      errorMsg.value = res.mes || '登录失败'
    }
  } catch (error) {
    errorMsg.value = '登录失败，请稍后重试'
  } finally {
    loading.value = false
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
button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.error-msg {
  color: #e74c3c;
  font-size: 14px;
  text-align: center;
}
</style>
