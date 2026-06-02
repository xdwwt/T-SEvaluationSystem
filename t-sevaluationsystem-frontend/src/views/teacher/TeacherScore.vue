  <template>
  <div class="score-page">
    <div class="page-header">
      <h2 class="page-title">学生成绩</h2>
      <p class="page-desc">选择授课班级，录入或更新学生成绩</p>
    </div>

    <!-- 班级课程选择 -->
    <div class="section">
      <h3 class="section-title">
        <span class="title-bar"></span>
        选择班级课程
      </h3>
      <div class="selector-row">
        <select v-model="selectedClass" @change="handleClassChange" class="selector">
          <option value="">请选择班级课程</option>
          <option v-for="item in classList" :key="item.id" :value="item">
            {{ item.semester }} · {{ item.className }} · {{ item.courseName }}
          </option>
        </select>
        <button class="btn-refresh" @click="fetchClasses">刷新</button>
      </div>
    </div>

    <!-- 成绩占比设置 -->
    <div class="section" v-if="selectedClass">
      <h3 class="section-title">
        <span class="title-bar"></span>
        成绩占比设置
      </h3>
      <div class="ratio-row">
        <div class="ratio-item">
          <label class="ratio-label">平时分占比</label>
          <div class="ratio-input-wrap">
            <input
              type="number"
              v-model.number="usualRatio"
              @change="recalcAllScores"
              class="ratio-input"
              min="0"
              max="100"
              step="1"
            />
            <span class="ratio-suffix">%</span>
          </div>
        </div>
        <div class="ratio-divider">:</div>
        <div class="ratio-item">
          <label class="ratio-label">期末分占比</label>
          <div class="ratio-input-wrap">
            <input
              type="number"
              :value="100 - usualRatio"
              class="ratio-input"
              readonly
              tabindex="-1"
            />
            <span class="ratio-suffix">%</span>
          </div>
        </div>
      </div>
      <p class="ratio-hint">修改占比后，下方所有学生的总评成绩将自动重新计算</p>
    </div>

    <!-- 成绩录入 -->
    <div class="section" v-if="selectedClass">
      <div class="section-header">
        <h3 class="section-title">
          <span class="title-bar"></span>
          成绩录入
        </h3>
        <button class="btn-save" @click="handleSave" :disabled="saving">
          {{ saving ? '保存中...' : '保存成绩' }}
        </button>
      </div>

      <div class="table-area" v-if="studentList.length > 0">
        <table>
          <thead>
            <tr>
              <th>学号</th>
              <th>姓名</th>
              <th>平时分</th>
              <th>期末分</th>
              <th>总评成绩</th>
              <th>评语</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in studentList" :key="item.studentId">
              <td>{{ item.studentNo }}</td>
              <td>{{ item.studentName }}</td>
              <td>
                <input type="number" v-model.number="item.usualScore" @input="calcScore(item)" class="score-input" min="0" max="100" step="0.01" placeholder="0" />
              </td>
              <td>
                <input type="number" v-model.number="item.finalScore" @input="calcScore(item)" class="score-input" min="0" max="100" step="0.01" placeholder="0" />
              </td>
              <td>
                <input type="number" v-model.number="item.score" class="score-input" readonly />
              </td>
              <td>
                <input type="text" v-model="item.comment" class="comment-input" placeholder="请输入评语" />
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="empty-state" v-else-if="!loading">
        <div class="empty-text">该班级暂无学生</div>
      </div>
    </div>

    <!-- 已录入成绩 -->
    <div class="section">
      <h3 class="section-title">
        <span class="title-bar"></span>
        已录入成绩记录
      </h3>

      <div class="table-area" v-if="scoreList.length > 0">
        <table>
          <thead>
            <tr>
              <th>学生</th>
              <th>课程</th>
              <th>班级</th>
              <th>学期</th>
              <th>平时分</th>
              <th>期末分</th>
              <th>总评</th>
              <th>评语</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in scoreList" :key="item.id">
              <td>{{ item.studentName }}</td>
              <td>{{ item.courseName }}</td>
              <td>{{ item.className }}</td>
              <td>{{ item.semester }}</td>
              <td>{{ item.usualScore ?? '-' }}</td>
              <td>{{ item.finalScore ?? '-' }}</td>
              <td><strong>{{ item.score ?? '-' }}</strong></td>
              <td>{{ item.comment || '-' }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="empty-state" v-else>
        <div class="empty-text">暂无已录入成绩</div>
      </div>
    </div>
  </div>

  <!-- Toast 提示 -->
  <transition name="toast">
    <div v-if="toastVisible" class="toast-mask">
      <div class="toast-box">
        <div class="toast-icon">&#10003;</div>
        <div class="toast-text">{{ toastMessage }}</div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  getTeachingClassesApi,
  getTeacherClassStudentsApi,
  submitScoreApi,
  getTeacherScoreListApi
} from '@/api/score.js'

