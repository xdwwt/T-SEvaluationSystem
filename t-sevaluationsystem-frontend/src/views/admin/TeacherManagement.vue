<template>
  <div class="teacher-page">
    <!-- 搜索区 -->
    <div class="search-area">
      <div class="search-row">
        <div class="search-item">
          <label>工号</label>
          <input v-model="searchForm.teacherNo" placeholder="请输入工号" />
        </div>
        <div class="search-item">
          <label>姓名</label>
          <input v-model="searchForm.name" placeholder="请输入姓名" />
        </div>
        <div class="search-item">
          <label>职称</label>
          <select v-model="searchForm.title">
            <option value="">全部</option>
            <option value="教授">教授</option>
            <option value="副教授">副教授</option>
            <option value="讲师">讲师</option>
            <option value="助教">助教</option>
          </select>
        </div>
        <div class="search-item">
          <label>院系</label>
          <select v-model="searchForm.department">
            <option value="">全部</option>
            <option value="计算机学院">计算机学院</option>
            <option value="数学学院">数学学院</option>
            <option value="外语学院">外语学院</option>
            <option value="文学院">文学院</option>
            <option value="理学院">理学院</option>
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
      <button class="btn-add" @click="showAdd = true">+ 新增</button>
      <button class="btn-refresh" @click="fetchList">刷新</button>
    </div>

    <!-- 数据表格 -->
    <div class="table-area">
      <table>
        <thead>
          <tr>
            <th>序号</th>
            <th>工号</th>
            <th>姓名</th>
            <th>性别</th>
            <th>职称</th>
            <th>院系</th>
            <th>电话</th>
            <th>邮箱</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in tableData" :key="item.id">
            <td>{{ index + 1 }}</td>
            <td>{{ item.teacherNo }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.gender === 1 ? '男' : '女' }}</td>
            <td>{{ item.title }}</td>
            <td>{{ item.department }}</td>
            <td>{{ item.phone }}</td>
            <td>{{ item.email }}</td>
            <td>
              <button class="btn-edit">修改</button>
              <button class="btn-delete">删除</button>
              <button class="btn-reset" @click="handleResetClick(item.teacherNo)">重置密码</button>
            </td>
          </tr>
          <tr v-if="tableData.length === 0">
            <td colspan="9" class="no-data">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 成功提示弹窗 -->
    <ConfirmDialog
      v-model:visible="showSuccess"
      title="提示"
      message="新增成功"
      :showCancel="false"
      @confirm="showSuccess = false"
    />

    <!-- 重置密码确认弹窗 -->
    <ConfirmDialog
      v-model:visible="showResetConfirm"
      title="确认重置"
      message="是否重置该教师的密码？"
      @confirm="handleResetConfirm"
    />

    <!-- 重置密码成功弹窗 -->
    <ConfirmDialog
      v-model:visible="showResetSuccess"
      title="提示"
      message="密码已重置为 123456"
      :showCancel="false"
      @confirm="showResetSuccess = false"
    />

    <!-- 新增教师弹窗 -->
    <div class="modal" v-if="showAdd" @click="showAdd = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>新增教师</h3>
          <span class="close-btn" @click="showAdd = false">x</span>
        </div>
        <div class="modal-body">
          <div class="form-row">
            <div class="form-col">
              <div class="form-item">
                <label>工号 <span class="required">*</span></label>
                <input v-model="form.teacherNo" placeholder="请输入工号" />
              </div>
            </div>
            <div class="form-col">
              <div class="form-item">
                <label>姓名 <span class="required">*</span></label>
                <input v-model="form.name" placeholder="请输入姓名" />
              </div>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <div class="form-item">
                <label>性别</label>
                <select v-model="form.gender">
                  <option :value="null">请选择</option>
                  <option :value="1">男</option>
                  <option :value="0">女</option>
                </select>
              </div>
            </div>
            <div class="form-col">
              <div class="form-item">
                <label>职称</label>
                <select v-model="form.title">
                  <option value="">请选择</option>
                  <option value="教授">教授</option>
                  <option value="副教授">副教授</option>
                  <option value="讲师">讲师</option>
                  <option value="助教">助教</option>
                </select>
              </div>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <div class="form-item">
                <label>院系</label>
                <select v-model="form.department">
                  <option value="">请选择</option>
                  <option value="计算机学院">计算机学院</option>
                  <option value="数学学院">数学学院</option>
                  <option value="外语学院">外语学院</option>
                  <option value="文学院">文学院</option>
                  <option value="理学院">理学院</option>
                </select>
              </div>
            </div>
            <div class="form-col">
              <div class="form-item">
                <label>电话</label>
                <input v-model="form.phone" placeholder="请输入电话" />
              </div>
            </div>
          </div>
          <div class="form-row">
            <div class="form-col">
              <div class="form-item">
                <label>邮箱</label>
                <input v-model="form.email" placeholder="请输入邮箱" />
              </div>
            </div>
            <div class="form-col">
              <div class="form-item">
                <label>入职日期</label>
                <input v-model="form.entryDate" type="date" />
              </div>
            </div>
          </div>
          <div class="error-msg" v-if="errorMsg">{{ errorMsg }}</div>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="showAdd = false">取消</button>
          <button class="btn-confirm" @click="handleSubmit" :disabled="loading">{{ loading ? '提交中...' : '确定' }}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { addTeacherApi, listTeacherApi, resetPasswordApi } from '@/api/teacher.js'
