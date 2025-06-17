<template>
  <div class="friends-container">
    <app-header />
    
    <el-main class="main-content">
      <div class="container">
        <div class="friends-wrapper">
          <!-- 左侧主要内容 -->
          <div class="friends-main">
            <!-- 搜索栏 -->
            <div class="card search-section">
              <div class="search-header">
                <h2>添加好友</h2>
              </div>
              <div class="search-form">
                <el-input
                  v-model="searchKeyword"
                  placeholder="搜索用户名、邮箱或手机号"
                  size="large"
                  clearable
                  @keyup.enter="handleSearch"
                >
                  <template #prefix>
                    <el-icon><Search /></el-icon>
                  </template>
                  <template #append>
                    <el-button type="primary" @click="handleSearch">搜索</el-button>
                  </template>
                </el-input>
              </div>
            </div>

            <!-- 直接添加好友 -->
            <div class="card add-friend-section">
              <div class="add-friend-header">
                <h3>直接添加好友</h3>
                <p>如果您知道对方的用户ID，可以直接发送好友请求</p>
              </div>
              <el-form 
                ref="addFriendFormRef" 
                :model="addFriendForm" 
                :rules="addFriendRules"
                @submit.prevent="handleAddFriend"
              >
                <el-form-item prop="targetUserId">
                  <el-input
                    v-model="addFriendForm.targetUserId"
                    placeholder="请输入用户ID"
                    size="large"
                    clearable
                    @keyup.enter="handleAddFriend"
                  >
                    <template #prefix>
                      <el-icon><User /></el-icon>
                    </template>
                    <template #append>
                      <el-button 
                        type="primary" 
                        :loading="sendingRequest"
                        @click="handleAddFriend"
                      >
                        发送请求
                      </el-button>
                    </template>
                  </el-input>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 标签切换 -->
            <div class="card">
              <el-tabs v-model="activeTab" @tab-change="handleTabChange">
                <el-tab-pane label="搜索结果" name="search">
                  <div v-if="searchResults.length" class="user-list">
                    <div v-for="user in searchResults" :key="user.id" class="user-item">
                      <div class="user-info">
                        <el-avatar :size="50" :src="user.avatar">
                          {{ user.nickname?.charAt(0) }}
                        </el-avatar>
                        <div class="user-details">
                          <div class="user-main">
                            <span class="nickname">{{ user.nickname }}</span>
                            <el-tag v-if="user.isOnline" type="success" size="small">在线</el-tag>
                          </div>
                          <div class="user-desc">{{ user.school }} · {{ user.major }}</div>
                          <div class="user-bio">{{ user.biography }}</div>
                        </div>
                      </div>
                      <div class="user-actions">
                        <el-button
                          v-if="user.friendStatus === 0"
                          type="primary"
                          size="small"
                          @click="sendFriendRequest(user)"
                        >
                          添加好友
                        </el-button>
                        <el-button
                          v-else-if="user.friendStatus === 1"
                          type="info"
                          size="small"
                          disabled
                        >
                          已发送请求
                        </el-button>
                        <el-button
                          v-else-if="user.friendStatus === 2"
                          type="success"
                          size="small"
                          disabled
                        >
                          已是好友
                        </el-button>
                      </div>
                    </div>
                  </div>
                  <el-empty v-else description="暂无搜索结果" />
                </el-tab-pane>
                
                <el-tab-pane label="好友申请" name="requests">
                  <div v-if="friendRequests.length" class="request-list">
                    <div v-for="request in friendRequests" :key="request.id" class="request-item">
                      <div class="request-info">
                        <el-avatar :size="50" :src="request.user.avatar">
                          {{ request.user.nickname?.charAt(0) }}
                        </el-avatar>
                        <div class="request-details">
                          <div class="user-main">
                            <span class="nickname">{{ request.user.nickname }}</span>
                            <span class="request-time">{{ formatTime(request.createTime) }}</span>
                          </div>
                          <div class="request-reason">申请理由：{{ request.reason || '希望成为好友' }}</div>
                        </div>
                      </div>
                      <div class="request-actions">
                        <el-button
                          type="primary"
                          size="small"
                          @click="acceptRequest(request)"
                        >
                          同意
                        </el-button>
                        <el-button
                          size="small"
                          @click="rejectRequest(request)"
                        >
                          拒绝
                        </el-button>
                      </div>
                    </div>
                  </div>
                  <el-empty v-else description="暂无好友申请" />
                </el-tab-pane>
                
                <el-tab-pane label="我的好友" name="friends">
                  <div v-if="myFriends.length" class="friends-list">
                    <div v-for="friend in myFriends" :key="friend.id" class="friend-item">
                      <div class="friend-info">
                        <el-avatar :size="50" :src="friend.avatar">
                          {{ friend.nickname?.charAt(0) }}
                        </el-avatar>
                        <div class="friend-details">
                          <div class="friend-main">
                            <span class="nickname">{{ friend.nickname }}</span>
                            <el-tag v-if="friend.isOnline" type="success" size="small">在线</el-tag>
                          </div>
                          <div class="friend-desc">{{ friend.school }} · {{ friend.major }}</div>
                          <div class="last-seen">最后在线：{{ formatTime(friend.lastLoginTime) }}</div>
                        </div>
                      </div>
                      <div class="friend-actions">
                        <el-dropdown @command="handleFriendCommand">
                          <el-button size="small">
                            更多操作
                            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                          </el-button>
                          <template #dropdown>
                            <el-dropdown-menu>
                              <el-dropdown-item :command="`chat-${friend.id}`">
                                <el-icon><ChatDotRound /></el-icon>
                                发送消息
                              </el-dropdown-item>
                              <el-dropdown-item :command="`profile-${friend.id}`">
                                <el-icon><User /></el-icon>
                                查看资料
                              </el-dropdown-item>
                              <el-dropdown-item :command="`delete-${friend.id}`" divided>
                                <el-icon><Delete /></el-icon>
                                删除好友
                              </el-dropdown-item>
                            </el-dropdown-menu>
                          </template>
                        </el-dropdown>
                      </div>
                    </div>
                  </div>
                  <el-empty v-else description="暂无好友" />
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
          
          <!-- 右侧边栏 -->
          <div class="friends-sidebar">
            <!-- 统计信息 -->
            <div class="card stats-card">
              <h3>好友统计</h3>
              <div class="stats-list">
                <div class="stat-item">
                  <span class="stat-label">好友总数</span>
                  <span class="stat-value">{{ myFriends.length }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">待处理申请</span>
                  <span class="stat-value">{{ friendRequests.length }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">在线好友</span>
                  <span class="stat-value">{{ onlineFriendsCount }}</span>
                </div>
              </div>
            </div>
            
            <!-- 推荐好友 -->
            <div class="card recommend-card">
              <h3>可能认识的人</h3>
              <div class="recommend-list">
                <div v-for="user in recommendFriends" :key="user.id" class="recommend-item">
                  <el-avatar :size="40" :src="user.avatar">
                    {{ user.nickname?.charAt(0) }}
                  </el-avatar>
                  <div class="recommend-info">
                    <div class="nickname">{{ user.nickname }}</div>
                    <div class="reason">{{ user.reason }}</div>
                  </div>
                  <el-button 
                    size="small" 
                    type="primary" 
                    @click="sendFriendRequest(user)"
                  >
                    添加
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-main>
    
    <!-- 发送好友申请对话框 -->
    <el-dialog
      v-model="showRequestDialog"
      title="发送好友申请"
      width="400px"
    >
      <el-form :model="requestForm" label-width="80px">
        <el-form-item label="申请理由">
          <el-input
            v-model="requestForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入申请理由（可选）"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="备注名称">
          <el-input
            v-model="requestForm.remark"
            placeholder="请输入备注名称（可选）"
            maxlength="50"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showRequestDialog = false">取消</el-button>
          <el-button type="primary" @click="confirmSendRequest">发送申请</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AppHeader from '@/components/Layout/AppHeader.vue'
import { friendAPI } from '@/api/friend'

const activeTab = ref('search')
const searchKeyword = ref('')
const showRequestDialog = ref(false)
const currentRequestUser = ref(null)
const sendingRequest = ref(false)
const addFriendFormRef = ref()

const searchResults = ref([])
const friendRequests = ref([])
const myFriends = ref([])
const recommendFriends = ref([])

// 直接添加好友表单
const addFriendForm = reactive({
  targetUserId: ''
})

// 表单验证规则
const addFriendRules = {
  targetUserId: [
    { required: true, message: '请输入用户ID', trigger: 'blur' },
    { pattern: /^\d+$/, message: '用户ID必须是数字', trigger: 'blur' }
  ]
}

const requestForm = reactive({
  reason: '',
  remark: ''
})

// 直接发送好友请求
const handleAddFriend = async () => {
  if (!addFriendFormRef.value) return
  
  await addFriendFormRef.value.validate(async (valid) => {
    if (valid) {
      sendingRequest.value = true
      
      try {
        // 调用发送好友请求API，注意参数名称变更为friendId
        const response = await friendAPI.sendFriendRequest(parseInt(addFriendForm.targetUserId))
        
        // 检查响应状态
        if (response.data.code === 200) {
          // 发送成功
          ElMessage.success('请求已发送')
          addFriendForm.targetUserId = '' // 清空输入框
        } else {
          // 发送失败
          ElMessage.error(response.data.message || '发送失败')
        }
      } catch (error) {
        // 处理网络错误或其他异常
        console.error('发送好友请求错误:', error)
        const errorMessage = error.response?.data?.message || '发送失败，请检查网络连接'
        ElMessage.error(errorMessage)
      } finally {
        sendingRequest.value = false
      }
    }
  })
}

// 定义加载数据的方法
const loadMyFriends = async () => {
  try {
    const response = await friendAPI.getFriendList({
      page: 1,
      size: 20,
      keyword: ''
    })
    
    if (response.data.code === 200) {
      myFriends.value = response.data.data.records || []
    } else {
      ElMessage.warning('获取好友列表失败')
    }
  } catch (error) {
    console.error('获取好友列表错误:', error)
    ElMessage.error('获取好友列表失败，请检查网络连接')
  }
}

const loadFriendRequests = async () => {
  try {
    const response = await friendAPI.getFriendRequests({
      page: 1,
      size: 10
    })
    
    if (response.data.code === 200) {
      friendRequests.value = response.data.data.records || []
    } else {
      ElMessage.warning('获取好友申请失败')
    }
  } catch (error) {
    console.error('获取好友申请错误:', error)
    ElMessage.error('获取好友申请失败，请检查网络连接')
  }
}

const loadSearchResults = async () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  
  try {
    const response = await friendAPI.searchUsers(searchKeyword.value)
    
    if (response.data.code === 200) {
      searchResults.value = response.data.data || []
      activeTab.value = 'search'
      ElMessage.success(`找到 ${searchResults.value.length} 个用户`)
    } else {
      ElMessage.warning(response.data.message || '搜索用户失败')
    }
  } catch (error) {
    console.error('搜索用户错误:', error)
    ElMessage.error('搜索用户失败，请检查网络连接')
  }
}

// 替换handleSearch方法
const handleSearch = () => {
  loadSearchResults()
}

// 替换handleTabChange方法
const handleTabChange = (tab) => {
  if (tab === 'requests') {
    // 加载好友申请
    loadFriendRequests()
  } else if (tab === 'friends') {
    // 加载好友列表
    loadMyFriends()
  }
}

const onlineFriendsCount = computed(() => {
  return myFriends.value.filter(friend => friend.isOnline).length
})

const sendFriendRequest = (user) => {
  currentRequestUser.value = user
  requestForm.reason = ''
  requestForm.remark = user.nickname
  showRequestDialog.value = true
}

const confirmSendRequest = async () => {
  try {
    // 使用API发送好友请求，传递完整参数
    const response = await friendAPI.sendFriendRequest(
      currentRequestUser.value.id,
      requestForm.reason,
      requestForm.remark
    )
    
    // 检查响应状态
    if (response.data.code === 200) {
      showRequestDialog.value = false
      ElMessage.success('好友申请已发送')
      
      // 更新用户状态
      if (currentRequestUser.value) {
        currentRequestUser.value.friendStatus = 1
      }
    } else {
      ElMessage.error(response.data.message || '发送失败')
    }
  } catch (error) {
    console.error('发送好友请求错误:', error)
    const errorMessage = error.response?.data?.message || '发送失败，请检查网络连接'
    ElMessage.error(errorMessage)
  }
}

const acceptRequest = (request) => {
  ElMessageBox.confirm(
    `确定要同意 ${request.user.nickname} 的好友申请吗？`,
    '确认操作',
    {
      confirmButtonText: '同意',
      cancelButtonText: '取消',
      type: 'info'
    }
  ).then(async () => {
    try {
      // 调用同意好友请求API
      const response = await friendAPI.acceptFriendRequest(request.id)
      
      // 检查响应状态
      if (response.data.code === 200) {
        // 从申请列表中移除
        const index = friendRequests.value.findIndex(r => r.id === request.id)
        if (index > -1) {
          friendRequests.value.splice(index, 1)
        }
        
        // 重新加载好友列表
        loadMyFriends()
        
        ElMessage.success('已同意好友申请')
      } else {
        ElMessage.error(response.data.message || '处理失败')
      }
    } catch (error) {
      console.error('同意好友申请错误:', error)
      const errorMessage = error.response?.data?.message || '操作失败，请检查网络连接'
      ElMessage.error(errorMessage)
    }
  })
}

const rejectRequest = (request) => {
  ElMessageBox.confirm(
    `确定要拒绝 ${request.user.nickname} 的好友申请吗？`,
    '确认操作',
    {
      confirmButtonText: '拒绝',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 调用拒绝好友请求API
      const response = await friendAPI.rejectFriendRequest(request.id)
      
      // 检查响应状态
      if (response.data.code === 200) {
        // 从申请列表中移除
        const index = friendRequests.value.findIndex(r => r.id === request.id)
        if (index > -1) {
          friendRequests.value.splice(index, 1)
        }
        
        ElMessage.success('已拒绝好友申请')
      } else {
        ElMessage.error(response.data.message || '处理失败')
      }
    } catch (error) {
      console.error('拒绝好友申请错误:', error)
      const errorMessage = error.response?.data?.message || '操作失败，请检查网络连接'
      ElMessage.error(errorMessage)
    }
  })
}

