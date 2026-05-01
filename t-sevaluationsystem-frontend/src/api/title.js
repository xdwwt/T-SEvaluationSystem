import request from './request'

export const addTitleApi = (data) => {
  return request.post('/admin/title/add', data)
}

export const listTitleApi = (data, pageNum = 1, pageSize = 20) => {
  return request.post('/admin/title/list', { ...data, pageNum, pageSize })
}

export const updateTitleApi = (data) => {
  return request.post('/admin/title/update', data)
}

export const deleteTitleApi = (id) => {
  return request.post(`/admin/title/delete/${id}`)
}

export const allTitleApi = () => {
  return request.post('/admin/title/listAll')
}
