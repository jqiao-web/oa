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
          <template v-for="item in menuItems" :key="item.path">
            <!-- 菜单（type=2 或无子目录的目录） -->
            <el-menu-item v-if="isMenuLeaf(item)" :index="item.path">
              <el-icon v-if="item.icon">
                <component :is="item.icon" />
              </el-icon>
              <template #title>{{ item.title }}</template>
            </el-menu-item>
            <!-- 子菜单（目录 type=1） -->
            <el-sub-menu v-else :index="item.path">
              <template #title>
                <el-icon v-if="item.icon">
                  <component :is="item.icon" />
                </el-icon>
                <span>{{ item.title }}</span>
              </template>
              <el-menu-item
                v-for="child in item.children"
                :key="child.path"
                :index="child.path"
              >
                <el-icon v-if="child.icon">
                  <component :is="child.icon" />
                </el-icon>
                <template #title>{{ child.title }}</template>
              </el-menu-item>
            </el-sub-menu>
          </template>
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
            <el-breadcrumb-item v-if="currentTitle">{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="notify-badge">
            <el-icon size="20" @click="$router.push('/notification')"><Bell /></el-icon>
          </el-badge>
          <el-dropdown trigger="click">
            <span class="user-info">
              <el-avatar :size="32" :src="userStore.userInfo?.avatar">
                {{ userStore.userInfo?.realName?.charAt(0) || 'U' }}
              </el-avatar>
              <span class="username">{{ userStore.userInfo?.realName || userStore.userInfo?.username || '用户' }}</span>
              <el-icon class="arrow"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item disabled>
                  <div class="user-info-panel">
                    <div class="user-name">{{ userStore.userInfo?.realName }}</div>
                    <div class="user-dept">{{ userStore.userInfo?.deptName }}</div>
                  </div>
                </el-dropdown-item>
                <el-dropdown-item divided>
                  <el-icon><User /></el-icon>个人中心
                </el-dropdown-item>
                <el-dropdown-item @click="handleLogout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
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
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore, type MenuItem } from '@/stores/user'
import { useNotificationStore } from '@/stores/notification'
import { logout as logoutApi } from '@/api/auth'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const notificationStore = useNotificationStore()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => (route.meta.title as string) || '')
const unreadCount = computed(() => notificationStore.unreadCount)

// 动态菜单：根据后端返回的菜单数据生成
const menuItems = computed<MenuItem[]>(() => {
  return userStore.generateMenuItems()
})

// 判断菜单项是否为叶子节点
function isMenuLeaf(item: MenuItem): boolean {
  return !item.children || item.children.length === 0
}

async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })

    // 调用后端登出接口
    try {
      if (userStore.token) {
        await logoutApi(userStore.token)
      }
    } catch {
      // 即使后端调用失败，也清除本地状态
    }

    userStore.logoutAction()
    ElMessage.success('已退出登录')
    router.push('/login')
  } catch {
    // 用户取消退出
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  border-bottom: 1px solid #3a4a5d;
}
.logo-text {
  white-space: nowrap;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #eee;
  padding: 0 20px;
  background: #fff;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.collapse-btn {
  cursor: pointer;
  color: #606266;
  transition: color 0.2s;
}
.collapse-btn:hover {
  color: #667eea;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}
.notify-badge {
  cursor: pointer;
}
.notify-badge :deep(.el-badge__content) {
  top: 8px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 6px;
  transition: background 0.2s;
}
.user-info:hover {
  background: #f5f7fa;
}
.username {
  font-size: 14px;
  color: #333;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.arrow {
  font-size: 12px;
  color: #999;
}
.main {
  background: #f5f7fa;
  padding: 20px;
}
.el-menu {
  border-right: none;
}
.user-info-panel {
  padding: 4px 0;
}
.user-name {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
}
.user-dept {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>
