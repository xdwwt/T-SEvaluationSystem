import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '@/views/common/LoginView.vue'
import AdminDashboard from '@/views/admin/AdminDashboard.vue'
import TeacherManagement from '@/views/admin/TeacherManagement.vue'
import StudentManagement from '@/views/admin/StudentManagement.vue'
import ClassManagement from '@/views/admin/ClassManagement.vue'
import CourseManagement from '@/views/admin/CourseManagement.vue'
import ArrangementManagement from '@/views/admin/ArrangementManagement.vue'
import MajorManagement from '@/views/admin/MajorManagement.vue'
import DepartmentManagement from '@/views/admin/DepartmentManagement.vue'
import TitleManagement from '@/views/admin/TitleManagement.vue'
import StudentDashboard from '@/views/student/StudentDashboard.vue'
import StudentEvaluation from '@/views/student/StudentEvaluation.vue'
import StudentScore from '@/views/student/StudentScore.vue'
import TeacherDashboard from '@/views/teacher/TeacherDashboard.vue'
import TeacherEvaluation from '@/views/teacher/TeacherEvaluation.vue'
import TeacherScore from '@/views/teacher/TeacherScore.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard,
    meta: { requiresAuth: true, role: 0 }
  },
  {
    path: '/admin/teacher',
    name: 'TeacherManagement',
    component: TeacherManagement,
    meta: { requiresAuth: true, role: 0 }
  },
  {
    path: '/admin/student',
    name: 'StudentManagement',
    component: StudentManagement,
    meta: { requiresAuth: true, role: 0 }
  },
  {
    path: '/admin/class',
    name: 'ClassManagement',
    component: ClassManagement,
    meta: { requiresAuth: true, role: 0 }
  },
  {
    path: '/admin/course',
    name: 'CourseManagement',
    component: CourseManagement,
    meta: { requiresAuth: true, role: 0 }
  },
  {
    path: '/admin/arrangement',
    name: 'ArrangementManagement',
    component: ArrangementManagement,
    meta: { requiresAuth: true, role: 0 }
  },
  {
    path: '/admin/major',
    name: 'MajorManagement',
    component: MajorManagement,
    meta: { requiresAuth: true, role: 0 }
  },
  {
    path: '/admin/department',
    name: 'DepartmentManagement',
    component: DepartmentManagement,
    meta: { requiresAuth: true, role: 0 }
  },
  {
    path: '/admin/title',
    name: 'TitleManagement',
    component: TitleManagement,
    meta: { requiresAuth: true, role: 0 }
  },
  {
    path: '/student',
    name: 'StudentDashboard',
    component: StudentDashboard,
    meta: { requiresAuth: true, role: 2 }
  },
  {
    path: '/student/evaluation',
    name: 'StudentEvaluation',
    component: StudentEvaluation,
    meta: { requiresAuth: true, role: 2 }
  },
  {
    path: '/student/score',
    name: 'StudentScore',
    component: StudentScore,
    meta: { requiresAuth: true, role: 2 }
  },
  {
    path: '/teacher',
    name: 'TeacherDashboard',
    component: TeacherDashboard,
    meta: { requiresAuth: true, role: 1 }
  },
  {
    path: '/teacher/evaluation',
    name: 'TeacherEvaluation',
    component: TeacherEvaluation,
    meta: { requiresAuth: true, role: 1 }
  },
  {
    path: '/teacher/score',
    name: 'TeacherScore',
    component: TeacherScore,
    meta: { requiresAuth: true, role: 1 }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
