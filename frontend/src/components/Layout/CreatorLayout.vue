<template>
  <div class="creator-layout">
    <!-- 侧边栏 -->
    <aside class="creator-sidebar">
      <div class="logo" @click="$router.push('/')">
        <el-icon><Headset /></el-icon>
        <span>听我说</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#001529"
        text-color="#fff"
        active-text-color="#409eff"
      >
        <el-menu-item index="/creator/audio">
          <el-icon><Headset /></el-icon>
          <span>音频管理</span>
        </el-menu-item>
        <el-menu-item index="/creator/transcript">
          <el-icon><Document /></el-icon>
          <span>AI转写</span>
        </el-menu-item>
        <el-menu-item index="/creator/finance">
          <el-icon><Wallet /></el-icon>
          <span>收益概览</span>
        </el-menu-item>
        <el-menu-item index="/creator/finance/log">
          <el-icon><List /></el-icon>
          <span>账单流水</span>
        </el-menu-item>
        <el-menu-item index="/creator/slots">
          <el-icon><Calendar /></el-icon>
          <span>咨询管理</span>
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
    <div class="creator-main">
      <header class="creator-header">
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
      
      <main class="creator-content">
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

const pageTitle = computed(() => route.meta.title || '创作者中心')

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
.creator-layout {
  display: flex;
  min-height: 100vh;
}

.creator-sidebar {
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
    cursor: pointer;
    
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

.creator-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--bg-page);
}

.creator-header {
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

.creator-content {
  flex: 1;
  padding: var(--spacing-lg);
  overflow: auto;
}
</style>
