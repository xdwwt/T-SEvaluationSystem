import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref(localStorage.getItem('token') || '')
  const username = ref('')
  const status = ref(null)

  // Actions
  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const clearToken = () => {
    token.value = ''
    username.value = ''
    status.value = null
    localStorage.removeItem('token')
  }

  const setUserInfo = (name, userStatus) => {
    username.value = name
    status.value = userStatus
  }

  return {
    token,
    username,
    status,
    setToken,
    clearToken,
    setUserInfo
  }
})
