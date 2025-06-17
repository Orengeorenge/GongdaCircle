import request from '@/utils/request'

// 帖子相关API
export const postAPI = {
  // 发布帖子
  createPost: (postData) => {
    return request.post('/post/publish', postData)
  },
  
  // 获取帖子列表
  getPostList: (params) => {
    return request.get('/post/list', { params })
  },
  
  // 获取帖子详情
  getPostById: (id) => {
    return request.get(`/post/${id}`)
  },
  
  // 获取我的帖子
  getMyPosts: (params) => {
    return request.get('/post/my', { params })
  },
  
  // 更新帖子
  updatePost: (id, postData) => {
    return request.put(`/post/${id}`, postData)
  },
  
  // 删除帖子
  deletePost: (id) => {
    return request.delete(`/post/${id}`)
  },
  
  // 点赞帖子
  likePost: (id) => {
    return request.post(`/post/${id}/like`)
  },
  
  // 收藏帖子
  collectPost: (id) => {
    return request.post(`/post/${id}/collect`)
  }
}

export default postAPI 