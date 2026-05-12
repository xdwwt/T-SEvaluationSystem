<template>
  <div class="dashboard-page">
    <div class="welcome-bar">
      <div>
        <h1 class="welcome-title">教师工作台</h1>
        <p class="welcome-subtitle">{{ currentDate }} · 欢迎回来，祝教学顺利</p>
      </div>
    </div>

    <div class="section">
      <h3 class="section-title">
        <span class="title-bar"></span>
        快捷入口
      </h3>
      <div class="quick-grid">
        <div class="quick-item" @click="$router.push('/teacher/score')">
          <div class="quick-accent accent-score"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14" />
                <polyline points="22 4 12 14.01 9 11.01" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">学生成绩</div>
              <div class="quick-desc">录入和管理学生成绩</div>
            </div>
          </div>
          <svg class="quick-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="9 18 15 12 9 6" />
          </svg>
        </div>

        <div class="quick-item" @click="$router.push('/teacher/evaluation')">
          <div class="quick-accent accent-evaluation"></div>
          <div class="quick-content">
            <div class="quick-icon-wrap">
              <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" />
              </svg>
            </div>
            <div class="quick-text">
              <div class="quick-label">我的评分</div>
              <div class="quick-desc">查看学生对我的教学评价</div>
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
import { computed } from 'vue'

const currentDate = computed(() => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const weekDays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  const weekDay = weekDays[now.getDay()]
  return `${year}年${month}月${day}日 ${weekDay}`
})
</script>

<style scoped>
.dashboard-page {
  padding: 24px;
}

.welcome-bar {
  margin-bottom: 28px;
}

.welcome-title {
  font-size: 26px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 6px 0;
}

.welcome-subtitle {
  font-size: 14px;
  color: #888;
  margin: 0;
}

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
  grid-template-columns: repeat(2, 1fr);
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

.accent-score { background: linear-gradient(180deg, #43e97b 0%, #38f9d7 100%); }
.accent-evaluation { background: linear-gradient(180deg, #667eea 0%, #764ba2 100%); }

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
</style>
