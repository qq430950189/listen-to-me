<template>
  <div class="tag-list">
    <div class="list-header">
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        新增标签
      </el-button>
    </div>
    
    <el-table v-loading="loading" :data="tagList" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="标签名称" min-width="200" />
      <el-table-column prop="createTime" label="创建时间" width="180" />
      
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(row.id)">删除</el-button>
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
    
    <!-- 创建/编辑弹窗 -->
    <el-dialog
      v-model="showCreateDialog"
      :title="editingTag ? '编辑标签' : '新增标签'"
      width="400px"
    >
      <el-form :model="tagForm" label-width="80px">
        <el-form-item label="标签名称">
          <el-input v-model="tagForm.name" placeholder="请输入标签名称" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getTagList, createTag, updateTag, deleteTag } from '@/api/admin/tag'

const loading = ref(false)
const tagList = ref([])
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)

const showCreateDialog = ref(false)
const submitting = ref(false)
const editingTag = ref(null)

const tagForm = reactive({
  id: null,
  name: ''
})

const loadData = async () => {
  loading.value = true
  try {
    const result = await getTagList({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    
    tagList.value = result.records || []
    total.value = result.total || 0
  } catch (error) {
    console.error('加载标签列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleEdit = (tag) => {
  editingTag.value = tag
  tagForm.id = tag.id
  tagForm.name = tag.name
  showCreateDialog.value = true
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该标签吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteTag(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    // 取消删除
  }
}

const handleSubmit = async () => {
  if (!tagForm.name) {
    ElMessage.warning('请输入标签名称')
    return
  }
  
  submitting.value = true
  try {
    if (editingTag.value) {
      await updateTag(tagForm)
      ElMessage.success('更新成功')
    } else {
      await createTag(tagForm)
      ElMessage.success('创建成功')
    }
    
    showCreateDialog.value = false
    resetForm()
    loadData()
  } catch (error) {
    console.error('保存失败:', error)
  } finally {
    submitting.value = false
  }
}

const resetForm = () => {
  tagForm.id = null
  tagForm.name = ''
  editingTag.value = null
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.tag-list {
  .list-header {
    margin-bottom: var(--spacing-lg);
  }
  
  .pagination-wrapper {
    margin-top: var(--spacing-lg);
    display: flex;
    justify-content: flex-end;
  }
}
</style>