import request from './request'

export const addTeacherApi = (data) => {
  return request.post('/admin/teacher/insert', data)
}

export const listTeacherApi = (data, pageNum = 1, pageSize = 20) => {
  return request.post('/admin/teacher/list', { ...data, pageNum, pageSize })
}

export const resetPasswordApi = (userId) => {
  return request.post('/admin/teacher/resetPassword', { userId })
}

export const deleteTeacherApi = (userId) => {
  return request.post('/admin/teacher/delete', { userId })
}
