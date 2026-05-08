import { defineStore } from 'pinia'
import { ref } from 'vue'
import { jwtDecode } from 'jwt-decode'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref(localStorage.getItem('token') || '')
  const status = ref(null)
  const infoId = ref(null)
  const userId = ref(null)
  const username = ref(null)

  // 解析 Token 的辅助函数
  const parseToken = (tk) => {
    if (!tk) return
    try {
      const decoded = jwtDecode(tk)
      status.value = decoded.status
      infoId.value = decoded.infoId
      userId.value = decoded.userId
      username.value = decoded.sub
    } catch (e) {
      status.value = null
      infoId.value = null
      userId.value = null
      username.value = null
    }
  }

  // 初始化时解析已有 Token
  if (token.value) {
    parseToken(token.value)
  }

  // Actions
  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
    parseToken(newToken)
  }

  const clearToken = () => {
    token.value = ''
    status.value = null
    infoId.value = null
    userId.value = null
    username.value = null
    localStorage.removeItem('token')
  }

  // 权限判断
  const isAdmin = () => status.value === 0
  const isTeacher = () => status.value === 1
  const isStudent = () => status.value === 2

  return {
    token,
    status,
    infoId,
    userId,
    username,
    setToken,
    clearToken,
    isAdmin,
    isTeacher,
    isStudent
  }
})
