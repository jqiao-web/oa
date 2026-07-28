<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in statCards" :key="item.title">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div>
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.title }}</div>
            </div>
            <el-icon :size="48" :style="{ color: item.color }"><component :is="item.icon" /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card header="待办审批">
          <el-table :data="pendingApprovals" stripe>
            <el-table-column prop="title" label="审批标题" />
            <el-table-column prop="applicant" label="申请人" width="100" />
            <el-table-column prop="time" label="提交时间" width="120" />
            <el-table-column label="操作" width="80">
              <template #default><el-button type="primary" link>审批</el-button></template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="最新通知">
          <div v-for="n in notifications" :key="n.id" class="notify-item">
            <el-tag :type="n.tagType" size="small">{{ n.type }}</el-tag>
            <span class="notify-text">{{ n.content }}</span>
            <span class="notify-time">{{ n.time }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
const statCards = [
  { title: '待审批', value: 3, icon: 'Document', color: '#e6a23c' },
  { title: '我的任务', value: 8, icon: 'List', color: '#409eff' },
  { title: '本月出勤', value: '22天', icon: 'Calendar', color: '#67c23a' },
  { title: '未读消息', value: 5, icon: 'Bell', color: '#f56c6c' },
]

const pendingApprovals = [
  { title: '张三的请假申请', applicant: '张三', time: '10:30' },
  { title: '李四的报销申请', applicant: '李四', time: '09:15' },
  { title: '王五的出差申请', applicant: '王五', time: '昨天' },
]

const notifications = [
  { id: 1, type: '审批', content: '张三提交了请假申请，请审批', time: '10:30', tagType: 'warning' as const },
  { id: 2, type: '任务', content: '您有一个新任务：完成前端开发', time: '09:00', tagType: 'primary' as const },
  { id: 3, type: '系统', content: '系统将于今晚22:00维护', time: '08:00', tagType: 'info' as const },
  { id: 4, type: '考勤', content: '今日上班打卡成功', time: '08:55', tagType: 'success' as const },
]
</script>

<style scoped>
.stat-card { margin-bottom: 0; }
.stat-content { display: flex; justify-content: space-between; align-items: center; }
.stat-value { font-size: 28px; font-weight: bold; color: #333; }
.stat-label { font-size: 14px; color: #999; margin-top: 4px; }
.notify-item { display: flex; align-items: center; gap: 8px; padding: 8px 0; border-bottom: 1px solid #f0f0f0; }
.notify-text { flex: 1; font-size: 14px; }
.notify-time { font-size: 12px; color: #999; }
</style>
