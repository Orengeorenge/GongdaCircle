// 认证相关工具函数

// 获取token
export function getToken() {
  return localStorage.getItem('token')
}

// 设置token
export function setToken(token) {
  localStorage.setItem('token', token)
}

// 移除token
export function removeToken() {
  localStorage.removeItem('token')
}

// 获取用户信息
export function getUserInfo() {
  const userStr = localStorage.getItem('user')
  return userStr ? JSON.parse(userStr) : null
}

// 设置用户信息
export function setUserInfo(user) {
  localStorage.setItem('user', JSON.stringify(user))
}

// 移除用户信息
export function removeUserInfo() {
  localStorage.removeItem('user')
}

// 检查是否已登录
export function isAuthenticated() {
  return !!getToken()
}

// 清除所有认证信息
export function clearAuth() {
  removeToken()
  removeUserInfo()
}

// 登出
export function logout() {
  clearAuth()
  // 重定向到登录页
  window.location.href = '/login'
} 