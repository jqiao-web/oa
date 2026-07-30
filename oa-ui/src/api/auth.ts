import request from '@/utils/request'

export interface LoginDTO {
  username: string
  password: string
}

export interface LoginVO {
  token: string
  expiresIn: number
  userInfo: UserInfo
}

export interface MenuVO {
  id: number
  parentId: number
  name: string
  type: number
  path: string
  component: string
  permission: string
  icon: string
  sort: number
  visible: number
  children: MenuVO[]
}

export interface UserInfo {
  userId: number
  username: string
  realName: string
  avatar: string
  deptId: number
  deptName: string
  permissions: string[]
  roles: string[]
  menus: MenuVO[]
}

export function login(data: LoginDTO) {
  return request.post('/auth/login', data) as Promise<LoginVO>
}

export function logout(token: string) {
  return request.post('/auth/logout', null, {
    headers: { Authorization: `Bearer ${token}` },
  }) as Promise<void>
}

export function getUserInfo() {
  return request.get('/auth/userinfo') as Promise<UserInfo>
}
