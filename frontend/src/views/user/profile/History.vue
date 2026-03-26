<template>
  <div class="history-page">
    <div class="page-container">
      <h2 class="page-title">播放历史</h2>
      
      <div v-loading="loading" class="history-list">
        <div
          v-for="item in historyList"
          :key="item.id"
          class="history-item"
          @click="goToDetail(item.audioId)"
        >
          <el-image
            :src="item.coverUrl"
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
            <h3 class="audio-title text-ellipsis">{{ item.audioTitle }}</h3>
            <div class="audio-meta">
              <span>播放进度: {{ formatTime(item.lastPosition) }}</span>
            </div>
            <div class="audio-meta">
              <span>{{ item.updateTime }}</span>
            </div>
          </div>
          
          <el-button text @click.stop="handleDelete(item.id)">
            <el-icon><Delete /></el-icon>
          </el-button>
        </div>
      </div>
      
      <el-empty v-if="!loading && historyList.length === 0" description="暂无播放历史" />
      
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPlayHistory } from '@/api/user/audio'

const router = useRouter()

const loading = ref(false)
const historyList = ref([])
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)

const formatTime = (seconds) => {
  if (!seconds) return '00:00'
  const m = Math.floor(seconds / 60)
  const s = Math.floor(seconds % 60)
  return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
}

const loadData = async () => {
  loading.value = true
  try {
    const result = await getPlayHistory({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    
    historyList.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载播放历史失败:', error)
  } finally {
    loading.value = false
  }
}

const goToDetail = (id) => {
  router.push(`/audio/${id}`)
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这条播放记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // TODO: 调用删除接口
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    // 取消删除
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.history-page {
  padding: var(--spacing-md) 0;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.history-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  background: var(--bg-color);
  border-radius: var(--radius-md);
  padding: var(--spacing-md);
  cursor: pointer;
  transition: all var(--transition-normal);
  
  &:hover {
    background: var(--bg-hover);
  }
  
  .audio-cover {
    width: 80px;
    height: 80px;
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
      font-size: 12px;
      color: var(--text-secondary);
      margin-top: var(--spacing-xs);
    }
  }
}

.pagination-wrapper {
  margin-top: var(--spacing-lg);
  text-align: center;
}
</style>
