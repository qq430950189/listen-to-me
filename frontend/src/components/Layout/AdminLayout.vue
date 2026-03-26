<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <aside class="admin-sidebar">
      <div class="logo">
        <el-icon><Setting /></el-icon>
        <span>管理后台</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409eff"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon>
          <span>数据大盘</span>
        </el-menu-item>
        <el-menu-item index="/admin/audit">
          <el-icon><Audit /></el-icon>
          <span>内容审核</span>
        </el-menu-item>
        <el-menu-item index="/admin/tags">
          <el-icon><CollectionTag /></el-icon>
          <span>标签管理</span>
        </el-menu-item>
      </el-menu>
      
      <div class="sidebar-footer">
        <el-button text @click="goHome">
          <el-icon><Back /></el-icon>
          返回首页
        </el-button>
      </div>
    </aside>
    
    <!-- 主内容区 -->
    <div class="admin-main">
      <header class="admin-header">
        <h2 class="page-title">{{ pageTitle }}</h2>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <el-avatar :size="32" :src="userStore.avatar">
                {{ userStore.nickname?.charAt(0) }}
              </el-avatar>
              <span>{{ userStore.nickname }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="home">返回首页</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>
      
      <main class="admin-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

const pageTitle = computed(() => route.meta.title || '管理后台')

const goHome = () => {
  router.push('/')
}

const handleCommand = (command) => {
  switch (command) {
    case 'home':
      router.push('/')
      break
    case 'logout':
      userStore.logout()
      break
  }
}
</script>

<style lang="scss" scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
}

.admin-sidebar {
  width: var(--sidebar-width);
  background: #001529;
  display: flex;
  flex-direction: column;
  
  .logo {
    height: var(--header-height);
    display: flex;
    align-items: center;
    justify-content: center;
    gap: var(--spacing-sm);
    color: #fff;
    font-size: 18px;
    font-weight: 600;
    
    .el-icon {
      font-size: 24px;
    }
  }
  
  .el-menu {
    border-right: none;
    flex: 1;
  }
  
  .sidebar-footer {
    padding: var(--spacing-md);
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    
    .el-button {
      color: #fff;
      width: 100%;
      justify-content: flex-start;
    }
  }
}

.admin-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--bg-page);
}

.admin-header {
  height: var(--header-height);
  background: var(--bg-color);
  padding: 0 var(--spacing-lg);
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: var(--shadow-sm);
  
  .page-title {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
  }
  
  .user-info {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
    cursor: pointer;
    color: var(--text-primary);
  }
}

.admin-content {
  flex: 1;
  padding: var(--spacing-lg);
  overflow: auto;
}
</style>
