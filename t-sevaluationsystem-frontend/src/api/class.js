import request from './request'

export const addClassApi = (data) => {
  return request.post('/admin/class/insert', data)
}

export const listClassApi = (data, pageNum = 1, pageSize = 20) => {
  return request.post('/admin/class/list', { ...data, pageNum, pageSize })
}

export const updateClassApi = (data) => {
  return request.post('/admin/class/update', data)
}

export const deleteClassApi = (id) => {
  return request.post('/admin/class/delete', { id })
}
