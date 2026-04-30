<template>
  <div class="class-page">
    <!-- 搜索区 -->
    <div class="search-area">
      <div class="search-row">
        <div class="search-item">
          <label>班级名称</label>
          <input v-model="searchForm.className" placeholder="请输入班级名称" />
        </div>
        <div class="search-item">
          <label>年级</label>
          <select v-model="searchForm.grade">
            <option value="">全部</option>
            <option value="2021">2021</option>
            <option value="2022">2022</option>
            <option value="2023">2023</option>
            <option value="2024">2024</option>
          </select>
        </div>
        <div class="search-item">
          <label>专业</label>
          <select v-model="searchForm.major">
            <option value="">全部</option>
            <option value="计算机科学与技术">计算机科学与技术</option>
            <option value="软件工程">软件工程</option>
            <option value="数学与应用数学">数学与应用数学</option>
            <option value="英语">英语</option>
            <option value="汉语言文学">汉语言文学</option>
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
            <th>班级名称</th>
            <th>年级</th>
            <th>专业</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in tableData" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td>{{ item.className }}</td>
            <td>{{ item.grade }}</td>
            <td>{{ item.major }}</td>
            <td>
              <button class="btn-edit" @click="handleEditClick(item)">修改</button>
              <button class="btn-delete" @click="handleDeleteClick(item.id)">删除</button>
              <button class="btn-manage" @click="handleManageStudents(item)">管理学生</button>
            </td>
          </tr>
          <tr v-if="tableData.length === 0">
            <td colspan="5" class="no-data">暂无数据</td>
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
      message="是否删除该班级？删除后不可恢复。"
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

    <!-- 新增/编辑班级弹窗 -->
    <div class="modal" v-if="showAdd" @click="handleCloseModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑班级' : '新增班级' }}</h3>
          <span class="close-btn" @click="handleCloseModal">x</span>
        </div>
        <div class="modal-body">
          <div class="form-row">
            <div class="form-col">
              <div class="form-item">
                <label>班级名称 <span class="required">*</span></label>
                <input v-model="form.className" placeholder="请输入班级名称" />
              </div>
            </div>
            <div class="form-col">
              <div class="form-item">
                <label>年级</label>
                <select v-model="form.grade">
                  <option value="">请选择</option>
                  <option value="2021">2021</option>
                  <option value="2022">2022</option>
                  <option value="2023">2023</option>
                  <option value="2024">2024</option>
                </select>
              </div>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <div class="form-item">
                <label>专业</label>
                <select v-model="form.major">
                  <option value="">请选择</option>
                  <option value="计算机科学与技术">计算机科学与技术</option>
                  <option value="软件工程">软件工程</option>
                  <option value="数学与应用数学">数学与应用数学</option>
                  <option value="英语">英语</option>
                  <option value="汉语言文学">汉语言文学</option>
                </select>
              </div>
            </div>
            <div class="form-col"></div>
          </div>
          <div class="error-msg" v-if="errorMsg">{{ errorMsg }}</div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="handleCloseModal">取消</button>
          <button class="btn-confirm" @click="handleSubmit" :disabled="loading">{{ loading ? '提交中...' : '确定' }}</button>
        </div>
      </div>
    </div>

    <!-- 班级学生管理弹窗 -->
    <div class="modal" v-if="showStudentManage" @click="showStudentManage = false">
      <div class="modal-content student-manage-modal" @click.stop>
        <div class="modal-header">
          <h3>管理班级学生 - {{ currentClass.className }}</h3>
          <span class="close-btn" @click="showStudentManage = false">x</span>
        </div>
        <div class="modal-body">
          <!-- 已分配学生 -->
          <div class="student-section">
            <h4>已分配学生（{{ classStudents.length }}人）</h4>
            <div class="student-table-wrapper">
              <table class="student-table">
                <thead>
                  <tr>
                    <th>学号</th>
                    <th>姓名</th>
                    <th>性别</th>
                    <th>年级</th>
                    <th>专业</th>
                    <th>操作</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="student in classStudents" :key="student.id">
                    <td>{{ student.studentNo }}</td>
                    <td>{{ student.name }}</td>
                    <td>{{ student.gender === 1 ? '男' : '女' }}</td>
                    <td>{{ student.grade }}</td>
                    <td>{{ student.major }}</td>
                    <td>
                      <button class="btn-remove" @click="handleRemoveStudent(student.id)">移除</button>
                    </td>
                  </tr>
                  <tr v-if="classStudents.length === 0">
                    <td colspan="6" class="no-data">暂无学生</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- 添加学生 -->
          <div class="student-section add-section">
            <h4>添加学生</h4>
            <div class="add-student-row">
              <select v-model="selectedStudentId">
                <option value="">请选择学生</option>
                <option v-for="s in unassignedStudents" :key="s.id" :value="s.id">
                  {{ s.studentNo }} - {{ s.name }}
                </option>
              </select>
              <button class="btn-confirm" @click="handleAddStudent" :disabled="!selectedStudentId">添加</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { addClassApi, listClassApi, updateClassApi, deleteClassApi } from '@/api/class.js'
