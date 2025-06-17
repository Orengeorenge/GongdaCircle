<template>
  <div class="profile-container">
    <app-header />
    
    <el-main class="main-content">
      <div class="container">
        <div class="profile-wrapper">
          <!-- 左侧菜单 -->
          <div class="profile-sidebar">
            <div class="card">
              <el-menu
                :default-active="activeMenu"
                @select="handleMenuSelect"
                class="profile-menu"
              >
                <el-menu-item index="basic">
                  <el-icon><User /></el-icon>
                  <span>基本资料</span>
                </el-menu-item>
                <el-menu-item index="avatar">
                  <el-icon><Picture /></el-icon>
                  <span>头像设置</span>
                </el-menu-item>
                <el-menu-item index="password">
                  <el-icon><Lock /></el-icon>
                  <span>修改密码</span>
                </el-menu-item>
                <!-- 登录日志功能待移除 -->
                <!--
                <el-menu-item index="logs">
                  <el-icon><Clock /></el-icon>
                  <span>登录日志</span>
                </el-menu-item>
                -->
                <!-- 隐私设置功能待移除 -->
                <!--
                <el-menu-item index="privacy">
                  <el-icon><Setting /></el-icon>
                  <span>隐私设置</span>
                </el-menu-item>
                -->
              </el-menu>
            </div>
          </div>
          
          <!-- 右侧内容 -->
          <div class="profile-main">
            <!-- 基本资料 -->
            <BasicInfo 
              v-if="activeMenu === 'basic'"
              ref="basicInfoRef"
              @update:user-info="updateUserInfo"
            />
            
            <!-- 头像设置 -->
            <AvatarSetting 
              v-if="activeMenu === 'avatar'"
              :user-avatar="userInfo.avatar"
              :user-nickname="userInfo.nickname"
              @update:avatar="updateAvatar"
            />
            
            <!-- 修改密码 -->
            <PasswordChange 
              v-if="activeMenu === 'password'"
              @password-changed="handlePasswordChanged"
            />
            
            <!-- 登录日志功能待移除 -->
            <!--
            <LoginLogs 
              v-if="activeMenu === 'logs'"
            />
            -->
            
            <!-- 隐私设置功能待移除 -->
            <!--
            <PrivacySetting 
              v-if="activeMenu === 'privacy'"
            />
            -->
          </div>
        </div>
      </div>
    </el-main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, watch } from 'vue'
import { User, Picture, Lock, Clock, Setting } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/Layout/AppHeader.vue'
import BasicInfo from './BasicInfo.vue'
import AvatarSetting from './AvatarSetting.vue'
import PasswordChange from './PasswordChange.vue'
import LoginLogs from './LoginLogs.vue'
import PrivacySetting from './PrivacySetting.vue'
import { getUserInfo, setUserInfo } from '@/utils/auth'
import { authAPI } from '@/api'

const activeMenu = ref('basic')
const basicInfoRef = ref(null)

// 用户信息
const userInfo = reactive({
  id: '',
  username: '',
  nickname: '',
  email: '',
  avatar: '',
  phone: '',
  gender: 2,
  birthday: '',
  school: '',
  major: '',
  grade: '',
  biography: ''
})

// 监听菜单切换
const handleMenuSelect = (index) => {
  activeMenu.value = index
  
  // 当切换到基本资料时，确保数据是最新的
  if (index === 'basic') {
    nextTick(() => {
      if (basicInfoRef.value) {
        basicInfoRef.value.setFormData(userInfo)
      }
    })
  }
}

// 更新用户信息
const updateUserInfo = async (updatedInfo) => {
  try {
    // 更新本地状态
    Object.assign(userInfo, updatedInfo)
    // 更新localStorage中的用户信息
    setUserInfo(userInfo)
    
    // 调用API更新服务器上的用户信息
    if (userInfo.id) {
      await authAPI.updateUserInfo(userInfo.id, updatedInfo)
      ElMessage.success('基本资料更新成功')
    }
  } catch (error) {
    console.error('更新用户信息失败:', error)
    ElMessage.error('更新用户信息失败，请稍后重试')
  }
}

// 更新头像
const updateAvatar = async (url) => {
  try {
    // 更新本地状态
    userInfo.avatar = url
    // 更新localStorage中的用户信息
    setUserInfo(userInfo)
    
    // 调用API更新服务器上的头像
    if (userInfo.id) {
      await authAPI.updateAvatar(userInfo.id, { avatar: url })
      ElMessage.success('头像设置成功')
    }
  } catch (error) {
    console.error('更新头像失败:', error)
    ElMessage.error('更新头像失败，请稍后重试')
  }
}

// 处理密码修改成功事件
const handlePasswordChanged = () => {
  ElMessage.success('密码修改成功，下次登录时生效')
}

// 获取最新用户信息
const fetchUserInfo = async () => {
  try {
    const response = await authAPI.getCurrentUser()
    if (response.data.code === 200) {
      const fetchedUserInfo = response.data.data
      Object.assign(userInfo, fetchedUserInfo)
      // 更新localStorage中的用户信息
      setUserInfo(userInfo)
      
      // 更新基本资料表单
      nextTick(() => {
        if (basicInfoRef.value && activeMenu.value === 'basic') {
          basicInfoRef.value.setFormData(userInfo)
        }
      })
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

onMounted(() => {
  // 尝试从localStorage获取用户信息
  const storedUserInfo = getUserInfo()
  if (storedUserInfo) {
    Object.assign(userInfo, storedUserInfo)
  }
  
  // 设置基本资料表单数据
  nextTick(() => {
    if (basicInfoRef.value) {
      basicInfoRef.value.setFormData(userInfo)
    }
  })
  
  // 从服务器获取最新的用户信息
  fetchUserInfo()
})

// 监听activeMenu变化，确保切换回基本资料页面时数据是最新的
watch(activeMenu, (newValue) => {
  if (newValue === 'basic') {
    nextTick(() => {
      if (basicInfoRef.value) {
        basicInfoRef.value.setFormData(userInfo)
      }
    })
  }
})
</script>

<style lang="scss" scoped>
.profile-container {
  min-height: 100vh;
  background-color: var(--secondary-color);
}

.main-content {
  padding: 20px 0;
}

.profile-wrapper {
  display: grid;
  grid-template-columns: 250px 1fr;
  gap: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.profile-sidebar {
  .card {
    padding: 0;
  }
  
  .profile-menu {
    border-right: none;
    
    .el-menu-item {
      border-radius: 8px;
      margin: 4px 8px;
      
      &.is-active {
        background-color: var(--primary-color);
        color: white;
      }
    }
  }
}

@media (max-width: 768px) {
  .profile-wrapper {
    grid-template-columns: 1fr;
  }
  
  .profile-sidebar {
    .profile-menu {
      display: flex;
      overflow-x: auto;
      
      .el-menu-item {
        flex-shrink: 0;
        margin: 4px;
      }
    }
  }
}
</style> 