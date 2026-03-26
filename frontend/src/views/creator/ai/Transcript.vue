<template>
  <div class="transcript-page">
    <div class="transcript-header">
      <h3>AI 转写服务</h3>
      <p>使用 AI 自动将音频转换为文字，生成智能分段标题</p>
    </div>
    
    <el-card class="transcript-form">
      <el-form label-width="100px">
        <el-form-item label="选择音频">
          <el-select
            v-model="selectedAudioId"
            placeholder="请选择要转写的音频"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="audio in audioList"
              :key="audio.id"
              :label="audio.title"
              :value="audio.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            :loading="submitting"
            :disabled="!selectedAudioId"
            @click="handleRequestTranscript"
          >
            申请 AI 转写
          </el-button>
          <el-button
            :disabled="!selectedAudioId"
            @click="handleRequestNote"
          >
            申请 AI 摘要
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card v-if="currentTask" class="task-status">
      <template #header>
        <div class="card-header">
          <span>任务状态</span>
          <el-tag :type="getTaskStatusType(currentTask.status)">
            {{ getTaskStatusText(currentTask.status) }}
          </el-tag>
        </div>
      </template>
      
      <div v-if="currentTask.status === 2" class="transcript-result">
        <h4>转写文本</h4>
        <div class="full-text">
          {{ currentTask.fullText }}
        </div>
        
        <h4 v-if="currentTask.segments?.length">分段标题</h4>
        <div v-if="currentTask.segments?.length" class="segments">
          <div
            v-for="(segment, index) in currentTask.segments"
            :key="index"
            class="segment-item"
          >
            <span class="time">{{ formatTime(segment.time) }}</span>
            <span class="title">{{ segment.title }}</span>
          </div>
        </div>
      </div>
      
      <el-progress
        v-else
        :percentage="currentTask.progress || 0"
        :status="currentTask.status === 3 ? 'exception' : ''"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyAudioPage } from '@/api/creator/audio'
import { requestTranscript, requestNote, getAiTaskStatus } from '@/api/creator/ai'

const audioList = ref([])
const selectedAudioId = ref(null)
const submitting = ref(false)
const currentTask = ref(null)

const loadAudioList = async () => {
  try {
    const result = await getMyAudioPage({ pageNum: 1, pageSize: 100 })
    audioList.value = result.records || []
  } catch (error) {
    console.error('加载音频列表失败:', error)
  }
}

const handleRequestTranscript = async () => {
  if (!selectedAudioId.value) return
  
  submitting.value = true
  try {
    const taskId = await requestTranscript(selectedAudioId.value)
    ElMessage.success('已提交转写任务')
    
    // 轮询任务状态
    pollTaskStatus(taskId)
  } catch (error) {
    console.error('提交转写任务失败:', error)
  } finally {
    submitting.value = false
  }
}

const handleRequestNote = async () => {
  if (!selectedAudioId.value) return
  
  submitting.value = true
  try {
    const taskId = await requestNote(selectedAudioId.value)
    ElMessage.success('已提交摘要任务')
    
    pollTaskStatus(taskId)
  } catch (error) {
    console.error('提交摘要任务失败:', error)
  } finally {
    submitting.value = false
  }
}

const pollTaskStatus = async (taskId) => {
  const poll = async () => {
    try {
      const task = await getAiTaskStatus(taskId)
      currentTask.value = task
      
      if (task.status === 1) {
        setTimeout(poll, 2000)
      }
    } catch (error) {
      console.error('查询任务状态失败:', error)
    }
  }
  
  poll()
}

const getTaskStatusType = (status) => {
  const types = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
  return types[status] || 'info'
}

const getTaskStatusText = (status) => {
  const texts = { 0: '待处理', 1: '处理中', 2: '已完成', 3: '失败' }
  return texts[status] || '未知'
}

const formatTime = (seconds) => {
  if (!seconds) return '00:00'
  const m = Math.floor(seconds / 60)
  const s = Math.floor(seconds % 60)
  return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
}

onMounted(() => {
  loadAudioList()
})
</script>

<style lang="scss" scoped>
.transcript-page {
  .transcript-header {
    margin-bottom: var(--spacing-lg);
    
    h3 {
      font-size: 18px;
      color: var(--text-primary);
      margin-bottom: var(--spacing-sm);
    }
    
    p {
      color: var(--text-secondary);
    }
  }
  
  .transcript-form {
    margin-bottom: var(--spacing-lg);
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .transcript-result {
    .full-text {
      padding: var(--spacing-md);
      background: var(--bg-page);
      border-radius: var(--radius-sm);
      margin-bottom: var(--spacing-lg);
      white-space: pre-wrap;
      line-height: 1.8;
    }
    
    h4 {
      margin-bottom: var(--spacing-md);
      color: var(--text-primary);
    }
    
    .segments {
      .segment-item {
        display: flex;
        gap: var(--spacing-md);
        padding: var(--spacing-sm) 0;
        border-bottom: 1px solid var(--border-lighter);
        
        .time {
          color: var(--primary-color);
          font-family: monospace;
        }
        
        .title {
          color: var(--text-primary);
        }
      }
    }
  }
}
</style>
