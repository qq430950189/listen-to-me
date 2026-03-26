<template>
  <div class="audit-detail">
    <div v-loading="loading" class="detail-card">
      <template v-if="audio">
        <div class="audio-info">
          <el-image
            :src="audio.coverUrl"
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
            <h2>{{ audio.title }}</h2>
            <p>价格：{{ audio.price > 0 ? '¥' + audio.price.toFixed(2) : '免费' }}</p>
            <p>创建时间：{{ audio.createTime }}</p>
          </div>
        </div>
        
        <el-divider />
        
        <div class="audit-form">
          <h3>审核操作</h3>
          
          <el-form :model="auditForm" label-width="100px">
            <el-form-item label="审核结果">
              <el-radio-group v-model="auditForm.auditStatus">
                <el-radio :label="1">通过</el-radio>
                <el-radio :label="2">拒绝</el-radio>
              </el-radio-group>
            </el-form-item>
            
            <el-form-item label="审核意见">
              <el-input
                v-model="auditForm.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入审核意见（选填）"
              />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" :loading="submitting" @click="handleSubmit">
                提交审核
              </el-button>
              <el-button @click="goBack">返回</el-button>
            </el-form-item>
          </el-form>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getAudioDetail } from '@/api/creator/audio'
import { auditAudio } from '@/api/admin/audio'

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const submitting = ref(false)
const audio = ref(null)

const auditForm = reactive({
  audioId: null,
  auditStatus: 1,
  remark: ''
})

const loadDetail = async () => {
  loading.value = true
  try {
    audio.value = await getAudioDetail(route.params.id)
    auditForm.audioId = audio.value.id
  } catch (error) {
    console.error('加载音频详情失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSubmit = async () => {
  submitting.value = true
  try {
    await auditAudio(auditForm)
    ElMessage.success('审核成功')
    goBack()
  } catch (error) {
    console.error('审核失败:', error)
  } finally {
    submitting.value = false
  }
}

const goBack = () => {
  router.push('/admin/audit')
}

onMounted(() => {
  loadDetail()
})
</script>

<style lang="scss" scoped>
.audit-detail {
  max-width: 800px;
  
  .detail-card {
    background: var(--bg-color);
    border-radius: var(--radius-md);
    padding: var(--spacing-xl);
  }
  
  .audio-info {
    display: flex;
    gap: var(--spacing-lg);
    
    .audio-cover {
      width: 120px;
      height: 120px;
      border-radius: var(--radius-md);
      
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
      h2 {
        font-size: 20px;
        margin-bottom: var(--spacing-md);
      }
      
      p {
        color: var(--text-secondary);
        margin-bottom: var(--spacing-xs);
      }
    }
  }
  
  .audit-form {
    h3 {
      margin-bottom: var(--spacing-lg);
    }
  }
}
</style>