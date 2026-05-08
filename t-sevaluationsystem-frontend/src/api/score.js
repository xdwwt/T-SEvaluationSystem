import request from './request'

// ========== 学生端 ==========

// 查询学生成绩列表
export const getStudentScoreListApi = () => {
  return request({
    url: '/student/score/list',
    method: 'GET'
  })
}

// ========== 教师端（预留） ==========

// 查询教师授课班级列表
export const getTeachingClassesApi = () => {
  return request({
    url: '/teacher/class/list',
    method: 'GET'
  })
}

// 查询某班级课程的学生列表（用于录入成绩）
export const getTeacherClassStudentsApi = (params) => {
  return request({
    url: '/teacher/class/students',
    method: 'GET',
    params
  })
}

// 提交/更新成绩
export const submitScoreApi = (data) => {
  return request({
    url: '/teacher/score/submit',
    method: 'POST',
    data
  })
}

// 查询教师已录入成绩列表
export const getTeacherScoreListApi = () => {
  return request({
    url: '/teacher/score/list',
    method: 'GET'
  })
}
