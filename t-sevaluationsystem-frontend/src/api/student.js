import request from './request'

export const addStudentApi = (data) => {
  return request.post('/admin/student/insert', data)
}

export const listStudentApi = (data, pageNum = 1, pageSize = 20) => {
  return request.post('/admin/student/list', { ...data, pageNum, pageSize })
}

export const updateStudentApi = (data) => {
  return request.post('/admin/student/update', data)
}

export const deleteStudentApi = (userId) => {
  return request.post('/admin/student/delete', { userId })
}

export const resetPasswordApi = (userId) => {
  return request.post('/admin/student/resetPassword', { userId })
}
