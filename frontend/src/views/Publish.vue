<template>
  <div class="publish-container">
    <app-header />
    
    <el-main class="main-content">
      <div class="container">
        <div class="publish-wrapper">
          <div class="card publish-card">
            <div class="publish-header">
              <h2>发布新动态</h2>
              <el-button @click="goBack">返回</el-button>
            </div>
            
            <el-form 
              ref="publishFormRef" 
              :model="publishForm" 
              :rules="publishRules"
              label-position="top"
            >
              <!-- 帖子类型选择 -->
              <el-form-item label="动态类型">
                <el-radio-group v-model="publishForm.type" @change="handleTypeChange">
                  <el-radio-button :label="1">
                    <el-icon><EditPen /></el-icon>
                    文字
                  </el-radio-button>
                  <el-radio-button :label="2">
                    <el-icon><Picture /></el-icon>
                    图片
                  </el-radio-button>
                  <el-radio-button :label="3">
                    <el-icon><VideoCamera /></el-icon>
                    视频
                  </el-radio-button>
                  <el-radio-button :label="4">
                    <el-icon><Link /></el-icon>
                    链接
                  </el-radio-button>
                </el-radio-group>
              </el-form-item>
              
              <!-- 标题 -->
              <el-form-item label="标题" prop="title">
                <el-input
                  v-model="publishForm.title"
                  placeholder="给你的动态起个标题吧（可选）"
                  maxlength="200"
                  show-word-limit
                />
              </el-form-item>
              
              <!-- 内容 -->
              <el-form-item label="内容" prop="content">
                <el-input
                  v-model="publishForm.content"
                  type="textarea"
                  placeholder="分享你的想法..."
                  :rows="8"
                  maxlength="5000"
                  show-word-limit
                  resize="none"
                />
              </el-form-item>
              
              <!-- 图片上传 -->
              <el-form-item v-if="publishForm.type === 2" label="图片">
                <div class="upload-section">
                  <el-upload
                    v-model:file-list="imageList"
                    action="#"
                    list-type="picture-card"
                    :auto-upload="false"
                    :limit="9"
                    :on-preview="handlePictureCardPreview"
                    :on-remove="handleRemove"
                    :before-upload="beforeUpload"
                  >
                    <el-icon><Plus /></el-icon>
                  </el-upload>
                  <div class="upload-tip">
                    最多上传9张图片，支持 jpg、png、gif 格式
                  </div>
                </div>
              </el-form-item>
              
              <!-- 视频上传 -->
              <el-form-item v-if="publishForm.type === 3" label="视频">
                <div class="upload-section">
                  <el-upload
                    action="#"
                    :limit="1"
                    :auto-upload="false"
                    :before-upload="beforeVideoUpload"
                  >
                    <el-button type="primary">
                      <el-icon><Upload /></el-icon>
                      选择视频
                    </el-button>
                  </el-upload>
                  <div class="upload-tip">
                    支持 mp4、mov、avi 格式，大小不超过100MB
                  </div>
                </div>
              </el-form-item>
              
              <!-- 链接输入 -->
              <el-form-item v-if="publishForm.type === 4" label="链接地址" prop="linkUrl">
                <el-input
                  v-model="publishForm.linkUrl"
                  placeholder="请输入链接地址"
                  @blur="parseLinkInfo"
                />
              </el-form-item>
              
              <!-- 话题标签 -->
              <el-form-item label="话题标签">
                <div class="tags-input">
                  <el-tag
                    v-for="tag in publishForm.tags"
                    :key="tag"
                    closable
                    @close="removeTag(tag)"
                    class="tag-item"
                  >
                    #{{ tag }}
                  </el-tag>
                  <el-input
                    v-if="inputVisible"
                    ref="inputRef"
                    v-model="inputValue"
                    size="small"
                    @keyup.enter="handleInputConfirm"
                    @blur="handleInputConfirm"
                    class="tag-input"
                  />
                  <el-button
                    v-else
                    size="small"
                    @click="showInput"
                    class="add-tag-btn"
                  >
                    + 添加话题
                  </el-button>
                </div>
                <div class="popular-tags">
                  <span class="popular-tags-title">热门话题：</span>
                  <el-tag
                    v-for="tag in popularTags"
                    :key="tag"
                    @click="addPopularTag(tag)"
                    class="popular-tag"
                    effect="plain"
                  >
                    #{{ tag }}
                  </el-tag>
                </div>
              </el-form-item>
              
              <!-- 位置信息 -->
              <el-form-item label="位置">
                <el-input
                  v-model="publishForm.location"
                  placeholder="你在哪里？（可选）"
                  prefix-icon="Location"
                />
              </el-form-item>
              
              <!-- 发布设置 -->
              <el-form-item label="发布设置">
                <div class="publish-settings">
                  <el-checkbox v-model="publishForm.isTop">设为置顶</el-checkbox>
                  <el-checkbox v-model="publishForm.allowComment">允许评论</el-checkbox>
                </div>
              </el-form-item>
              
              <!-- 操作按钮 -->
              <el-form-item>
                <div class="action-buttons">
                  <el-button @click="saveDraft">保存草稿</el-button>
                  <el-button type="primary" :loading="publishing" @click="handlePublish">
                    发布动态
                  </el-button>
                </div>
              </el-form-item>
            </el-form>
          </div>
          
          <!-- 右侧预览 -->
          <div class="card preview-card">
            <h3>预览</h3>
            <div class="preview-content">
              <div class="preview-post">
                <div class="preview-header">
                  <el-avatar :size="32">
                    {{ userInfo.nickname?.charAt(0) }}
                  </el-avatar>
                  <div class="preview-user">
                    <div class="username">{{ userInfo.nickname }}</div>
                    <div class="time">刚刚</div>
                  </div>
                </div>
                
                <div class="preview-body">
                  <h4 v-if="publishForm.title" class="preview-title">
                    {{ publishForm.title }}
                  </h4>
                  <p v-if="publishForm.content" class="preview-text">
                    {{ publishForm.content }}
                  </p>
                  <div v-if="publishForm.tags.length" class="preview-tags">
                    <el-tag v-for="tag in publishForm.tags" :key="tag" size="small">
                      #{{ tag }}
                    </el-tag>
                  </div>
                  <div v-if="publishForm.location" class="preview-location">
                    <el-icon><Location /></el-icon>
                    {{ publishForm.location }}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-main>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/Layout/AppHeader.vue'