import {
  listClassStudentsApi,
  listUnassignedStudentsApi,
  addStudentToClassApi,
  removeStudentFromClassApi
} from '@/api/classStudent.js'
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

// 班级学生管理
const showStudentManage = ref(false)
const currentClass = ref({})
const classStudents = ref([])
const unassignedStudents = ref([])
const selectedStudentId = ref('')

// 分页
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)
const pages = ref(0)

const searchForm = ref({
  className: '',
  grade: '',
  major: ''
})

const form = ref({
  className: '',
  grade: '',
  major: ''
})

const fetchList = async () => {
  try {
    const res = await listClassApi(searchForm.value, pageNum.value, pageSize.value)
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
    grade: '',
    major: ''
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

const handleDeleteClick = (id) => {
  deleteId.value = id
  showDeleteConfirm.value = true
}

const handleDeleteConfirm = async () => {
  try {
    const res = await deleteClassApi(deleteId.value)
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
    className: '',
    grade: '',
    major: ''
  }
  errorMsg.value = ''
  showAdd.value = true
}

const handleEditClick = (item) => {
  isEdit.value = true
  form.value = {
    id: item.id,
    className: item.className,
    grade: item.grade || '',
    major: item.major || ''
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
  if (!form.value.className) {
    errorMsg.value = '班级名称不能为空'
    return
  }

  loading.value = true
  try {
    const res = isEdit.value
      ? await updateClassApi(form.value)
      : await addClassApi(form.value)
    if (res.code === 1) {
      successMsg.value = isEdit.value ? '修改成功' : '新增成功'
      showSuccess.value = true
      handleCloseModal()
      form.value = {
        className: '',
        grade: '',
        major: ''
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

const handleManageStudents = (item) => {
  currentClass.value = item
  showStudentManage.value = true
  fetchClassStudents()
  fetchUnassignedStudents()
}

const fetchClassStudents = async () => {
  try {
    const res = await listClassStudentsApi(currentClass.value.id)
    if (res.code === 1) {
      classStudents.value = res.data || []
    }
  } catch (error) {
    console.error('获取班级学生失败', error)
  }
}

const fetchUnassignedStudents = async () => {
  try {
    const res = await listUnassignedStudentsApi()
    if (res.code === 1) {
      unassignedStudents.value = res.data || []
    }
  } catch (error) {
    console.error('获取未分配学生失败', error)
  }
}

const handleAddStudent = async () => {
  if (!selectedStudentId.value) {
    alert('请选择学生')
    return
  }
  try {
    const res = await addStudentToClassApi(currentClass.value.id, selectedStudentId.value)
    if (res.code === 1) {
      selectedStudentId.value = ''
      fetchClassStudents()
      fetchUnassignedStudents()
    } else {
      alert(res.mes || '添加失败')
    }
  } catch (error) {
    alert('请求失败，请稍后重试')
  }
}

const handleRemoveStudent = async (id) => {
  try {
    const res = await removeStudentFromClassApi(id)
    if (res.code === 1) {
      fetchClassStudents()
      fetchUnassignedStudents()
    } else {
      alert(res.mes || '移除失败')
    }
  } catch (error) {
    alert('请求失败，请稍后重试')
  }
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.class-page {
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

/* 管理学生按钮 */
.btn-manage {
  padding: 4px 10px;
  background: #27ae60;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-manage:active {
  transform: scale(0.96);
  opacity: 0.9;
}

/* 学生管理弹窗 */
.student-manage-modal {
  width: 800px;
}

.student-section {
  margin-bottom: 20px;
}

.student-section h4 {
  margin: 0 0 12px 0;
  font-size: 15px;
  color: #333;
  font-weight: 600;
}

.student-table-wrapper {
  max-height: 300px;
  overflow-y: auto;
  border: 1px solid #eee;
  border-radius: 6px;
}

.student-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.student-table th {
  background: #f8f9fa;
  padding: 10px 12px;
  text-align: left;
  font-weight: 600;
  color: #333;
  border-bottom: 1px solid #eee;
}

.student-table td {
  padding: 10px 12px;
  border-bottom: 1px solid #eee;
  color: #666;
}

.student-table tr:hover {
  background: #f8f9fa;
}

.btn-remove {
  padding: 4px 10px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-remove:active {
  transform: scale(0.96);
  opacity: 0.9;
}

.add-section {
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.add-student-row {
  display: flex;
  gap: 12px;
  align-items: center;
}

.add-student-row select {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  outline: none;
}

.add-student-row select:focus {
  border-color: #2c3e50;
  box-shadow: 0 0 0 3px rgba(44, 62, 80, 0.1);
}

.add-student-row .btn-confirm {
  padding: 10px 24px;
}
</style>
