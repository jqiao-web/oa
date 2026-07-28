<template>
  <el-card>
    <template #header>
      <div style="display: flex; justify-content: space-between; align-items: center">
        <span>打卡记录 - 2026年7月</span>
        <el-date-picker v-model="month" type="month" placeholder="选择月份" style="width: 150px" />
      </div>
    </template>
    <el-table :data="records" stripe>
      <el-table-column prop="date" label="日期" width="120" />
      <el-table-column prop="weekday" label="星期" width="80" />
      <el-table-column prop="clockIn" label="上班打卡" width="120" />
      <el-table-column prop="clockOut" label="下班打卡" width="120" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.statusType">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="workHours" label="工时" width="100" />
      <el-table-column prop="remark" label="备注" />
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const month = ref('2026-07')
const records = [
  { date: '07-28', weekday: '一', clockIn: '08:55', clockOut: '18:02', status: '正常', statusType: 'success', workHours: '9h07m', remark: '' },
  { date: '07-25', weekday: '五', clockIn: '09:02', clockOut: '18:30', status: '正常', statusType: 'success', workHours: '9h28m', remark: '' },
  { date: '07-24', weekday: '四', clockIn: '09:15', clockOut: '18:00', status: '迟到', statusType: 'warning', workHours: '8h45m', remark: '堵车' },
  { date: '07-23', weekday: '三', clockIn: '08:50', clockOut: '20:00', status: '正常', statusType: 'success', workHours: '11h10m', remark: '加班' },
  { date: '07-22', weekday: '二', clockIn: '--', clockOut: '--', status: '请假', statusType: 'info', workHours: '--', remark: '年假' },
  { date: '07-21', weekday: '一', clockIn: '08:58', clockOut: '17:30', status: '早退', statusType: 'danger', workHours: '8h32m', remark: '' },
]
</script>
