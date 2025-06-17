# Axios 请求工具使用指南

## 📋 概述

本项目使用统一的axios请求工具，位于 `src/utils/request.js`，提供了完整的请求/响应拦截、错误处理、token管理等功能。

## 🔧 功能特性

### ✅ 核心功能
- **自动Token携带**: 从localStorage自动获取并添加到请求头
- **401自动处理**: Token过期时自动清除认证信息并跳转登录
- **重复请求取消**: 自动取消重复的请求
- **错误统一处理**: 统一的错误提示和状态码处理
- **请求重试机制**: 支持失败请求的自动重试
- **开发调试**: 开发环境下的请求/响应日志

### 🛡️ 安全特性
- **请求超时**: 10秒超时设置
- **重复请求控制**: 防止用户快速重复点击
- **Token自动管理**: 无需手动处理认证状态
- **错误边界**: 完善的错误捕获和处理

## 📦 配置信息

### 基础配置
```javascript
const request = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})
```

### 状态码处理
- **200-299**: 正常响应
- **401**: 自动清除认证信息并跳转登录
- **403**: 权限不足提示
- **404**: 资源不存在提示
- **422**: 表单验证错误
- **500**: 服务器错误
- **502/503/504**: 服务不可用

## 🚀 使用方式

### 1. 基础使用

#### 在API文件中使用
```javascript
import request from '@/utils/request'

// GET请求
export const getUserInfo = (id) => {
  return request.get(`/user/${id}`)
}

// POST请求
export const createUser = (userData) => {
  return request.post('/user', userData)
}

// PUT请求
export const updateUser = (id, userData) => {
  return request.put(`/user/${id}`, userData)
}

// DELETE请求
export const deleteUser = (id) => {
  return request.delete(`/user/${id}`)
}
```

#### 在组件中使用
```javascript
import { authAPI } from '@/api'

// 登录示例
const handleLogin = async () => {
  try {
    const response = await authAPI.login({
      username: 'test',
      password: '123456'
    })
    
    if (response.data.code === 200) {
      // 登录成功
      console.log('登录成功', response.data.data)
    }
  } catch (error) {
    // 错误会被拦截器自动处理，这里可以做额外处理
    console.error('登录失败', error)
  }
}
```

### 2. 带重试的请求

```javascript
import request from '@/utils/request'

// 使用重试机制
const fetchImportantData = async () => {
  try {
    const response = await request.requestWithRetry({
      method: 'get',
      url: '/important-data'
    }, 3, 1000) // 重试3次，间隔1秒
    
    return response.data
  } catch (error) {
    console.error('获取数据失败', error)
  }
}
```

### 3. 取消请求

```javascript
import request from '@/utils/request'

// 在组件卸载时取消所有请求
onUnmounted(() => {
  request.cancelAllRequests()
})
```

### 4. 自定义请求配置

```javascript
import request from '@/utils/request'

// 上传文件（不取消重复请求）
const uploadFile = (file) => {
  const formData = new FormData()
  formData.append('file', file)
  
  return request.post('/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    timeout: 60000, // 上传文件延长超时时间
    // 禁用重复请求取消
    skipDuplicateCheck: true
  })
}
```

## 📋 API模块示例

### 认证API (`src/api/auth.js`)
```javascript
import request from '@/utils/request'

export const authAPI = {
  login: (loginData) => request.post('/auth/login', loginData),
  register: (registerData) => request.post('/auth/register', registerData),
  getCurrentUser: () => request.get('/auth/me'),
  refreshToken: () => request.post('/auth/refresh'),
  logout: () => request.post('/auth/logout')
}
```

### 帖子API (`src/api/post.js`)
```javascript
import request from '@/utils/request'

export const postAPI = {
  createPost: (postData) => request.post('/post/publish', postData),
  getPostList: (params) => request.get('/post/list', { params }),
  getPostById: (id) => request.get(`/post/${id}`),
  updatePost: (id, postData) => request.put(`/post/${id}`, postData),
  deletePost: (id) => request.delete(`/post/${id}`)
}
```

## 🔄 错误处理

### 自动处理的错误
- 网络连接错误
- 请求超时
- 服务器错误（5xx）
- 认证失效（401）
- 权限不足（403）

### 业务错误处理
```javascript
const handleBusinessLogic = async () => {
  try {
    const response = await authAPI.login(loginData)
    
    // 检查业务状态码
    if (response.data.code === 200) {
      // 成功处理
    } else {
      // 业务失败（错误信息会被拦截器自动显示）
      console.log('业务错误:', response.data.message)
    }
  } catch (error) {
    // 网络错误或其他异常（已被拦截器处理）
    console.error('请求异常:', error)
  }
}
```

## 📊 请求日志

### 开发环境日志
在开发环境下，控制台会显示详细的请求/响应信息：

```
🚀 请求发送: {
  url: "/auth/login",
  method: "POST", 
  data: { username: "test", password: "***" },
  headers: { Authorization: "Bearer ..." }
}

✅ 响应接收: {
  url: "/auth/login",
  method: "POST",
  status: 200,
  data: { code: 200, message: "success", data: {...} }
}
```

## 🛠️ 扩展配置

### 添加新的状态码处理
```javascript
// 在 request.js 的响应拦截器中添加
case 429:
  ElMessage.error('请求过于频繁，请稍后重试')
  break
```

### 添加新的请求头
```javascript
// 在请求拦截器中添加
config.headers['Custom-Header'] = 'custom-value'
```

## 📝 最佳实践

### 1. API模块组织
- 按功能模块划分API文件（auth.js, post.js, friend.js等）
- 使用统一的命名规范
- 在 `api/index.js` 中统一导出

### 2. 错误处理
- 依赖拦截器的自动错误处理
- 只在需要特殊处理时编写额外的错误逻辑
- 使用try-catch包装异步请求

### 3. 性能优化
- 利用自动取消重复请求功能
- 合理使用重试机制
- 在组件卸载时取消未完成的请求

### 4. 调试技巧
- 在开发环境查看控制台日志
- 使用浏览器开发者工具的Network面板
- 设置断点调试请求流程

## 🔧 故障排除

### 常见问题
1. **Token未携带**: 检查localStorage中是否有token
2. **401错误循环**: 确保登录页面路径正确
3. **请求被取消**: 检查是否有重复请求
4. **超时错误**: 检查网络连接和服务器状态

---

**版本**: v1.0.0  
**最后更新**: 2024年1月 