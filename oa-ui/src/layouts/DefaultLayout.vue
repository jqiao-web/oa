<template>
  <el-container class="layout-container">
    <!-- 左侧菜单 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo">
        <el-icon size="24"><Odometer /></el-icon>
        <span v-show="!isCollapse" class="logo-text">OA 协同办公</span>
      </div>
      <el-scrollbar>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409eff"
        >
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <template #title>工作台</template>
          </el-menu-item>

          <el-sub-menu index="approval">
            <template #title><el-icon><Document /></el-icon><span>审批管理</span></template>
            <el-menu-item index="/approval/submit">提交审批</el-menu-item>
            <el-menu-item index="/approval/mine">我发起的</el-menu-item>
            <el-menu-item index="/approval/todo">待我审批</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="attendance">
            <template #title><el-icon><Location /></el-icon><span>考勤管理</span></template>
            <el-menu-item index="/attendance/clock">打卡</el-menu-item>
            <el-menu-item index="/attendance/record">打卡记录</el-menu-item>
            <el-menu-item index="/attendance/leave">请假管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="project">
            <template #title><el-icon><FolderOpened /></el-icon><span>项目管理</span></template>
            <el-menu-item index="/project/list">项目列表</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="/document">
            <el-icon><Files /></el-icon>
            <template #title>文档中心</template>
          </el-menu-item>

          <el-menu-item index="/notification">
            <el-icon><Bell /></el-icon>
            <template #title>消息中心</template>
          </el-menu-item>

          <el-sub-menu index="system">
            <template #title><el-icon><Setting /></el-icon><span>系统管理</span></template>
            <el-menu-item index="/system/user">用户管理</el-menu-item>
            <el-menu-item index="/system/role">角色管理</el-menu-item>
            <el-menu-item index="/system/dept">部门管理</el-menu-item>
            <el-menu-item index="/system/menu">菜单管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <!-- 右侧内容区 -->
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse" size="20">
            <Fold v-if="!isCollapse" /><Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="notify-badge">
            <el-icon size="20" @click="$router.push('/notification')"><Bell /></el-icon>
          </el-badge>
          <el-dropdown trigger="click">
            <span class="user-info">
              <el-avatar :size="32" />
              <span class="username">{{ userStore.userInfo?.realName || '管理员' }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useNotificationStore } from '@/stores/notification'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const notificationStore = useNotificationStore()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => (route.meta.title as string) || '')
const unreadCount = computed(() => notificationStore.unreadCount)

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout-container { height: 100vh; }
.aside { background-color: #304156; transition: width 0.3s; overflow-x: hidden; }
.logo { height: 60px; display: flex; align-items: center; justify-content: center; gap: 8px; color: #fff; font-size: 16px; font-weight: bold; border-bottom: 1px solid #3a4a5d; }
.logo-text { white-space: nowrap; }
.header { display: flex; align-items: center; justify-content: space-between; border-bottom: 1px solid #eee; padding: 0 20px; }
.header-left { display: flex; align-items: center; gap: 12px; }
.collapse-btn { cursor: pointer; }
.header-right { display: flex; align-items: center; gap: 20px; }
.notify-badge { cursor: pointer; }
.user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.username { font-size: 14px; color: #333; }
.main { background: #f5f7fa; }
.el-menu { border-right: none; }
</style>
