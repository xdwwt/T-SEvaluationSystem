<template>
  <div class="evaluation-page">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">教师评价</h2>
      <p class="page-desc">对授课教师进行教学评价，帮助提升教学质量</p>
    </div>

    <!-- 待评价教师 -->
    <div class="section">
      <h3 class="section-title">
        <span class="title-bar"></span>
        待评价教师
        <span class="count-badge" v-if="teacherList.length > 0">{{ teacherList.length }}</span>
      </h3>

      <div class="teacher-grid" v-if="teacherList.length > 0">
        <div class="teacher-card" v-for="item in teacherList" :key="item.id">
          <div class="card-header">
            <div class="teacher-avatar">{{ item.teacherName?.charAt(0) }}</div>
            <div class="teacher-info">
              <div class="teacher-name">{{ item.teacherName }}</div>
              <div class="course-name">{{ item.courseName }}</div>
            </div>
          </div>
          <div class="card-meta">
            <span class="meta-tag">{{ item.semester }}</span>
          </div>
          <div class="card-action">
            <button class="btn-evaluate" @click="openEvaluateDialog(item)">立即评价</button>
          </div>
        </div>
      </div>

      <div class="empty-state" v-else>
        <div class="empty-icon">✓</div>
        <div class="empty-text">您已完成所有教师评价，暂无待评价教师</div>
      </div>
    </div>

    <!-- 已评价记录 -->
    <div class="section">
      <h3 class="section-title">
        <span class="title-bar"></span>
        已评价记录
      </h3>

      <div class="table-area" v-if="evaluationList.length > 0">
        <table>
          <thead>
            <tr>
              <th>教师</th>
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
              <td>{{ item.teacherName }}</td>
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
        <div class="empty-text">暂无已评价记录</div>
      </div>
    </div>

    <!-- 评价弹窗 -->
    <div class="dialog-overlay" v-if="showDialog" @click.self="closeDialog">
      <div class="dialog-box">
        <div class="dialog-header">
          <h3>教学评价</h3>
          <button class="btn-close" @click="closeDialog">×</button>
        </div>
        <div class="dialog-body">
          <div class="evaluate-target">
            <span class="label">评价对象：</span>
            <span class="value">{{ currentTeacher.teacherName }} - {{ currentTeacher.courseName }}</span>
          </div>

          <div class="score-items">
            <div class="score-item">
              <div class="score-label">
                <span>教学态度</span>
                <span class="score-value">{{ evaluateForm.teachingScore }} 分</span>
              </div>
              <input type="range" min="1" max="25" step="1" v-model.number="evaluateForm.teachingScore" />
              <div class="score-hint">1 - 25 分</div>
            </div>

            <div class="score-item">
              <div class="score-label">
                <span>教学内容</span>
                <span class="score-value">{{ evaluateForm.contentScore }} 分</span>
              </div>
              <input type="range" min="1" max="25" step="1" v-model.number="evaluateForm.contentScore" />
              <div class="score-hint">1 - 25 分</div>
            </div>

            <div class="score-item">
              <div class="score-label">
                <span>教学方法</span>
                <span class="score-value">{{ evaluateForm.methodScore }} 分</span>
              </div>
              <input type="range" min="1" max="25" step="1" v-model.number="evaluateForm.methodScore" />
              <div class="score-hint">1 - 25 分</div>
            </div>

            <div class="score-item">
              <div class="score-label">
                <span>教学效果</span>
                <span class="score-value">{{ evaluateForm.effectScore }} 分</span>
              </div>
              <input type="range" min="1" max="25" step="1" v-model.number="evaluateForm.effectScore" />
              <div class="score-hint">1 - 25 分</div>
            </div>
          </div>

          <div class="total-score">
            综合评分：<strong>{{ totalScore }}</strong> / 100 分
          </div>
        </div>
        <div class="dialog-footer">
          <button class="btn-cancel" @click="closeDialog">取消</button>
          <button class="btn-confirm" @click="handleSubmit" :disabled="submitting">
            {{ submitting ? '提交中...' : '提交评价' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  getTeachersToEvaluateApi,
  submitEvaluationApi,
  getStudentEvaluationListApi
} from '@/api/evaluation.js'

const teacherList = ref([])
const evaluationList = ref([])
const showDialog = ref(false)
const submitting = ref(false)
const currentTeacher = ref({})

const evaluateForm = ref({
  teachingScore: 20,
  contentScore: 20,
  methodScore: 20,
  effectScore: 20
})

const totalScore = computed(() => {
  return evaluateForm.value.teachingScore +
    evaluateForm.value.contentScore +
    evaluateForm.value.methodScore +
    evaluateForm.value.effectScore
})

const fetchTeachers = async () => {
  try {
    const res = await getTeachersToEvaluateApi()
    if (res.code === 1) {
      teacherList.value = res.data || []
    }
  } catch (error) {
    console.error('获取待评价教师失败', error)
  }
}

const fetchEvaluations = async () => {
  try {
    const res = await getStudentEvaluationListApi()
    if (res.code === 1) {
      evaluationList.value = res.data || []
    }
  } catch (error) {
    console.error('获取已评价记录失败', error)
  }
}

const openEvaluateDialog = (teacher) => {
  currentTeacher.value = teacher
  evaluateForm.value = {
    teachingScore: 20,
    contentScore: 20,
    methodScore: 20,
    effectScore: 20
  }
  showDialog.value = true
}

const closeDialog = () => {
  showDialog.value = false
  currentTeacher.value = {}
}

const handleSubmit = async () => {
  if (submitting.value) return

  const { teachingScore, contentScore, methodScore, effectScore } = evaluateForm.value
  if (teachingScore < 1 || teachingScore > 25 ||
      contentScore < 1 || contentScore > 25 ||
      methodScore < 1 || methodScore > 25 ||
      effectScore < 1 || effectScore > 25) {
    alert('各项评分必须在 1-25 分之间')
    return
  }

  submitting.value = true
  try {
    const res = await submitEvaluationApi({
      teacherId: currentTeacher.value.teacherId,
      courseId: currentTeacher.value.courseId,
      semester: currentTeacher.value.semester,
      teachingScore,
      contentScore,
      methodScore,
      effectScore
    })
    if (res.code === 1) {
      alert('评价提交成功')
      closeDialog()
      fetchTeachers()
      fetchEvaluations()
    } else {
      alert(res.mes || '提交失败')
    }
  } catch (error) {
    console.error('提交评价失败', error)
    alert('提交失败')
  } finally {
    submitting.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  fetchTeachers()
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

.section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
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

.count-badge {
  background: #e74c3c;
  color: white;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: 600;
}

/* 教师卡片 */
.teacher-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
}

.teacher-card {
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 20px;
  transition: all 0.25s ease;
}

.teacher-card:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.teacher-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  font-weight: 600;
}

.teacher-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.course-name {
  font-size: 13px;
  color: #888;
  margin-top: 2px;
}

.card-meta {
  margin-bottom: 16px;
}

.meta-tag {
  background: #f0f0f5;
  color: #666;
  font-size: 12px;
  padding: 4px 10px;
  border-radius: 6px;
}

.btn-evaluate {
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btn-evaluate:hover {
  opacity: 0.9;
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 40px;
  color: #999;
}

.empty-icon {
  font-size: 40px;
  color: #43e97b;
  margin-bottom: 12px;
}

.empty-text {
  font-size: 14px;
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

/* 弹窗 */
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
  z-index: 1000;
}

.dialog-box {
  background: white;
  border-radius: 16px;
  width: 480px;
  max-width: 90vw;
  max-height: 90vh;
  overflow-y: auto;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.dialog-header h3 {
  margin: 0;
  font-size: 17px;
  color: #1a1a2e;
}

.btn-close {
  background: none;
  border: none;
  font-size: 24px;
  color: #999;
  cursor: pointer;
}

.dialog-body {
  padding: 24px;
}

.evaluate-target {
  margin-bottom: 20px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
  font-size: 14px;
}

.evaluate-target .label {
  color: #666;
}

.evaluate-target .value {
  font-weight: 600;
  color: #333;
}

.score-items {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.score-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.score-label {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
  color: #333;
}

.score-value {
  font-weight: 600;
  color: #667eea;
}

.score-item input[type="range"] {
  width: 100%;
  height: 6px;
  border-radius: 3px;
  background: #e0e0e0;
  outline: none;
  -webkit-appearance: none;
}

.score-item input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  cursor: pointer;
}

.score-hint {
  font-size: 12px;
  color: #999;
}

.total-score {
  margin-top: 20px;
  padding: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 10px;
  text-align: center;
  font-size: 15px;
}

.total-score strong {
  font-size: 24px;
  margin: 0 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid #f0f0f0;
}

.btn-cancel {
  padding: 10px 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}

.btn-confirm {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
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
