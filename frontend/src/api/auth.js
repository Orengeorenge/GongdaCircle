import request from '@/utils/request'

// 认证相关API
export const authAPI = {
  // 用户登录
  login: (loginData) => {
    return request.post('/user/login', loginData)
  },
  
  // 用户注册
  register: (registerData) => {
    return request.post('/user/register', registerData)
  },
  
  // 获取当前用户信息
  getCurrentUser: () => {
    return request.get('/auth/me')
  },
  
  // 更新用户信息
  updateUserInfo: (userId, userData) => {
    return request.put(`/user/${userId}`, userData)
  },
  
  // 修改密码
  changePassword: (userId, passwordData) => {
    return request.put(`/user/${userId}/password`, passwordData)
  },
  
  // 更新头像
  updateAvatar: (userId, avatarData) => {
    return request.put(`/user/${userId}/avatar`, avatarData)
  },
  
  // 刷新token
  refreshToken: () => {
    return request.post('/auth/refresh')
  },
  
  // 用户登出
  logout: () => {
    return request.post('/auth/logout')
  },
  
  // 检查用户名是否存在
  checkUsername: (username) => {
    return request.get(`/user/check/username?username=${encodeURIComponent(username)}`)
  },
  
  // 检查邮箱是否存在
  checkEmail: (email) => {
    return request.get(`/user/check/email?email=${encodeURIComponent(email)}`)
  }
}

export default authAPI 