// 替换handleFriendCommand方法中的删除好友逻辑
const handleFriendCommand = (command) => {
  const [action, friendId] = command.split('-')
  
  switch (action) {
    case 'chat':
      ElMessage.info('聊天功能开发中...')
      break
    case 'profile':
      ElMessage.info('查看资料功能开发中...')
      break
    case 'delete':
      ElMessageBox.confirm(
        '确定要删除这个好友吗？',
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(async () => {
        try {
          // 调用删除好友API
          const response = await friendAPI.deleteFriend(friendId)
          
          if (response.data.code === 200) {
            // 从列表中移除
            const index = myFriends.value.findIndex(f => f.id === parseInt(friendId))
            if (index > -1) {
              myFriends.value.splice(index, 1)
            }
            ElMessage.success('已删除好友')
          } else {
            ElMessage.error(response.data.message || '删除失败')
          }
        } catch (error) {
          console.error('删除好友错误:', error)
          ElMessage.error('删除失败，请检查网络连接')
        }
      })
      break
  }
}

const formatTime = (time) => {
  const now = new Date()
  const diff = now - time
  const minutes = Math.floor(diff / (1000 * 60))
  const hours = Math.floor(diff / (1000 * 60 * 60))
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))
  
  if (minutes < 1) return '刚刚'
  if (minutes < 60) return `${minutes}分钟前`
  if (hours < 24) return `${hours}小时前`
  if (days < 7) return `${days}天前`
  
  return time.toLocaleDateString()
}

