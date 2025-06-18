import request from '@/utils/request'

// 用户相关API
export const userAPI = {
  // 获取当前用户信息
  getCurrentUser: () => {
    return request.get('/auth/me')
  },
  
  // 更新用户信息 - 使用用户名，而不是ID
  updateUserInfo: (username, userData) => {
    if (!username) {
      console.error('更新用户信息: 缺少用户名参数')
      return Promise.reject(new Error('缺少用户名参数'))
    }
    
    // 确保username是字符串
    const usernameStr = String(username).trim()
    console.log('更新用户API: 用户名:', usernameStr)
    
    // 按照UserUpdateDTO的要求构造请求体 - 使用新的不需要密码的接口
    const requestData = {
      username: userData.username || '',
      nickname: userData.nickname || '',
      email: userData.email || '',
      // 处理手机号: 如果为空字符串或undefined，则不传递该字段，避免触发唯一约束
      ...(userData.phone ? { phone: userData.phone } : {}),
      gender: userData.gender !== undefined ? userData.gender : 2,
      birthday: userData.birthday || '',
      school: userData.school || '',
      major: userData.major || '',
      grade: userData.grade || '',
      biography: userData.biography || ''
    }
    
    console.log(`准备更新用户信息: 用户名=${usernameStr}`, requestData)
    // 使用新的profile接口，不需要密码字段
    return request.put(`/user/${usernameStr}/profile`, requestData)
  },
  
  // 更新头像
  updateAvatar: (username, avatarUrl) => {
    if (!username) {
      console.error('更新头像: 缺少用户名参数')
      return Promise.reject(new Error('缺少用户名参数'))
    }
    
    return request.put(`/user/${username}/avatar`, { avatar: avatarUrl })
  },
  
  // 修改密码
  changePassword: (username, data) => {
    if (!username) {
      console.error('修改密码: 缺少用户名参数')
      return Promise.reject(new Error('缺少用户名参数'))
    }
    
    console.log('修改密码API被调用:', { username, data })
    
    return request.put(`/user/${username}/password`, data)
      .then(response => {
        // 确保我们返回的是响应数据而不是整个axios响应对象
        console.log('修改密码响应:', response)
        return response.data
      })
      .catch(error => {
        console.error('修改密码API错误:', error)
        throw error
      })
  },
  
  // 获取用户信息
  getUserInfo: (username) => {
    if (!username) {
      console.error('获取用户信息: 缺少用户名参数')
      return Promise.reject(new Error('缺少用户名参数'))
    }
    
    // 确保username是字符串
    const usernameStr = String(username).trim()
    
    console.log(`获取用户信息: 用户名=${usernameStr}`)
    return request.get(`/user/${usernameStr}`)
  }
}

export default userAPI 