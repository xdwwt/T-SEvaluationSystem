<template>
  <div class="evaluation-page">
    <div class="page-header">
      <h2 class="page-title">我的评分</h2>
      <p class="page-desc">查看学生对我的教学评价</p>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid" v-if="evaluationList.length > 0">
      <div class="stat-card">
        <div class="stat-value">{{ evaluationList.length }}</div>
        <div class="stat-label">评价人数</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ avgTotalScore }}</div>
        <div class="stat-label">平均总分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ avgTeachingScore }}</div>
        <div class="stat-label">教学态度均分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ avgContentScore }}</div>
        <div class="stat-label">教学内容均分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ avgMethodScore }}</div>
        <div class="stat-label">教学方法均分</div>
      </div>
      <div class="stat-card">
        <div class="stat-value">{{ avgEffectScore }}</div>
        <div class="stat-label">教学效果均分</div>
      </div>
    </div>

    <!-- 评价明细 -->
    <div class="section">
      <h3 class="section-title">
        <span class="title-bar"></span>
        评价明细
      </h3>

      <div class="table-area" v-if="evaluationList.length > 0">
        <table>
          <thead>
            <tr>
              <th>学生</th>
              <th>课程</th>
              <th>学期</th>
              <th>教学态度</th>
              <th>教学内容</th>
              <th>教学方法</th>
              <th>教学效果</th>
              <th>总分</th>
              <th>评价时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in evaluationList" :key="item.id">
              <td>{{ item.studentName }}</td>
              <td>{{ item.courseName }}</td>
              <td>{{ item.semester }}</td>
              <td>{{ item.teachingScore }}</td>
              <td>{{ item.contentScore }}</td>
              <td>{{ item.methodScore }}</td>
              <td>{{ item.effectScore }}</td>
              <td><strong>{{ item.totalScore }}</strong></td>
              <td>{{ formatDate(item.createTime) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="empty-state" v-else>
        <div class="empty-text">暂无评价记录</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getTeacherEvaluationListApi } from '@/api/evaluation.js'

const evaluationList = ref([])

const avgTotalScore = computed(() => calcAvg('totalScore'))
const avgTeachingScore = computed(() => calcAvg('teachingScore'))
const avgContentScore = computed(() => calcAvg('contentScore'))
const avgMethodScore = computed(() => calcAvg('methodScore'))
const avgEffectScore = computed(() => calcAvg('effectScore'))

function calcAvg(field) {
  const values = evaluationList.value.filter(e => e[field] != null).map(e => parseFloat(e[field]))
  if (values.length === 0) return '-'
  return (values.reduce((a, b) => a + b, 0) / values.length).toFixed(2)
}

const fetchEvaluations = async () => {
  try {
    const res = await getTeacherEvaluationListApi()
    if (res.code === 1) {
      evaluationList.value = res.data || []
    }
  } catch (error) {
    console.error('获取评价列表失败', error)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

onMounted(() => {
  fetchEvaluations()
})
</script>

<style scoped>
.evaluation-page {
  padding: 24px;
  max-width: 1200px;
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
  font-size: 24px;
  font-weight: 700;
  color: #667eea;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 13px;
  color: #888;
}

/* 明细表格 */
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

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 40px;
  color: #999;
}

.empty-text {
  font-size: 14px;
}
</style>
