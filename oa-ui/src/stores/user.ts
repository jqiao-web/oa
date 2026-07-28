import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getToken, setToken, removeToken } from '@/utils/auth'

export interface UserInfo {
  userId: number
  username: string
  realName: string
  avatar: string
  permissions: string[]
  roles: string[]
}

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const userInfo = ref<UserInfo | null>(null)

  function login(tokenValue: string) {
    token.value = tokenValue
    setToken(tokenValue)
  }

  function setUser(info: UserInfo) {
    userInfo.value = info
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    removeToken()
  }

  return { token, userInfo, login, setUser, logout }
})
