<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>部门管理</span>
        <el-button type="primary">新增部门</el-button>
      </div>
    </template>
    <el-table :data="depts" row-key="id" default-expand-all>
      <el-table-column prop="name" label="部门名称" />
      <el-table-column prop="leader" label="部门主管" width="120" />
      <el-table-column prop="userCount" label="人数" width="80" />
      <el-table-column prop="sort" label="排序" width="80" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusType">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default>
          <el-button type="primary" link>编辑</el-button>
          <el-button type="primary" link>新增子部门</el-button>
          <el-button type="danger" link>删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
const depts = [
  {
    id: 1, name: '总公司', leader: 'CEO', userCount: 50, sort: 1, status: '启用', statusType: 'success',
    children: [
      {
        id: 2, name: '技术部', leader: '张三', userCount: 15, sort: 1, status: '启用', statusType: 'success',
        children: [
          { id: 5, name: '前端组', leader: '王五', userCount: 6, sort: 1, status: '启用', statusType: 'success' },
          { id: 6, name: '后端组', leader: '赵六', userCount: 8, sort: 2, status: '启用', statusType: 'success' },
        ]
      },
      { id: 3, name: '人事部', leader: '李四', userCount: 5, sort: 2, status: '启用', statusType: 'success' },
      { id: 4, name: '财务部', leader: '钱七', userCount: 4, sort: 3, status: '启用', statusType: 'success' },
    ]
  }
]
</script>
