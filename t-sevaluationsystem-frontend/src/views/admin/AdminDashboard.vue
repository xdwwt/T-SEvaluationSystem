<template>
  <div class="dashboard-page">
    <!-- 顶部欢迎栏 -->
    <div class="welcome-bar">
      <div>
        <h1 class="welcome-title">管理员控制台</h1>
        <p class="welcome-subtitle">{{ currentDate }} · 实时监控教学评价系统运行状态</p>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card card-teacher">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
            <circle cx="12" cy="7" r="4" />
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.teacherCount }}</div>
          <div class="stat-label">教师人数</div>
        </div>
        <div class="stat-decoration"></div>
      </div>

      <div class="stat-card card-student">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
            <circle cx="9" cy="7" r="4" />
            <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
            <path d="M16 3.13a4 4 0 0 1 0 7.75" />
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.studentCount }}</div>
          <div class="stat-label">学生人数</div>
        </div>
        <div class="stat-decoration"></div>
      </div>

      <div class="stat-card card-class">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
            <polyline points="9 22 9 12 15 12 15 22" />
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.classCount }}</div>
          <div class="stat-label">班级数量</div>
        </div>
        <div class="stat-decoration"></div>
      </div>

      <div class="stat-card card-course">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.courseCount }}</div>
          <div class="stat-label">课程数量</div>
        </div>
        <div class="stat-decoration"></div>
      </div>

      <div class="stat-card card-arrangement">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
            <line x1="16" y1="2" x2="16" y2="6" />
            <line x1="8" y1="2" x2="8" y2="6" />
            <line x1="3" y1="10" x2="21" y2="10" />
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.arrangementCount }}</div>
          <div class="stat-label">排课记录</div>
        </div>
        <div class="stat-decoration"></div>
      </div>

      <div class="stat-card card-major">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M12 2L2 7l10 5 10-5-10-5z" />
            <path d="M2 17l10 5 10-5" />
            <path d="M2 12l10 5 10-5" />
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.majorCount }}</div>
          <div class="stat-label">专业数量</div>
        </div>
        <div class="stat-decoration"></div>
      </div>

      <div class="stat-card card-department">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
            <polyline points="9 22 9 12 15 12 15 22" />
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.departmentCount }}</div>
          <div class="stat-label">院系数量</div>
        </div>
        <div class="stat-decoration"></div>
      </div>

      <div class="stat-card card-title">
        <div class="stat-icon">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
            <polyline points="14 2 14 8 20 8" />
            <line x1="16" y1="13" x2="8" y2="13" />
            <line x1="16" y1="17" x2="8" y2="17" />
          </svg>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ stats.titleCount }}</div>
          <div class="stat-label">职称数量</div>
        </div>
        <div class="stat-decoration"></div>
      </div>
    </div>

    <!-- 快捷入口 -->
    <div class="section">
      <h3 class="section-title">
        <span class="title-bar"></span>
        功能导航
      </h3>
      <div class="quick-grid">
        <div class="quick-item" @click="$router.push('/admin/teacher')">
          <div class="quick-accent accent-teacher"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">教师管理</div>
              <div class="quick-desc">维护教师信息、重置密码</div>
            </div>
          </div>
          <svg class="quick-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>

        <div class="quick-item" @click="$router.push('/admin/student')">
          <div class="quick-accent accent-student"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
                <circle cx="9" cy="7" r="4" />
                <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
                <path d="M16 3.13a4 4 0 0 1 0 7.75" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">学生管理</div>
              <div class="quick-desc">维护学生信息、班级分配</div>
            </div>
          </div>
          <svg class="quick-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>

        <div class="quick-item" @click="$router.push('/admin/class')">
          <div class="quick-accent accent-class"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
                <polyline points="9 22 9 12 15 12 15 22" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">班级管理</div>
              <div class="quick-desc">维护班级信息、学生归属</div>
            </div>
          </div>
          <svg class="quick-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>

        <div class="quick-item" @click="$router.push('/admin/course')">
          <div class="quick-accent accent-course"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20" />
                <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">课程管理</div>
              <div class="quick-desc">维护课程信息、学分设置</div>
            </div>
          </div>
          <svg class="quick-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>

        <div class="quick-item" @click="$router.push('/admin/arrangement')">
          <div class="quick-accent accent-arrangement"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
                <line x1="16" y1="2" x2="16" y2="6" />
                <line x1="8" y1="2" x2="8" y2="6" />
                <line x1="3" y1="10" x2="21" y2="10" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">排课管理</div>
              <div class="quick-desc">教师、班级、课程关联</div>
            </div>
          </div>
          <svg class="quick-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>

        <div class="quick-item" @click="$router.push('/admin/major')">
          <div class="quick-accent accent-major"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 2L2 7l10 5 10-5-10-5z" />
                <path d="M2 17l10 5 10-5" />
                <path d="M2 12l10 5 10-5" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">专业管理</div>
              <div class="quick-desc">维护专业信息</div>
            </div>
          </div>
          <svg class="quick-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>

        <div class="quick-item" @click="$router.push('/admin/department')">
          <div class="quick-accent accent-department"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
                <polyline points="9 22 9 12 15 12 15 22" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">院系管理</div>
              <div class="quick-desc">维护院系信息</div>
            </div>
          </div>
          <svg class="quick-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>

        <div class="quick-item" @click="$router.push('/admin/title')">
          <div class="quick-accent accent-title"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                <polyline points="14 2 14 8 20 8" />
                <line x1="16" y1="13" x2="8" y2="13" />
                <line x1="16" y1="17" x2="8" y2="17" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">职称管理</div>
              <div class="quick-desc">维护职称信息</div>
            </div>
          </div>
          <svg class="quick-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getDashboardStatsApi } from '@/api/dashboard.js'

