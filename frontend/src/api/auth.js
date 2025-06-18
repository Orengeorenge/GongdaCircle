import request from '@/utils/request'

// 认证相关API
export const authAPI = {
  // 用户登录
  login: (loginData) => {
    return request.post('/user/login', loginData)
  },
  
  // 用户注册 - 处理空字段以避免唯一约束问题
  register: (registerData) => {
    // 复制数据，确保不修改原始对象
    const processedData = { ...registerData }
    
    // 如果手机号为空或未定义，设置为null而非空字符串，避免唯一约束冲突
    if (!processedData.phone || processedData.phone.trim() === '') {
      // 删除phone字段，而不是设置为空字符串
      delete processedData.phone
    }
    
    // 记录处理后的数据
    console.log('处理后的注册数据:', processedData)
    
    return request.post('/user/register', processedData)
  },
  
  // 获取当前用户信息
  getCurrentUser: () => {
    return request.get('/auth/me')
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

/**
 * 修改密码
 * @param {string} username - 用户名
 * @param {Object} data - 密码数据
 * @param {string} data.oldPassword - 旧密码
 * @param {string} data.newPassword - 新密码
 * @returns {Promise}
 */
export function changePassword(username, data) {
  return request({
    url: `/user/${username}/password`,
    method: 'put',
    data
  })
}

export default authAPI 