<template>
  <div class="creator-audio-list">
    <div class="list-header">
      <el-button type="primary" @click="goToCreate">
        <el-icon><Plus /></el-icon>
        发布音频
      </el-button>
      
      <el-input
        v-model="keyword"
        placeholder="搜索音频..."
        clearable
        style="width: 300px"
        @keyup.enter="handleSearch"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>
    
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
      
      <el-table-column prop="price" label="价格" width="120">
        <template #default="{ row }">
          <span v-if="row.price > 0" class="price">¥{{ row.price.toFixed(2) }}</span>
          <span v-else class="free">免费</span>
        </template>
      </el-table-column>
      
      <el-table-column prop="viewCount" label="播放量" width="100" />
      
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="auditStatus" label="审核状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getAuditStatusType(row.auditStatus)">
            {{ getAuditStatusText(row.auditStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="createTime" label="创建时间" width="180" />
      
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="goToEdit(row.id)">编辑</el-button>
          <el-button type="primary" link @click="handleDelete(row.id)">删除</el-button>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyAudioPage, deleteAudio } from '@/api/creator/audio'

const router = useRouter()

const loading = ref(false)
const audioList = ref([])
const keyword = ref('')
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const result = await getMyAudioPage({
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

const getStatusType = (status) => {
  const types = { 0: 'info', 1: 'warning', 2: 'success' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '草稿', 1: '转码中', 2: '已发布' }
  return texts[status] || '未知'
}

const getAuditStatusType = (status) => {
  const types = { 0: 'warning', 1: 'success', 2: 'danger' }
  return types[status] || 'info'
}

const getAuditStatusText = (status) => {
  const texts = { 0: '待审核', 1: '通过', 2: '违规' }
  return texts[status] || '未知'
}

const goToCreate = () => {
  router.push('/creator/audio/create')
}

const goToEdit = (id) => {
  router.push(`/creator/audio/edit/${id}`)
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该音频吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteAudio(id)
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
.creator-audio-list {
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: var(--spacing-lg);
  }
  
  .cover-placeholder {
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: var(--bg-hover);
    color: var(--text-placeholder);
  }
  
  .price {
    color: #f56c6c;
    font-weight: 500;
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
