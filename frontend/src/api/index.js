// 统一导出所有API模块
export { authAPI } from './auth'
export { postAPI } from './post'
export { friendAPI } from './friend'
export { userAPI } from './user'

// 默认导出request实例，方便直接使用
export { default as request } from '@/utils/request' 