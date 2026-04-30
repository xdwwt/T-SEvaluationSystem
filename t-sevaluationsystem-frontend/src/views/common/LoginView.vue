<template>
  <div class="login-page">
    <div class="login-box">
      <div class="login-header">
        <div class="logo-icon">📚</div>
        <h2>师生互评系统</h2>
        <p>Teaching Evaluation System</p>
      </div>
      <div class="login-form">
        <div class="input-group">
          <label>账号</label>
          <input v-model="username" placeholder="请输入账号" @keyup.enter="handleLogin" />
        </div>
        <div class="input-group">
          <label>密码</label>
          <input v-model="password" type="password" placeholder="请输入密码" @keyup.enter="handleLogin" />
        </div>
        <button @click="handleLogin" :disabled="loading" class="login-btn">
          {{ loading ? '登录中...' : '登 录' }}
        </button>
        <div class="error-msg" v-if="errorMsg">{{ errorMsg }}</div>
      </div>
    </div>
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
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: url('/images/login-bg.jpg') no-repeat center center;
  background-size: cover;
  position: relative;
}

.login-page::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.15);
}

.login-box {
  width: 440px;
  background: rgba(255, 255, 255, 0.92);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 1;
}

.login-header {
  background: #2c3e50;
  color: white;
  text-align: center;
  padding: 40px 30px;
}

.logo-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.login-header h2 {
  font-size: 24px;
  font-weight: 600;
  margin: 0 0 8px 0;
}

.login-header p {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
  letter-spacing: 1px;
}

.login-form {
  padding: 35px 30px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.input-group label {
  font-size: 14px;
  color: #555;
  font-weight: 500;
}

.input-group input {
  padding: 12px 14px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 15px;
  transition: all 0.3s;
  outline: none;
}

.input-group input:focus {
  border-color: #2c3e50;
  box-shadow: 0 0 0 3px rgba(44, 62, 80, 0.1);
}

.input-group input::placeholder {
  color: #aaa;
}

.login-btn {
  padding: 14px;
  background: #2c3e50;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
  margin-top: 5px;
}

.login-btn:hover {
  background: #34495e;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(44, 62, 80, 0.3);
}

.login-btn:active:not(:disabled) {
  transform: translateY(0) scale(0.98);
  box-shadow: 0 2px 6px rgba(44, 62, 80, 0.3);
}

.login-btn:disabled {
  background: #95a5a6;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.error-msg {
  color: #e74c3c;
  font-size: 14px;
  text-align: center;
  padding: 5px 0;
}
</style>
