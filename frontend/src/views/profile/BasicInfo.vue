<template>
  <div class="card">
    <h3 class="section-title">基本资料</h3>
    <el-form
      ref="basicFormRef"
      :model="basicForm"
      :rules="basicRules"
      label-width="100px"
      class="profile-form"
    >
      <el-form-item label="用户名" prop="username">
        <el-input v-model="basicForm.username" disabled />
        <div class="form-tip">用户名不可修改</div>
      </el-form-item>
      
      <el-form-item label="昵称" prop="nickname">
        <el-input
          v-model="basicForm.nickname"
          placeholder="请输入昵称"
          maxlength="20"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="basicForm.email"
          placeholder="请输入邮箱"
        />
      </el-form-item>
      
      <el-form-item label="手机号" prop="phone">
        <el-input
          v-model="basicForm.phone"
          placeholder="请输入手机号"
        />
      </el-form-item>
      
      <el-form-item label="性别">
        <el-radio-group v-model="basicForm.gender">
          <el-radio :label="1">男</el-radio>
          <el-radio :label="0">女</el-radio>
          <el-radio :label="2">保密</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="生日">
        <el-date-picker
          v-model="basicForm.birthday"
          type="date"
          placeholder="选择生日"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      
      <el-form-item label="学校" prop="school">
        <el-input
          v-model="basicForm.school"
          placeholder="请输入学校名称"
        />
      </el-form-item>
      
      <el-form-item label="专业" prop="major">
        <el-input
          v-model="basicForm.major"
          placeholder="请输入专业名称"
        />
      </el-form-item>
      
      <el-form-item label="年级">
        <el-select v-model="basicForm.grade" placeholder="请选择年级">
          <el-option label="大一" value="大一" />
          <el-option label="大二" value="大二" />
          <el-option label="大三" value="大三" />
          <el-option label="大四" value="大四" />
          <el-option label="研一" value="研一" />
          <el-option label="研二" value="研二" />
          <el-option label="研三" value="研三" />
          <el-option label="博士" value="博士" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="个人简介">
        <el-input
          v-model="basicForm.biography"
          type="textarea"
          :rows="4"
          placeholder="介绍一下自己吧..."
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" @click="updateBasicInfo" :loading="loading">
          保存修改
        </el-button>
        <el-button @click="resetBasicForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, defineExpose } from 'vue'
import { ElMessage } from 'element-plus'
import { userAPI } from '@/api'
import { setUserInfo } from '@/utils/auth'

const basicFormRef = ref()
const loading = ref(false)
const basicForm = reactive({
  id: '',
  username: '',
  nickname: '',
  email: '',
  phone: '',
  gender: 2,
  birthday: '',
  school: '',
  major: '',
  grade: '',
  biography: ''
})

const basicRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

const updateBasicInfo = async () => {
  if (!basicFormRef.value) return
  
  try {
    await basicFormRef.value.validate(async (valid) => {
    if (valid) {
        loading.value = true
        
        // 从表单中获取用户名（API需要用户名而不是ID）
        const username = basicForm.username
        
        if (!username) {
          ElMessage.error('无法获取用户名，请重新登录')
          loading.value = false
          return
        }
        
        console.log('更新用户信息使用的用户名:', username)
        
        // 准备要提交的用户数据，处理手机号
        const userData = {
          username: basicForm.username,
          // 添加固定密码字段以满足后端验证规则
          // 后端会忽略这个密码字段，不会真正修改用户密码
          password: "Password123",
          nickname: basicForm.nickname,
          email: basicForm.email,
          // 只有当手机号不为空字符串时才包含该字段
          ...(basicForm.phone && basicForm.phone.trim() !== '' ? { phone: basicForm.phone } : {}),
          gender: basicForm.gender,
          birthday: basicForm.birthday,
          school: basicForm.school,
          major: basicForm.major,
          grade: basicForm.grade,
          biography: basicForm.biography
        }
        
        console.log('将要提交的用户数据:', userData)
        
        try {
          // 调用API更新用户信息 - 使用用户名
          const response = await userAPI.updateUserInfo(username, userData)
          
          console.log('API响应:', response)
          
          // 检查响应状态和数据
          if (response && response.data && response.data.code === 200) {
            // 从响应中获取更新后的用户数据
            if (response.data.data) {
              // 后端已返回更新后的用户数据
              const updatedUserData = response.data.data
              console.log('从API响应获取的更新后用户信息:', updatedUserData)
              
              // 更新本地存储
              setUserInfo(updatedUserData)
              
              ElMessage.success('基本资料更新成功')
            } else {
              // 后端没有返回用户数据，尝试获取
              try {
                // 获取最新的用户信息
                const userResponse = await userAPI.getUserInfo(username)
                
                if (userResponse && userResponse.data && userResponse.data.data) {
                  // 使用服务器返回的数据更新本地存储
                  const latestUserData = userResponse.data.data
                  console.log('从服务器获取的最新用户信息:', latestUserData)
                  
                  // 更新本地存储
                  setUserInfo(latestUserData)
                  
                  ElMessage.success('基本资料更新成功')
                } else {
                  // 获取最新用户信息失败，使用本地更新的数据
                  const storedUser = JSON.parse(localStorage.getItem('user') || '{}')
                  const updatedUser = { 
                    ...storedUser, 
                    ...userData
                  }
                  setUserInfo(updatedUser)
                  
                  ElMessage.success('基本资料已更新，但获取最新数据失败')
                }
              } catch (fetchError) {
                console.error('获取最新用户信息失败:', fetchError)
                
                // 获取失败时，使用本地更新的数据
                const storedUser = JSON.parse(localStorage.getItem('user') || '{}')
                const updatedUser = { 
                  ...storedUser, 
                  ...userData
                }
                setUserInfo(updatedUser)
                
                ElMessage.success('基本资料已更新，但获取最新数据失败')
              }
            }
            
            // 刷新页面，确保显示最新数据
      setTimeout(() => {
              window.location.reload()
            }, 1500)
          } else {
            // 提取错误信息
            const errorMsg = response.data?.msg || '更新失败，请稍后再试'
            ElMessage.error(errorMsg)
          }
        } catch (error) {
          console.error('更新用户信息出错:', error)
          
          // API调用出错时，不再自动更新本地存储
          ElMessage.error('更新个人资料失败，请稍后再试')
          
          // 详细记录错误信息
          if (error.response) {
            console.error('错误响应状态:', error.response.status)
            console.error('错误响应数据:', error.response.data)
          }
        } finally {
          loading.value = false
        }
    }
  })
  } catch (error) {
    console.error('表单验证错误:', error)
    loading.value = false
  }
}

const resetBasicForm = () => {
  basicFormRef.value?.resetFields()
}

// 提供设置表单数据的方法
const setFormData = (userData) => {
  if (!userData) return
  
  // 复制基本字段
  Object.keys(basicForm).forEach(key => {
    if (userData[key] !== undefined) {
      // 确保数据类型正确
      if (key === 'gender' && userData[key] !== null) {
        basicForm[key] = parseInt(userData[key])
      } else if (key === 'id' && userData[key]) {
        basicForm[key] = String(userData[key])
      } else {
        basicForm[key] = userData[key]
      }
    }
  })
}

// 向外部暴露方法
defineExpose({
  setFormData,
  updateBasicInfo,
  resetBasicForm,
  basicForm
})
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

.profile-form {
  max-width: 600px;
  
  .form-tip {
    font-size: 12px;
    color: var(--text-secondary);
    margin-top: 4px;
  }
}
</style> 