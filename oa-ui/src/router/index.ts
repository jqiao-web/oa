import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes: RouteRecordRaw[] = [
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

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, _from, next) => {
  document.title = `${(to.meta.title as string) || ''} - OA协同办公系统`
  const token = getToken()
  if (to.path === '/login') {
    next()
  } else if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router