import { postAPI } from '@/api/post'
import { getUserInfo } from '@/utils/auth'

const router = useRouter()

const publishFormRef = ref()
const inputRef = ref()
const publishing = ref(false)
const inputVisible = ref(false)
const inputValue = ref('')
const imageList = ref([])

const userInfo = ref({
  nickname: '工大学子',
  avatar: ''
})

const publishForm = reactive({
  type: 1, // 1-文字 2-图片 3-视频 4-链接
  title: '',
  content: '',
  tags: [],
  location: '',
  linkUrl: '',
  isTop: false,
  allowComment: true
})

const publishRules = {
  content: [
    { required: true, message: '请输入内容', trigger: 'blur' },
    { max: 5000, message: '内容不能超过5000个字符', trigger: 'blur' }
  ],
  title: [
    { max: 200, message: '标题不能超过200个字符', trigger: 'blur' }
  ],
  linkUrl: [
    { type: 'url', message: '请输入正确的链接地址', trigger: 'blur' }
  ]
}

const popularTags = ref([
  '工大校园', '学习心得', '社团活动', '美食推荐', '求职经验',
  '运动健身', '旅行分享', '技术交流', '生活感悟', '校园生活'
])

const goBack = () => {
  router.go(-1)
}

const handleTypeChange = (type) => {
  // 切换类型时重置相关字段
  if (type !== 4) {
    publishForm.linkUrl = ''
  }
  if (type !== 2) {
    imageList.value = []
  }
}

const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    inputRef.value?.input?.focus()
  })
}

const handleInputConfirm = () => {
  if (inputValue.value && !publishForm.tags.includes(inputValue.value)) {
    publishForm.tags.push(inputValue.value)
  }
  inputVisible.value = false
  inputValue.value = ''
}

const removeTag = (tag) => {
  const index = publishForm.tags.indexOf(tag)
  if (index > -1) {
    publishForm.tags.splice(index, 1)
  }
}

const addPopularTag = (tag) => {
  if (!publishForm.tags.includes(tag)) {
    publishForm.tags.push(tag)
  }
}

const handlePictureCardPreview = (file) => {
  // 图片预览
  console.log('预览图片:', file)
}

const handleRemove = (file, fileList) => {
  // 移除图片
  console.log('移除图片:', file)
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过5MB!')
    return false
  }
  return true
}

const beforeVideoUpload = (file) => {
  const isVideo = file.type.startsWith('video/')
  const isLt100M = file.size / 1024 / 1024 < 100
  
  if (!isVideo) {
    ElMessage.error('只能上传视频文件!')
    return false
  }
  if (!isLt100M) {
    ElMessage.error('视频大小不能超过100MB!')
    return false
  }
  return true
}

