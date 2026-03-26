<template>
  <div class="audio-detail-page">
    <div v-loading="loading" class="page-container">
      <template v-if="audioDetail">
        <!-- 音频信息 -->
        <div class="audio-header">
          <el-image
            :src="audioDetail.coverUrl"
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
            <h1 class="audio-title">{{ audioDetail.title }}</h1>
            
            <div class="audio-meta">
              <div class="creator-info">
                <el-avatar :size="40" :src="audioDetail.creator?.avatar">
                  {{ audioDetail.creator?.nickname?.charAt(0) }}
                </el-avatar>
                <span class="creator-name">{{ audioDetail.creator?.nickname }}</span>
              </div>
              
              <div class="stats">
                <span><el-icon><View /></el-icon> {{ audioDetail.stats?.viewCount || 0 }}</span>
                <span><el-icon><Star /></el-icon> {{ audioDetail.stats?.collectCount || 0 }}</span>
                <span><el-icon><ChatDotRound /></el-icon> {{ audioDetail.stats?.commentCount || 0 }}</span>
              </div>
            </div>
            
            <div class="audio-tags">
              <el-tag
                v-for="tag in audioDetail.tags"
                :key="tag"
                size="small"
                type="info"
              >
                {{ tag }}
              </el-tag>
            </div>
            
            <div class="audio-price">
              <template v-if="audioDetail.price > 0">
                <span class="price">¥{{ audioDetail.price.toFixed(2) }}</span>
                <span v-if="audioDetail.isPurchased" class="purchased-tag">已购买</span>
                <span v-else-if="audioDetail.trialDuration > 0" class="trial-tag">
                  可试听{{ Math.floor(audioDetail.trialDuration / 60) }}分钟
                </span>
              </template>
              <template v-else>
                <span class="free">免费</span>
              </template>
            </div>
            
            <div class="audio-actions">
              <el-button
                type="primary"
                size="large"
                @click="handlePlay"
              >
                <el-icon><VideoPlay /></el-icon>
                {{ audioDetail.isPurchased ? '播放' : '试听' }}
              </el-button>
              
              <el-button
                v-if="!audioDetail.isPurchased && audioDetail.price > 0"
                type="danger"
                size="large"
                @click="handleBuy"
              >
                立即购买
              </el-button>
              
              <el-button size="large" @click="handleCollect">
                <el-icon><Star /></el-icon>
                收藏
              </el-button>
            </div>
          </div>
        </div>
        
        <!-- 购买弹窗 -->
        <el-dialog
          v-model="showBuyDialog"
          title="确认购买"
          width="400px"
        >
          <div class="buy-content">
            <p>音频：{{ audioDetail.title }}</p>
            <p class="buy-price">支付金额：<span>¥{{ audioDetail.price.toFixed(2) }}</span></p>
          </div>
          <template #footer>
            <el-button @click="showBuyDialog = false">取消</el-button>
            <el-button type="primary" :loading="payLoading" @click="confirmBuy">
              确认支付
            </el-button>
          </template>
        </el-dialog>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getAudioDetail } from '@/api/user/audio'
import { createOrder } from '@/api/user/order'
import { usePlayerStore } from '@/store/player'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const playerStore = usePlayerStore()
const userStore = useUserStore()

const loading = ref(false)
const audioDetail = ref(null)
const showBuyDialog = ref(false)
const payLoading = ref(false)

const loadDetail = async () => {
  loading.value = true
  try {
    audioDetail.value = await getAudioDetail(route.params.id)
  } catch (error) {
    console.error('加载音频详情失败:', error)
  } finally {
    loading.value = false
  }
}

const handlePlay = () => {
  playerStore.setCurrentAudio(audioDetail.value)
  playerStore.play()
}

const handleBuy = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  showBuyDialog.value = true
}

const confirmBuy = async () => {
  payLoading.value = true
  try {
    const order = await createOrder({
      audioId: audioDetail.value.id,
      payChannel: 'alipay'
    })
    
    ElMessage.success('订单创建成功')
    showBuyDialog.value = false
    
    // 跳转到订单详情
    router.push(`/order/${order.orderSn}`)
  } catch (error) {
    console.error('创建订单失败:', error)
  } finally {
    payLoading.value = false
  }
}

const handleCollect = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }
  ElMessage.success('收藏成功')
}

onMounted(() => {
  loadDetail()
})
</script>

<style lang="scss" scoped>
.audio-detail-page {
  padding: var(--spacing-md) 0;
}

.audio-header {
  display: flex;
  gap: var(--spacing-xl);
  background: var(--bg-color);
  border-radius: var(--radius-md);
  padding: var(--spacing-xl);
  
  .audio-cover {
    width: 300px;
    height: 300px;
    border-radius: var(--radius-md);
    flex-shrink: 0;
    
    .cover-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--bg-hover);
      color: var(--text-placeholder);
      
      .el-icon {
        font-size: 80px;
      }
    }
  }
  
  .audio-info {
    flex: 1;
    display: flex;
    flex-direction: column;
  }
  
  .audio-title {
    font-size: 24px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: var(--spacing-md);
  }
  
  .audio-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: var(--spacing-md);
    
    .creator-info {
      display: flex;
      align-items: center;
      gap: var(--spacing-sm);
      
      .creator-name {
        font-size: 14px;
        color: var(--text-primary);
      }
    }
    
    .stats {
      display: flex;
      gap: var(--spacing-md);
      font-size: 14px;
      color: var(--text-secondary);
      
      span {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
  }
  
  .audio-tags {
    display: flex;
    gap: var(--spacing-sm);
    margin-bottom: var(--spacing-md);
  }
  
  .audio-price {
    margin-bottom: var(--spacing-lg);
    
    .price {
      font-size: 28px;
      font-weight: 600;
      color: #f56c6c;
    }
    
    .free {
      font-size: 20px;
      font-weight: 600;
      color: var(--success-color);
    }
    
    .purchased-tag,
    .trial-tag {
      margin-left: var(--spacing-sm);
      font-size: 12px;
      padding: 4px 8px;
      border-radius: var(--radius-sm);
    }
    
    .purchased-tag {
      color: var(--primary-color);
      background: rgba(64, 158, 255, 0.1);
    }
    
    .trial-tag {
      color: var(--warning-color);
      background: rgba(230, 162, 60, 0.1);
    }
  }
  
  .audio-actions {
    display: flex;
    gap: var(--spacing-md);
    margin-top: auto;
  }
}

.buy-content {
  .buy-price {
    margin-top: var(--spacing-md);
    font-size: 18px;
    
    span {
      color: #f56c6c;
      font-weight: 600;
    }
  }
}

@media (max-width: 768px) {
  .audio-header {
    flex-direction: column;
    
    .audio-cover {
      width: 100%;
      height: auto;
      aspect-ratio: 1;
    }
  }
}
</style>
