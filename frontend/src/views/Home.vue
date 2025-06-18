<template>
  <div class="home-container">
    <app-header />
    
    <el-main class="main-content">
      <div class="container">
        <el-row :gutter="20">
          <!-- 左侧内容区域 -->
          <el-col :lg="16" :md="24" :sm="24" :xs="24">
            <!-- 快速发布卡片 -->
            <div class="card quick-post">
              <div class="quick-post-header">
                <el-avatar :size="40" :src="userInfo.avatar">
                  {{ userInfo.nickname?.charAt(0) }}
                </el-avatar>
                <el-input
                  placeholder="分享你的想法..."
                  readonly
                  class="quick-input"
                  @click="goToPublish"
                />
              </div>
              <div class="quick-post-actions">
                <el-button text @click="goToPublish">
                  <el-icon><EditPen /></el-icon>
                  发布动态
                </el-button>
                <el-button text>
                  <el-icon><Picture /></el-icon>
                  图片
                </el-button>
                <el-button text>
                  <el-icon><VideoCamera /></el-icon>
                  视频
                </el-button>
              </div>
            </div>
            
            <!-- 帖子列表 -->
            <div class="posts-container">
              <div v-for="post in posts" :key="post.id" class="card post-card">
                <div class="post-header">
                  <div class="user-info">
                    <el-avatar :size="40" :src="post.userInfo?.avatar || ''">
                      {{ post.userInfo?.nickname?.charAt(0) || '用户' }}
                    </el-avatar>
                    <div class="user-details">
                      <div class="username">{{ post.userInfo?.nickname || '未知用户' }}</div>
                      <div class="post-time">{{ formatTime(post.createTime) }}</div>
                    </div>
                  </div>
                  <el-dropdown>
                    <el-icon class="more-icon"><More /></el-icon>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item>举报</el-dropdown-item>
                        <el-dropdown-item>隐藏</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
                
                <div class="post-content">
                  <h3 v-if="post.title" class="post-title">{{ post.title }}</h3>
                  <p class="post-text">{{ post.content }}</p>
                  
                  <div v-if="post.images && post.images.length" class="post-images">
                    <el-image
                      v-for="(image, index) in post.images"
                      :key="index"
                      :src="image"
                      fit="cover"
                      class="post-image"
                      :preview-src-list="post.images"
                      :initial-index="index"
                    />
                  </div>
                  
                  <div v-if="post.tags && post.tags.length" class="post-tags">
                    <el-tag v-for="tag in post.tags" :key="tag" size="small" type="info">
                      #{{ tag }}
                    </el-tag>
                  </div>
                </div>
                
                <div class="post-actions">
                  <div class="action-stats">
                    <span class="stat-item">{{ post.likeCount }} 点赞</span>
                    <span class="stat-item">{{ post.commentCount }} 评论</span>
                    <span class="stat-item">{{ post.viewCount }} 浏览</span>
                  </div>
                  
                  <div class="action-buttons">
                    <el-button 
                      text 
                      :type="post.isLiked ? 'primary' : ''"
                      @click="toggleLike(post)"
                    >
                      <el-icon><Like /></el-icon>
                      点赞
                    </el-button>
                    <el-button text @click="showComments(post)">
                      <el-icon><ChatDotRound /></el-icon>
                      评论
                    </el-button>
                    <el-button text @click="collectPost(post)">
                      <el-icon><Star /></el-icon>
                      收藏
                    </el-button>
                    <el-button text>
                      <el-icon><Share /></el-icon>
                      分享
                    </el-button>
                  </div>
                </div>
              </div>
              
              <!-- 加载更多 -->
              <div class="load-more">
                <el-button 
                  v-if="hasMore" 
                  :loading="loadingMore" 
                  @click="loadMore"
                  style="width: 100%"
                >
                  加载更多
                </el-button>
                <div v-else class="no-more">没有更多内容了</div>
              </div>
            </div>
          </el-col>
          
          <!-- 右侧边栏 -->
          <el-col :lg="8" :md="0" :sm="0" :xs="0">
            <div class="sidebar">
              <!-- 热门话题 -->
              <div class="card">
                <h3 class="sidebar-title">热门话题</h3>
                <div class="hot-topics">
                  <div v-for="topic in hotTopics" :key="topic.name" class="topic-item">
                    <span class="topic-name">#{{ topic.name }}</span>
                    <span class="topic-count">{{ topic.count }}条讨论</span>
                  </div>
                </div>
              </div>
              
              <!-- 推荐用户 -->
              <div class="card">
                <h3 class="sidebar-title">推荐关注</h3>
                <div class="recommend-users">
                  <div v-for="user in recommendUsers" :key="user.id" class="user-item">
                    <el-avatar :size="32" :src="user.avatar">
                      {{ user.nickname?.charAt(0) }}
                    </el-avatar>
                    <div class="user-info">
                      <div class="nickname">{{ user.nickname }}</div>
                      <div class="desc">{{ user.desc }}</div>
                    </div>
                    <el-button size="small" type="primary">关注</el-button>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/Layout/AppHeader.vue'
