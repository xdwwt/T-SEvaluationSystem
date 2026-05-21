<template>
  <div class="sidebar" v-if="userStore.token && route.path !== '/login'">
    <div class="logo">师生互评系统</div>

    <!-- 管理员菜单 -->
    <div class="menu-group" v-if="userStore.isAdmin()">
      <div class="menu-item" :class="{ active: route.path === '/admin' }" @click="navigate('/admin')">首页</div>
      <div class="menu-item" :class="{ active: route.path === '/admin/teacher' }" @click="navigate('/admin/teacher')">教师管理</div>
      <div class="menu-item" :class="{ active: route.path === '/admin/student' }" @click="navigate('/admin/student')">学生管理</div>
      <div class="menu-item" :class="{ active: route.path === '/admin/course' }" @click="navigate('/admin/course')">课程管理</div>
      <div class="menu-item" :class="{ active: route.path === '/admin/major' }" @click="navigate('/admin/major')">专业管理</div>
      <div class="menu-item" :class="{ active: route.path === '/admin/department' }" @click="navigate('/admin/department')">院系管理</div>
      <div class="menu-item" :class="{ active: route.path === '/admin/title' }" @click="navigate('/admin/title')">职称管理</div>
      <div class="menu-item" :class="{ active: route.path === '/admin/class' }" @click="navigate('/admin/class')">班级管理</div>
      <div class="menu-item" :class="{ active: route.path === '/admin/arrangement' }" @click="navigate('/admin/arrangement')">排课管理</div>
    </div>

    <!-- 教师菜单 -->
    <div class="menu-group" v-if="userStore.isTeacher()">
      <div class="menu-item" :class="{ active: route.path === '/teacher' }" @click="navigate('/teacher')">首页</div>
      <div class="menu-item" :class="{ active: route.path === '/teacher/score' }" @click="navigate('/teacher/score')">学生成绩</div>
      <div class="menu-item" :class="{ active: route.path === '/teacher/evaluation' }" @click="navigate('/teacher/evaluation')">我的评分</div>
      <!-- <div class="menu-item" :class="{ active: route.path === '/teacher/info' }" @click="navigate('/teacher/info')">个人信息</div> -->
    </div>

    <!-- 学生菜单 -->
    <div class="menu-group" v-if="userStore.isStudent()">
      <div class="menu-item" :class="{ active: route.path === '/student' }" @click="navigate('/student')">首页</div>
      <div class="menu-item" :class="{ active: route.path === '/student/evaluation' }" @click="navigate('/student/evaluation')">教师评价</div>
      <div class="menu-item" :class="{ active: route.path === '/student/score' }" @click="navigate('/student/score')">我的成绩</div>
    </div>

    <div class="user-actions">
      <div class="action-item" @click="showPwdDialog = true">
        <span>修改密码</span>
      </div>
      <div class="action-item logout" @click="showLogoutDialog = true">
        <span>退出登录</span>
      </div>
    </div>
  </div>

  <ConfirmDialog
    v-model:visible="showLogoutDialog"
    title="退出登录"
    message="确定要退出登录吗？"
    @confirm="handleLogout"
  />

  <!-- 修改密码弹窗 -->
  <div class="dialog-overlay" v-if="showPwdDialog" @click.self="showPwdDialog = false">
    <div class="dialog-box">
      <div class="dialog-header">
        <h3>修改密码</h3>
        <button class="btn-close" @click="showPwdDialog = false">×</button>
      </div>
      <div class="dialog-body">
        <div class="form-item">
          <label>旧密码</label>
          <input type="password" v-model="pwdForm.oldPassword" placeholder="请输入旧密码" />
        </div>
        <div class="form-item">
          <label>新密码</label>
          <input type="password" v-model="pwdForm.newPassword" placeholder="请输入新密码" />
        </div>
        <div class="form-item">
          <label>确认新密码</label>
          <input type="password" v-model="pwdForm.confirmPassword" placeholder="请再次输入新密码" />
        </div>
      </div>
      <div class="dialog-footer">
        <button class="btn-cancel" @click="showPwdDialog = false">取消</button>
        <button class="btn-confirm" @click="handleChangePassword" :disabled="pwdSubmitting">
          {{ pwdSubmitting ? '提交中...' : '确定' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user.js'
import ConfirmDialog from './ConfirmDialog.vue'
import { changePasswordApi } from '@/api/login.js'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const showLogoutDialog = ref(false)
const showPwdDialog = ref(false)
const pwdSubmitting = ref(false)
const pwdForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const navigate = (path) => {
  router.push(path)
}

const handleLogout = () => {
  userStore.clearToken()
  router.push('/login')
}

const handleChangePassword = async () => {
  const { oldPassword, newPassword, confirmPassword } = pwdForm.value
  if (!oldPassword || !newPassword || !confirmPassword) {
    alert('请填写完整')
    return
  }
  if (newPassword !== confirmPassword) {
    alert('两次输入的新密码不一致')
    return
  }
  if (newPassword.length < 4) {
    alert('新密码长度不能少于4位')
    return
  }

  pwdSubmitting.value = true
  try {
    const res = await changePasswordApi(oldPassword, newPassword)
    if (res.code === 1) {
      alert('密码修改成功，请重新登录')
      showPwdDialog.value = false
      userStore.clearToken()
      router.push('/login')
    } else {
      alert(res.mes || '修改失败')
    }
  } catch (error) {
    console.error('修改密码失败', error)
    alert('修改失败')
  } finally {
    pwdSubmitting.value = false
  }
}
</script>

<style scoped>
.sidebar {
  width: 200px;
  height: 100vh;
  background: #2c3e50;
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  padding: 0;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 100;
}

.logo {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
  padding: 20px;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.menu-group {
  margin-bottom: 20px;
}

.menu-item {
  padding: 12px 20px;
  cursor: pointer;
  transition: background 0.2s;
}

.menu-item:hover {
  background: rgba(255,255,255,0.1);
}

.menu-item.active {
  background: rgba(255,255,255,0.15);
  box-shadow: inset 4px 0 0 #3498db;
  color: #fff;
  font-weight: 500;
}

.user-actions {
  margin-top: auto;
  border-top: 1px solid rgba(255,255,255,0.1);
}

.action-item {
  padding: 15px 20px;
  cursor: pointer;
  transition: background 0.2s;
  font-size: 14px;
}

.action-item:hover {
  background: rgba(255,255,255,0.1);
}

.action-item.logout {
  color: #e74c3c;
}

.action-item.logout:hover {
  background: rgba(231, 76, 60, 0.1);
}

/* 修改密码弹窗 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.dialog-box {
  background: white;
  border-radius: 12px;
  width: 400px;
  max-width: 90vw;
  overflow: hidden;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.dialog-header h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.btn-close {
  background: none;
  border: none;
  font-size: 22px;
  color: #999;
  cursor: pointer;
}

.dialog-body {
  padding: 20px;
}

.form-item {
  margin-bottom: 16px;
}

.form-item label {
  display: block;
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
}

.form-item input {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
}

.form-item input:focus {
  border-color: #667eea;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 12px 20px;
  border-top: 1px solid #f0f0f0;
}

.btn-cancel {
  padding: 8px 18px;
  border: 1px solid #ddd;
  border-radius: 6px;
  background: white;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}

.btn-confirm {
  padding: 8px 18px;
  border: none;
  border-radius: 6px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 14px;
  cursor: pointer;
}

.btn-confirm:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>
