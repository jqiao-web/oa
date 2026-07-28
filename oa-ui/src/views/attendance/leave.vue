<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>请假管理</span>
        <el-button type="primary" @click="dialogVisible = true">申请请假</el-button>
      </div>
    </template>
    <el-table :data="list" stripe>
      <el-table-column prop="type" label="请假类型" width="100" />
      <el-table-column prop="days" label="天数" width="80" />
      <el-table-column prop="startDate" label="开始日期" width="120" />
      <el-table-column prop="endDate" label="结束日期" width="120" />
      <el-table-column prop="reason" label="请假事由" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.statusType">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="请假申请" width="500px">
      <el-form label-width="80px">
        <el-form-item label="请假类型">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option label="年假" value="annual" />
            <el-option label="事假" value="personal" />
            <el-option label="病假" value="sick" />
            <el-option label="调休" value="comp" />
          </el-select>
        </el-form-item>
        <el-form-item label="请假天数">
          <el-input-number v-model="form.days" :min="0.5" :step="0.5" />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="请假事由">
          <el-input v-model="form.reason" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitLeave">提交</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const dialogVisible = ref(false)
const form = reactive({ type: '', days: 1, startDate: '', reason: '' })

const list = [
  { type: '年假', days: 3, startDate: '2026-08-01', endDate: '2026-08-03', reason: '家庭旅行', status: '已批准', statusType: 'success' },
  { type: '事假', days: 1, startDate: '2026-07-22', endDate: '2026-07-22', reason: '办理个人事务', status: '已批准', statusType: 'success' },
  { type: '病假', days: 2, startDate: '2026-07-10', endDate: '2026-07-11', reason: '感冒发烧', status: '已批准', statusType: 'success' },
]

function submitLeave() {
  dialogVisible.value = false
  ElMessage.success('请假申请已提交（静态模式）')
}
</script>
