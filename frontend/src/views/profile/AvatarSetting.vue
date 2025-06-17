<template>
  <div class="card">
    <h3 class="section-title">头像设置</h3>
    <div class="avatar-section">
      <div class="current-avatar">
        <el-avatar :size="120" :src="userAvatar">
          {{ userNickname?.charAt(0) }}
        </el-avatar>
        <div class="avatar-info">
          <p>当前头像</p>
          <span class="avatar-tip">建议使用正方形图片，大小不超过2MB</span>
        </div>
      </div>
      
      <div class="avatar-upload">
        <el-upload
          action="#"
          :show-file-list="false"
          :before-upload="beforeAvatarUpload"
          :on-success="handleAvatarSuccess"
          accept="image/*"
        >
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            上传新头像
          </el-button>
        </el-upload>
        
        <div class="avatar-presets">
          <p>或选择默认头像</p>
          <div class="preset-avatars">
            <div
              v-for="preset in presetAvatars"
              :key="preset.id"
              class="preset-avatar"
              @click="selectPresetAvatar(preset.url)"
            >
              <el-avatar :size="60" :src="preset.url" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'
import { Upload } from '@element-plus/icons-vue'

const props = defineProps({
  userAvatar: {
    type: String,
    default: ''
  },
  userNickname: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:avatar'])

// 预设头像
const presetAvatars = ref([
  { id: 1, url: 'https://via.placeholder.com/120/4f46e5/ffffff?text=A' },
  { id: 2, url: 'https://via.placeholder.com/120/06b6d4/ffffff?text=B' },
  { id: 3, url: 'https://via.placeholder.com/120/10b981/ffffff?text=C' },
  { id: 4, url: 'https://via.placeholder.com/120/f59e0b/ffffff?text=D' },
  { id: 5, url: 'https://via.placeholder.com/120/ef4444/ffffff?text=E' },
  { id: 6, url: 'https://via.placeholder.com/120/8b5cf6/ffffff?text=F' }
])

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2
  
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB!')
    return false
  }
  return true
}

const handleAvatarSuccess = (response) => {
  // 模拟头像上传成功
  ElMessage.success('头像上传成功')
  // 这里可以获取到上传后的图片URL，然后更新
  // 此处使用模拟URL
  const avatarUrl = 'https://via.placeholder.com/120/8b5cf6/ffffff?text=NEW'
  emit('update:avatar', avatarUrl)
}

const selectPresetAvatar = (url) => {
  emit('update:avatar', url)
  ElMessage.success('头像设置成功')
}
</script>

<style lang="scss" scoped>
.section-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 24px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-color);
}

.avatar-section {
  .current-avatar {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 30px;
    
    .avatar-info {
      p {
        font-weight: 500;
        margin-bottom: 4px;
      }
      
      .avatar-tip {
        font-size: 12px;
        color: var(--text-secondary);
      }
    }
  }
  
  .avatar-upload {
    .avatar-presets {
      margin-top: 24px;
      
      p {
        margin-bottom: 12px;
        color: var(--text-secondary);
      }
      
      .preset-avatars {
        display: flex;
        gap: 12px;
        flex-wrap: wrap;
        
        .preset-avatar {
          cursor: pointer;
          border: 2px solid transparent;
          border-radius: 50%;
          transition: border-color 0.3s;
          
          &:hover {
            border-color: var(--primary-color);
          }
        }
      }
    }
  }
}
</style> 