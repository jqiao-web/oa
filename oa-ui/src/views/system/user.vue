<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>用户管理</span>
        <el-button type="primary" @click="dialogVisible = true">新增用户</el-button>
      </div>
    </template>
    <el-table :data="users" stripe>
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="dept" label="部门" width="120" />
      <el-table-column prop="position" label="职位" width="120" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusType">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="roles" label="角色" />
      <el-table-column label="操作" width="180">
        <template #default>
          <el-button type="primary" link>编辑</el-button>
          <el-button type="primary" link>重置密码</el-button>
          <el-button type="danger" link>禁用</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="新增用户" width="500px">
      <el-form label-width="80px">
        <el-form-item label="用户名"><el-input /></el-form-item>
        <el-form-item label="姓名"><el-input /></el-form-item>
        <el-form-item label="部门"><el-input /></el-form-item>
        <el-form-item label="手机号"><el-input /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogVisible = false">确定</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const dialogVisible = ref(false)
const users = [
  { username: 'admin', realName: '系统管理员', dept: '技术部', position: '技术总监', phone: '13800000001', status: '启用', statusType: 'success', roles: '超级管理员' },
  { username: 'zhangsan', realName: '张三', dept: '技术部', position: '前端开发', phone: '13800000002', status: '启用', statusType: 'success', roles: '普通员工' },
  { username: 'lisi', realName: '李四', dept: '人事部', position: 'HR经理', phone: '13800000003', status: '启用', statusType: 'success', roles: '部门经理' },
  { username: 'wangwu', realName: '王五', dept: '财务部', position: '财务专员', phone: '13800000004', status: '禁用', statusType: 'danger', roles: '普通员工' },
]
</script>
