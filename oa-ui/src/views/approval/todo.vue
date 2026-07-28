<template>
  <el-card>
    <template #header><span>待我审批</span></template>
    <el-table :data="list" stripe>
      <el-table-column prop="title" label="审批标题" />
      <el-table-column prop="applicant" label="申请人" width="100" />
      <el-table-column prop="type" label="类型" width="100" />
      <el-table-column prop="time" label="提交时间" width="120" />
      <el-table-column label="操作" width="160">
        <template #default>
          <el-button type="success" link @click="handleApprove">同意</el-button>
          <el-button type="danger" link @click="handleReject">拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'

const list = [
  { title: '张三的请假申请 - 年假2天', applicant: '张三', type: '请假', time: '2026-07-27' },
  { title: '李四的报销申请 - ¥1,800', applicant: '李四', type: '报销', time: '2026-07-26' },
  { title: '王五的采购申请 - 办公电脑', applicant: '王五', type: '采购', time: '2026-07-25' },
]

function handleApprove() { ElMessage.success('已同意（静态模式）') }
function handleReject() { ElMessage.warning('已拒绝（静态模式）') }
</script>
