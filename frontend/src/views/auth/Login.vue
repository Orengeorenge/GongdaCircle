<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="logo-section">
        <h1 class="logo">工大人社交圈</h1>
        <p class="subtitle">连接每一位工大人</p>
      </div>
      
      <el-form 
        ref="loginFormRef" 
        :model="loginForm" 
        :rules="loginRules" 
        class="login-form"
        size="large"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="loginForm.remember">
            记住我
          </el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="auth-links">
        <span>还没有账号？</span>
        <router-link to="/register" class="link">立即注册</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authAPI } from '@/api/auth'
import { setToken, setUserInfo } from '@/utils/auth'

const router = useRouter()
const loading = ref(false)
const loginFormRef = ref()

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { 
      pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,20}$/,
      message: '密码必须包含大小写字母和数字，长度8-20位', 
      trigger: 'blur' 
    }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      
      try {
        // 调用登录API
        const response = await authAPI.login({
          username: loginForm.username,
          password: loginForm.password
        })
        
        // 检查响应状态
        if (response.data.code === 200) {
          // 登录成功
          const data = response.data.data
          
          // 存储token和用户信息
          setToken(data.token)
          setUserInfo(data.userInfo)
          
          ElMessage.success('登录成功')
          router.push('/home')
        } else {
          // 登录失败
          ElMessage.error(response.data.message || '登录失败')
        }
      } catch (error) {
        // 处理网络错误或其他异常
        console.error('登录错误:', error)
        const errorMessage = error.response?.data?.message || '登录失败，请检查网络连接'
        ElMessage.error(errorMessage)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.auth-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-light) 100%);
  padding: 20px;
}

.auth-card {
  background: var(--background-white);
  border-radius: 12px;
  box-shadow: var(--shadow-md);
  padding: 40px;
  width: 100%;
  max-width: 400px;
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
  
  .logo {
    font-size: 28px;
    font-weight: bold;
    color: var(--primary-color);
    margin-bottom: 8px;
  }
  
  .subtitle {
    color: var(--text-secondary);
    font-size: 14px;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 20px;
  }
  
  .login-btn {
    width: 100%;
    height: 44px;
    font-size: 16px;
  }
}

.auth-links {
  text-align: center;
  margin-top: 24px;
  color: var(--text-secondary);
  
  .link {
    color: var(--primary-color);
    text-decoration: none;
    margin-left: 8px;
    
    &:hover {
      text-decoration: underline;
    }
  }
}

@media (max-width: 480px) {
  .auth-card {
    padding: 30px 20px;
  }
}
</style> 