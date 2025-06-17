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
        <el-button type="primary" @click="updateBasicInfo">
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

const basicFormRef = ref()
const basicForm = reactive({
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
  
  await basicFormRef.value.validate((valid) => {
    if (valid) {
      // 模拟更新请求
      setTimeout(() => {
        ElMessage.success('基本资料更新成功')
      }, 500)
    }
  })
}

const resetBasicForm = () => {
  basicFormRef.value?.resetFields()
}

// 提供设置表单数据的方法
const setFormData = (userData) => {
  Object.assign(basicForm, userData)
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