const classList = ref([])
const selectedClass = ref('')
const studentList = ref([])
const originalStudentList = ref([])
const scoreList = ref([])
const loading = ref(false)
const saving = ref(false)
const toastVisible = ref(false)
const toastMessage = ref('')
let toastTimer = null

// 成绩占比：平时分占比（百分比），默认40%
const usualRatio = ref(40)

const showToast = (msg, duration = 2000) => {
  toastMessage.value = msg
  toastVisible.value = true
  if (toastTimer) clearTimeout(toastTimer)
  toastTimer = setTimeout(() => {
    toastVisible.value = false
  }, duration)
}

const fetchClasses = async () => {
  try {
    const res = await getTeachingClassesApi()
    if (res.code === 1) {
      classList.value = res.data || []
    }
  } catch (error) {
    console.error('获取班级列表失败', error)
  }
}

const handleClassChange = async () => {
  if (!selectedClass.value) {
    studentList.value = []
    originalStudentList.value = []
    return
  }
  loading.value = true
  try {
    const params = {
      classId: selectedClass.value.classId,
      courseId: selectedClass.value.courseId,
      semester: selectedClass.value.semester
    }
    const res = await getTeacherClassStudentsApi(params)
    if (res.code === 1) {
      originalStudentList.value = (res.data || []).map(s => {
        const usualScore = s.usualScore != null ? parseFloat(s.usualScore) : null
        const finalScore = s.finalScore != null ? parseFloat(s.finalScore) : null
        const savedScore = s.score != null ? parseFloat(s.score) : null
        return {
          ...s,
          usualScore,
          finalScore,
          score: savedScore ?? (usualScore != null && finalScore != null
            ? calcScoreValue(usualScore, finalScore)
            : null),
          comment: s.comment || ''
        }
      })
      // 上方显示所有学生（已录入的也可以修改）
      studentList.value = originalStudentList.value

      // 将已录入的学生同步到下方列表
      const entered = originalStudentList.value.filter(s => s.score !== null)
      const newRecords = entered.map(s => ({
        id: s.scoreId || Date.now() + Math.random(),
        studentId: s.studentId,
        studentName: s.studentName,
        studentNo: s.studentNo,
        courseId: selectedClass.value.courseId,
        courseName: selectedClass.value.courseName,
        classId: selectedClass.value.classId,
        className: selectedClass.value.className,
        semester: selectedClass.value.semester,
        usualScore: s.usualScore,
        finalScore: s.finalScore,
        score: s.score,
        comment: s.comment,
        createTime: new Date().toISOString()
      }))
      // 移除同班级旧记录避免重复
      scoreList.value = scoreList.value.filter(item => !(
        String(item.classId) === String(selectedClass.value.classId) &&
        String(item.courseId) === String(selectedClass.value.courseId) &&
        item.semester === selectedClass.value.semester
      ))
      scoreList.value = [...newRecords, ...scoreList.value]
    }
  } catch (error) {
    console.error('获取学生列表失败', error)
  } finally {
    loading.value = false
  }
}

const handleSave = async () => {
  if (!selectedClass.value || studentList.value.length === 0) return
  saving.value = true
  try {
    const payload = studentList.value.map(s => ({
      studentId: s.studentId,
      classId: selectedClass.value.classId,
      courseId: selectedClass.value.courseId,
      semester: selectedClass.value.semester,
      usualScore: s.usualScore === '' ? null : s.usualScore,
      finalScore: s.finalScore === '' ? null : s.finalScore,
      score: s.score === '' ? null : s.score,
      comment: s.comment,
      usualRatio: usualRatio.value
    }))
    const res = await submitScoreApi(payload)
    if (res.code === 1) {
      showToast(res.mes || '保存成功')

      await fetchScores()
      await handleClassChange()

      // 如果后端没有返回当前班级的记录，手动补充到下方列表
      const hasCurrent = scoreList.value.some(item =>
        String(item.classId) === String(selectedClass.value.classId) &&
        String(item.courseId) === String(selectedClass.value.courseId) &&
        item.semester === selectedClass.value.semester
      )
      if (!hasCurrent) {
        const savedRecords = payload.map(p => {
          const stu = originalStudentList.value.find(s => s.studentId === p.studentId)
          return {
            id: Date.now() + Math.random(),
            studentId: p.studentId,
            studentName: stu?.studentName,
            studentNo: stu?.studentNo,
            courseId: p.courseId,
            courseName: selectedClass.value.courseName,
            classId: p.classId,
            className: selectedClass.value.className,
            semester: p.semester,
            usualScore: p.usualScore,
            finalScore: p.finalScore,
            score: p.score,
            comment: p.comment,
            createTime: new Date().toISOString()
          }
        })
        scoreList.value = [...savedRecords, ...scoreList.value]
      }
    } else {
      showToast(res.mes || '保存失败')
    }
  } catch (error) {
    console.error('保存成绩失败', error)
    showToast('保存失败')
  } finally {
    saving.value = false
  }
}

