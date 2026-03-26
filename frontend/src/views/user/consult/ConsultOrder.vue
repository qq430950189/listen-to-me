<template>
  <div class="consult-order-page">
    <div class="page-container">
      <h2 class="page-title">预约咨询</h2>
      
      <div v-loading="loading" class="consult-list">
        <div
          v-for="item in consultList"
          :key="item.id"
          class="consult-item"
        >
          <div class="creator-info">
            <el-avatar :size="48" :src="item.creatorAvatar">
              {{ item.creatorName?.charAt(0) }}
            </el-avatar>
            <div class="creator-meta">
              <h3>{{ item.creatorName }}</h3>
              <p>{{ item.startTime }} - {{ item.endTime }}</p>
            </div>
          </div>
          
          <div class="consult-status">
            <el-tag :type="getStatusType(item.status)">
              {{ getStatusText(item.status) }}
            </el-tag>
          </div>
          
          <div class="consult-actions">
            <el-button
              v-if="item.status === 0"
              type="primary"
              size="small"
              @click="handleBook(item)"
            >
              立即预约
            </el-button>
          </div>
        </div>
      </div>
      
      <el-empty v-if="!loading && consultList.length === 0" description="暂无可预约的咨询" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyConsultOrders, createConsult } from '@/api/user/consult'

const loading = ref(false)
const consultList = ref([])

const loadData = async () => {
  loading.value = true
  try {
    const result = await getMyConsultOrders({})
    consultList.value = result.records || []
  } catch (error) {
    console.error('加载咨询列表失败:', error)
  } finally {
    loading.value = false
  }
}

const getStatusType = (status) => {
  const types = {
    0: 'success',
    1: 'warning',
    2: 'primary',
    3: 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '可预约',
    1: '锁定中',
    2: '已预约',
    3: '已完成'
  }
  return texts[status] || '未知'
}

const handleBook = async (item) => {
  try {
    await createConsult({ slotId: item.id })
    ElMessage.success('预约成功')
    loadData()
  } catch (error) {
    console.error('预约失败:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.consult-order-page {
  padding: var(--spacing-md) 0;
}

.consult-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.consult-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  background: var(--bg-color);
  border-radius: var(--radius-md);
  padding: var(--spacing-md);
  
  .creator-info {
    display: flex;
    align-items: center;
    gap: var(--spacing-md);
    flex: 1;
    
    .creator-meta {
      h3 {
        font-size: 16px;
        color: var(--text-primary);
        margin-bottom: var(--spacing-xs);
      }
      
      p {
        font-size: 12px;
        color: var(--text-secondary);
      }
    }
  }
  
  .consult-actions {
    flex-shrink: 0;
  }
}
</style>
