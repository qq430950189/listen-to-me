// 分页Hook
import { ref, reactive, computed } from 'vue'

export const usePage = (fetchFn, defaultPageSize = 10) => {
  // 分页数据
  const list = ref([])
  const loading = ref(false)
  const finished = ref(false)
  const error = ref(null)
  
  // 分页参数
  const pagination = reactive({
    pageNum: 1,
    pageSize: defaultPageSize,
    total: 0
  })
  
  // 计算属性
  const hasMore = computed(() => {
    return list.value.length < pagination.total
  })
  
  // 加载数据
  const loadData = async (params = {}, reset = false) => {
    if (loading.value) return
    
    if (reset) {
      list.value = []
      pagination.pageNum = 1
      finished.value = false
    }
    
    loading.value = true
    error.value = null
    
    try {
      const result = await fetchFn({
        ...params,
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize
      })
      
      // 兼容不同的返回格式
      const records = result.records || result.list || result || []
      const total = result.total || records.length || 0
      
      if (reset) {
        list.value = records
      } else {
        list.value = [...list.value, ...records]
      }
      
      pagination.total = total
      
      // 判断是否加载完成
      if (list.value.length >= total || records.length < pagination.pageSize) {
        finished.value = true
      }
      
      return result
    } catch (err) {
      error.value = err
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 加载更多
  const loadMore = async (params = {}) => {
    if (loading.value || finished.value) return
    
    pagination.pageNum++
    return loadData(params, false)
  }
  
  // 刷新
  const refresh = async (params = {}) => {
    return loadData(params, true)
  }
  
  // 重置
  const resetPage = () => {
    list.value = []
    pagination.pageNum = 1
    pagination.total = 0
    finished.value = false
    error.value = null
  }
  
  return {
    list,
    loading,
    finished,
    error,
    pagination,
    hasMore,
    loadData,
    loadMore,
    refresh,
    resetPage
  }
}

export default usePage