const stats = ref({
  teacherCount: 0,
  studentCount: 0,
  classCount: 0,
  courseCount: 0,
  arrangementCount: 0,
  majorCount: 0,
  departmentCount: 0,
  titleCount: 0
})

const currentDate = computed(() => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  const weekDay = weekDays[now.getDay()]
  return `${year}年${month}月${day}日 ${weekDay}`
})

const fetchStats = async () => {
  try {
    const res = await getDashboardStatsApi()
    if (res.code === 1) {
      stats.value = res.data
    }
  } catch (error) {
    console.error('获取统计数据失败', error)
  }
}

onMounted(() => {
  fetchStats()
})
</script>

<style scoped>
.dashboard-page {
  padding: 24px;
  max-width: 1400px;
}

/* 欢迎栏 */
.welcome-bar {
  margin-bottom: 28px;
}

.welcome-title {
  font-size: 26px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 6px 0;
  letter-spacing: -0.5px;
}

.welcome-subtitle {
  font-size: 14px;
  color: #888;
  margin: 0;
}

/* 统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  position: relative;
  border-radius: 16px;
  padding: 24px;
  color: white;
  overflow: hidden;
  cursor: default;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
}

.stat-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.15);
}

.card-teacher {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-student {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.card-class {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.card-course {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.card-arrangement {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.card-major {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

.card-department {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.card-title {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.stat-icon {
  position: relative;
  z-index: 1;
  margin-bottom: 16px;
}

.stat-icon svg {
  width: 28px;
  height: 28px;
  opacity: 0.9;
}

.stat-info {
  position: relative;
  z-index: 1;
}

.stat-value {
  font-size: 36px;
  font-weight: 800;
  line-height: 1;
  margin-bottom: 8px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-label {
  font-size: 14px;
  opacity: 0.85;
  font-weight: 500;
}

.stat-decoration {
  position: absolute;
  top: -30px;
  right: -30px;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.stat-card:hover .stat-decoration {
  transform: scale(1.2);
  background: rgba(255, 255, 255, 0.15);
}

/* 功能导航区 */
.section {
  background: white;
  border-radius: 16px;
  padding: 28px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.section-title {
  font-size: 17px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.title-bar {
  width: 4px;
  height: 18px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.quick-item {
  position: relative;
  display: flex;
  align-items: center;
  background: #fafbfc;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.25s ease;
  border: 1px solid #f0f0f0;
}

.quick-item:hover {
  background: white;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
  border-color: transparent;
}

.quick-accent {
  width: 4px;
  align-self: stretch;
  flex-shrink: 0;
}

.accent-teacher { background: linear-gradient(180deg, #667eea 0%, #764ba2 100%); }
.accent-student { background: linear-gradient(180deg, #f093fb 0%, #f5576c 100%); }
.accent-class { background: linear-gradient(180deg, #4facfe 0%, #00f2fe 100%); }
.accent-course { background: linear-gradient(180deg, #43e97b 0%, #38f9d7 100%); }
.accent-arrangement { background: linear-gradient(180deg, #fa709a 0%, #fee140 100%); }
.accent-major { background: linear-gradient(180deg, #a8edea 0%, #fed6e3 100%); }
.accent-department { background: linear-gradient(180deg, #667eea 0%, #764ba2 100%); }
.accent-title { background: linear-gradient(180deg, #f093fb 0%, #f5576c 100%); }

.quick-content {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px 16px;
  flex: 1;
  min-width: 0;
}

.quick-icon-wrap {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  background: #f0f0f5;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.25s ease;
}

.quick-icon-wrap svg {
  width: 20px;
  height: 20px;
  color: #666;
}

.quick-item:hover .quick-icon-wrap {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.quick-item:hover .quick-icon-wrap svg {
  color: white;
}

.quick-text {
  min-width: 0;
}

.quick-label {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.quick-desc {
  font-size: 12px;
  color: #999;
}

.quick-arrow {
  width: 16px;
  height: 16px;
  color: #ccc;
  margin-right: 16px;
  flex-shrink: 0;
  transition: all 0.25s ease;
}

.quick-item:hover .quick-arrow {
  color: #667eea;
  transform: translateX(4px);
}

/* 响应式 */
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(3, 1fr);
  }
  .quick-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  .quick-grid {
    grid-template-columns: 1fr;
  }
}
</style>
