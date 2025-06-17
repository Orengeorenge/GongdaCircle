<template>
  <div class="card">
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
</template>

<script setup>
import { ref, onMounted } from 'vue'

const loginLogs = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const totalLogs = ref(50)

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

const loadLoginLogs = () => {
  // 模拟加载登录日志
  loginLogs.value = mockLoginLogs
}

const handlePageChange = (page) => {
  currentPage.value = page
  loadLoginLogs()
}

onMounted(() => {
  loadLoginLogs()
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

.pagination-wrapper {
  margin-top: 20px;
  text-align: center;
}
</style> 