import { postAPI } from '@/api/post'
import { getUserInfo } from '@/utils/auth'

const router = useRouter()

// 用户信息
const userInfo = ref({
  nickname: '工大学子',
  avatar: ''
})

// 帖子数据与状态管理
const posts = ref([])
const loading = ref(false)
const loadingMore = ref(false)
const refreshing = ref(false)
const hasMore = ref(true)
const error = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const totalPages = ref(1)

// 侧边栏数据
const hotTopics = ref([
  { name: '工大校园', count: 128 },
  { name: '学习心得', count: 86 },
  { name: '社团活动', count: 64 },
  { name: '美食推荐', count: 42 },
  { name: '求职经验', count: 38 }
])

const recommendUsers = ref([
  { id: 1, nickname: '学霸小明', desc: '计算机学院 · 大三', avatar: '' },
  { id: 2, nickname: '文艺小芳', desc: '文学院 · 大二', avatar: '' },
  { id: 3, nickname: '运动达人', desc: '体育学院 · 大四', avatar: '' }
])

// 初始加载帖子
const loadPosts = async () => {
  try {
    loading.value = true
    error.value = null
    
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    
    console.log('发起请求获取帖子列表:', params)
    const response = await postAPI.getPostList(params)
    
    // 记录完整响应，帮助调试
    console.log('获取帖子列表响应:', response)
    
    // 处理响应数据
    if (response.code === 200) {
      // 确保每个帖子都有userInfo字段，如果没有则添加默认值
      const processedPosts = (response.data.records || []).map(post => {
        if (!post.userInfo) {
          post.userInfo = {
            nickname: '未知用户',
            avatar: ''
          }
        }
        return post
      })
      
      posts.value = processedPosts
      totalPages.value = response.data.pages || 1
      hasMore.value = currentPage.value < totalPages.value
    } else {
      error.value = response.message || '加载帖子失败'
      ElMessage.error(error.value)
    }
  } catch (err) {
    console.error('加载帖子出错:', err)
    error.value = '网络错误，请稍后重试'
    ElMessage.error(error.value)
  } finally {
    loading.value = false
  }
}

// 加载更多帖子
const loadMore = async () => {
  if (loadingMore.value || !hasMore.value) return
  
  try {
    loadingMore.value = true
    currentPage.value++
    
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    
    console.log('发起请求加载更多帖子:', params)
    const response = await postAPI.getPostList(params)
    console.log('加载更多帖子响应:', response)
    
    // 处理响应数据
    if (response.code === 200) {
      // 确保每个帖子都有userInfo字段，如果没有则添加默认值
      const newPosts = (response.data.records || []).map(post => {
        if (!post.userInfo) {
          post.userInfo = {
            nickname: '未知用户',
            avatar: ''
          }
        }
        return post
      })
      
      posts.value = [...posts.value, ...newPosts]
      totalPages.value = response.data.pages || 1
      hasMore.value = currentPage.value < totalPages.value
    } else {
      ElMessage.error(response.message || '加载更多帖子失败')
      currentPage.value--
    }
  } catch (err) {
    console.error('加载更多帖子出错:', err)
    ElMessage.error('网络错误，请稍后重试')
    currentPage.value--
  } finally {
    loadingMore.value = false
  }
}

// 下拉刷新
const refreshPosts = async () => {
  try {
    refreshing.value = true
    currentPage.value = 1
    error.value = null
    
    const params = {
      page: currentPage.value,
      size: pageSize.value
    }
    
    console.log('发起请求刷新帖子列表:', params)
    const response = await postAPI.getPostList(params)
    console.log('刷新帖子响应:', response)
    
    // 处理响应数据
    if (response.code === 200) {
      // 确保每个帖子都有userInfo字段，如果没有则添加默认值
      const processedPosts = (response.data.records || []).map(post => {
        if (!post.userInfo) {
          post.userInfo = {
            nickname: '未知用户',
            avatar: ''
          }
        }
        return post
      })
      
      posts.value = processedPosts
      totalPages.value = response.data.pages || 1
      hasMore.value = currentPage.value < totalPages.value
      ElMessage.success('刷新成功')
    } else {
      error.value = response.message || '刷新失败'
      ElMessage.error(error.value)
    }
  } catch (err) {
    console.error('刷新帖子出错:', err)
    error.value = '网络错误，请稍后重试'
    ElMessage.error(error.value)
  } finally {
    refreshing.value = false
  }
}

// 格式化时间显示
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  
  const time = new Date(timeStr)
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

// 格式化标签字符串为数组
const formatTags = (tagsStr) => {
  if (!tagsStr) return []
  return tagsStr.split(',').filter(tag => tag.trim() !== '')
}

// 格式化图片字符串为数组
const formatImages = (imagesStr) => {
  if (!imagesStr) return []
  return imagesStr.split(',').filter(img => img.trim() !== '')
}