// 推荐好友的模拟数据
const mockRecommendFriends = [
  {
    id: 7,
    nickname: '班长',
    avatar: '',
    reason: '同专业同学'
  },
  {
    id: 8,
    nickname: '学习委员',
    avatar: '',
    reason: '共同好友推荐'
  },
  {
    id: 9,
    nickname: '社团伙伴',
    avatar: '',
    reason: '参加过相同活动'
  }
]

onMounted(() => {
  // 加载好友列表和申请列表
  loadMyFriends()
  loadFriendRequests()
  
  // 暂时使用模拟推荐好友数据
  recommendFriends.value = mockRecommendFriends
})
</script>

<style lang="scss" scoped>
.friends-container {
  min-height: 100vh;
  background-color: var(--secondary-color);
}

.main-content {
  padding: 20px 0;
}

.friends-wrapper {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.search-section {
  margin-bottom: 20px;
  
  .search-header {
    margin-bottom: 20px;
    
    h2 {
      color: var(--text-primary);
      font-size: 20px;
      font-weight: 600;
    }
  }
}

.add-friend-section {
  margin-bottom: 20px;
  
  .add-friend-header {
    margin-bottom: 16px;
    
    h3 {
      color: var(--text-primary);
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 4px;
    }
    
    p {
      color: var(--text-secondary);
      font-size: 14px;
      margin: 0;
    }
  }
}

.user-list,
.request-list,
.friends-list {
  .user-item,
  .request-item,
  .friend-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;
    border-bottom: 1px solid var(--border-color);
    
    &:last-child {
      border-bottom: none;
    }
    
    .user-info,
    .request-info,
    .friend-info {
      display: flex;
      gap: 12px;
      flex: 1;
      
      .user-details,
      .request-details,
      .friend-details {
        flex: 1;
        
        .user-main,
        .friend-main {
          display: flex;
          align-items: center;
          gap: 8px;
          margin-bottom: 4px;
          
          .nickname {
            font-weight: 500;
            color: var(--text-primary);
          }
          
          .request-time {
            font-size: 12px;
            color: var(--text-secondary);
          }
        }
        
        .user-desc,
        .friend-desc {
          font-size: 14px;
          color: var(--text-secondary);
          margin-bottom: 4px;
        }
        
        .user-bio {
          font-size: 13px;
          color: var(--text-secondary);
          line-height: 1.4;
        }
        
        .request-reason {
          font-size: 13px;
          color: var(--text-secondary);
          line-height: 1.4;
        }
        
        .last-seen {
          font-size: 12px;
          color: var(--text-secondary);
        }
      }
    }
    
    .user-actions,
    .request-actions,
    .friend-actions {
      display: flex;
      gap: 8px;
    }
  }
}

.friends-sidebar {
  .card {
    margin-bottom: 20px;
    
    h3 {
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 16px;
    }
  }
}

.stats-card {
  .stats-list {
    .stat-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 0;
      
      .stat-label {
        color: var(--text-secondary);
      }
      
      .stat-value {
        font-weight: 600;
        color: var(--primary-color);
      }
    }
  }
}

.recommend-card {
  .recommend-list {
    .recommend-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 0;
      border-bottom: 1px solid var(--border-color);
      
      &:last-child {
        border-bottom: none;
      }
      
      .recommend-info {
        flex: 1;
        
        .nickname {
          font-weight: 500;
          color: var(--text-primary);
          margin-bottom: 4px;
        }
        
        .reason {
          font-size: 12px;
          color: var(--text-secondary);
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .friends-wrapper {
    grid-template-columns: 1fr;
    
    .friends-sidebar {
      order: -1;
    }
  }
  
  .user-item,
  .request-item,
  .friend-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
    
    .user-actions,
    .request-actions,
    .friend-actions {
      width: 100%;
      justify-content: flex-end;
    }
  }
}
</style> 