<template>
  <el-header class="app-header">
    <div class="header-content">
      <div class="logo-section">
        <router-link to="/home" class="logo">
          工大人社交圈
        </router-link>
      </div>
      
      <div class="nav-section">
        <el-menu
          :default-active="activeIndex"
          mode="horizontal"
          background-color="transparent"
          text-color="#ffffff"
          active-text-color="#ffffff"
          @select="handleSelect"
        >
          <el-menu-item index="/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/publish">
            <el-icon><EditPen /></el-icon>
            <span>发布</span>
          </el-menu-item>
          <el-menu-item index="/friends">
            <el-icon><User /></el-icon>
            <span>好友</span>
          </el-menu-item>
        </el-menu>
      </div>
      
      <div class="user-section">
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="32" :src="userInfo.avatar">
              {{ userInfo.nickname?.charAt(0) }}
            </el-avatar>
            <span class="username">{{ userInfo.nickname }}</span>
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人中心
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon><Setting /></el-icon>
                设置
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </el-header>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()

const userInfo = ref({
  nickname: '工大学子',
  avatar: ''
})

const activeIndex = computed(() => route.path)

const handleSelect = (key) => {
  router.push(key)
}

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'settings':
      ElMessage.info('设置功能开发中...')
      break
    case 'logout':
      handleLogout()
      break
  }
}

const handleLogout = () => {
  ElMessageBox.confirm(
    '确定要退出登录吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    ElMessage.success('退出成功')
    router.push('/login')
  })
}

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    userInfo.value = JSON.parse(user)
  }
})
</script>

<style lang="scss" scoped>
.app-header {
  background: var(--primary-color);
  border-bottom: 1px solid var(--border-color);
  height: 60px;
  line-height: 60px;
  padding: 0;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.logo-section {
  .logo {
    font-size: 20px;
    font-weight: bold;
    color: white;
    text-decoration: none;
    
    &:hover {
      color: #e6f3ff;
    }
  }
}

.nav-section {
  flex: 1;
  margin: 0 40px;
  
  .el-menu {
    border-bottom: none;
    
    .el-menu-item {
      height: 60px;
      line-height: 60px;
      border-bottom: none;
      
      &:hover {
        background-color: rgba(255, 255, 255, 0.1);
      }
      
      &.is-active {
        background-color: rgba(255, 255, 255, 0.2);
        border-bottom: 2px solid white;
      }
    }
  }
}

.user-section {
  .user-info {
    display: flex;
    align-items: center;
    color: white;
    cursor: pointer;
    padding: 8px 12px;
    border-radius: 20px;
    transition: background-color 0.3s;
    
    &:hover {
      background-color: rgba(255, 255, 255, 0.1);
    }
    
    .username {
      margin: 0 8px;
      font-size: 14px;
    }
  }
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 15px;
  }
  
  .nav-section {
    margin: 0 20px;
  }
  
  .user-section .username {
    display: none;
  }
}
</style> 