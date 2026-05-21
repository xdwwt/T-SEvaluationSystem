import request from './request'

export const loginApi = (username, password) => {
  return request({
    url: '/main/login',
    method: 'POST',
    params: { userId: username, password }
  })
}

export const changePasswordApi = (oldPassword, newPassword) => {
  return request({
    url: '/main/password/change',
    method: 'POST',
    params: { oldPassword, newPassword }
  })
}
