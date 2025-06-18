import request from '@/utils/request'

// 帖子相关API
export const postAPI = {
  // 发布帖子
  createPost: (postData) => {
    return request.post('/post/publish', postData)
      .then(response => response.data)
  },
  
  // 上传图片
  uploadImage: (file, onProgress) => {
    // 创建FormData对象
    const formData = new FormData()
    formData.append('file', file)
    
    return request.post('/upload/image', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: progressEvent => {
        if (onProgress) {
          const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          onProgress(percentCompleted)
        }
      }
    }).then(response => response.data)
  },
  
  // 上传视频
  uploadVideo: (file, onProgress) => {
    // 创建FormData对象
    const formData = new FormData()
    formData.append('file', file)
    
    return request.post('/upload/video', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      onUploadProgress: progressEvent => {
        if (onProgress) {
          const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
          onProgress(percentCompleted)
        }
      }
    }).then(response => response.data)
  },
  
  // 获取帖子列表
  getPostList: (params) => {
    return request.get('/post/list', { params })
      .then(response => response.data)
  },
  
  // 获取帖子详情
  getPostById: (id) => {
    return request.get(`/post/${id}`)
      .then(response => response.data)
  },
  
  // 获取我的帖子
  getMyPosts: (params) => {
    return request.get('/post/my', { params })
      .then(response => response.data)
  },
  
  // 更新帖子
  updatePost: (id, postData) => {
    return request.put(`/post/${id}`, postData)
      .then(response => response.data)
  },
  
  // 删除帖子
  deletePost: (id) => {
    return request.delete(`/post/${id}`)
      .then(response => response.data)
  },
  
  // 点赞帖子
  likePost: (id) => {
    return request.post(`/post/${id}/like`)
      .then(response => response.data)
  },
  
  // 收藏帖子
  collectPost: (id) => {
    return request.post(`/post/${id}/collect`)
      .then(response => response.data)
  }
}

export default postAPI 