import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { post, get } from '../utils/request'
import router from '../router'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const username = ref(localStorage.getItem('username') || '')
  const nickname = ref(localStorage.getItem('nickname') || '')
  const avatar = ref(localStorage.getItem('avatar') || '')
  const isCreator = ref(localStorage.getItem('isCreator') === 'true')

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)

  // 登录
  const login = async (loginData) => {
    try {
      const data = await post('/common/auth/login', loginData)
      
      token.value = data.token
      userId.value = data.userId
      username.value = data.username
      nickname.value = data.nickname || data.username
      avatar.value = data.avatar || ''
      isCreator.value = data.isCreator || false
      
      // 持久化存储
      localStorage.setItem('token', data.token)
      localStorage.setItem('userId', data.userId)
      localStorage.setItem('username', data.username)
      localStorage.setItem('nickname', nickname.value)
      localStorage.setItem('avatar', avatar.value)
      localStorage.setItem('isCreator', isCreator.value)
      
      return data
    } catch (error) {
      throw error
    }
  }

  // 注册
  const register = async (registerData) => {
    try {
      await post('/common/auth/register', registerData)
      return true
    } catch (error) {
      throw error
    }
  }

  // 登出
  const logout = () => {
    token.value = ''
    userId.value = ''
    username.value = ''
    nickname.value = ''
    avatar.value = ''
    isCreator.value = false
    
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('username')
    localStorage.removeItem('nickname')
    localStorage.removeItem('avatar')
    localStorage.removeItem('isCreator')
    
    router.push('/login')
  }

  // 刷新Token
  const refreshToken = async () => {
    try {
      const data = await post('/common/auth/refresh')
      
      token.value = data.token
      userId.value = data.userId
      username.value = data.username
      nickname.value = data.nickname || data.username
      avatar.value = data.avatar || ''
      isCreator.value = data.isCreator || false
      
      localStorage.setItem('token', data.token)
      
      return data
    } catch (error) {
      logout()
      throw error
    }
  }

  // 获取用户信息
  const getUserInfo = async () => {
    try {
      // 这里可以调用获取用户信息的接口
      // const data = await get('/common/user/info')
      // 更新用户信息
      return {
        userId: userId.value,
        username: username.value,
        nickname: nickname.value,
        avatar: avatar.value,
        isCreator: isCreator.value
      }
    } catch (error) {
      throw error
    }
  }

  return {
    token,
    userId,
    username,
    nickname,
    avatar,
    isCreator,
    isLoggedIn,
    login,
    register,
    logout,
    refreshToken,
    getUserInfo
  }
})
