import request from './request'

export const loginApi = (username, password) => {
  return request({
    url: '/main/login',
    method: 'POST',
    params: { userId: username, password }
  })
}
