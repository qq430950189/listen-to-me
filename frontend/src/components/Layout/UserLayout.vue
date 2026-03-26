<template>
  <div class="user-layout">
    <!-- 顶部导航 -->
    <header class="user-header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <el-icon><Headset /></el-icon>
          <span>听我说</span>
        </div>
        
        <div class="header-actions">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索音频..."
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          
          <!-- 用户信息 -->
          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :size="32" :src="userStore.avatar">
                  {{ userStore.nickname?.charAt(0) }}
                </el-avatar>
                <span class="nickname">{{ userStore.nickname }}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>
                    个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="myAudio">
                    <el-icon><FolderOpened /></el-icon>
                    我的音频
                  </el-dropdown-item>
                  <el-dropdown-item command="history">
                    <el-icon><Clock /></el-icon>
                    播放历史
                  </el-dropdown-item>
                  <el-dropdown-item v-if="userStore.isCreator" command="creator">
                    <el-icon><Edit /></el-icon>
                    创作者中心
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="$router.push('/login')">登录</el-button>
          </template>
        </div>
      </div>
    </header>
    
    <!-- 主内容区 -->
    <main class="user-main">
      <router-view />
    </main>
    
    <!-- 底部播放器 -->
    <div v-if="playerStore.showPlayer" class="player-bar">
      <AudioPlayer />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { usePlayerStore } from '@/store/player'
import AudioPlayer from '@/components/AudioPlayer/index.vue'

const router = useRouter()
const userStore = useUserStore()
const playerStore = usePlayerStore()

const searchKeyword = ref('')

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/', query: { keyword: searchKeyword.value } })
  }
}

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/my')
      break
    case 'myAudio':
      router.push('/my')
      break
    case 'history':
      router.push('/history')
      break
    case 'creator':
      router.push('/creator')
      break
    case 'logout':
      userStore.logout()
      break
  }
}
</script>

<style lang="scss" scoped>
.user-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-page);
}

.user-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: var(--header-height);
  background: var(--bg-color);
  box-shadow: var(--shadow-sm);
  z-index: 100;
  
  .header-content {
    max-width: 1200px;
    margin: 0 auto;
    height: 100%;
    padding: 0 var(--spacing-md);
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  .logo {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
    font-size: 20px;
    font-weight: 600;
    color: var(--primary-color);
    cursor: pointer;
    
    .el-icon {
      font-size: 28px;
    }
  }
  
  .header-actions {
    display: flex;
    align-items: center;
    gap: var(--spacing-md);
  }
  
  .search-input {
    width: 240px;
  }
  
  .user-info {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
    cursor: pointer;
    
    .nickname {
      font-size: 14px;
      color: var(--text-primary);
    }
  }
}

.user-main {
  flex: 1;
  margin-top: var(--header-height);
  padding-bottom: 80px;
}

.player-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 100;
}
</style>