// 点赞帖子
const toggleLike = async (post) => {
  try {
    console.log('发起点赞请求:', post.id)
    const response = await postAPI.likePost(post.id)
    console.log('点赞响应:', response)
    
    if (response.code === 200) {
      // 根据后端返回更新点赞状态和数量
  post.isLiked = !post.isLiked
      post.likeCount = post.isLiked ? post.likeCount + 1 : post.likeCount - 1
      ElMessage.success(post.isLiked ? '点赞成功' : '已取消点赞')
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('点赞操作失败:', error)
    ElMessage.error('网络错误，请稍后重试')
  }
}

// 显示评论
const showComments = (post) => {
  ElMessage.info('评论功能开发中...')
}

// 收藏帖子
const collectPost = async (post) => {
  try {
    console.log('发起收藏请求:', post.id)
    const response = await postAPI.collectPost(post.id)
    console.log('收藏响应:', response)
    
    if (response.code === 200) {
  ElMessage.success('收藏成功')
    } else {
      ElMessage.error(response.message || '收藏失败')
    }
  } catch (error) {
    console.error('收藏操作失败:', error)
    ElMessage.error('网络错误，请稍后重试')
  }
}

// 跳转到发布页面
const goToPublish = () => {
  router.push('/publish')
}

// 页面加载时获取用户信息和帖子列表
onMounted(() => {
  // 获取当前用户信息
  const user = getUserInfo()
  if (user) {
    userInfo.value = user
  }
  
  // 加载帖子列表
  loadPosts()
})
</script>

<style lang="scss" scoped>
.home-container {
  min-height: 100vh;
  background-color: var(--secondary-color);
}

.main-content {
  padding: 20px 0;
}

.quick-post {
  margin-bottom: 20px;
  
  .quick-post-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
    
    .quick-input {
      flex: 1;
      
      :deep(.el-input__wrapper) {
        background-color: var(--secondary-color);
        cursor: pointer;
      }
    }
  }
  
  .quick-post-actions {
    display: flex;
    gap: 16px;
    
    .el-button {
      color: var(--text-secondary);
    }
  }
}

.post-card {
  margin-bottom: 20px;
  
  .post-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 16px;
    
    .user-info {
      display: flex;
      gap: 12px;
      
      .user-details {
        .username {
          font-weight: 500;
          color: var(--text-primary);
          margin-bottom: 4px;
        }
        
        .post-time {
          font-size: 12px;
          color: var(--text-secondary);
        }
      }
    }
    
    .more-icon {
      cursor: pointer;
      color: var(--text-secondary);
      
      &:hover {
        color: var(--text-primary);
      }
    }
  }
  
  .post-content {
    margin-bottom: 16px;
    
    .post-title {
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 8px;
      color: var(--text-primary);
    }
    
    .post-text {
      line-height: 1.6;
      color: var(--text-primary);
      margin-bottom: 12px;
    }
    
    .post-images {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
      margin-bottom: 12px;
      
      .post-image {
        width: 120px;
        height: 120px;
        border-radius: 8px;
        cursor: pointer;
      }
    }
    
    .post-tags {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }
  }
  
  .post-actions {
    border-top: 1px solid var(--border-color);
    padding-top: 12px;
    
    .action-stats {
      display: flex;
      gap: 16px;
      margin-bottom: 12px;
      
      .stat-item {
        font-size: 13px;
        color: var(--text-secondary);
      }
    }
    
    .action-buttons {
      display: flex;
      gap: 8px;
      
      .el-button {
        flex: 1;
        color: var(--text-secondary);
        
        &.el-button--primary {
          color: var(--primary-color);
        }
      }
    }
  }
}

.sidebar {
  position: sticky;
  top: 80px;
  
  .sidebar-title {
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 16px;
    color: var(--text-primary);
  }
  
  .hot-topics {
    .topic-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 0;
      cursor: pointer;
      
      &:hover {
        background-color: var(--secondary-color);
        margin: 0 -12px;
        padding: 8px 12px;
        border-radius: 4px;
      }
      
      .topic-name {
        color: var(--primary-color);
        font-weight: 500;
      }
      
      .topic-count {
        font-size: 12px;
        color: var(--text-secondary);
      }
    }
  }
  
  .recommend-users {
    .user-item {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 0;
      
      .user-info {
        flex: 1;
        
        .nickname {
          font-weight: 500;
          margin-bottom: 4px;
        }
        
        .desc {
          font-size: 12px;
          color: var(--text-secondary);
        }
      }
    }
  }
}

.load-more {
  text-align: center;
  margin-top: 20px;
  
  .no-more {
    color: var(--text-secondary);
    padding: 20px;
  }
}

@media (max-width: 768px) {
  .quick-post-actions {
    justify-content: space-between;
    
    .el-button {
      flex: 1;
      margin: 0;
    }
  }
  
  .action-buttons {
    .el-button {
      font-size: 12px;
      padding: 8px 4px;
    }
  }
}
</style> 