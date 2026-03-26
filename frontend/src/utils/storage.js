// 本地存储工具
const STORAGE_PREFIX = 'listen_to_me_'

export const storage = {
  // 获取
  get(key) {
    const value = localStorage.getItem(STORAGE_PREFIX + key)
    try {
      return value ? JSON.parse(value) : null
    } catch {
      return value
    }
  },

  // 设置
  set(key, value) {
    const data = typeof value === 'object' ? JSON.stringify(value) : value
    localStorage.setItem(STORAGE_PREFIX + key, data)
  },

  // 删除
  remove(key) {
    localStorage.removeItem(STORAGE_PREFIX + key)
  },

  // 清空
  clear() {
    const keys = Object.keys(localStorage)
    keys.forEach(key => {
      if (key.startsWith(STORAGE_PREFIX)) {
        localStorage.removeItem(key)
      }
    })
  }
}

export default storage
