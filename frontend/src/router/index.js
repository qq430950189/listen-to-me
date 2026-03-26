import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'

// 路由模块
import appRoutes from './modules/app'
import userRoutes from './modules/user'
import creatorRoutes from './modules/creator'
import adminRoutes from './modules/admin'

const routes = [
  ...appRoutes,
  ...userRoutes,
  ...creatorRoutes,
  ...adminRoutes
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 需要登录的页面
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }
  
  // 需要创作者权限的页面
  if (to.meta.requiresCreator && !userStore.isCreator) {
    next({ name: 'Home' })
    return
  }
  
  // 需要管理员权限的页面
  if (to.meta.requiresAdmin && !userStore.isAdmin) {
    next({ name: 'Home' })
    return
  }
  
  // 已登录用户访问登录页
  if (to.name === 'Login' && userStore.isLoggedIn) {
    next({ name: 'Home' })
    return
  }
  
  next()
})

export default router
