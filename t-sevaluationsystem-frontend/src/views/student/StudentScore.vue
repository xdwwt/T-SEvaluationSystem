<template>
  <div class="score-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">我的成绩</h2>
      <p class="page-desc">查看各课程的成绩详情</p>
      <button class="btn-refresh" @click="fetchScores">刷新</button>
    </div>

    <!-- 成绩统计卡片 -->
    <div class="stats-grid" v-if="scoreList.length > 0">
      <div class="stat-card">
        <div class="stat-value">{{ scoreList.length }}</div>
        <div class="stat-label">课程总数</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ avgScore }}</div>
        <div class="stat-label">平均分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ maxScore }}</div>
        <div class="stat-label">最高分</div>
      </div>
    </div>

    <!-- 成绩表格 -->
    <div class="section">
      <h3 class="section-title">
        <span class="title-bar"></span>
        成绩明细
      </h3>

      <div class="table-area" v-if="scoreList.length > 0">
        <table>
          <thead>
            <tr>
              <th>课程</th>
              <th>教师</th>
              <th>学期</th>
              <th>平时分</th>
              <th>期末分</th>
              <th>总评成绩</th>
              <th>评语</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in scoreList" :key="item.id">
              <td>{{ item.courseName }}</td>
              <td>{{ item.teacherName }}</td>
              <td>{{ item.semester }}</td>
              <td>{{ item.usualScore ?? '-' }}</td>
              <td>{{ item.finalScore ?? '-' }}</td>
              <td>
                <span class="score-tag" :class="getScoreClass(item.score)">
                  {{ item.score ?? '-' }}
                </span>
              </td>
              <td>{{ item.comment || '-' }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="empty-state" v-else-if="!loading">
        <div class="empty-text">暂无成绩记录</div>
        <div class="empty-subtext" v-if="errorMsg">{{ errorMsg }}</div>
      </div>
      <div class="empty-state" v-else>
        <div class="empty-text">加载中...</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getStudentScoreListApi } from '@/api/score.js'

const scoreList = ref([])
const loading = ref(false)
const errorMsg = ref('')

const avgScore = computed(() => {
  const scores = scoreList.value.filter(s => s.score != null).map(s => parseFloat(s.score))
  if (scores.length === 0) return '-'
  return (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(2)
})

const maxScore = computed(() => {
  const scores = scoreList.value.filter(s => s.score != null).map(s => parseFloat(s.score))
  if (scores.length === 0) return '-'
  return Math.max(...scores).toFixed(2)
})

const fetchScores = async () => {
  loading.value = true
  errorMsg.value = ''
  try {
    const res = await getStudentScoreListApi()
    console.log('成绩列表响应:', res)
    if (res.code === 1) {
      scoreList.value = res.data || []
      if (scoreList.value.length === 0) {
        errorMsg.value = '教师尚未发放成绩，或成绩暂不可查看'
      }
    } else {
      errorMsg.value = res.mes || '获取成绩失败'
    }
  } catch (error) {
    console.error('获取成绩列表失败', error)
    errorMsg.value = '网络异常，请稍后重试'
  } finally {
    loading.value = false
  }
}

const getScoreClass = (score) => {
  if (score == null) return ''
  const s = parseFloat(score)
  if (s >= 90) return 'excellent'
  if (s >= 80) return 'good'
  if (s >= 60) return 'pass'
  return 'fail'
}

onMounted(() => {
  fetchScores()
})
</script>

<style scoped>
.score-page {
  padding: 24px;
}

.page-header {
  margin-bottom: 28px;
}

.page-title {
  font-size: 22px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 6px 0;
}

.page-desc {
  font-size: 13px;
  color: #888;
  margin: 0;
}

/* 统计卡片 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.stat-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  color: #888;
}

/* 成绩区域 */
.section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.section-title {
  font-size: 16px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 20px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.title-bar {
  width: 4px;
  height: 16px;
  background: linear-gradient(180deg, #667eea 0%, #764ba2 100%);
  border-radius: 2px;
}

/* 表格 */
.table-area {
  overflow-x: auto;
}

.table-area table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.table-area th,
.table-area td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}

.table-area th {
  font-weight: 600;
  color: #666;
  background: #fafbfc;
}

.table-area td {
  color: #333;
}

.score-tag {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 6px;
  font-weight: 600;
  font-size: 13px;
}

.score-tag.excellent {
  background: #e8f5e9;
  color: #2e7d32;
}

.score-tag.good {
  background: #e3f2fd;
  color: #1565c0;
}

.score-tag.pass {
  background: #fff3e0;
  color: #e65100;
}

.score-tag.fail {
  background: #ffebee;
  color: #c62828;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 40px;
  color: #999;
}

.empty-text {
  font-size: 14px;
}

.empty-subtext {
  font-size: 12px;
  color: #aaa;
  margin-top: 6px;
}

.btn-refresh {
  margin-top: 12px;
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
  color: #666;
  font-size: 13px;
  cursor: pointer;
}
</style>
