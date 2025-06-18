<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="logo-section">
        <h1 class="logo">加入工大人社交圈</h1>
        <p class="subtitle">开启你的校园社交之旅</p>
      </div>
      
      <el-form 
        ref="registerFormRef" 
        :model="registerForm" 
        :rules="registerRules" 
        class="register-form"
        size="large"
      >
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
          />
        </el-form-item>
        
        <el-form-item prop="nickname">
          <el-input
            v-model="registerForm.nickname"
            placeholder="请输入昵称"
            prefix-icon="Avatar"
          />
        </el-form-item>
        
        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="请输入邮箱"
            prefix-icon="Message"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请确认密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <el-form-item prop="agreement">
          <el-checkbox v-model="registerForm.agreement">
            我已阅读并同意
            <span class="agreement-link">《用户协议》</span>
            和
            <span class="agreement-link">《隐私政策》</span>
          </el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            class="register-btn"
            :loading="loading"
            @click="handleRegister"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="auth-links">
        <span>已有账号？</span>
        <router-link to="/login" class="link">立即登录</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authAPI } from '@/api/auth'

const router = useRouter()
const loading = ref(false)
const registerFormRef = ref()

const registerForm = reactive({
  username: '',
  nickname: '',
  email: '',
  password: '',
  confirmPassword: '',
  agreement: false
})

const validateUsername = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入用户名'))
    return
  }
  
  // 检查用户名格式
  if (!/^[a-zA-Z0-9_]{3,20}$/.test(value)) {
    callback(new Error('用户名只能包含字母、数字、下划线，长度3-20位'))
    return
  }
  
  // 异步检查用户名是否已存在
  authAPI.checkUsername(value)
    .then(response => {
      if (response.data.code === 200 && response.data.data === true) {
        callback(new Error('该用户名已被使用'))
      } else {
        callback()
      }
    })
    .catch(() => {
      // 如果API调用失败，允许用户继续注册
      callback()
    })
}

const validateEmail = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入邮箱'))
    return
  }
  
  // 检查邮箱格式
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
    callback(new Error('请输入正确的邮箱格式'))
    return
  }
  
  // 异步检查邮箱是否已存在
  authAPI.checkEmail(value)
    .then(response => {
      if (response.data.code === 200 && response.data.data === true) {
        callback(new Error('该邮箱已被使用'))
      } else {
        callback()
      }
    })
    .catch(() => {
      // 如果API调用失败，允许用户继续注册
      callback()
    })
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { validator: validateUsername, trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 10, message: '昵称长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { validator: validateEmail, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { 
      pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,20}$/,
      message: '密码必须包含大小写字母和数字，长度8-20位', 
      trigger: 'blur' 
    }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  agreement: [
    { type: 'boolean', required: true, message: '请同意用户协议', trigger: 'change' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      
      try {
        // 构建注册数据，确保不传递空手机号
        const registerData = {
          username: registerForm.username,
          password: registerForm.password,
          nickname: registerForm.nickname,
          email: registerForm.email,
          gender: 2, // 默认保密
          school: '工业大学' // 可选默认值
          // 不传递phone字段，让后端使用null值而不是空字符串
        }
        
        console.log('注册数据:', registerData)
        
        // 调用注册API，传递完整的用户信息
        const response = await authAPI.register(registerData)
        
        // 检查响应状态
        if (response.data.code === 200) {
          // 注册成功
          ElMessage.success('注册成功，请登录')
          router.push('/login')
        } else {
          // 注册失败
          ElMessage.error(response.data.message || '注册失败')
        }
      } catch (error) {
        // 处理网络错误或其他异常
        console.error('注册错误:', error)
        if (error.response && error.response.data && error.response.data.message) {
          // 显示详细的错误信息
          const errorMessage = error.response.data.message
          if (errorMessage.includes('Duplicate entry') && errorMessage.includes('uk_phone')) {
            ElMessage.error('手机号已被使用，请使用其他手机号')
          } else if (errorMessage.includes('Duplicate entry') && errorMessage.includes('uk_email')) {
            ElMessage.error('邮箱已被使用，请使用其他邮箱')
          } else if (errorMessage.includes('Duplicate entry') && errorMessage.includes('uk_username')) {
            ElMessage.error('用户名已被使用，请使用其他用户名')
          } else {
            ElMessage.error(errorMessage)
          }
        } else {
          ElMessage.error('注册失败，请检查网络连接')
        }
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
  max-width: 450px;
}

.logo-section {
  text-align: center;
  margin-bottom: 40px;
  
  .logo {
    font-size: 24px;
    font-weight: bold;
    color: var(--primary-color);
    margin-bottom: 8px;
  }
  
  .subtitle {
    color: var(--text-secondary);
    font-size: 14px;
  }
}

.register-form {
  .el-form-item {
    margin-bottom: 20px;
  }
  
  .register-btn {
    width: 100%;
    height: 44px;
    font-size: 16px;
  }
  
  .agreement-link {
    color: var(--primary-color);
    cursor: pointer;
    
    &:hover {
      text-decoration: underline;
    }
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