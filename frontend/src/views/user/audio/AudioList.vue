<template>
  <div class="audio-list-page">
    <div class="page-container">
      <!-- 搜索和筛选 -->
      <div class="filter-bar">
        <el-input
          v-model="keyword"
          placeholder="搜索音频..."
          clearable
          class="search-input"
          @keyup.enter="handleSearch"
          @clear="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
      </div>
      
      <!-- 音频列表 -->
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
              <span class="views">
                <el-icon><View /></el-icon>
                {{ audio.viewCount || 0 }}
              </span>
            </div>
            
            <div class="audio-price">
              <template v-if="audio.price > 0">
                <span class="price">¥{{ audio.price.toFixed(2) }}</span>
                <span v-if="audio.isPurchased" class="purchased-tag">已购</span>
              </template>
              <template v-else>
                <span class="free">免费</span>
              </template>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 空状态 -->
      <el-empty v-if="!loading && audioList.length === 0" description="暂无音频" />
      
      <!-- 分页 -->
      <div v-if="total > 0" class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pageNum"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[12, 24, 48]"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getAudioPage } from '@/api/user/audio'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const audioList = ref([])
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const result = await getAudioPage({
      keyword: keyword.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    
    audioList.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载音频列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pageNum.value = 1
  loadData()
}

const goToDetail = (id) => {
  router.push(`/audio/${id}`)
}

// 监听路由查询参数
watch(() => route.query.keyword, (val) => {
  keyword.value = val || ''
  pageNum.value = 1
  loadData()
}, { immediate: true })

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.audio-list-page {
  padding: var(--spacing-md) 0;
}

.filter-bar {
  margin-bottom: var(--spacing-lg);
  
  .search-input {
    max-width: 400px;
  }
}

.audio-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: var(--spacing-lg);
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
      
      .el-icon {
        font-size: 48px;
      }
    }
  }
  
  .audio-info {
    padding: var(--spacing-md);
    
    .audio-title {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-primary);
      line-height: 1.4;
      height: 40px;
    }
    
    .audio-meta {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: var(--spacing-sm);
      font-size: 12px;
      color: var(--text-secondary);
      
      .views {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
    
    .audio-price {
      display: flex;
      align-items: center;
      gap: var(--spacing-sm);
      margin-top: var(--spacing-sm);
      
      .price {
        color: #f56c6c;
        font-weight: 600;
      }
      
      .free {
        color: var(--success-color);
        font-weight: 500;
      }
      
      .purchased-tag {
        font-size: 12px;
        color: var(--primary-color);
        background: rgba(64, 158, 255, 0.1);
        padding: 2px 6px;
        border-radius: var(--radius-sm);
      }
    }
  }
}

.pagination-wrapper {
  margin-top: var(--spacing-lg);
  display: flex;
  justify-content: center;
}
</style>
