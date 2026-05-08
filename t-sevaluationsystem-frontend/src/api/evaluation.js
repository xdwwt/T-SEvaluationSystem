import request from './request'

// ========== 学生端 ==========

// 查询待评价教师列表
export const getTeachersToEvaluateApi = () => {
  return request({
    url: '/student/teacher/list',
    method: 'GET'
  })
}

// 提交教师评价
export const submitEvaluationApi = (data) => {
  return request({
    url: '/student/evaluation/submit',
    method: 'POST',
    data
  })
}

// 查询学生已评价记录
export const getStudentEvaluationListApi = () => {
  return request({
    url: '/student/evaluation/list',
    method: 'GET'
  })
}

// ========== 教师端（预留） ==========

// 查询教师收到的评价列表
export const getTeacherEvaluationListApi = () => {
  return request({
    url: '/teacher/evaluation/list',
    method: 'GET'
  })
}
