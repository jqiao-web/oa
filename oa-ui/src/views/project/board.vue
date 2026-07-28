<template>
  <div>
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px">
      <h3 style="margin: 0">OA协同办公系统 - 任务看板</h3>
      <el-button type="primary" @click="addDialog = true">新建任务</el-button>
    </div>
    <el-row :gutter="16">
      <el-col :span="6" v-for="col in columns" :key="col.key">
        <div class="board-column">
          <div class="column-header" :style="{ borderTopColor: col.color }">
            <span>{{ col.title }}</span>
            <el-tag size="small" round>{{ col.tasks.length }}</el-tag>
          </div>
          <div class="task-list">
            <el-card v-for="task in col.tasks" :key="task.id" shadow="hover" class="task-card">
              <div class="task-title">{{ task.title }}</div>
              <div class="task-meta">
                <el-tag :type="task.priorityType" size="small">{{ task.priority }}</el-tag>
                <span class="task-assignee">{{ task.assignee }}</span>
              </div>
              <div class="task-deadline" v-if="task.deadline">截止：{{ task.deadline }}</div>
            </el-card>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-dialog v-model="addDialog" title="新建任务" width="500px">
      <el-form label-width="80px">
        <el-form-item label="任务标题"><el-input v-model="newTask.title" /></el-form-item>
        <el-form-item label="负责人"><el-input v-model="newTask.assignee" /></el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="newTask.priority">
            <el-option label="高" value="高" /><el-option label="中" value="中" /><el-option label="低" value="低" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期"><el-date-picker v-model="newTask.deadline" type="date" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAdd">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const addDialog = ref(false)
const newTask = reactive({ title: '', assignee: '', priority: '中', deadline: '' })

const columns = [
  {
    key: 'todo', title: '待办', color: '#909399',
    tasks: [
      { id: 1, title: '设计数据库表结构', assignee: '张三', priority: '高', priorityType: 'danger', deadline: '07-30' },
      { id: 2, title: '编写API接口文档', assignee: '李四', priority: '中', priorityType: 'warning', deadline: '08-01' },
    ]
  },
  {
    key: 'doing', title: '进行中', color: '#409eff',
    tasks: [
      { id: 3, title: '前端页面开发', assignee: '王五', priority: '高', priorityType: 'danger', deadline: '08-05' },
      { id: 4, title: '后端接口联调', assignee: '赵六', priority: '中', priorityType: 'warning', deadline: '08-03' },
      { id: 5, title: '单元测试编写', assignee: '张三', priority: '低', priorityType: 'info', deadline: '08-10' },
    ]
  },
  {
    key: 'review', title: '待验收', color: '#e6a23c',
    tasks: [
      { id: 6, title: '登录模块验收', assignee: '李四', priority: '中', priorityType: 'warning', deadline: '07-29' },
    ]
  },
  {
    key: 'done', title: '已完成', color: '#67c23a',
    tasks: [
      { id: 7, title: '项目初始化搭建', assignee: '王五', priority: '高', priorityType: 'danger', deadline: '07-20' },
      { id: 8, title: '需求文档评审', assignee: '赵六', priority: '中', priorityType: 'warning', deadline: '07-18' },
    ]
  },
]

function handleAdd() {
  addDialog.value = false
  ElMessage.success('任务已创建（静态模式）')
}
</script>

<style scoped>
.board-column { background: #f5f7fa; border-radius: 8px; min-height: 400px; }
.column-header { padding: 12px 16px; font-weight: bold; display: flex; justify-content: space-between; align-items: center; border-top: 3px solid; border-radius: 8px 8px 0 0; }
.task-list { padding: 8px; }
.task-card { margin-bottom: 8px; cursor: pointer; }
.task-title { font-size: 14px; margin-bottom: 8px; }
.task-meta { display: flex; justify-content: space-between; align-items: center; }
.task-assignee { font-size: 12px; color: #999; }
.task-deadline { font-size: 12px; color: #999; margin-top: 4px; }
</style>
