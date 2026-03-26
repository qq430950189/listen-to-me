<template>
  <div class="creator-audio-edit">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="rules"
      label-width="100px"
      class="audio-form"
    >
      <el-form-item label="标题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入音频标题" maxlength="100" show-word-limit />
      </el-form-item>
      
      <el-form-item label="封面">
        <FileUpload
          v-model="formData.coverUrl"
          accept="image/*"
          :max-size="5"
          tip="支持 jpg、png 格式，最大 5MB"
        />
      </el-form-item>
      
      <el-form-item label="音频文件" prop="rawPath">
        <FileUpload
          v-model="formData.rawPath"
          accept="audio/*"
          :max-size="100"
          tip="支持 mp3、wav 格式，最大 100MB"
        />
      </el-form-item>
      
      <el-form-item label="价格" prop="price">
        <el-input-number
          v-model="formData.price"
          :min="0"
          :precision="2"
          :step="1"
          placeholder="设置为0表示免费"
        />
        <span class="form-tip">设置为0表示免费音频</span>
      </el-form-item>
      
      <el-form-item label="试听时长">
        <el-input-number
          v-model="formData.trialDuration"
          :min="0"
          :max="600"
          placeholder="秒"
        />
        <span class="form-tip">单位：秒，0表示不可试听</span>
      </el-form-item>
      
      <el-form-item label="状态">
        <el-radio-group v-model="formData.status">
          <el-radio :label="0">保存草稿</el-radio>
          <el-radio :label="2">立即发布</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          {{ isEdit ? '保存' : '发布' }}
        </el-button>
        <el-button @click="handleCancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createAudio, updateAudio, getAudioDetail } from '@/api/creator/audio'
import FileUpload from '@/components/FileUpload/index.vue'

const route = useRoute()
const router = useRouter()

const formRef = ref(null)
const submitting = ref(false)

const isEdit = computed(() => !!route.params.id)

const formData = reactive({
  id: null,
  title: '',
  coverUrl: '',
  rawPath: '',
  price: 0,
  trialDuration: 0,
  status: 0,
  tagIds: []
})

const rules = {
  title: [
    { required: true, message: '请输入音频标题', trigger: 'blur' }
  ],
  rawPath: [
    { required: true, message: '请上传音频文件', trigger: 'change' }
  ]
}

const loadDetail = async () => {
  if (!route.params.id) return
  
  try {
    const detail = await getAudioDetail(route.params.id)
    Object.assign(formData, {
      id: detail.id,
      title: detail.title,
      coverUrl: detail.coverUrl,
      rawPath: detail.rawPath,
      price: detail.price,
      trialDuration: detail.trialDuration,
      status: detail.status
    })
  } catch (error) {
    console.error('加载音频详情失败:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      if (isEdit.value) {
        await updateAudio(formData)
        ElMessage.success('保存成功')
      } else {
        await createAudio(formData)
        ElMessage.success('发布成功')
      }
      router.push('/creator/audio')
    } catch (error) {
      console.error('保存失败:', error)
    } finally {
      submitting.value = false
    }
  })
}

const handleCancel = () => {
  router.push('/creator/audio')
}

onMounted(() => {
  loadDetail()
})
</script>

<style lang="scss" scoped>
.creator-audio-edit {
  max-width: 800px;
  
  .audio-form {
    background: var(--bg-color);
    padding: var(--spacing-xl);
    border-radius: var(--radius-md);
  }
  
  .form-tip {
    margin-left: var(--spacing-sm);
    color: var(--text-secondary);
    font-size: 12px;
  }
}
</style>
