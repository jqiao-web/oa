<template>
  <el-row :gutter="20">
    <el-col :span="6">
      <el-card header="文件夹">
        <el-tree :data="folders" default-expand-all highlight-current @node-click="selectFolder" />
      </el-card>
    </el-col>
    <el-col :span="18">
      <el-card>
        <template #header>
          <div style="display: flex; justify-content: space-between; align-items: center">
            <span>{{ currentFolder }}</span>
            <el-button type="primary" size="small">上传文件</el-button>
          </div>
        </template>
        <el-table :data="files" stripe>
          <el-table-column prop="name" label="文件名" />
          <el-table-column prop="type" label="类型" width="100" />
          <el-table-column prop="size" label="大小" width="100" />
          <el-table-column prop="uploader" label="上传者" width="100" />
          <el-table-column prop="time" label="上传时间" width="120" />
          <el-table-column label="操作" width="160">
            <template #default>
              <el-button type="primary" link>预览</el-button>
              <el-button type="primary" link>下载</el-button>
              <el-button type="danger" link>删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const currentFolder = ref('全部文档')
const folders = [
  { label: '全部文档', children: [
    { label: '项目文档' },
    { label: '技术文档' },
    { label: '会议记录' },
    { label: '培训资料' },
  ]}
]

const files = [
  { name: '需求规格说明书v2.0.docx', type: 'Word', size: '2.3MB', uploader: '张三', time: '2026-07-25' },
  { name: '系统架构设计.pdf', type: 'PDF', size: '5.1MB', uploader: '李四', time: '2026-07-23' },
  { name: 'API接口文档.md', type: 'Markdown', size: '120KB', uploader: '王五', time: '2026-07-20' },
  { name: '项目进度表.xlsx', type: 'Excel', size: '890KB', uploader: '赵六', time: '2026-07-18' },
  { name: '会议纪要-0715.docx', type: 'Word', size: '340KB', uploader: '张三', time: '2026-07-15' },
]

function selectFolder(data: { label: string }) {
  currentFolder.value = data.label
}
</script>
