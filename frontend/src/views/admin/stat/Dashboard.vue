<template>
  <div class="admin-dashboard">
    <div class="stat-cards">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon" style="background: #e6f7ff">
            <el-icon style="color: #1890ff"><Money /></el-icon>
          </div>
          <div class="stat-info">
            <span class="label">总销售额</span>
            <span class="value">¥{{ dashboard.totalSales?.toFixed(2) || '0.00' }}</span>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon" style="background: #f6ffed">
            <el-icon style="color: #52c41a"><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <span class="label">今日销售额</span>
            <span class="value">¥{{ dashboard.todaySales?.toFixed(2) || '0.00' }}</span>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon" style="background: #fff7e6">
            <el-icon style="color: #fa8c16"><User /></el-icon>
          </div>
          <div class="stat-info">
            <span class="label">活跃用户</span>
            <span class="value">{{ dashboard.activeUsers || 0 }}</span>
          </div>
        </div>
      </el-card>
      
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-icon" style="background: #f9f0ff">
            <el-icon style="color: #722ed1"><DataAnalysis /></el-icon>
          </div>
          <div class="stat-info">
            <span class="label">转化率</span>
            <span class="value">{{ (dashboard.conversionRate * 100)?.toFixed(2) || '0.00' }}%</span>
          </div>
        </div>
      </el-card>
    </div>
    
    <el-card class="recent-orders">
      <template #header>
        <span>最近订单</span>
      </template>
      
      <el-table :data="recentOrders" stripe>
        <el-table-column prop="orderSn" label="订单号" width="200" />
        <el-table-column prop="username" label="用户" width="120" />
        <el-table-column prop="audioTitle" label="音频" min-width="200" />
        <el-table-column prop="payAmount" label="金额" width="120">
          <template #default="{ row }">
            ¥{{ row.payAmount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="payTime" label="支付时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboard } from '@/api/admin/stat'

const dashboard = ref({})
const recentOrders = ref([])

const loadDashboard = async () => {
  try {
    const result = await getDashboard()
    dashboard.value = result || {}
    recentOrders.value = result?.recentOrders || []
  } catch (error) {
    console.error('加载数据大盘失败:', error)
  }
}

onMounted(() => {
  loadDashboard()
})
</script>

<style lang="scss" scoped>
.admin-dashboard {
  .stat-cards {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: var(--spacing-lg);
    margin-bottom: var(--spacing-lg);
  }
  
  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
      gap: var(--spacing-md);
    }
    
    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: var(--radius-md);
      display: flex;
      align-items: center;
      justify-content: center;
      
      .el-icon {
        font-size: 24px;
      }
    }
    
    .stat-info {
      .label {
        display: block;
        font-size: 12px;
        color: var(--text-secondary);
        margin-bottom: 4px;
      }
      
      .value {
        font-size: 20px;
        font-weight: 600;
        color: var(--text-primary);
      }
    }
  }
  
  .recent-orders {
    margin-top: var(--spacing-lg);
  }
}
</style>