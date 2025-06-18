import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, clearAuth } from './auth'

// 创建axios实例
const request = axios.create({
  // 使用相对路径，让请求通过Vite的代理转发
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// 存储正在进行的请求
const pendingRequests = new Map()

/**
 * 生成请求的唯一key
 */
const generateRequestKey = (config) => {
  const { method, url, params, data } = config
  return [method, url, JSON.stringify(params), JSON.stringify(data)].join('&')
}

/**
 * 添加请求到pending列表
 */
const addPendingRequest = (config) => {
  const requestKey = generateRequestKey(config)
  config.cancelToken = config.cancelToken || new axios.CancelToken(cancel => {
    if (!pendingRequests.has(requestKey)) {
      pendingRequests.set(requestKey, cancel)
    }
  })
}

/**
 * 移除pending中的请求
 */
const removePendingRequest = (config) => {
  const requestKey = generateRequestKey(config)
  if (pendingRequests.has(requestKey)) {
    const cancelToken = pendingRequests.get(requestKey)
    cancelToken(requestKey)
    pendingRequests.delete(requestKey)
  }
}

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 取消重复请求
    removePendingRequest(config)
    addPendingRequest(config)
    
    // 自动携带token
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 打印请求信息（开发环境）
    if (process.env.NODE_ENV === 'development') {
      console.log('🚀 请求发送:', {
        url: config.url,
        method: config.method?.toUpperCase(),
        params: config.params,
        data: config.data,
        headers: config.headers
      })
    }
    
    return config
  },
  error => {
    console.error('❌ 请求错误:', error)
    ElMessage.error('请求发送失败')
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 移除已完成的请求
    removePendingRequest(response.config)
    
    // 打印响应信息（开发环境）
    if (process.env.NODE_ENV === 'development') {
      console.log('✅ 响应接收:', {
        url: response.config.url,
        method: response.config.method?.toUpperCase(),
        status: response.status,
        data: response.data
      })
    }
    
    return response
  },
  error => {
    // 移除已完成的请求
    if (error.config) {
      removePendingRequest(error.config)
    }
    
    // 取消请求的情况
    if (axios.isCancel(error)) {
      console.log('请求被取消:', error.message)
      return Promise.reject(error)
    }
    
    console.error('❌ 响应错误:', error)
    
    if (error.response) {
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          // Token过期或无效，清除认证信息并跳转登录
          ElMessage.error('登录已过期，请重新登录')
          clearAuth()
          // 避免多次跳转
          if (!window.location.pathname.includes('/login')) {
            setTimeout(() => {
              window.location.href = '/login'
            }, 1000)
          }
          break
          
        case 403:
          ElMessage.error('没有权限访问该资源')
          break
          
        case 404:
          ElMessage.error('请求的资源不存在')
          break
          
        case 422:
          // 表单验证错误
          const message = data?.message || '数据验证失败'
          ElMessage.error(message)
          break
          
        case 500:
          ElMessage.error('服务器内部错误，请稍后重试')
          break
          
        case 502:
        case 503:
        case 504:
          ElMessage.error('服务暂时不可用，请稍后重试')
          break
          
        default:
          // 其他错误，显示后端返回的错误信息
          const errorMessage = data?.message || `请求失败 (${status})`
          ElMessage.error(errorMessage)
      }
    } else if (error.code === 'ECONNABORTED') {
      // 请求超时
      ElMessage.error('请求超时，请检查网络连接')
    } else if (error.message === 'Network Error') {
      // 网络错误
      ElMessage.error('网络连接异常，请检查网络设置')
    } else {
      // 其他错误
      ElMessage.error('请求失败，请稍后重试')
    }
    
    return Promise.reject(error)
  }
)

/**
 * 创建带重试机制的请求
 * @param {Object} config - axios配置
 * @param {Number} retryTimes - 重试次数
 * @param {Number} retryDelay - 重试延迟
 */
const requestWithRetry = (config, retryTimes = 3, retryDelay = 1000) => {
  return new Promise((resolve, reject) => {
    const makeRequest = (currentTry) => {
      request(config)
        .then(resolve)
        .catch(error => {
          if (currentTry < retryTimes && !axios.isCancel(error)) {
            console.log(`请求失败，${retryDelay}ms后进行第${currentTry + 1}次重试`)
            setTimeout(() => {
              makeRequest(currentTry + 1)
            }, retryDelay)
          } else {
            reject(error)
          }
        })
    }
    makeRequest(0)
  })
}

// 扩展request实例，添加便捷方法
request.requestWithRetry = requestWithRetry

// 导出取消请求的方法
request.cancelAllRequests = () => {
  pendingRequests.forEach((cancel, key) => {
    cancel(key)
  })
  pendingRequests.clear()
}

export default request 