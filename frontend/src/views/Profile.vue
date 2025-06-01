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
                <el-menu-item index="logs">
                  <el-icon><Clock /></el-icon>
                  <span>登录日志</span>
                </el-menu-item>
                <el-menu-item index="privacy">
                  <el-icon><Setting /></el-icon>
                  <span>隐私设置</span>
                </el-menu-item>
              </el-menu>
            </div>
          </div>
          
          <!-- 右侧内容 -->
          <div class="profile-main">
            <!-- 基本资料 -->
            <div v-if="activeMenu === 'basic'" class="card">
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
            
            <!-- 头像设置 -->
            <div v-if="activeMenu === 'avatar'" class="card">
              <h3 class="section-title">头像设置</h3>
              <div class="avatar-section">
                <div class="current-avatar">
                  <el-avatar :size="120" :src="userInfo.avatar">
                    {{ userInfo.nickname?.charAt(0) }}
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
            
            <!-- 修改密码 -->
            <div v-if="activeMenu === 'password'" class="card">
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
                  <el-button type="primary" @click="changePassword">
                    修改密码
                  </el-button>
                  <el-button @click="resetPasswordForm">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
            
            <!-- 登录日志 -->
            <div v-if="activeMenu === 'logs'" class="card">
              <h3 class="section-title">登录日志</h3>
              <el-table :data="loginLogs" style="width: 100%">
                <el-table-column prop="loginTime" label="登录时间" width="180" />
                <el-table-column prop="ip" label="IP地址" width="150" />
                <el-table-column prop="location" label="登录地点" width="180" />
                <el-table-column prop="device" label="设备信息" />
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="{ row }">
                    <el-tag :type="row.status === '成功' ? 'success' : 'danger'">
                      {{ row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
              
              <div class="pagination-wrapper">
                <el-pagination
                  v-model:current-page="currentPage"
                  :page-size="pageSize"
                  :total="totalLogs"
                  layout="total, prev, pager, next"
                  @current-change="handlePageChange"
                />
              </div>
            </div>
            
            <!-- 隐私设置 -->
            <div v-if="activeMenu === 'privacy'" class="card">
              <h3 class="section-title">隐私设置</h3>
              <div class="privacy-settings">
                <div class="setting-item">
                  <div class="setting-info">
                    <div class="setting-title">个人资料可见性</div>
                    <div class="setting-desc">控制其他用户查看你的个人资料信息</div>
                  </div>
                  <el-select v-model="privacySettings.profileVisibility">
                    <el-option label="所有人可见" value="public" />
                    <el-option label="仅好友可见" value="friends" />
                    <el-option label="仅自己可见" value="private" />
                  </el-select>
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <div class="setting-title">在线状态</div>
                    <div class="setting-desc">是否向其他用户显示你的在线状态</div>
                  </div>
                  <el-switch v-model="privacySettings.showOnlineStatus" />
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <div class="setting-title">接收好友申请</div>
                    <div class="setting-desc">是否允许其他用户向你发送好友申请</div>
                  </div>
                  <el-switch v-model="privacySettings.allowFriendRequests" />
                </div>
                
                <div class="setting-item">
                  <div class="setting-info">
                    <div class="setting-title">消息通知</div>
                    <div class="setting-desc">是否接收系统通知和消息提醒</div>
                  </div>
                  <el-switch v-model="privacySettings.enableNotifications" />
                </div>
                
                <div class="setting-actions">
                  <el-button type="primary" @click="savePrivacySettings">
                    保存设置
                  </el-button>
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import AppHeader from '@/components/Layout/AppHeader.vue'

const activeMenu = ref('basic')

const userInfo = ref({
  username: 'student1',
  nickname: '工大学子',
  email: 'student1@gongda.edu.cn',
  avatar: ''
})

// 基本资料表单
const basicFormRef = ref()
const basicForm = reactive({
  username: 'student1',
  nickname: '工大学子',
  email: 'student1@gongda.edu.cn',
  phone: '13800138001',
  gender: 2,
  birthday: '2000-01-01',
  school: '工业大学',
  major: '计算机科学与技术',
  grade: '大三',
  biography: '热爱学习，喜欢技术分享'
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

// 密码修改表单
const passwordFormRef = ref()
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

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
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 登录日志
const loginLogs = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalLogs = ref(50)

// 隐私设置
const privacySettings = reactive({
  profileVisibility: 'public',
  showOnlineStatus: true,
  allowFriendRequests: true,
  enableNotifications: true
})

// 预设头像
const presetAvatars = ref([
  { id: 1, url: 'https://via.placeholder.com/120/4f46e5/ffffff?text=A' },
  { id: 2, url: 'https://via.placeholder.com/120/06b6d4/ffffff?text=B' },
  { id: 3, url: 'https://via.placeholder.com/120/10b981/ffffff?text=C' },
  { id: 4, url: 'https://via.placeholder.com/120/f59e0b/ffffff?text=D' },
  { id: 5, url: 'https://via.placeholder.com/120/ef4444/ffffff?text=E' },
  { id: 6, url: 'https://via.placeholder.com/120/8b5cf6/ffffff?text=F' }
])

// 模拟登录日志数据
const mockLoginLogs = [
  {
    loginTime: '2024-01-10 10:30:25',
    ip: '192.168.1.100',
    location: '北京市',
    device: 'Chrome 119.0.0.0 / Windows 10',
    status: '成功'
  },
  {
    loginTime: '2024-01-09 08:15:10',
    ip: '192.168.1.100',
    location: '北京市',
    device: 'Chrome 119.0.0.0 / Windows 10',
    status: '成功'
  },
  {
    loginTime: '2024-01-08 22:45:30',
    ip: '192.168.1.101',
    location: '北京市',
    device: 'Safari 17.0 / macOS',
    status: '成功'
  },
  {
    loginTime: '2024-01-08 14:20:15',
    ip: '192.168.1.102',
    location: '上海市',
    device: 'Chrome 119.0.0.0 / Android',
    status: '失败'
  }
]

const handleMenuSelect = (index) => {
  activeMenu.value = index
  
  if (index === 'logs') {
    loadLoginLogs()
  }
}

const updateBasicInfo = async () => {
  if (!basicFormRef.value) return
  
  await basicFormRef.value.validate((valid) => {
    if (valid) {
      // 模拟更新请求
      setTimeout(() => {
        ElMessage.success('基本资料更新成功')
        userInfo.value.nickname = basicForm.nickname
        userInfo.value.email = basicForm.email
      }, 500)
    }
  })
}

const resetBasicForm = () => {
  basicFormRef.value?.resetFields()
}

const changePassword = async () => {
  if (!passwordFormRef.value) return
  
  await passwordFormRef.value.validate((valid) => {
    if (valid) {
      // 模拟修改密码请求
      setTimeout(() => {
        ElMessage.success('密码修改成功')
        resetPasswordForm()
      }, 500)
    }
  })
}

const resetPasswordForm = () => {
  passwordFormRef.value?.resetFields()
}

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
}

const selectPresetAvatar = (url) => {
  userInfo.value.avatar = url
  ElMessage.success('头像设置成功')
}

const loadLoginLogs = () => {
  // 模拟加载登录日志
  loginLogs.value = mockLoginLogs
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadLoginLogs()
}

const savePrivacySettings = () => {
  // 模拟保存隐私设置
  setTimeout(() => {
    ElMessage.success('隐私设置保存成功')
  }, 500)
}

onMounted(() => {
  const user = localStorage.getItem('user')
  if (user) {
    const userData = JSON.parse(user)
    userInfo.value = userData
    Object.assign(basicForm, userData)
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

.password-form {
  max-width: 400px;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: center;
}

.privacy-settings {
  .setting-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 0;
    border-bottom: 1px solid var(--border-color);
    
    &:last-child {
      border-bottom: none;
    }
    
    .setting-info {
      flex: 1;
      margin-right: 20px;
      
      .setting-title {
        font-weight: 500;
        color: var(--text-primary);
        margin-bottom: 4px;
      }
      
      .setting-desc {
        font-size: 13px;
        color: var(--text-secondary);
        line-height: 1.4;
      }
    }
  }
  
  .setting-actions {
    margin-top: 24px;
    text-align: right;
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
  
  .setting-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style> 