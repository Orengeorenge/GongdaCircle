import request from '@/utils/request'

// 好友相关API
export const friendAPI = {
  // 发送好友请求
  sendFriendRequest: (friendId, reason = '', remark = '') => {
    return request.post('/friend/apply', { 
      friendId,
      reason,
      remark
    })
  },
  
  // 获取好友申请列表
  getFriendRequests: (params) => {
    return request.get('/friend/requests', { params })
  },
  
  // 同意好友申请
  acceptFriendRequest: (friendshipId) => {
    return request.post(`/friend/${friendshipId}/approve`)
  },
  
  // 拒绝好友申请
  rejectFriendRequest: (friendshipId) => {
    return request.post(`/friend/${friendshipId}/reject`)
  },
  
  // 获取好友列表
  getFriendList: (params) => {
    return request.get('/friend/list', { params })
  },
  
  // 删除好友
  deleteFriend: (friendshipId) => {
    return request.delete(`/friend/${friendshipId}`)
  },
  
  // 搜索用户
  searchUsers: (keyword) => {
    return request.get('/user/search', { params: { keyword } })
  }
}

export default friendAPI 