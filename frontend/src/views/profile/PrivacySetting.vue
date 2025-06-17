<template>
  <div class="card">
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
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'

// 隐私设置
const privacySettings = reactive({
  profileVisibility: 'public',
  showOnlineStatus: true,
  allowFriendRequests: true,
  enableNotifications: true
})

const savePrivacySettings = () => {
  // 模拟保存隐私设置
  setTimeout(() => {
    ElMessage.success('隐私设置保存成功')
  }, 500)
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
  .setting-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style> 