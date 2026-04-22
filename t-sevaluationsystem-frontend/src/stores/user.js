import { defineStore } from 'pinia'
import { ref } from 'vue'
import { jwtDecode } from 'jwt-decode'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref(localStorage.getItem('token') || '')
  const status = ref(null)

  // 初始化时解析已有 Token
  if (token.value) {
    try {
      const decoded = jwtDecode(token.value)
      status.value = decoded.status
    } catch (e) {
      status.value = null
    }
  }

  // Actions
  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
    // 解析 Token 获取角色
    try {
      const decoded = jwtDecode(newToken)
      status.value = decoded.status
    } catch (e) {
      status.value = null
    }
  }

  const clearToken = () => {
    token.value = ''
    status.value = null
    localStorage.removeItem('token')
  }

  // 权限判断
  const isAdmin = () => status.value === 0
  const isTeacher = () => status.value === 1
  const isStudent = () => status.value === 2

  return {
    token,
    status,
    setToken,
    clearToken,
    isAdmin,
    isTeacher,
    isStudent
  }
})
