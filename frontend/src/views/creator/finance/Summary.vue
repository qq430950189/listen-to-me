<template>
  <div class="finance-summary">
    <div class="summary-cards">
      <el-card class="summary-card">
        <div class="card-content">
          <div class="card-icon" style="background: #e6f7ff">
            <el-icon style="color: #1890ff"><Wallet /></el-icon>
          </div>
          <div class="card-info">
            <span class="label">总收益</span>
            <span class="value">¥{{ summary.totalIncome?.toFixed(2) || '0.00' }}</span>
          </div>
        </div>
      </el-card>
      
      <el-card class="summary-card">
        <div class="card-content">
          <div class="card-icon" style="background: #f6ffed">
            <el-icon style="color: #52c41a"><Money /></el-icon>
          </div>
          <div class="card-info">
            <span class="label">可提现余额</span>
            <span class="value">¥{{ summary.withdrawableBalance?.toFixed(2) || '0.00' }}</span>
          </div>
        </div>
      </el-card>
      
      <el-card class="summary-card">
        <div class="card-content">
          <div class="card-icon" style="background: #fff7e6">
            <el-icon style="color: #fa8c16"><Lock /></el-icon>
          </div>
          <div class="card-info">
            <span class="label">冻结金额</span>
            <span class="value">¥{{ summary.frozenBalance?.toFixed(2) || '0.00' }}</span>
          </div>
        </div>
      </el-card>
      
      <el-card class="summary-card">
        <div class="card-content">
          <div class="card-icon" style="background: #f9f0ff">
            <el-icon style="color: #722ed1"><Document /></el-icon>
          </div>
          <div class="card-info">
            <span class="label">已结算订单</span>
            <span class="value">{{ summary.settledOrderCount || 0 }} 笔</span>
          </div>
        </div>
      </el-card>
    </div>
    
    <el-card class="withdraw-card">
      <template #header>
        <div class="card-header">
          <span>提现申请</span>
        </div>
      </template>
      
      <el-form :model="withdrawForm" label-width="100px">
        <el-form-item label="可提现金额">
          <span class="withdraw-amount">¥{{ summary.withdrawableBalance?.toFixed(2) || '0.00' }}</span>
        </el-form-item>
        
        <el-form-item label="提现金额">
          <el-input-number
            v-model="withdrawForm.amount"
            :min="0"
            :max="summary.withdrawableBalance || 0"
            :precision="2"
            :step="100"
          />
        </el-form-item>
        
        <el-form-item label="提现渠道">
          <el-radio-group v-model="withdrawForm.channel">
            <el-radio label="alipay">支付宝</el-radio>
            <el-radio label="wechat">微信</el-radio>
            <el-radio label="bank">银行卡</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="submitting"
            :disabled="!withdrawForm.amount || withdrawForm.amount <= 0"
            @click="handleWithdraw"
          >
            申请提现
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getFinanceSummary, requestWithdraw } from '@/api/creator/finance'

const summary = ref({})
const submitting = ref(false)

const withdrawForm = reactive({
  amount: 0,
  channel: 'alipay'
})

const loadSummary = async () => {
  try {
    summary.value = await getFinanceSummary()
  } catch (error) {
    console.error('加载收益概览失败:', error)
  }
}

const handleWithdraw = async () => {
  if (!withdrawForm.amount || withdrawForm.amount <= 0) {
    ElMessage.warning('请输入提现金额')
    return
  }
  
  submitting.value = true
  try {
    await requestWithdraw(withdrawForm)
    ElMessage.success('提现申请已提交')
    withdrawForm.amount = 0
    loadSummary()
  } catch (error) {
    console.error('提现申请失败:', error)
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadSummary()
})
</script>

<style lang="scss" scoped>
.finance-summary {
  .summary-cards {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: var(--spacing-lg);
    margin-bottom: var(--spacing-lg);
  }
  
  .summary-card {
    .card-content {
      display: flex;
      align-items: center;
      gap: var(--spacing-md);
    }
    
    .card-icon {
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
    
    .card-info {
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
  
  .withdraw-card {
    .card-header {
      font-size: 16px;
      font-weight: 500;
    }
    
    .withdraw-amount {
      font-size: 24px;
      font-weight: 600;
      color: var(--primary-color);
    }
  }
}
</style>
