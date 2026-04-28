import request from './request'

export const addCourseApi = (data) => {
  return request.post('/admin/course/insert', data)
}

export const listCourseApi = (data, pageNum = 1, pageSize = 20) => {
  return request.post('/admin/course/list', { ...data, pageNum, pageSize })
}

export const updateCourseApi = (data) => {
  return request.post('/admin/course/update', data)
}

export const deleteCourseApi = (id) => {
  return request.post('/admin/course/delete', { id })
}
