import request from './request'

export const getDashboardStatsApi = () => {
  return request.post('/admin/dashboard/stats')
}
