<template>
  <div class="login-page">
    <!-- 动态背景粒子 -->
    <div class="bg-animation">
      <div v-for="i in 20" :key="i" class="particle" :style="getParticleStyle(i)"></div>
    </div>

    <!-- 左侧品牌展示区 -->
    <div class="brand-panel">
      <div class="brand-content">
        <div class="brand-logo">
          <el-icon :size="48" color="#fff"><Odometer /></el-icon>
        </div>
        <h1 class="brand-title">OA Cloud</h1>
        <p class="brand-subtitle">智慧协同办公平台</p>
        <ul class="brand-features">
          <li><el-icon><Check /></el-icon>一站式办公解决方案</li>
          <li><el-icon><Check /></el-icon>多租户权限管理</li>
          <li><el-icon><Check /></el-icon>微服务架构支持</li>
        </ul>
        <div class="brand-decoration">
          <div class="circle circle-1"></div>
          <div class="circle circle-2"></div>
          <div class="circle circle-3"></div>
        </div>
      </div>
    </div>

    <!-- 右侧登录表单区 -->
    <div class="login-panel">
      <div class="login-form-wrapper">
        <div class="form-header">
          <h2 class="form-title">欢迎回来</h2>
          <p class="form-subtitle">请登录您的账户以继续</p>
        </div>

        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          size="large"
          @keyup.enter="handleLogin"
          class="login-form"
        >
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入账号"
              :prefix-icon="User"
              clearable
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-form-item>
            <div class="form-options">
              <el-checkbox v-model="form.remember">记住我</el-checkbox>
              <a class="forgot-link" @click.prevent="handleForgot">忘记密码?</a>
            </div>
          </el-form-item>

          <el-button
            type="primary"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登录中...</span>
          </el-button>

          <div class="register-link">
            <span>还没有账号?</span>
            <a>立即注册</a>
          </div>
        </el-form>

        <div class="form-footer">
          <p>© 2025 OA Cloud. All rights reserved.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Check } from '@element-plus/icons-vue'
import type { FormInstance } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { login } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  remember: false,
})

const rules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 50, message: '账号长度 3-50 位', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 100, message: '密码长度 6-100 位', trigger: 'blur' },
  ],
}

function getParticleStyle(index: number) {
  const size = Math.random() * 6 + 2
  return {
    width: `${size}px`,
    height: `${size}px`,
    left: `${Math.random() * 100}%`,
    top: `${Math.random() * 100}%`,
    animationDuration: `${Math.random() * 20 + 10}s`,
    animationDelay: `${Math.random() * 10}s`,
  }
}

async function handleLogin() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }

  loading.value = true
  try {
    const result = await login({
      username: form.username,
      password: form.password,
    })

    // 只保存 token，userInfo 由路由守卫通过 fetchUserInfo() 获取
    // 因为 login 接口返回的 userInfo 不含 menus，需要调用 /auth/userinfo 才能拿到完整菜单树
    userStore.loginAction(result.token)

    ElMessage.success('登录成功，欢迎回来！')

    // 保存到 localStorage
    if (form.remember) {
      localStorage.setItem('oa_remember', form.username)
    } else {
      localStorage.removeItem('oa_remember')
    }

    // 跳转到首页或指定页
    const redirect = (router.currentRoute.value.query.redirect as string) || '/dashboard'
    router.push(redirect)
  } catch (error: any) {
    // 错误已由 request 拦截器统一处理
  } finally {
    loading.value = false
  }
}

function handleForgot() {
  ElMessage.info('请联系系统管理员重置密码')
}

// 自动填充记住的账号
const remembered = localStorage.getItem('oa_remember')
if (remembered) {
  form.username = remembered
  form.remember = true
}
</script>

<style scoped>
.login-page {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  position: relative;
}

/* 动态背景 */
.bg-animation {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
  z-index: 0;
}

.particle {
  position: absolute;
  background: rgba(102, 126, 234, 0.3);
  border-radius: 50%;
  animation: float 15s infinite ease-in-out;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.3;
  }
  25% {
    transform: translateY(-30px) translateX(20px) scale(1.1);
    opacity: 0.6;
  }
  50% {
    transform: translateY(-10px) translateX(-20px) scale(0.9);
    opacity: 0.4;
  }
  75% {
    transform: translateY(-40px) translateX(10px) scale(1.05);
    opacity: 0.5;
  }
}

/* 左侧品牌区 */
.brand-panel {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #6b8dd6 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  z-index: 1;
}

.brand-content {
  position: relative;
  z-index: 2;
  color: #fff;
  padding: 60px;
}

.brand-logo {
  width: 72px;
  height: 72px;
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 32px;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.brand-title {
  font-size: 42px;
  font-weight: 700;
  margin: 0 0 12px 0;
  letter-spacing: 2px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.brand-subtitle {
  font-size: 18px;
  margin: 0 0 40px 0;
  opacity: 0.9;
  font-weight: 300;
  letter-spacing: 4px;
}

.brand-features {
  list-style: none;
  padding: 0;
  margin: 0;
}

.brand-features li {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  font-size: 15px;
  opacity: 0.85;
}

.brand-features li .el-icon {
  color: #4ade80;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  padding: 4px;
}

/* 装饰圆圈 */
.brand-decoration .circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -80px;
  right: -80px;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -60px;
  left: -60px;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 40%;
  right: 10%;
  opacity: 0.5;
}

/* 右侧登录区 */
.login-panel {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  position: relative;
  z-index: 1;
}

.login-form-wrapper {
  width: 420px;
  max-width: 90%;
}

.form-header {
  margin-bottom: 36px;
}

.form-title {
  font-size: 32px;
  font-weight: 700;
  color: #1a202c;
  margin: 0 0 8px 0;
}

.form-subtitle {
  font-size: 15px;
  color: #718096;
  margin: 0;
}

.login-form {
  margin-bottom: 16px;
}

.login-form :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: all 0.3s;
}

.login-form :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.15);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.25);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.forgot-link {
  color: #667eea;
  cursor: pointer;
  font-size: 14px;
  transition: color 0.2s;
}

.forgot-link:hover {
  color: #764ba2;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 4px;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s;
}

.login-btn:hover {
  background: linear-gradient(135deg, #5f6fd9 0%, #6a4290 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

.register-link {
  text-align: center;
  margin-top: 16px;
  font-size: 14px;
  color: #718096;
}

.register-link a {
  color: #667eea;
  cursor: pointer;
  margin-left: 6px;
  font-weight: 500;
}

.register-link a:hover {
  text-decoration: underline;
}

.form-footer {
  text-align: center;
  margin-top: 40px;
  font-size: 12px;
  color: #a0aec0;
}

/* 响应式 */
@media (max-width: 768px) {
  .login-page {
    flex-direction: column;
  }

  .brand-panel {
    flex: none;
    padding: 40px 20px;
    min-height: 280px;
  }

  .brand-content {
    padding: 0;
    text-align: center;
  }

  .brand-logo {
    margin: 0 auto 16px;
  }

  .brand-features {
    display: none;
  }

  .brand-title {
    font-size: 28px;
  }

  .login-panel {
    flex: 1;
    padding: 40px 20px;
  }
}
</style>
