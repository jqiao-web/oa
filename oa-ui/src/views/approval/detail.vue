<template>
  <el-card header="审批详情">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="审批标题">请假申请 - 年假3天</el-descriptions-item>
      <el-descriptions-item label="申请人">张三</el-descriptions-item>
      <el-descriptions-item label="审批类型">请假申请</el-descriptions-item>
      <el-descriptions-item label="提交时间">2026-07-25 09:30</el-descriptions-item>
      <el-descriptions-item label="当前状态">
        <el-tag type="warning">审批中</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="请假天数">3天</el-descriptions-item>
      <el-descriptions-item label="审批事由" :span="2">家中有事需要处理，申请年假3天</el-descriptions-item>
    </el-descriptions>

    <el-divider content-position="left">审批流程</el-divider>
    <el-timeline>
      <el-timeline-item timestamp="2026-07-25 09:30" placement="top" color="#409eff">
        <el-card>张三 提交申请</el-card>
      </el-timeline-item>
      <el-timeline-item timestamp="2026-07-25 14:00" placement="top" color="#67c23a">
        <el-card>部门经理-李四 同意：同意请假</el-card>
      </el-timeline-item>
      <el-timeline-item timestamp="待审批" placement="top" color="#e6a23c">
        <el-card>HR-王五 待审批</el-card>
      </el-timeline-item>
    </el-timeline>

    <div style="margin-top: 20px">
      <el-button @click="$router.back()">返回</el-button>
    </div>
  </el-card>
</template>
