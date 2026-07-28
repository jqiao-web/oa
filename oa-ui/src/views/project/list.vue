<template>
  <div>
    <div style="display: flex; justify-content: space-between; margin-bottom: 20px">
      <el-input v-model="search" placeholder="搜索项目" style="width: 300px" clearable prefix-icon="Search" />
      <el-button type="primary" @click="createDialog = true">创建项目</el-button>
    </div>
    <el-row :gutter="20">
      <el-col :span="8" v-for="p in projects" :key="p.id">
        <el-card shadow="hover" class="project-card" @click="$router.push(`/project/board/${p.id}`)">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center">
              <span class="project-name">{{ p.name }}</span>
              <el-tag :type="p.statusType" size="small">{{ p.status }}</el-tag>
            </div>
          </template>
          <p class="project-desc">{{ p.desc }}</p>
          <div class="project-meta">
            <span><el-icon><User /></el-icon> {{ p.members }}人</span>
            <span><el-icon><Document /></el-icon> {{ p.tasks }}个任务</span>
            <span>{{ p.deadline }}</span>
          </div>
          <el-progress :percentage="p.progress" :color="p.progressColor" style="margin-top: 12px" />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="createDialog" title="创建项目" width="500px">
      <el-form label-width="80px">
        <el-form-item label="项目名称"><el-input v-model="newProject.name" /></el-form-item>
        <el-form-item label="项目描述"><el-input v-model="newProject.desc" type="textarea" /></el-form-item>
        <el-form-item label="截止日期"><el-date-picker v-model="newProject.deadline" type="date" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialog = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const search = ref('')
const createDialog = ref(false)
const newProject = reactive({ name: '', desc: '', deadline: '' })

const projects = [
  { id: 1, name: 'OA协同办公系统', desc: '企业内部协同办公平台开发', members: 8, tasks: 24, deadline: '2026-09-30', status: '进行中', statusType: 'primary', progress: 65, progressColor: '#409eff' },
  { id: 2, name: '电商小程序', desc: '微信小程序电商项目开发', members: 5, tasks: 16, deadline: '2026-08-15', status: '进行中', statusType: 'primary', progress: 40, progressColor: '#67c23a' },
  { id: 3, name: '数据大屏', desc: '企业数据可视化大屏', members: 3, tasks: 8, deadline: '2026-07-31', status: '已完成', statusType: 'success', progress: 100, progressColor: '#67c23a' },
]

function handleCreate() {
  createDialog.value = false
  ElMessage.success('项目已创建（静态模式）')
}
</script>

<style scoped>
.project-card { margin-bottom: 20px; cursor: pointer; }
.project-card:hover { transform: translateY(-2px); }
.project-name { font-weight: bold; }
.project-desc { color: #666; font-size: 13px; margin: 0; }
.project-meta { display: flex; gap: 16px; font-size: 12px; color: #999; margin-top: 8px; }
.project-meta span { display: flex; align-items: center; gap: 4px; }
</style>
