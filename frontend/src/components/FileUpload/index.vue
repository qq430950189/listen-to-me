<template>
  <div class="file-upload">
    <el-upload
      ref="uploadRef"
      :action="uploadUrl"
      :headers="headers"
      :data="uploadData"
      :show-file-list="false"
      :before-upload="handleBeforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :on-progress="handleProgress"
      :disabled="uploading"
      drag
    >
      <template v-if="!fileUrl">
        <div class="upload-area">
          <el-icon class="upload-icon"><Upload /></el-icon>
          <div class="upload-text">
            <span>将文件拖到此处，或</span>
            <em>点击上传</em>
          </div>
          <div class="upload-tip">
            {{ tip || `支持 ${acceptTypes.join('、')} 格式，最大 ${maxSize}MB` }}
          </div>
        </div>
      </template>
      
      <template v-else>
        <div class="upload-preview">
          <!-- 图片预览 -->
          <template v-if="isImage">
            <el-image :src="fileUrl" fit="contain" class="preview-image" />
          </template>
          
          <!-- 音频预览 -->
          <template v-else-if="isAudio">
            <div class="preview-audio">
              <el-icon><Headset /></el-icon>
              <span>{{ fileName }}</span>
            </div>
          </template>
          
          <!-- 其他文件 -->
          <template v-else>
            <div class="preview-file">
              <el-icon><Document /></el-icon>
              <span>{{ fileName }}</span>
            </div>
          </template>
          
          <!-- 上传进度 -->
          <div v-if="uploading" class="upload-progress">
            <el-progress :percentage="progress" :stroke-width="3" />
          </div>
          
          <!-- 操作按钮 -->
          <div class="preview-actions">
            <el-button type="primary" link @click.stop="handleReUpload">
              重新上传
            </el-button>
          </div>
        </div>
      </template>
    </el-upload>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/store/user'
import { getUploadUrl } from '@/api/common'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  accept: {
    type: String,
    default: 'image/*,audio/*'
  },
  maxSize: {
    type: Number,
    default: 50
  },
  tip: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue', 'success', 'error'])

const userStore = useUserStore()
const uploadRef = ref(null)

const uploadUrl = ref('')
const headers = computed(() => ({
  Authorization: `Bearer ${userStore.token}`
}))
const uploadData = ref({})

const uploading = ref(false)
const progress = ref(0)
const fileUrl = ref(props.modelValue)
const fileName = ref('')

const acceptTypes = computed(() => {
  const types = []
  if (props.accept.includes('image')) types.push('图片')
  if (props.accept.includes('audio')) types.push('音频')
  return types
})

const isImage = computed(() => {
  return fileUrl.value && /\.(jpg|jpeg|png|gif|webp)$/i.test(fileUrl.value)
})

const isAudio = computed(() => {
  return fileUrl.value && /\.(mp3|wav|ogg|m4a)$/i.test(fileUrl.value)
})

watch(() => props.modelValue, (val) => {
  fileUrl.value = val
})

const handleBeforeUpload = async (file) => {
  // 检查文件类型
  const isValidType = props.accept.split(',').some(type => {
    if (type.includes('/*')) {
      const category = type.replace('/*', '')
      return file.type.startsWith(category)
    }
    return file.type === type
  })
  
  if (!isValidType) {
    ElMessage.error('文件类型不支持')
    return false
  }
  
  // 检查文件大小
  const isValidSize = file.size / 1024 / 1024 < props.maxSize
  if (!isValidSize) {
    ElMessage.error(`文件大小不能超过 ${props.maxSize}MB`)
    return false
  }
  
  // 获取上传URL
  try {
    const result = await getUploadUrl(file.name)
    uploadUrl.value = result.uploadUrl
    fileName.value = file.name
    uploading.value = true
    progress.value = 0
    return true
  } catch (error) {
    ElMessage.error('获取上传地址失败')
    return false
  }
}

const handleSuccess = (response) => {
  uploading.value = false
  progress.value = 100
  
  // 使用返回的URL或构造URL
  const url = response?.url || response?.data?.url || uploadUrl.value
  fileUrl.value = url
  
  emit('update:modelValue', url)
  emit('success', { url, fileName: fileName.value })
  
  ElMessage.success('上传成功')
}

const handleError = (error) => {
  uploading.value = false
  progress.value = 0
  
  ElMessage.error('上传失败')
  emit('error', error)
}

const handleProgress = (event) => {
  progress.value = Math.round(event.percent)
}

const handleReUpload = () => {
  fileUrl.value = ''
  fileName.value = ''
  uploadUrl.value = ''
  emit('update:modelValue', '')
}
</script>

<style lang="scss" scoped>
.file-upload {
  :deep(.el-upload) {
    width: 100%;
  }
  
  :deep(.el-upload-dragger) {
    width: 100%;
    border-radius: var(--radius-md);
  }
}

.upload-area {
  padding: var(--spacing-xl);
  text-align: center;
  
  .upload-icon {
    font-size: 48px;
    color: var(--text-placeholder);
    margin-bottom: var(--spacing-md);
  }
  
  .upload-text {
    color: var(--text-secondary);
    
    em {
      color: var(--primary-color);
      font-style: normal;
    }
  }
  
  .upload-tip {
    margin-top: var(--spacing-sm);
    font-size: 12px;
    color: var(--text-placeholder);
  }
}

.upload-preview {
  position: relative;
  padding: var(--spacing-md);
  min-height: 150px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  
  .preview-image {
    max-width: 100%;
    max-height: 200px;
    border-radius: var(--radius-sm);
  }
  
  .preview-audio,
  .preview-file {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: var(--spacing-sm);
    
    .el-icon {
      font-size: 48px;
      color: var(--primary-color);
    }
    
    span {
      color: var(--text-secondary);
    }
  }
  
  .upload-progress {
    width: 80%;
    margin-top: var(--spacing-md);
  }
  
  .preview-actions {
    margin-top: var(--spacing-sm);
  }
}
</style>
