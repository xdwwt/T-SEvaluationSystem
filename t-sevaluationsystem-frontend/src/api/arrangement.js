import request from './request'

export const addArrangementApi = (data) => {
  return request.post('/admin/arrangement/insert', data)
}

export const listArrangementApi = (data, pageNum = 1, pageSize = 20) => {
  return request.post('/admin/arrangement/list', { ...data, pageNum, pageSize })
}

export const updateArrangementApi = (data) => {
  return request.post('/admin/arrangement/update', data)
}

export const deleteArrangementApi = (id) => {
  return request.post('/admin/arrangement/delete', { id })
}
