<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>消息中心</span>
        <el-radio-group v-model="filter" size="small">
          <el-radio-button label="all">全部</el-radio-button>
          <el-radio-button label="unread">未读</el-radio-button>
          <el-radio-button label="read">已读</el-radio-button>
        </el-radio-group>
      </div>
    </template>
    <div v-for="n in filteredList" :key="n.id" class="notify-item" :class="{ unread: !n.read }">
      <div class="notify-icon">
        <el-icon :size="24" :color="n.color"><component :is="n.icon" /></el-icon>
      </div>
      <div class="notify-content">
        <div class="notify-title">{{ n.title }}</div>
        <div class="notify-text">{{ n.content }}</div>
        <div class="notify-time">{{ n.time }}</div>
      </div>
      <el-tag v-if="!n.read" type="danger" size="small" round>未读</el-tag>
    </div>
    <el-empty v-if="filteredList.length === 0" description="暂无消息" />
  </el-card>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const filter = ref('all')

const notifications = [
  { id: 1, title: '审批通知', content: '张三提交了请假申请（年假3天），请及时审批', time: '10分钟前', icon: 'Document', color: '#e6a23c', read: false },
  { id: 2, title: '任务分配', content: '您被分配了新任务：完成前端页面开发', time: '1小时前', icon: 'List', color: '#409eff', read: false },
  { id: 3, title: '系统通知', content: '系统将于今晚22:00-23:00进行维护升级', time: '2小时前', icon: 'Setting', color: '#909399', read: false },
  { id: 4, title: '考勤提醒', content: '今日上班打卡成功，打卡时间08:55', time: '今天 08:55', icon: 'Location', color: '#67c23a', read: true },
  { id: 5, title: '审批结果', content: '您的报销申请（¥2,500）已通过', time: '昨天', icon: 'CircleCheck', color: '#67c23a', read: true },
  { id: 6, title: '项目更新', content: 'OA协同办公系统 项目新增了3个任务', time: '昨天', icon: 'FolderOpened', color: '#409eff', read: true },
]

const filteredList = computed(() => {
  if (filter.value === 'unread') return notifications.filter(n => !n.read)
  if (filter.value === 'read') return notifications.filter(n => n.read)
  return notifications
})
</script>

<style scoped>
.notify-item { display: flex; align-items: center; gap: 12px; padding: 16px; border-bottom: 1px solid #f0f0f0; }
.notify-item.unread { background: #f0f7ff; }
.notify-icon { flex-shrink: 0; }
.notify-content { flex: 1; }
.notify-title { font-weight: bold; font-size: 14px; margin-bottom: 4px; }
.notify-text { font-size: 13px; color: #666; }
.notify-time { font-size: 12px; color: #999; margin-top: 4px; }
</style>
