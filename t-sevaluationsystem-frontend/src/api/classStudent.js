import request from './request'

export const listClassStudentsApi = (classId) => {
  return request.post('/admin/class/student/list', { classId })
}

export const listUnassignedStudentsApi = () => {
  return request.post('/admin/class/student/unassigned')
}

export const addStudentToClassApi = (classId, studentId) => {
  return request.post('/admin/class/student/add', { classId, studentId })
}

export const removeStudentFromClassApi = (id) => {
  return request.post('/admin/class/student/remove', { id })
}
