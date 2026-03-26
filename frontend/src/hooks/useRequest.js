// 请求Hook
import { ref } from 'vue'

export const useRequest = (fetchFn, options = {}) => {
  const {
    immediate = false,
    initialData = null,
    onSuccess = null,
    onError = null
  } = options
  
  // 状态
  const data = ref(initialData)
  const loading = ref(false)
  const error = ref(null)
  
  // 执行请求
  const execute = async (...args) => {
    loading.value = true
    error.value = null
    
    try {
      const result = await fetchFn(...args)
      data.value = result
      
      if (onSuccess) {
        onSuccess(result)
      }
      
      return result
    } catch (err) {
      error.value = err
      
      if (onError) {
        onError(err)
      }
      
      throw err
    } finally {
      loading.value = false
    }
  }
  
  // 重置
  const reset = () => {
    data.value = initialData
    loading.value = false
    error.value = null
  }
  
  // 立即执行
  if (immediate) {
    execute()
  }
  
  return {
    data,
    loading,
    error,
    execute,
    reset
  }
}

export default useRequest
