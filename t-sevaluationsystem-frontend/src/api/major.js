import request from './request'

export const addMajorApi = (data) => {
  return request.post('/admin/major/insert', data)
}

export const listMajorApi = (data, pageNum = 1, pageSize = 20) => {
  return request.post('/admin/major/list', { ...data, pageNum, pageSize })
}

export const updateMajorApi = (data) => {
  return request.post('/admin/major/update', data)
}

export const deleteMajorApi = (id) => {
  return request.post('/admin/major/delete', { id })
}

export const allMajorApi = (departmentId) => {
  return request.post('/admin/major/all', departmentId ? { departmentId } : {})
}
