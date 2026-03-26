<template>
  <div class="order-detail-page">
    <div class="page-container">
      <div v-loading="loading" class="order-card">
        <template v-if="order">
          <div class="order-header">
            <h2>订单详情</h2>
            <el-tag :type="getStatusType(order.payStatus)">
              {{ getStatusText(order.payStatus) }}
            </el-tag>
          </div>
          
          <div class="order-info">
            <div class="info-row">
              <span class="label">订单号：</span>
              <span class="value">{{ order.orderSn }}</span>
            </div>
            <div class="info-row">
              <span class="label">创建时间：</span>
              <span class="value">{{ order.createTime }}</span>
            </div>
            <div class="info-row" v-if="order.payTime">
              <span class="label">支付时间：</span>
              <span class="value">{{ order.payTime }}</span>
            </div>
          </div>
          
          <el-divider />
          
          <div class="audio-info">
            <el-image
              :src="order.audioCover"
              class="audio-cover"
              fit="cover"
            >
              <template #error>
                <div class="cover-placeholder">
                  <el-icon><Headset /></el-icon>
                </div>
              </template>
            </el-image>
            <div class="audio-meta">
              <h3>{{ order.audioTitle }}</h3>
              <p class="price">¥{{ order.payAmount.toFixed(2) }}</p>
            </div>
          </div>
          
          <el-divider />
          
          <div class="pay-info">
            <div class="pay-amount">
              <span>支付金额：</span>
              <span class="amount">¥{{ order.payAmount.toFixed(2) }}</span>
            </div>
          </div>
          
          <div class="order-actions">
            <template v-if="order.payStatus === 0">
              <el-button @click="handleCancel">取消订单</el-button>
              <el-button type="primary" @click="handlePay">立即支付</el-button>
            </template>
            <template v-else-if="order.payStatus === 1">
              <el-button type="primary" @click="goToAudio">去收听</el-button>
            </template>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderDetail, cancelOrder } from '@/api/user/order'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const order = ref(null)

const loadOrder = async () => {
  loading.value = true
  try {
    order.value = await getOrderDetail(route.params.orderSn)
  } catch (error) {
    console.error('加载订单详情失败:', error)
  } finally {
    loading.value = false
  }
}

const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'success',
    2: 'info'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    0: '待支付',
    1: '已支付',
    2: '已取消'
  }
  return texts[status] || '未知'
}

const handleCancel = async () => {
  try {
    await ElMessageBox.confirm('确定要取消订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await cancelOrder(order.value.orderSn)
    ElMessage.success('订单已取消')
    loadOrder()
  } catch (error) {
    // 取消操作
  }
}

const handlePay = () => {
  ElMessage.info('支付功能开发中...')
}

const goToAudio = () => {
  router.push(`/audio/${order.value.audioId}`)
}

onMounted(() => {
  loadOrder()
})
</script>

<style lang="scss" scoped>
.order-detail-page {
  padding: var(--spacing-md) 0;
}

.order-card {
  max-width: 600px;
  margin: 0 auto;
  background: var(--bg-color);
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-lg);
  
  h2 {
    font-size: 20px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

.order-info {
  .info-row {
    display: flex;
    margin-bottom: var(--spacing-sm);
    
    .label {
      color: var(--text-secondary);
      width: 80px;
    }
    
    .value {
      color: var(--text-primary);
    }
  }
}

.audio-info {
  display: flex;
  gap: var(--spacing-md);
  
  .audio-cover {
    width: 80px;
    height: 80px;
    border-radius: var(--radius-sm);
    
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
  
  .audio-meta {
    h3 {
      font-size: 16px;
      color: var(--text-primary);
      margin-bottom: var(--spacing-sm);
    }
    
    .price {
      color: #f56c6c;
      font-size: 18px;
      font-weight: 600;
    }
  }
}

.pay-info {
  .pay-amount {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .amount {
      font-size: 24px;
      font-weight: 600;
      color: #f56c6c;
    }
  }
}

.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-md);
  margin-top: var(--spacing-lg);
}
</style>
