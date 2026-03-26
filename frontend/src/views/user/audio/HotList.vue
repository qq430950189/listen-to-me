<template>
  <div class="hot-list-page">
    <div class="page-container">
      <h2 class="page-title">热门榜单</h2>
      
      <div v-loading="loading" class="hot-list">
        <div
          v-for="(audio, index) in audioList"
          :key="audio.id"
          class="hot-item"
          @click="goToDetail(audio.id)"
        >
          <div class="rank" :class="getRankClass(index)">
            {{ index + 1 }}
          </div>
          
          <el-image
            :src="audio.coverUrl"
            class="audio-cover"
            fit="cover"
          >
            <template #error>
              <div class="cover-placeholder">
                <el-icon><Headset /></el-icon>
              </div>
            </template>
          </el-image>
          
          <div class="audio-info">
            <h3 class="audio-title text-ellipsis">{{ audio.title }}</h3>
            <div class="audio-meta">
              <span class="creator">{{ audio.creatorName }}</span>
              <span class="views">
                <el-icon><View /></el-icon>
                {{ audio.viewCount || 0 }}
              </span>
            </div>
          </div>
          
          <div class="audio-price">
            <template v-if="audio.price > 0">
              <span class="price">¥{{ audio.price.toFixed(2) }}</span>
            </template>
            <template v-else>
              <span class="free">免费</span>
            </template>
          </div>
        </div>
      </div>
      
      <el-empty v-if="!loading && audioList.length === 0" description="暂无热门音频" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHotAudioList } from '@/api/user/audio'

const router = useRouter()

const loading = ref(false)
const audioList = ref([])

const loadData = async () => {
  loading.value = true
  try {
    audioList.value = await getHotAudioList(20)
  } catch (error) {
    console.error('加载热门音频失败:', error)
  } finally {
    loading.value = false
  }
}

const getRankClass = (index) => {
  if (index === 0) return 'first'
  if (index === 1) return 'second'
  if (index === 2) return 'third'
  return ''
}

const goToDetail = (id) => {
  router.push(`/audio/${id}`)
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.hot-list-page {
  padding: var(--spacing-md) 0;
}

.hot-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.hot-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  background: var(--bg-color);
  border-radius: var(--radius-md);
  padding: var(--spacing-md);
  cursor: pointer;
  transition: all var(--transition-normal);
  
  &:hover {
    transform: translateX(4px);
    box-shadow: var(--shadow-md);
  }
  
  .rank {
    width: 32px;
    height: 32px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 600;
    border-radius: var(--radius-sm);
    background: var(--bg-hover);
    color: var(--text-secondary);
    
    &.first {
      background: #ff4d4f;
      color: #fff;
    }
    
    &.second {
      background: #ff7a45;
      color: #fff;
    }
    
    &.third {
      background: #ffa940;
      color: #fff;
    }
  }
  
  .audio-cover {
    width: 64px;
    height: 64px;
    border-radius: var(--radius-sm);
    flex-shrink: 0;
    
    .cover-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--bg-hover);
      color: var(--text-placeholder);
    }
  }
  
  .audio-info {
    flex: 1;
    min-width: 0;
    
    .audio-title {
      font-size: 16px;
      font-weight: 500;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .audio-meta {
      display: flex;
      gap: var(--spacing-md);
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
  
  .audio-price {
    .price {
      color: #f56c6c;
      font-weight: 600;
    }
    
    .free {
      color: var(--success-color);
    }
  }
}
</style>
