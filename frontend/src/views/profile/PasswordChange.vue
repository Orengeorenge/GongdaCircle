<template>
  <div class="card">
    <h3 class="section-title">修改密码</h3>
    <el-form
      ref="passwordFormRef"
      :model="passwordForm"
      :rules="passwordRules"
      label-width="100px"
      class="password-form"
    >
      <el-form-item label="当前密码" prop="currentPassword">
        <el-input
          v-model="passwordForm.currentPassword"
          type="password"
          placeholder="请输入当前密码"
          show-password
          @input="onPasswordInput"
        />
      </el-form-item>
      
      <el-form-item label="新密码" prop="newPassword">
        <el-input
          v-model="passwordForm.newPassword"
          type="password"
          placeholder="请输入新密码"
          show-password
        />
      </el-form-item>
      
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="passwordForm.confirmPassword"
          type="password"
          placeholder="请再次输入新密码"
          show-password
        />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="changePassword" :loading="loading">
          修改密码
        </el-button>
        <el-button @click="resetPasswordForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { userAPI } from '@/api/user'
import { getUserInfo } from '@/utils/auth'

const passwordFormRef = ref()
const loading = ref(false)
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 检查密码输入，帮助调试
const onPasswordInput = (value) => {
  console.log('当前密码输入:', value)
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { 
      pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,20}$/,
      message: '密码必须包含大小写字母和数字，长度8-20位', 
      trigger: 'blur' 
    }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const changePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        loading.value = true
        
        // 获取当前用户信息
        const currentUser = getUserInfo()
        if (!currentUser || !currentUser.username) {
          ElMessage.error('无法获取当前用户信息，请重新登录')
          return
        }
        
        // 输出调试信息
        console.log('当前用户信息:', currentUser)
        console.log('表单数据:', passwordForm)
        
        // 构建请求数据 - 后端参数名为oldPassword而不是currentPassword
        const passwordData = {
          oldPassword: passwordForm.currentPassword,
          newPassword: passwordForm.newPassword
        }
        
        console.log('准备发送的密码数据:', passwordData)
        
        try {
          // 调用修改密码API
          const response = await userAPI.changePassword(currentUser.username, passwordData)
          
          console.log('密码修改响应:', response)
          
          if (response.code === 200) {
            ElMessage.success('密码修改成功')
            resetPasswordForm()
          } else {
            // 显示后端返回的错误信息
            ElMessage.error(response.message || '密码修改失败')
          }
        } catch (apiError) {
          console.error('API调用错误:', apiError)
          
          // 处理API错误
          if (apiError.response && apiError.response.data) {
            const errorData = apiError.response.data
            ElMessage.error(errorData.message || '密码修改失败')
            console.error('服务器错误:', errorData)
          } else {
            ElMessage.error(apiError.message || '修改密码失败，请稍后重试')
          }
        }
      } catch (error) {
        console.error('修改密码失败:', error)
        // 显示详细的错误信息
        if (error.response && error.response.data) {
          console.error('服务器返回的错误:', error.response.data)
          ElMessage.error(error.response.data.message || '修改密码失败，请检查输入')
        } else {
          ElMessage.error(error.message || '修改密码失败，请稍后重试')
        }
      } finally {
        loading.value = false
      }
    }
  })
}

const resetPasswordForm = () => {
  passwordFormRef.value?.resetFields()
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

.password-form {
  max-width: 400px;
}
</style> 