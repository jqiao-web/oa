<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>我发起的审批</span>
        <el-input v-model="search" placeholder="搜索审批" style="width: 200px" clearable />
      </div>
    </template>
    <el-table :data="list" stripe>
      <el-table-column prop="title" label="审批标题" />
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.statusType">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="提交时间" width="120" />
      <el-table-column label="操作" width="80">
        <template #default>
          <el-button type="primary" link @click="$router.push('/approval/detail/1')">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const search = ref('')
const list = [
  { title: '我的请假申请 - 年假3天', type: '请假', status: '审批中', statusType: 'warning', time: '2026-07-25' },
  { title: '我的报销申请 - ¥2,500', type: '报销', status: '已通过', statusType: 'success', time: '2026-07-20' },
  { title: '我的出差申请 - 上海', type: '出差', status: '已拒绝', statusType: 'danger', time: '2026-07-15' },
  { title: '我的请假申请 - 事假1天', type: '请假', status: '已通过', statusType: 'success', time: '2026-07-10' },
]
</script>
