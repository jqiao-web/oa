import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { getUserInfo, type UserInfo, type MenuVO } from '@/api/auth'
import type { RouteRecordRaw } from 'vue-router'

export { type UserInfo, type MenuVO }

// 基础路由（所有用户都能访问）
const baseRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录', hidden: true },
  },
  {
    path: '/',
    component: () => import('@/layouts/DefaultLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '工作台', icon: 'Odometer' },
      },
      // ========== 审批模块 ==========
      {
        path: 'approval/submit',
        name: 'ApprovalSubmit',
        component: () => import('@/views/approval/submit.vue'),
        meta: { title: '提交审批', icon: 'EditPen' },
      },
      {
        path: 'approval/mine',
        name: 'ApprovalMine',
        component: () => import('@/views/approval/mine.vue'),
        meta: { title: '我发起的', icon: 'Document' },
      },
      {
        path: 'approval/todo',
        name: 'ApprovalTodo',
        component: () => import('@/views/approval/todo.vue'),
        meta: { title: '待我审批', icon: 'Clock' },
      },
      {
        path: 'approval/detail/:id',
        name: 'ApprovalDetail',
        component: () => import('@/views/approval/detail.vue'),
        meta: { title: '审批详情', hidden: true },
      },
      // ========== 考勤模块 ==========
      {
        path: 'attendance/clock',
        name: 'AttendanceClock',
        component: () => import('@/views/attendance/clock.vue'),
        meta: { title: '打卡', icon: 'Location' },
      },
      {
        path: 'attendance/record',
        name: 'AttendanceRecord',
        component: () => import('@/views/attendance/record.vue'),
        meta: { title: '打卡记录', icon: 'Calendar' },
      },
      {
        path: 'attendance/leave',
        name: 'AttendanceLeave',
        component: () => import('@/views/attendance/leave.vue'),
        meta: { title: '请假管理', icon: 'Tickets' },
      },
      // ========== 项目模块 ==========
      {
        path: 'project/list',
        name: 'ProjectList',
        component: () => import('@/views/project/list.vue'),
        meta: { title: '项目列表', icon: 'FolderOpened' },
      },
      {
        path: 'project/board/:id',
        name: 'ProjectBoard',
        component: () => import('@/views/project/board.vue'),
        meta: { title: '任务看板', hidden: true },
      },
      // ========== 文档模块 ==========
      {
        path: 'document',
        name: 'Document',
        component: () => import('@/views/document/index.vue'),
        meta: { title: '文档中心', icon: 'Files' },
      },
      // ========== 通知中心 ==========
      {
        path: 'notification',
        name: 'Notification',
        component: () => import('@/views/notification/index.vue'),
        meta: { title: '消息中心', icon: 'Bell' },
      },
      // ========== 系统管理 ==========
      {
        path: 'system/user',
        name: 'SystemUser',
        component: () => import('@/views/system/user.vue'),
        meta: { title: '用户管理', icon: 'User' },
      },
      {
        path: 'system/role',
        name: 'SystemRole',
        component: () => import('@/views/system/role.vue'),
        meta: { title: '角色管理', icon: 'UserFilled' },
      },
      {
        path: 'system/dept',
        name: 'SystemDept',
        component: () => import('@/views/system/dept.vue'),
        meta: { title: '部门管理', icon: 'OfficeBuilding' },
      },
      {
        path: 'system/menu',
        name: 'SystemMenu',
        component: () => import('@/views/system/menu.vue'),
        meta: { title: '菜单管理', icon: 'Menu' },
      },
    ],
  },
]

