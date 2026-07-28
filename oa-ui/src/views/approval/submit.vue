<template>
  <div>
    <el-card header="选择审批模板">
      <el-row :gutter="16">
        <el-col :span="6" v-for="tpl in templates" :key="tpl.code">
          <el-card shadow="hover" class="tpl-card" @click="selectTemplate(tpl)">
            <el-icon size="36" :color="tpl.color"><component :is="tpl.icon" /></el-icon>
            <div class="tpl-name">{{ tpl.name }}</div>
          </el-card>
        </el-col>
      </el-row>
    </el-card>

    <el-card v-if="selected" :header="selected.name + ' - 填写表单'" style="margin-top: 20px">
      <el-form label-width="100px" style="max-width: 600px">
        <el-form-item v-if="selected.code === 'leave'" label="请假类型">
          <el-select v-model="formData.leaveType" placeholder="请选择">
            <el-option label="年假" value="annual" />
            <el-option label="事假" value="personal" />
            <el-option label="病假" value="sick" />
            <el-option label="调休" value="comp" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="selected.code === 'leave'" label="请假天数">
          <el-input-number v-model="formData.days" :min="0.5" :step="0.5" />
        </el-form-item>
        <el-form-item v-if="selected.code === 'reimburse'" label="报销金额">
          <el-input-number v-model="formData.amount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="审批事由">
          <el-input v-model="formData.reason" type="textarea" :rows="3" placeholder="请填写审批事由" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交审批</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const templates = [
  { name: '请假申请', code: 'leave', icon: 'Calendar', color: '#409eff' },
  { name: '报销申请', code: 'reimburse', icon: 'Wallet', color: '#67c23a' },
  { name: '出差申请', code: 'travel', icon: 'Van', color: '#e6a23c' },
  { name: '采购申请', code: 'purchase', icon: 'ShoppingCart', color: '#f56c6c' },
]

const selected = ref<typeof templates[0] | null>(null)
const formData = reactive({ leaveType: '', days: 1, amount: 0, reason: '' })

function selectTemplate(tpl: typeof templates[0]) {
  selected.value = tpl
}
function handleSubmit() {
  ElMessage.success('审批已提交（静态模式）')
}
</script>

<style scoped>
.tpl-card { text-align: center; cursor: pointer; transition: transform 0.2s; padding: 20px 0; }
.tpl-card:hover { transform: translateY(-4px); }
.tpl-name { margin-top: 8px; font-size: 14px; color: #333; }
</style>
