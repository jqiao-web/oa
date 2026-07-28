<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>角色管理</span>
        <el-button type="primary">新增角色</el-button>
      </div>
    </template>
    <el-table :data="roles" stripe>
      <el-table-column prop="name" label="角色名称" width="150" />
      <el-table-column prop="code" label="角色标识" width="150" />
      <el-table-column prop="desc" label="描述" />
      <el-table-column prop="userCount" label="用户数" width="80" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusType">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default>
          <el-button type="primary" link>编辑</el-button>
          <el-button type="warning" link>分配权限</el-button>
          <el-button type="danger" link>删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
const roles = [
  { name: '超级管理员', code: 'super_admin', desc: '拥有系统所有权限', userCount: 1, status: '启用', statusType: 'success' },
  { name: '部门经理', code: 'dept_manager', desc: '管理本部门数据和审批', userCount: 5, status: '启用', statusType: 'success' },
  { name: '普通员工', code: 'employee', desc: '基本操作权限', userCount: 20, status: '启用', statusType: 'success' },
  { name: 'HR', code: 'hr', desc: '考勤和人事管理权限', userCount: 3, status: '启用', statusType: 'success' },
]
</script>
