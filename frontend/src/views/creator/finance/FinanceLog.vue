<template>
  <div class="finance-log">
    <div class="filter-bar">
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @change="handleSearch"
      />
    </div>
    
    <el-table v-loading="loading" :data="logList" stripe>
      <el-table-column prop="createTime" label="时间" width="180" />
      
      <el-table-column prop="type" label="类型" width="120">
        <template #default="{ row }">
          <el-tag :type="getTypeTag(row.type)">
            {{ getTypeText(row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      
      <el-table-column prop="amount" label="金额" width="120">
        <template #default="{ row }">
          <span :class="row.amount >= 0 ? 'income' : 'expense'">
            {{ row.amount >= 0 ? '+' : '' }}{{ row.amount?.toFixed(2) }}
          </span>
        </template>
      </el-table-column>
      
      <el-table-column prop="balance" label="余额" width="120">
        <template #default="{ row }">
          ¥{{ row.balance?.toFixed(2) }}
        </template>
      </el-table-column>
      
      <el-table-column prop="remark" label="备注" min-width="200" />
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
import { getFinanceLogPage } from '@/api/creator/finance'

const loading = ref(false)
const logList = ref([])
const dateRange = ref([])
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    
    if (dateRange.value?.length === 2) {
      params.startDate = dateRange.value[0].toISOString()
      params.endDate = dateRange.value[1].toISOString()
    }
    
    const result = await getFinanceLogPage(params)
    logList.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载账单流水失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pageNum.value = 1
  loadData()
}

const getTypeTag = (type) => {
  const tags = {
    income: 'success',
    withdraw: 'warning',
    refund: 'danger'
  }
  return tags[type] || 'info'
}

const getTypeText = (type) => {
  const texts = {
    income: '收入',
    withdraw: '提现',
    refund: '退款'
  }
  return texts[type] || '未知'
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.finance-log {
  .filter-bar {
    margin-bottom: var(--spacing-lg);
  }
  
  .income {
    color: #52c41a;
    font-weight: 500;
  }
  
  .expense {
    color: #f5222d;
    font-weight: 500;
  }
  
  .pagination-wrapper {
    margin-top: var(--spacing-lg);
    display: flex;
    justify-content: flex-end;
  }
}
</style>
