<template>
  <div class="sidebar" v-if="userStore.token && route.path !== '/login'">
    <div class="logo">教学评价系统</div>

    <!-- 管理员菜单 -->
    <div class="menu-group" v-if="userStore.isAdmin()">
      <div class="menu-item" @click="navigate('/admin')">首页</div>
      <div class="menu-item" @click="navigate('/admin/teacher')">教师管理</div>
      <div class="menu-item" @click="navigate('/admin/student')">学生管理</div>
      <div class="menu-item" @click="navigate('/admin/course')">课程管理</div>
      <div class="menu-item" @click="navigate('/admin/class')">班级管理</div>
    </div>

    <!-- 教师菜单 -->
    <div class="menu-group" v-if="userStore.isTeacher()">
      <div class="menu-item" @click="navigate('/teacher')">首页</div>
      <div class="menu-item" @click="navigate('/teacher/score')">学生成绩</div>
      <div class="menu-item" @click="navigate('/teacher/evaluation')">我的评分</div>
      <div class="menu-item" @click="navigate('/teacher/info')">个人信息</div>
    </div>

    <!-- 学生菜单 -->
    <div class="menu-group" v-if="userStore.isStudent()">
      <div class="menu-item" @click="navigate('/student')">首页</div>
      <div class="menu-item" @click="navigate('/student/evaluation')">教师评价</div>
      <div class="menu-item" @click="navigate('/student/score')">我的成绩</div>
    </div>

    <div class="logout" @click="showLogoutDialog = true">退出登录</div>
  </div>

  <ConfirmDialog
    v-model:visible="showLogoutDialog"
    title="退出登录"
    message="确定要退出登录吗？"
    @confirm="handleLogout"
  />
</template>

<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user.js'
import ConfirmDialog from './ConfirmDialog.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const showLogoutDialog = ref(false)

const navigate = (path) => {
  router.push(path)
}

const handleLogout = () => {
  userStore.clearToken()
  router.push('/login')
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

.logout {
  margin-top: auto;
  padding: 15px 20px;
  cursor: pointer;
  border-top: 1px solid rgba(255,255,255,0.1);
  color: #e74c3c;
}

.logout:hover {
  background: rgba(231, 76, 60, 0.1);
}
</style>
