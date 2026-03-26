<template>
  <div class="audit-list">
    <el-table v-loading="loading" :data="audioList" stripe>
      <el-table-column label="封面" width="100">
        <template #default="{ row }">
          <el-image
            :src="row.coverUrl"
            style="width: 60px; height: 60px"
            fit="cover"
          >
            <template #error>
              <div class="cover-placeholder">
                <el-icon><Headset /></el-icon>
              </div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      
      <el-table-column prop="title" label="标题" min-width="200" />
      
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">
          <span v-if="row.price > 0">¥{{ row.price.toFixed(2) }}</span>
          <span v-else class="free">免费</span>
        </template>
      </el-table-column>
      
      <el-table-column prop="createTime" label="创建时间" width="180" />
      
      <el-table-column prop="auditStatus" label="审核状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getAuditStatusType(row.auditStatus)">
            {{ getAuditStatusText(row.auditStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="goToDetail(row.id)">
            审核
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <div class="pagination-wrapper">
      <el-pagination
        v-model:current-page="pageNum"
        :total="total"
        :page-size="pageSize"
        layout="total, prev, pager, next"
        @current-change="loadData"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAuditAudioPage } from '@/api/admin/audio'

const router = useRouter()

const loading = ref(false)
const audioList = ref([])
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const result = await getAuditAudioPage({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    
    audioList.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载审核列表失败:', error)
  } finally {
    loading.value = false
  }
}

const getAuditStatusType = (status) => {
  const types = { 0: 'warning', 1: 'success', 2: 'danger' }
  return types[status] || 'info'
}

const getAuditStatusText = (status) => {
  const texts = { 0: '待审核', 1: '通过', 2: '违规' }
  return texts[status] || '未知'
}

const goToDetail = (id) => {
  router.push(`/admin/audit/${id}`)
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.audit-list {
  .cover-placeholder {
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: var(--bg-hover);
    color: var(--text-placeholder);
  }
  
  .free {
    color: var(--success-color);
  }
  
  .pagination-wrapper {
    margin-top: var(--spacing-lg);
    display: flex;
    justify-content: flex-end;
  }
}
</style>