<template>
  <div class="arrangement-page">
    <!-- 搜索区 -->
    <div class="search-area">
      <div class="search-row">
        <div class="search-item">
          <label>班级</label>
          <input v-model="searchForm.className" placeholder="请输入班级" @keyup.enter="handleSearch" />
        </div>
        <div class="search-item">
          <label>教师</label>
          <input v-model="searchForm.teacherName" placeholder="请输入教师姓名" @keyup.enter="handleSearch" />
        </div>
        <div class="search-item">
          <label>课程</label>
          <input v-model="searchForm.courseName" placeholder="请输入课程名称" @keyup.enter="handleSearch" />
        </div>
        <div class="search-item">
          <label>学期</label>
          <select v-model="searchForm.semester">
            <option value="">全部</option>
            <option value="2024-2025-1">2024-2025-1</option>
            <option value="2024-2025-2">2024-2025-2</option>
            <option value="2025-2026-1">2025-2026-1</option>
            <option value="2025-2026-2">2025-2026-2</option>
          </select>
        </div>
        <div class="search-btns">
          <button class="btn-search" @click="handleSearch">查询</button>
          <button class="btn-reset" @click="handleReset">重置</button>
        </div>
      </div>
    </div>

    <!-- 操作按钮区 -->
    <div class="action-area">
      <button class="btn-add" @click="handleAddClick">+ 新增</button>
      <button class="btn-refresh" @click="fetchList">刷新</button>
    </div>

    <!-- 数据表格 -->
    <div class="table-area">
      <table>
        <thead>
          <tr>
            <th>序号</th>
            <th>班级</th>
            <th>教师</th>
            <th>课程</th>
            <th>学期</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in tableData" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td>{{ item.className }}</td>
            <td>{{ item.teacherName }}</td>
            <td>{{ item.courseName }}</td>
            <td>{{ item.semester }}</td>
            <td>
              <button class="btn-view" @click="handleViewStudents(item)">查看学生</button>
              <button class="btn-edit" @click="handleEditClick(item)">修改</button>
              <button class="btn-delete" @click="handleDeleteClick(item.id)">删除</button>
            </td>
          </tr>
          <tr v-if="tableData.length === 0">
            <td colspan="6" class="no-data">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <div class="pagination-area" v-if="total > 0">
      <div class="pagination-info">共 {{ total }} 条</div>
      <div class="pagination-btns">
        <button class="page-btn" :disabled="pageNum === 1" @click="handlePrevPage">上一页</button>
        <button
          v-for="p in visiblePages"
          :key="p"
          class="page-btn"
          :class="{ active: p === pageNum }"
          @click="handlePageChange(p)"
        >
          {{ p }}
        </button>
        <button class="page-btn" :disabled="pageNum === pages" @click="handleNextPage">下一页</button>
      </div>
      <div class="pagination-jump">
        <span>跳至</span>
        <input v-model.number="jumpPage" type="number" min="1" :max="pages" @keyup.enter="handleJumpPage" />
        <span>页</span>
        <button class="page-btn jump-btn" @click="handleJumpPage">GO</button>
      </div>
    </div>

    <!-- 成功提示弹窗 -->
    <ConfirmDialog
      v-model:visible="showSuccess"
      title="提示"
      :message="successMsg"
      :showCancel="false"
      @confirm="showSuccess = false"
    />

    <!-- 删除确认弹窗 -->
    <ConfirmDialog
      v-model:visible="showDeleteConfirm"
      title="确认删除"
      message="是否删除该排课记录？删除后不可恢复。"
      @confirm="handleDeleteConfirm"
    />

    <!-- 删除成功弹窗 -->
    <ConfirmDialog
      v-model:visible="showDeleteSuccess"
      title="提示"
      message="删除成功"
      :showCancel="false"
      @confirm="showDeleteSuccess = false"
    />

    <!-- 查看学生弹窗 -->
    <div class="modal" v-if="showStudentModal" @click="handleCloseStudentModal">
      <div class="modal-content student-modal" @click.stop>
        <div class="modal-header">
          <h3>排课学生 - {{ currentArrangement.className }} · {{ currentArrangement.courseName }} · {{ currentArrangement.semester }}</h3>
          <span class="close-btn" @click="handleCloseStudentModal">x</span>
        </div>
        <div class="modal-body">
          <div v-if="studentLoading" class="loading-text">加载中...</div>
          <div v-else-if="studentList.length === 0" class="empty-state">
            <div class="empty-text">该班级暂无学生</div>
            <div class="empty-subtext">请先在班级管理中为学生分配班级</div>
          </div>
          <div v-else class="student-table-wrapper">
            <table class="student-table">
              <thead>
                <tr>
                  <th>学号</th>
                  <th>姓名</th>
                  <th>性别</th>
                  <th>年级</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="s in studentList" :key="s.id">
                  <td>{{ s.studentNo }}</td>
                  <td>{{ s.name }}</td>
                  <td>{{ s.gender === 1 ? '男' : '女' }}</td>
                  <td>{{ s.grade }}</td>
                </tr>
              </tbody>
            </table>
            <div class="student-count">共 {{ studentList.length }} 人</div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="handleCloseStudentModal">关闭</button>
        </div>
      </div>
    </div>

    <!-- 新增/编辑排课弹窗 -->
    <div class="modal" v-if="showAdd" @click="handleCloseModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑排课' : '新增排课' }}</h3>
          <span class="close-btn" @click="handleCloseModal">x</span>
        </div>
        <div class="modal-body">
          <div class="form-row">
            <div class="form-col">
              <div class="form-item">
                <label>班级 <span class="required">*</span></label>
                <select v-model="form.classId">
                  <option value="">请选择</option>
                  <option v-for="c in classOptions" :key="c.id" :value="c.id">{{ c.className }}</option>
                </select>
              </div>
            </div>
            <div class="form-col">
              <div class="form-item">
                <label>教师 <span class="required">*</span></label>
                <select v-model="form.teacherId">
                  <option value="">请选择</option>
                  <option v-for="t in teacherOptions" :key="t.id" :value="t.id">{{ t.name }}</option>
                </select>
              </div>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <div class="form-item">
                <label>课程 <span class="required">*</span></label>
                <select v-model="form.courseId">
                  <option value="">请选择</option>
                  <option v-for="c in courseOptions" :key="c.id" :value="c.id">{{ c.courseName }}</option>
                </select>
              </div>
            </div>
            <div class="form-col">
              <div class="form-item">
                <label>学期 <span class="required">*</span></label>
                <select v-model="form.semester">
                  <option value="">请选择</option>
                  <option value="2024-2025-1">2024-2025-1</option>
                  <option value="2024-2025-2">2024-2025-2</option>
                  <option value="2025-2026-1">2025-2026-1</option>
                  <option value="2025-2026-2">2025-2026-2</option>
                </select>
              </div>
            </div>
          </div>
          <div class="error-msg" v-if="errorMsg">{{ errorMsg }}</div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="handleCloseModal">取消</button>
          <button class="btn-confirm" @click="handleSubmit" :disabled="loading">{{ loading ? '提交中...' : '确定' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { addArrangementApi, listArrangementApi, updateArrangementApi, deleteArrangementApi } from '@/api/arrangement.js'
import { allTeacherApi } from '@/api/teacher.js'
import { allClassApi } from '@/api/class.js'
import { allCourseApi } from '@/api/course.js'
import { listClassStudentsApi } from '@/api/classStudent.js'
import ConfirmDialog from '@/components/ConfirmDialog.vue'

const showAdd = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const errorMsg = ref('')
const tableData = ref([])
const showSuccess = ref(false)

const showDeleteConfirm = ref(false)
const showDeleteSuccess = ref(false)
const deleteId = ref('')
const successMsg = ref('')

// 查看学生
const showStudentModal = ref(false)
const currentArrangement = ref({})
const studentList = ref([])
const studentLoading = ref(false)

// 分页
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)
const pages = ref(0)

// 搜索条件
const searchForm = ref({
  className: '',
  teacherName: '',
  courseName: '',
  semester: ''
})

// 表单
const form = ref({
  classId: '',
  teacherId: '',
  courseId: '',
  semester: ''
})

const classOptions = ref([])
const teacherOptions = ref([])
const courseOptions = ref([])

const fetchOptions = async () => {
  try {
    const [teacherRes, classRes, courseRes] = await Promise.all([
      allTeacherApi(),
      allClassApi(),
      allCourseApi()
    ])
    if (teacherRes.code === 1) teacherOptions.value = teacherRes.data || []
    if (classRes.code === 1) classOptions.value = classRes.data || []
    if (courseRes.code === 1) courseOptions.value = courseRes.data || []
  } catch (error) {
    console.error('获取下拉选项失败', error)
  }
}

const fetchList = async () => {
  try {
    const res = await listArrangementApi(searchForm.value, pageNum.value, pageSize.value)
    console.log('后端返回:', res)
    if (res.code === 1) {
      tableData.value = res.data.records || []
      total.value = res.data.total || 0
      pages.value = res.data.pages || 0
    } else {
      console.warn('接口返回非成功状态:', res)
    }
  } catch (error) {
    console.error('获取列表失败', error)
  }
}

const handleSearch = () => {
  pageNum.value = 1
  fetchList()
}

const handleReset = () => {
  searchForm.value = {
    className: '',
    teacherName: '',
    courseName: '',
    semester: ''
  }
  pageNum.value = 1
  fetchList()
}

const handlePageChange = (newPage) => {
  if (newPage < 1 || newPage > pages.value) return
  pageNum.value = newPage
  fetchList()
}

const handlePrevPage = () => {
  if (pageNum.value > 1) {
    pageNum.value--
    fetchList()
  }
}

const handleNextPage = () => {
  if (pageNum.value < pages.value) {
    pageNum.value++
    fetchList()
  }
}

const jumpPage = ref('')
const handleJumpPage = () => {
  const num = parseInt(jumpPage.value)
  if (num && num >= 1 && num <= pages.value) {
    handlePageChange(num)
    jumpPage.value = ''
  }
}

const visiblePages = computed(() => {
  const result = []
  const maxVisible = 5
  let start = Math.max(1, pageNum.value - Math.floor(maxVisible / 2))
  let end = Math.min(pages.value, start + maxVisible - 1)
  if (end - start + 1 < maxVisible) {
    start = Math.max(1, end - maxVisible + 1)
  }
  for (let i = start; i <= end; i++) {
    result.push(i)
  }
  return result
})

const handleViewStudents = async (item) => {
  currentArrangement.value = item
  showStudentModal.value = true
  studentLoading.value = true
  try {
    const res = await listClassStudentsApi(item.classId)
    if (res.code === 1) {
      studentList.value = res.data || []
    } else {
      studentList.value = []
    }
  } catch (error) {
    console.error('获取学生列表失败', error)
    studentList.value = []
  } finally {
    studentLoading.value = false
  }
}

const handleCloseStudentModal = () => {
  showStudentModal.value = false
  currentArrangement.value = {}
  studentList.value = []
}

const handleDeleteClick = (id) => {
  deleteId.value = id
  showDeleteConfirm.value = true
}

const handleDeleteConfirm = async () => {
  try {
    const res = await deleteArrangementApi(deleteId.value)
    if (res.code === 1) {
      showDeleteSuccess.value = true
      fetchList()
    } else {
      alert(res.mes || '删除失败')
    }
  } catch (error) {
    alert('请求失败，请稍后重试')
  }
}

const handleAddClick = () => {
  isEdit.value = false
  form.value = {
    classId: '',
    teacherId: '',
    courseId: '',
    semester: ''
  }
  errorMsg.value = ''
  showAdd.value = true
}

const handleEditClick = (item) => {
  isEdit.value = true
  form.value = {
    id: item.id,
    classId: item.classId,
    teacherId: item.teacherId,
    courseId: item.courseId,
    semester: item.semester || ''
  }
  errorMsg.value = ''
  showAdd.value = true
}

const handleCloseModal = () => {
  showAdd.value = false
  isEdit.value = false
  errorMsg.value = ''
}

const handleSubmit = async () => {
  errorMsg.value = ''
  if (!form.value.classId || !form.value.teacherId || !form.value.courseId || !form.value.semester) {
    errorMsg.value = '班级、教师、课程、学期均为必填项'
    return
  }

  loading.value = true
  try {
    const res = isEdit.value
      ? await updateArrangementApi(form.value)
      : await addArrangementApi(form.value)
    if (res.code === 1) {
      successMsg.value = isEdit.value ? '修改成功' : '新增成功'
      showSuccess.value = true
      handleCloseModal()
      form.value = {
        classId: '',
        teacherId: '',
        courseId: '',
        semester: ''
      }
      fetchList()
    } else {
      errorMsg.value = res.mes || (isEdit.value ? '修改失败' : '新增失败')
    }
  } catch (error) {
    errorMsg.value = '请求失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchList()
  fetchOptions()
})
</script>

<style scoped>
.arrangement-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 40px);
  padding: 20px;
  box-sizing: border-box;
}

