<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>菜单管理</span>
        <el-button type="primary">新增菜单</el-button>
      </div>
    </template>
    <el-table :data="menus" row-key="id" default-expand-all>
      <el-table-column prop="name" label="菜单名称" />
      <el-table-column prop="icon" label="图标" width="80">
        <template #default="{ row }">
          <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="路由路径" width="180" />
      <el-table-column prop="permission" label="权限标识" width="180" />
      <el-table-column prop="type" label="类型" width="80">
        <template #default="{ row }">
          <el-tag :type="row.type === '目录' ? '' : row.type === '菜单' ? 'success' : 'warning'" size="small">{{ row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排序" width="60" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.statusType" size="small">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default>
          <el-button type="primary" link>编辑</el-button>
          <el-button type="primary" link>新增</el-button>
          <el-button type="danger" link>删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
const menus = [
  {
    id: 1, name: '工作台', icon: 'Odometer', path: '/dashboard', permission: 'dashboard:view', type: '菜单', sort: 1, status: '显示', statusType: 'success',
  },
  {
    id: 2, name: '审批管理', icon: 'Document', path: '', permission: '', type: '目录', sort: 2, status: '显示', statusType: 'success',
    children: [
      { id: 21, name: '提交审批', icon: '', path: '/approval/submit', permission: 'approval:submit', type: '菜单', sort: 1, status: '显示', statusType: 'success' },
      { id: 22, name: '我发起的', icon: '', path: '/approval/mine', permission: 'approval:mine', type: '菜单', sort: 2, status: '显示', statusType: 'success' },
      { id: 23, name: '待我审批', icon: '', path: '/approval/todo', permission: 'approval:todo', type: '菜单', sort: 3, status: '显示', statusType: 'success' },
    ]
  },
  {
    id: 3, name: '考勤管理', icon: 'Location', path: '', permission: '', type: '目录', sort: 3, status: '显示', statusType: 'success',
    children: [
      { id: 31, name: '打卡', icon: '', path: '/attendance/clock', permission: 'attendance:clock', type: '菜单', sort: 1, status: '显示', statusType: 'success' },
      { id: 32, name: '打卡记录', icon: '', path: '/attendance/record', permission: 'attendance:record', type: '菜单', sort: 2, status: '显示', statusType: 'success' },
      { id: 33, name: '请假管理', icon: '', path: '/attendance/leave', permission: 'attendance:leave', type: '菜单', sort: 3, status: '显示', statusType: 'success' },
    ]
  },
  {
    id: 4, name: '系统管理', icon: 'Setting', path: '', permission: '', type: '目录', sort: 9, status: '显示', statusType: 'success',
    children: [
      { id: 41, name: '用户管理', icon: '', path: '/system/user', permission: 'system:user', type: '菜单', sort: 1, status: '显示', statusType: 'success' },
      { id: 42, name: '角色管理', icon: '', path: '/system/role', permission: 'system:role', type: '菜单', sort: 2, status: '显示', statusType: 'success' },
      { id: 43, name: '部门管理', icon: '', path: '/system/dept', permission: 'system:dept', type: '菜单', sort: 3, status: '显示', statusType: 'success' },
      { id: 44, name: '菜单管理', icon: '', path: '/system/menu', permission: 'system:menu', type: '菜单', sort: 4, status: '显示', statusType: 'success' },
    ]
  },
]
</script>
