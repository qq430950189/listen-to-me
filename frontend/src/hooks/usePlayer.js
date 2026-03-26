// 播放器Hook
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { usePlayerStore } from '../store/player'

export const usePlayer = () => {
  const playerStore = usePlayerStore()
  
  // 音频元素引用
  const audioRef = ref(null)
  
  // 是否正在拖动进度条
  const isDragging = ref(false)
  
  // 进度百分比
  const progress = computed(() => {
    if (!playerStore.duration) return 0
    return (playerStore.currentTime / playerStore.duration) * 100
  })
  
  // 格式化当前时间
  const formattedCurrentTime = computed(() => {
    return formatTime(playerStore.currentTime)
  })
  
  // 格式化总时长
  const formattedDuration = computed(() => {
    return formatTime(playerStore.duration)
  })
  
  // 格式化时间
  const formatTime = (seconds) => {
    if (!seconds || seconds <= 0) return '00:00'
    const m = Math.floor(seconds / 60)
    const s = Math.floor(seconds % 60)
    return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
  }
  
  // 播放/暂停
  const togglePlay = () => {
    if (!audioRef.value) return
    
    if (playerStore.isPlaying) {
      audioRef.value.pause()
    } else {
      // 检查试听限制
      if (playerStore.isTrialEnded) {
        return
      }
      audioRef.value.play()
    }
    
    playerStore.togglePlay()
  }
  
  // 设置进度
  const seek = (time) => {
    if (!audioRef.value) return
    
    // 检查试听限制
    if (playerStore.isTrial && time > playerStore.trialDuration) {
      time = playerStore.trialDuration
    }
    
    audioRef.value.currentTime = time
    playerStore.setCurrentTime(time)
  }
  
  // 设置进度百分比
  const seekPercent = (percent) => {
    const time = (percent / 100) * playerStore.duration
    seek(time)
  }
  
  // 设置音量
  const setVolume = (volume) => {
    if (!audioRef.value) return
    audioRef.value.volume = volume
    playerStore.setVolume(volume)
  }
  
  // 设置播放速率
  const setPlaybackRate = (rate) => {
    if (!audioRef.value) return
    audioRef.value.playbackRate = rate
    playerStore.setPlaybackRate(rate)
  }
  
  // 加载音频
  const loadAudio = (audio) => {
    if (!audioRef.value) return
    
    playerStore.setCurrentAudio(audio)
    audioRef.value.src = audio.hlsPath || audio.url
    audioRef.value.load()
  }
  
  // 事件处理
  const onTimeUpdate = () => {
    if (!audioRef.value || isDragging.value) return
    playerStore.setCurrentTime(audioRef.value.currentTime)
  }
  
  const onLoadedMetadata = () => {
    if (!audioRef.value) return
    playerStore.setDuration(audioRef.value.duration)
  }
  
  const onEnded = () => {
    playerStore.pause()
    
    // 同步进度
    playerStore.syncProgress()
  }
  
  const onPlay = () => {
    playerStore.play()
  }
  
  const onPause = () => {
    playerStore.pause()
  }
  
  const onError = (e) => {
    console.error('音频播放错误:', e)
    playerStore.pause()
  }
  
  // 绑定事件
  const bindEvents = () => {
    if (!audioRef.value) return
    
    audioRef.value.addEventListener('timeupdate', onTimeUpdate)
    audioRef.value.addEventListener('loadedmetadata', onLoadedMetadata)
    audioRef.value.addEventListener('ended', onEnded)
    audioRef.value.addEventListener('play', onPlay)
    audioRef.value.addEventListener('pause', onPause)
    audioRef.value.addEventListener('error', onError)
  }
  
  // 解绑事件
  const unbindEvents = () => {
    if (!audioRef.value) return
    
    audioRef.value.removeEventListener('timeupdate', onTimeUpdate)
    audioRef.value.removeEventListener('loadedmetadata', onLoadedMetadata)
    audioRef.value.removeEventListener('ended', onEnded)
    audioRef.value.removeEventListener('play', onPlay)
    audioRef.value.removeEventListener('pause', onPause)
    audioRef.value.removeEventListener('error', onError)
  }
  
  // 监听音量变化
  watch(() => playerStore.volume, (val) => {
    if (audioRef.value) {
      audioRef.value.volume = val
    }
  })
  
  // 生命周期
  onMounted(() => {
    bindEvents()
    if (audioRef.value) {
      audioRef.value.volume = playerStore.volume
    }
  })
  
  onUnmounted(() => {
    unbindEvents()
  })
  
  return {
    audioRef,
    isDragging,
    progress,
    formattedCurrentTime,
    formattedDuration,
    togglePlay,
    seek,
    seekPercent,
    setVolume,
    setPlaybackRate,
    loadAudio,
    bindEvents,
    unbindEvents
  }
}

export default usePlayer
