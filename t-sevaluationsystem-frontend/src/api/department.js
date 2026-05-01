import request from './request'

export const addDepartmentApi = (data) => {
  return request.post('/admin/department/add', data)
}

export const listDepartmentApi = (data, pageNum = 1, pageSize = 20) => {
  return request.post('/admin/department/list', { ...data, pageNum, pageSize })
}

export const updateDepartmentApi = (data) => {
  return request.post('/admin/department/update', data)
}

export const deleteDepartmentApi = (id) => {
  return request.post(`/admin/department/delete/${id}`)
}

export const allDepartmentApi = () => {
  return request.post('/admin/department/listAll')
}
