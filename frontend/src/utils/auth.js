// 认证工具函数
import { useUserStore } from '../store/user'

// 检查是否登录
export const isLoggedIn = () => {
  const userStore = useUserStore()
  return userStore.isLoggedIn
}

// 检查是否是创作者
export const isCreator = () => {
  const userStore = useUserStore()
  return userStore.isCreator
}

// 获取Token
export const getToken = () => {
  const userStore = useUserStore()
  return userStore.token
}

// 获取用户ID
export const getUserId = () => {
  const userStore = useUserStore()
  return userStore.userId
}

// 需要登录
export const requireAuth = (to, from, next) => {
  if (isLoggedIn()) {
    next()
  } else {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  }
}

// 需要创作者权限
export const requireCreator = (to, from, next) => {
  const userStore = useUserStore()
  if (userStore.isLoggedIn && userStore.isCreator) {
    next()
  } else if (!userStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else {
    next({ name: 'Home' })
  }
}

export default {
  isLoggedIn,
  isCreator,
  getToken,
  getUserId,
  requireAuth,
  requireCreator
}