/* 搜索区 */
.search-area {
  flex-shrink: 0;
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 15px;
}

.search-row {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 15px;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-item label {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
}

.search-item input,
.search-item select {
  padding: 6px 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  width: 150px;
}

.search-btns {
  display: flex;
  gap: 10px;
  margin-left: auto;
}

.btn-search {
  padding: 6px 16px;
  background: #2c3e50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-search:active {
  transform: scale(0.96);
  opacity: 0.9;
}

.btn-reset {
  padding: 6px 16px;
  background: #f0f0f0;
  color: #666;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-reset:active {
  transform: scale(0.96);
  opacity: 0.9;
}

/* 操作按钮区 */
.action-area {
  flex-shrink: 0;
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.btn-add {
  padding: 8px 16px;
  background: #2c3e50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-add:active {
  transform: scale(0.96);
  opacity: 0.9;
}

.btn-refresh {
  padding: 8px 16px;
  background: white;
  color: #666;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-refresh:active {
  transform: scale(0.96);
  opacity: 0.9;
}

/* 表格区 */
.table-area {
  flex: 1;
  overflow: auto;
  min-height: 0;
  background: white;
  border-radius: 8px;
}

.table-area table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.table-area th {
  background: #f8f9fa;
  padding: 12px;
  text-align: left;
  font-weight: 600;
  color: #333;
  border-bottom: 1px solid #eee;
}

.table-area td {
  padding: 12px;
  border-bottom: 1px solid #eee;
  color: #666;
}

.table-area tr:hover {
  background: #f8f9fa;
}

.no-data {
  text-align: center;
  color: #999;
  padding: 40px;
}

.btn-view {
  padding: 4px 10px;
  background: #27ae60;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
  transition: all 0.15s;
}

.btn-view:active {
  transform: scale(0.96);
  opacity: 0.9;
}

.btn-edit {
  padding: 4px 10px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
  transition: all 0.15s;
}

.btn-edit:active {
  transform: scale(0.96);
  opacity: 0.9;
}

.btn-delete {
  padding: 4px 10px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
  transition: all 0.15s;
}

.btn-delete:active {
  transform: scale(0.96);
  opacity: 0.9;
}

/* 弹窗 */
.modal {
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

.modal-content {
  background: white;
  border-radius: 12px;
  width: 600px;
  max-height: 85vh;
  overflow: hidden;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.2);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  color: #333;
}

.close-btn {
  font-size: 20px;
  color: #999;
  cursor: pointer;
  line-height: 1;
}

.close-btn:hover {
  color: #666;
}

.modal-body {
  padding: 24px;
  overflow-y: auto;
  max-height: 60vh;
}

.form-row {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;
}

.form-col {
  flex: 1;
}

.form-item {
  margin-bottom: 0;
}

.form-item label {
  display: block;
  margin-bottom: 6px;
  font-size: 13px;
  color: #555;
  font-weight: 500;
}

.form-item .required {
  color: #e74c3c;
}

.form-item input,
.form-item select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  transition: all 0.3s;
  outline: none;
}

.form-item input:focus,
.form-item select:focus {
  border-color: #2c3e50;
  box-shadow: 0 0 0 3px rgba(44, 62, 80, 0.1);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid #eee;
  background: #fafafa;
}

.btn-cancel {
  padding: 10px 24px;
  background: white;
  color: #666;
  border: 1px solid #ddd;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.15s;
}

.btn-cancel:active {
  transform: scale(0.96);
  opacity: 0.9;
}

.btn-confirm {
  padding: 10px 24px;
  background: #2c3e50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.15s;
}

.btn-confirm:active {
  transform: scale(0.96);
  opacity: 0.9;
}

.btn-confirm:disabled {
  background: #95a5a6;
  cursor: not-allowed;
}

.error-msg {
  color: #e74c3c;
  font-size: 14px;
  margin-top: 10px;
}

/* 分页 */
.pagination-area {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 15px;
  padding: 12px 20px;
  background: white;
  border-radius: 8px;
}

.pagination-info {
  font-size: 14px;
  color: #666;
}

.pagination-btns {
  display: flex;
  gap: 6px;
}

.page-btn {
  padding: 6px 12px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
  color: #333;
  transition: all 0.2s;
}

.page-btn:active:not(:disabled) {
  transform: scale(0.96);
  opacity: 0.9;
}

.page-btn:hover:not(:disabled) {
  border-color: #2c3e50;
  color: #2c3e50;
}

.page-btn.active {
  background: #2c3e50;
  color: white;
  border-color: #2c3e50;
}

.page-btn:disabled {
  cursor: not-allowed;
  color: #bbb;
  border-color: #eee;
}

.pagination-jump {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #666;
}

.pagination-jump input {
  width: 50px;
  padding: 5px 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  text-align: center;
  font-size: 13px;
}

.jump-btn {
  padding: 5px 12px;
  background: #2c3e50;
  color: white;
  border: none;
  transition: all 0.15s;
}

.jump-btn:active {
  transform: scale(0.96);
  opacity: 0.9;
}

/* 查看学生弹窗 */
.student-modal {
  width: 700px;
}

.student-modal .modal-body {
  max-height: 55vh;
}

.loading-text {
  text-align: center;
  padding: 40px;
  color: #999;
  font-size: 14px;
}

.student-table-wrapper {
  overflow-x: auto;
}

.student-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.student-table th,
.student-table td {
  padding: 10px 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.student-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #333;
}

.student-table td {
  color: #666;
}

.student-count {
  text-align: right;
  padding: 12px 0;
  font-size: 13px;
  color: #888;
}

.empty-subtext {
  font-size: 12px;
  color: #aaa;
  margin-top: 6px;
}
</style>
