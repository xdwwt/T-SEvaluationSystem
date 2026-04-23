import request from './request'

export const addTeacherApi = (data) => {
  return request.post('/admin/teacher/insert', data)
}

export const listTeacherApi = (data) => {
  return request.post('/admin/teacher/list', data)
}

export const resetPasswordApi = (userId) => {
  return request.post('/admin/teacher/resetPassword', { userId })
}
