import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from '@/utils/auth'
import router from '@/router'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000,
})

service.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

service.interceptors.response.use(
  (response) => {
    const res = response.data
    // 如果返回的不是标准 R 结构，直接返回
    if (res.code === undefined || res.code === null) {
      return res
    }
    const { code, message, data } = res
    if (code === 200) {
      return data
    }
    if (code === 401) {
      removeToken()
      router.push('/login')
      ElMessage.error('登录已过期，请重新登录')
      return Promise.reject(new Error(message))
    }
    ElMessage.error(message || '请求失败')
    return Promise.reject(new Error(message || String(code)))
  },
  (error) => {
    // 处理 HTTP 错误状态码
    if (error.response?.status === 401) {
      removeToken()
      router.push('/login')
      ElMessage.error('登录已过期，请重新登录')
    } else {
      ElMessage.error(error.response?.data?.message || error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default service
