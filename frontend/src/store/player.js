import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const usePlayerStore = defineStore('player', () => {
  // 当前播放的音频
  const currentAudio = ref(null)
  
  // 播放状态
  const isPlaying = ref(false)
  
  // 当前播放时间（秒）
  const currentTime = ref(0)
  
  // 音频总时长（秒）
  const duration = ref(0)
  
  // 音量 (0-1)
  const volume = ref(parseFloat(localStorage.getItem('playerVolume') || '1'))
  
  // 播放速率
  const playbackRate = ref(1)
  
  // 是否显示播放器
  const showPlayer = computed(() => !!currentAudio.value)
  
  // 试听时长（秒）
  const trialDuration = computed(() => currentAudio.value?.trialDuration || 0)
  
  // 是否已购买
  const isPurchased = computed(() => currentAudio.value?.isPurchased || false)
  
  // 是否在试听中
  const isTrial = computed(() => !isPurchased.value && trialDuration.value > 0)
  
  // 试听是否结束
  const isTrialEnded = computed(() => isTrial.value && currentTime.value >= trialDuration.value)

  // 设置当前音频
  const setCurrentAudio = (audio) => {
    currentAudio.value = audio
    currentTime.value = 0
    isPlaying.value = false
  }

  // 播放
  const play = () => {
    if (isTrialEnded.value) {
      return false
    }
    isPlaying.value = true
    return true
  }

  // 暂停
  const pause = () => {
    isPlaying.value = false
  }

  // 切换播放/暂停
  const togglePlay = () => {
    if (isPlaying.value) {
      pause()
    } else {
      play()
    }
  }

  // 设置当前时间
  const setCurrentTime = (time) => {
    // 如果是试听且未购买，限制播放时间
    if (isTrial.value && time > trialDuration.value) {
      currentTime.value = trialDuration.value
      pause()
      return false
    }
    currentTime.value = time
    return true
  }

  // 设置总时长
  const setDuration = (time) => {
    duration.value = time
  }

  // 设置音量
  const setVolume = (val) => {
    volume.value = val
    localStorage.setItem('playerVolume', val.toString())
  }

  // 设置播放速率
  const setPlaybackRate = (rate) => {
    playbackRate.value = rate
  }

  // 关闭播放器
  const closePlayer = () => {
    currentAudio.value = null
    isPlaying.value = false
    currentTime.value = 0
    duration.value = 0
  }

  // 同步播放进度到后端
  const syncProgress = async () => {
    if (!currentAudio.value || !currentTime.value) return
    
    try {
      // 这里可以调用后端API同步进度
      // await post('/user/history', {
      //   audioId: currentAudio.value.id,
      //   lastPosition: Math.floor(currentTime.value)
      // })
      console.log('同步播放进度:', currentAudio.value.id, currentTime.value)
    } catch (error) {
      console.error('同步播放进度失败:', error)
    }
  }

  return {
    currentAudio,
    isPlaying,
    currentTime,
    duration,
    volume,
    playbackRate,
    showPlayer,
    trialDuration,
    isPurchased,
    isTrial,
    isTrialEnded,
    setCurrentAudio,
    play,
    pause,
    togglePlay,
    setCurrentTime,
    setDuration,
    setVolume,
    setPlaybackRate,
    closePlayer,
    syncProgress
  }
})