const parseLinkInfo = () => {
  // 解析链接信息（实际项目中可以调用API获取链接元信息）
  if (publishForm.linkUrl) {
    console.log('解析链接:', publishForm.linkUrl)
  }
}

const saveDraft = () => {
  ElMessage.success('草稿保存成功')
}

const handlePublish = async () => {
  if (!publishFormRef.value) return
  
  await publishFormRef.value.validate(async (valid) => {
    if (valid) {
      publishing.value = true
      
      try {
        // 准备发布数据，根据后端接口要求传递参数
        const postData = {
          title: publishForm.title || '', // 标题（可选）
          content: publishForm.content,   // 内容（必填）
          tags: publishForm.tags.join(','), // 标签，多个标签用逗号分隔
          type: publishForm.type,         // 帖子类型
          location: publishForm.location || '' // 位置信息（可选）
        }
        
        // 调用发布帖子API
        const response = await postAPI.createPost(postData)
        
        // 检查响应状态
        if (response.data.code === 200) {
          // 发布成功
          ElMessage.success('发布成功！')
          router.push('/home')
        } else {
          // 发布失败
          ElMessage.error(response.data.message || '发布失败')
        }
      } catch (error) {
        // 处理网络错误或其他异常
        console.error('发布错误:', error)
        const errorMessage = error.response?.data?.message || '发布失败，请检查网络连接'
        ElMessage.error(errorMessage)
      } finally {
        publishing.value = false
      }
    }
  })
}

onMounted(() => {
  // 获取用户信息
  const user = getUserInfo()
  if (user) {
    userInfo.value = user
  }
})
</script>

<style lang="scss" scoped>
.publish-container {
  min-height: 100vh;
  background-color: var(--secondary-color);
}

.main-content {
  padding: 20px 0;
}

.publish-wrapper {
  max-width: 1000px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
}

.publish-card {
  .publish-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    padding-bottom: 20px;
    border-bottom: 1px solid var(--border-color);
    
    h2 {
      color: var(--text-primary);
      font-size: 20px;
      font-weight: 600;
    }
  }
  
  .upload-section {
    .upload-tip {
      margin-top: 8px;
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
  
  .tags-input {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    align-items: center;
    
    .tag-item {
      cursor: pointer;
    }
    
    .tag-input {
      width: 120px;
    }
    
    .add-tag-btn {
      height: 24px;
      border-style: dashed;
    }
  }
  
  .popular-tags {
    margin-top: 12px;
    
    .popular-tags-title {
      font-size: 12px;
      color: var(--text-secondary);
      margin-right: 8px;
    }
    
    .popular-tag {
      margin-right: 8px;
      margin-bottom: 4px;
      cursor: pointer;
      
      &:hover {
        color: var(--primary-color);
        border-color: var(--primary-color);
      }
    }
  }
  
  .publish-settings {
    display: flex;
    gap: 20px;
  }
  
  .action-buttons {
    display: flex;
    gap: 12px;
    justify-content: flex-end;
    margin-top: 20px;
  }
}

.preview-card {
  position: sticky;
  top: 80px;
  height: fit-content;
  
  h3 {
    margin-bottom: 20px;
    color: var(--text-primary);
    font-size: 16px;
    font-weight: 600;
  }
  
  .preview-post {
    border: 1px solid var(--border-color);
    border-radius: 8px;
    padding: 16px;
    background-color: var(--secondary-color);
    
    .preview-header {
      display: flex;
      gap: 12px;
      margin-bottom: 12px;
      
      .preview-user {
        .username {
          font-weight: 500;
          color: var(--text-primary);
        }
        
        .time {
          font-size: 12px;
          color: var(--text-secondary);
        }
      }
    }
    
    .preview-body {
      .preview-title {
        font-size: 16px;
        font-weight: 600;
        margin-bottom: 8px;
        color: var(--text-primary);
      }
      
      .preview-text {
        line-height: 1.6;
        color: var(--text-primary);
        margin-bottom: 12px;
      }
      
      .preview-tags {
        display: flex;
        gap: 6px;
        flex-wrap: wrap;
        margin-bottom: 8px;
      }
      
      .preview-location {
        display: flex;
        align-items: center;
        gap: 4px;
        font-size: 12px;
        color: var(--text-secondary);
      }
    }
  }
}

@media (max-width: 768px) {
  .publish-wrapper {
    grid-template-columns: 1fr;
    
    .preview-card {
      order: -1;
      position: static;
    }
  }
  
  .action-buttons {
    justify-content: center;
    
    .el-button {
      flex: 1;
    }
  }
}
</style> 