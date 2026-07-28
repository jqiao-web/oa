<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card header="上班打卡">
          <div class="clock-section">
            <div class="time-display">{{ currentTime }}</div>
            <div class="date-display">2026年7月28日 星期一</div>
            <el-button type="primary" size="large" round @click="handleClock('in')" :disabled="clockedIn">
              {{ clockedIn ? '已打卡 ✓' : '上班打卡' }}
            </el-button>
            <div v-if="clockedIn" class="clock-result">
              <el-tag type="success">打卡成功</el-tag>
              <span>打卡时间：08:55:32</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="下班打卡">
          <div class="clock-section">
            <div class="time-display">{{ currentTime }}</div>
            <div class="date-display">2026年7月28日 星期一</div>
            <el-button type="success" size="large" round @click="handleClock('out')" :disabled="clockedOut">
              {{ clockedOut ? '已打卡 ✓' : '下班打卡' }}
            </el-button>
            <div v-if="clockedOut" class="clock-result">
              <el-tag type="success">打卡成功</el-tag>
              <span>打卡时间：18:02:15</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top: 20px" header="今日打卡记录">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="上班打卡">
          <el-tag v-if="clockedIn" type="success">08:55:32 正常</el-tag>
          <el-tag v-else type="info">未打卡</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="下班打卡">
          <el-tag v-if="clockedOut" type="success">18:02:15 正常</el-tag>
          <el-tag v-else type="info">未打卡</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="工作时长">{{ workHours }}</el-descriptions-item>
        <el-descriptions-item label="打卡地点">公司总部（GPS定位）</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'

const currentTime = ref('')
const clockedIn = ref(false)
const clockedOut = ref(false)
const workHours = ref('--')
let timer: number

function updateTime() {
  const now = new Date()
  currentTime.value = now.toLocaleTimeString('zh-CN', { hour12: false })
}

function handleClock(type: 'in' | 'out') {
  if (type === 'in') {
    clockedIn.value = true
    ElMessage.success('上班打卡成功（静态模式）')
  } else {
    clockedOut.value = true
    workHours.value = '9小时6分钟'
    ElMessage.success('下班打卡成功（静态模式）')
  }
}

onMounted(() => {
  updateTime()
  timer = window.setInterval(updateTime, 1000)
})
onUnmounted(() => clearInterval(timer))
</script>

<style scoped>
.clock-section { text-align: center; padding: 20px; }
.time-display { font-size: 48px; font-weight: bold; color: #333; }
.date-display { font-size: 14px; color: #999; margin: 8px 0 24px; }
.clock-result { margin-top: 16px; display: flex; align-items: center; justify-content: center; gap: 8px; }
</style>