import ConfirmDialog from '@/components/ConfirmDialog.vue'

const showAdd = ref(false)
const loading = ref(false)
const errorMsg = ref('')
const tableData = ref([])
const showSuccess = ref(false)
const showResetConfirm = ref(false)
const showResetSuccess = ref(false)
const resetUserId = ref('')

const searchForm = ref({
  teacherNo: '',
  name: '',
  title: '',
  department: ''
})

const form = ref({
  teacherNo: '',
  name: '',
  gender: null,
  title: '',
  department: '',
  phone: '',
  email: '',
  entryDate: ''
})

const fetchList = async () => {
  try {
    const res = await listTeacherApi(searchForm.value)
    console.log('后端返回:', res)
    if (res.code === 1) {
      tableData.value = res.data || []
    } else {
      console.warn('接口返回非成功状态:', res)
    }
  } catch (error) {
    console.error('获取列表失败', error)
  }
}

const handleSearch = () => {
  fetchList()
}

const handleReset = () => {
  searchForm.value = {
    teacherNo: '',
    name: '',
    title: '',
    department: ''
  }
  fetchList()
}

const handleResetClick = (teacherNo) => {
  resetUserId.value = teacherNo
  showResetConfirm.value = true
}

const handleResetConfirm = async () => {
  try {
    const res = await resetPasswordApi(resetUserId.value)
    if (res.code === 1) {
      showResetSuccess.value = true
    } else {
      alert(res.mes || '重置失败')
    }
  } catch (error) {
    alert('请求失败，请稍后重试')
  }
}

const handleSubmit = async () => {
  errorMsg.value = ''
  if (!form.value.teacherNo || !form.value.name) {
    errorMsg.value = '工号和姓名不能为空'
    return
  }

  loading.value = true
  try {
    const res = await addTeacherApi(form.value)
    if (res.code === 1) {
      showSuccess.value = true
      showAdd.value = false
      form.value = {
        teacherNo: '',
        name: '',
        gender: null,
        title: '',
        department: '',
        phone: '',
        email: '',
        entryDate: ''
      }
      fetchList()
    } else {
      errorMsg.value = res.mes || '新增失败'
    }
  } catch (error) {
    errorMsg.value = '请求失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchList()
})
</script>

<style scoped>
.teacher-page {
  padding: 20px;
}

/* 搜索区 */
.search-area {
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
}

.btn-reset {
  padding: 6px 16px;
  background: #f0f0f0;
  color: #666;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

/* 操作按钮区 */
.action-area {
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
}

.btn-refresh {
  padding: 8px 16px;
  background: white;
  color: #666;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

/* 表格区 */
.table-area {
  background: white;
  border-radius: 8px;
  overflow: hidden;
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
}

.btn-delete {
  padding: 4px 10px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-right: 5px;
}

.btn-reset {
  padding: 4px 10px;
  background: #f39c12;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
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
}

.btn-confirm {
  padding: 10px 24px;
  background: #2c3e50;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
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
</style>