const calcScoreValue = (usual, final) => {
  const ratio = usualRatio.value / 100
  return Math.round((usual * ratio + final * (1 - ratio)) * 100) / 100
}

const calcScore = (item) => {
  const usual = parseFloat(item.usualScore)
  const final = parseFloat(item.finalScore)
  if (!isNaN(usual) && !isNaN(final)) {
    item.score = calcScoreValue(usual, final)
  } else {
    item.score = null
  }
}

const recalcAllScores = () => {
  // 限制范围 0~100
  if (usualRatio.value < 0) usualRatio.value = 0
  if (usualRatio.value > 100) usualRatio.value = 100
  studentList.value.forEach(item => calcScore(item))
}

const fetchScores = async () => {
  try {
    const res = await getTeacherScoreListApi()
    if (res.code === 1) {
      const fetched = res.data || []
      // 有数据才合并，避免后端返回空时清空现有记录
      if (fetched.length > 0) {
        const fetchedKeys = new Set(fetched.map(item =>
          `${item.studentId}-${item.courseId}-${item.classId}-${item.semester}`
        ))
        scoreList.value = scoreList.value.filter(item => !fetchedKeys.has(
          `${item.studentId}-${item.courseId}-${item.classId}-${item.semester}`
        ))
        scoreList.value = [...fetched, ...scoreList.value]
      }
    }
  } catch (error) {
    console.error('获取成绩列表失败', error)
  }
}

onMounted(() => {
  fetchClasses()
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

.section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0;
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

/* 选择器 */
.selector-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.selector {
  flex: 1;
  padding: 10px 14px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  background: white;
  cursor: pointer;
}

.btn-refresh {
  padding: 10px 18px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background: white;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}

.btn-save {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  font-size: 14px;
  cursor: pointer;
}

.btn-save:disabled {
  opacity: 0.6;
  cursor: not-allowed;
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
  padding: 10px 12px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}

.table-area th {
  font-weight: 600;
  color: #666;
  background: #fafbfc;
}

.score-input {
  width: 70px;
  padding: 6px 8px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 14px;
  text-align: center;
}

.comment-input {
  width: 160px;
  padding: 6px 8px;
  border: 1px solid #e0e0e0;
  border-radius: 6px;
  font-size: 14px;
}

.toggle {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
}

.toggle input {
  width: 16px;
  height: 16px;
  cursor: pointer;
}

.toggle-label {
  font-size: 13px;
  color: #666;
}

/* 成绩占比设置 */
.ratio-row {
  display: flex;
  align-items: flex-end;
  gap: 16px;
}

.ratio-item {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.ratio-label {
  font-size: 13px;
  color: #666;
}

.ratio-input-wrap {
  display: flex;
  align-items: center;
  gap: 4px;
}

.ratio-input {
  width: 60px;
  padding: 8px 10px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  text-align: center;
}

.ratio-input[readonly] {
  background: #f5f5f5;
  color: #999;
}

.ratio-suffix {
  font-size: 14px;
  color: #666;
}

.ratio-divider {
  font-size: 18px;
  font-weight: 700;
  color: #999;
  padding-bottom: 8px;
}

.ratio-hint {
  font-size: 12px;
  color: #999;
  margin: 10px 0 0 0;
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

/* Toast 样式 */
.toast-mask {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  background: rgba(0, 0, 0, 0.35);
  backdrop-filter: blur(2px);
}

.toast-box {
  background: #fff;
  border-radius: 12px;
  padding: 28px 36px;
  text-align: center;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  min-width: 180px;
}

.toast-icon {
  width: 48px;
  height: 48px;
  margin: 0 auto 12px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-size: 24px;
  line-height: 48px;
  text-align: center;
}

.toast-text {
  font-size: 15px;
  color: #333;
  font-weight: 500;
}

/* 动画 */
.toast-enter-active,
.toast-leave-active {
  transition: opacity 0.25s ease;
}

.toast-enter-from,
.toast-leave-to {
  opacity: 0;
}
</style>