// 菜单图标映射（后端返回 icon 名称 -> Element Plus 图标组件名）
const iconMap: Record<string, string> = {
  Setting: 'Setting',
  Stamp: 'Stamp',
  Clock: 'Clock',
  Folder: 'Folder',
  Document: 'Document',
  Bell: 'Bell',
  User: 'User',
  UserFilled: 'UserFilled',
  OfficeBuilding: 'OfficeBuilding',
  Menu: 'Menu',
  EditPen: 'EditPen',
  Sent: 'Sent',
  List: 'List',
  Finished: 'Finished',
  Location: 'Location',
  Calendar: 'Calendar',
  Tickets: 'Tickets',
  DataAnalysis: 'DataAnalysis',
  Grid: 'Grid',
  FolderOpened: 'FolderOpened',
  ChatDotRound: 'ChatDotRound',
  Odometer: 'Odometer',
  Files: 'Files',
}

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const userInfo = ref<UserInfo | null>(null)
  const menus = ref<MenuVO[]>([])
  const filteredRoutes = ref<RouteRecordRaw[]>([])
  const dynamicRoutesAdded = ref(false)

  function loginAction(tokenValue: string) {
    token.value = tokenValue
    setToken(tokenValue)
  }

  function setUser(info: UserInfo) {
    userInfo.value = info
    if (info.menus) {
      menus.value = info.menus
    }
  }

  async function fetchUserInfo() {
    const info = await getUserInfo()
    setUser(info)
    return info
  }

  /**
   * 根据菜单权限过滤路由
   * 后端返回的菜单 path 已是完整路径（如 /system/user），直接与路由比较
   */
  function generateRoutes(): RouteRecordRaw[] {
    if (dynamicRoutesAdded.value) {
      return filteredRoutes.value
    }

    // 收集后端返回的所有菜单路径
    const menuPaths = new Set<string>()
    const collectPaths = (menuList: MenuVO[]) => {
      for (const menu of menuList) {
        if (menu.path) {
          menuPaths.add(menu.path)
        }
        if (menu.children && menu.children.length > 0) {
          collectPaths(menu.children)
        }
      }
    }
    collectPaths(menus.value)

    // 过滤路由：保留 dashboard 和匹配的路由
    const mainLayout = baseRoutes.find(r => r.path === '/')
    if (!mainLayout || !mainLayout.children) {
      filteredRoutes.value = [...baseRoutes]
      return filteredRoutes.value
    }

    const filteredChildren = mainLayout.children.filter(child => {
      // 隐藏路由（如详情页）始终保留
      if (child.meta?.hidden) {
        return true
      }
      // dashboard 始终保留
      if (child.path === 'dashboard') {
        return true
      }
      // 后端菜单 path 以 / 开头，需要将前端路由 path 转为完整路径比较
      const fullPath = '/' + child.path
      return menuPaths.has(fullPath)
    })

    const result: RouteRecordRaw[] = [
      baseRoutes[0], // login
      {
        ...mainLayout,
        children: filteredChildren,
      },
    ]

    filteredRoutes.value = result
    dynamicRoutesAdded.value = true
    return result
  }

  /**
   * 生成侧边栏菜单数据（扁平结构，用于 DefaultLayout 渲染）
   */
  function generateMenuItems() {
    return transformMenuToMenuItems(menus.value)
  }

  function logoutAction() {
    token.value = ''
    userInfo.value = null
    menus.value = []
    filteredRoutes.value = []
    dynamicRoutesAdded.value = false
    removeToken()
  }

  /**
   * 将后端菜单结构转换为适合 el-menu 的结构
   * 处理逻辑：
   * - type=1 (目录) -> el-sub-menu
   * - type=2 (菜单) -> el-menu-item
   * - type=3 (按钮) -> 跳过（不显示在菜单中）
   */
  function transformMenuToMenuItems(menuList: MenuVO[]): MenuItem[] {
    const result: MenuItem[] = []
    for (const menu of menuList) {
      // 按钮类型不显示
      if (menu.type === 3) continue
      // 不可见不显示
      if (menu.visible === 0) continue

      const item: MenuItem = {
        path: menu.path || '',
        title: menu.name,
        icon: iconMap[menu.icon] || 'Menu',
        type: menu.type,
      }

      if (menu.children && menu.children.length > 0) {
        const subItems = transformMenuToMenuItems(menu.children)
        if (subItems.length > 0) {
          item.children = subItems
        }
      }

      // 有子菜单时，检查是否有可显示的子项
      if (item.children && item.children.length === 0) {
        delete item.children
      }

      if (item.path || item.children) {
        result.push(item)
      }
    }
    return result
  }

  function resetState() {
    dynamicRoutesAdded.value = false
  }

  return {
    token,
    userInfo,
    menus,
    filteredRoutes,
    dynamicRoutesAdded,
    loginAction,
    setUser,
    fetchUserInfo,
    generateRoutes,
    generateMenuItems,
    logoutAction,
    resetState,
  }
})

export interface MenuItem {
  path: string
  title: string
  icon: string
  type: number
  children?: MenuItem[]
}
