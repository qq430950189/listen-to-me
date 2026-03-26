<template>
  <div class="my-audio-page">
    <div class="page-container">
      <h2 class="page-title">我的音频</h2>
      
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="已购音频" name="purchased" />
      </el-tabs>
      
      <div v-loading="loading" class="audio-grid">
        <div
          v-for="audio in audioList"
          :key="audio.id"
          class="audio-card"
          @click="goToDetail(audio.id)"
        >
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
            <h3 class="audio-title text-ellipsis-2">{{ audio.title }}</h3>
            <div class="audio-meta">
              <span class="creator">{{ audio.creatorName }}</span>
            </div>
          </div>
        </div>
      </div>
      
      <el-empty v-if="!loading && audioList.length === 0" description="暂无已购音频" />
      
      <div v-if="total > 0" class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pageNum"
          :total="total"
          :page-size="pageSize"
          layout="prev, pager, next"
          @current-change="loadData"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyPurchasedAudio } from '@/api/user/audio'

const router = useRouter()

const activeTab = ref('purchased')
const loading = ref(false)
const audioList = ref([])
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const result = await getMyPurchasedAudio({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    
    audioList.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载已购音频失败:', error)
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  pageNum.value = 1
  loadData()
}

const goToDetail = (id) => {
  router.push(`/audio/${id}`)
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.my-audio-page {
  padding: var(--spacing-md) 0;
}

.audio-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: var(--spacing-lg);
  margin-top: var(--spacing-md);
}

.audio-card {
  background: var(--bg-color);
  border-radius: var(--radius-md);
  overflow: hidden;
  cursor: pointer;
  transition: all var(--transition-normal);
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-md);
  }
  
  .audio-cover {
    width: 100%;
    aspect-ratio: 1;
    
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
    padding: var(--spacing-md);
    
    .audio-title {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-primary);
      margin-bottom: var(--spacing-xs);
    }
    
    .audio-meta {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.pagination-wrapper {
  margin-top: var(--spacing-lg);
  text-align: center;
}
</style>
