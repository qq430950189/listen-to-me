<template>
  <div class="slot-list">
    <div class="list-header">
      <h3>咨询时间管理</h3>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        批量生成时间槽
      </el-button>
    </div>
    
    <el-table v-loading="loading" :data="slotList" stripe>
      <el-table-column prop="startTime" label="开始时间" width="180" />
      <el-table-column prop="endTime" label="结束时间" width="180" />
      
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column label="预约用户" min-width="150">
        <template #default="{ row }">
          <span v-if="row.userNickname">{{ row.userNickname }}</span>
          <span v-else class="text-secondary">-</span>
        </template>
      </el-table-column>
      
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button
            v-if="row.status === 0"
            type="primary"
            link
            @click="handleToggleStatus(row)"
          >
            关闭
          </el-button>
          <el-button
            v-else-if="row.status === 3"
            type="primary"
            link
            @click="handleToggleStatus(row)"
          >
            开启
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
    
    <!-- 创建时间槽弹窗 -->
    <el-dialog
      v-model="showCreateDialog"
      title="批量生成时间槽"
      width="500px"
    >
      <el-form :model="createForm" label-width="100px">
        <el-form-item label="选择日期">
          <el-date-picker
            v-model="createForm.dates"
            type="dates"
            placeholder="选择一个或多个日期"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item label="时间段">
          <div class="time-range">
            <el-time-select
              v-model="createForm.startTime"
              placeholder="开始时间"
              :max-time="createForm.endTime"
            />
            <span class="separator">至</span>
            <el-time-select
              v-model="createForm.endTime"
              placeholder="结束时间"
              :min-time="createForm.startTime"
            />
          </div>
        </el-form-item>
        
        <el-form-item label="时长单位">
          <el-select v-model="createForm.duration" style="width: 100%">
            <el-option label="30分钟" :value="30" />
            <el-option label="1小时" :value="60" />
            <el-option label="2小时" :value="120" />
          </el-select>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleCreate">
          生成
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMySlots, createSlotsBatch, updateSlot } from '@/api/creator/consult'

const loading = ref(false)
const slotList = ref([])
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)

const showCreateDialog = ref(false)
const submitting = ref(false)

const createForm = reactive({
  dates: [],
  startTime: '09:00',
  endTime: '18:00',
  duration: 60
})

const loadData = async () => {
  loading.value = true
  try {
    const result = await getMySlots({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    
    slotList.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载时间槽失败:', error)
  } finally {
    loading.value = false
  }
}

const getStatusType = (status) => {
  const types = { 0: 'success', 1: 'warning', 2: 'primary', 3: 'info' }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = { 0: '可预约', 1: '锁定中', 2: '已预约', 3: '已关闭' }
  return texts[status] || '未知'
}

const handleToggleStatus = async (row) => {
  try {
    const newStatus = row.status === 0 ? 3 : 0
    await updateSlot(row.id, { status: newStatus })
    ElMessage.success('操作成功')
    loadData()
  } catch (error) {
    console.error('更新状态失败:', error)
  }
}

const handleCreate = async () => {
  if (!createForm.dates?.length) {
    ElMessage.warning('请选择日期')
    return
  }
  
  submitting.value = true
  try {
    const slots = []
    
    for (const date of createForm.dates) {
      const dateStr = new Date(date).toISOString().split('T')[0]
      slots.push({
        startTime: `${dateStr} ${createForm.startTime}:00`,
        endTime: `${dateStr} ${createForm.endTime}:00`,
        duration: createForm.duration
      })
    }
    
    await createSlotsBatch(slots)
    ElMessage.success('生成成功')
    showCreateDialog.value = false
    loadData()
  } catch (error) {
    console.error('生成时间槽失败:', error)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.slot-list {
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: var(--spacing-lg);
    
    h3 {
      font-size: 18px;
      font-weight: 500;
    }
  }
  
  .time-range {
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
    
    .separator {
      color: var(--text-secondary);
    }
  }
  
  .pagination-wrapper {
    margin-top: var(--spacing-lg);
    display: flex;
    justify-content: flex-end;
  }
}
</style>
