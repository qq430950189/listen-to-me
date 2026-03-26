<template>
  <div class="audio-player">
    <audio ref="audioRef" style="display: none"></audio>
    
    <div class="player-content">
      <!-- 音频信息 -->
      <div class="audio-info">
        <el-image
          :src="playerStore.currentAudio?.coverUrl"
          class="audio-cover"
          fit="cover"
        >
          <template #error>
            <div class="cover-placeholder">
              <el-icon><Headset /></el-icon>
            </div>
          </template>
        </el-image>
        <div class="audio-meta">
          <div class="audio-title text-ellipsis">
            {{ playerStore.currentAudio?.title || '未知音频' }}
          </div>
          <div class="audio-creator">
            {{ playerStore.currentAudio?.creatorName || '未知创作者' }}
          </div>
        </div>
      </div>
      
      <!-- 播放控制 -->
      <div class="player-controls">
        <!-- 播放按钮 -->
        <el-button
          :icon="playerStore.isPlaying ? 'VideoPause' : 'VideoPlay'"
          circle
          @click="togglePlay"
        />
        
        <!-- 进度条 -->
        <div class="progress-wrapper">
          <span class="time">{{ formattedCurrentTime }}</span>
          <el-slider
            v-model="progressValue"
            :show-tooltip="false"
            class="progress-slider"
            @change="handleSeek"
          />
          <span class="time">{{ formattedDuration }}</span>
        </div>
        
        <!-- 音量控制 -->
        <div class="volume-control">
          <el-popover
            placement="top"
            :width="40"
            trigger="hover"
          >
            <template #reference>
              <el-button :icon="volumeIcon" text />
            </template>
            <el-slider
              v-model="volumeValue"
              vertical
              :height="80"
              :show-tooltip="false"
            />
          </el-popover>
        </div>
        
        <!-- 播放速率 -->
        <el-dropdown @command="handleRateChange">
          <el-button text>
            {{ playbackRateText }}
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item
                v-for="rate in rateOptions"
                :key="rate"
                :command="rate"
              >
                {{ rate }}x
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        
        <!-- 关闭按钮 -->
        <el-button :icon="'Close'" text @click="closePlayer" />
      </div>
      
      <!-- 试听提示 -->
      <div v-if="playerStore.isTrial && !playerStore.isPurchased" class="trial-tip">
        <el-icon><InfoFilled /></el-icon>
        <span>试听中，完整内容需购买后收听</span>
        <el-button type="primary" size="small" @click="goToDetail">
          立即购买
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { usePlayerStore } from '@/store/player'

const router = useRouter()
const playerStore = usePlayerStore()

const audioRef = ref(null)
const progressValue = ref(0)
const volumeValue = ref(100)
const currentRate = ref(1)

const rateOptions = [0.5, 0.75, 1, 1.25, 1.5, 2]

const formattedCurrentTime = computed(() => formatTime(playerStore.currentTime))
const formattedDuration = computed(() => formatTime(playerStore.duration))
const playbackRateText = computed(() => `${currentRate.value}x`)
const volumeIcon = computed(() => {
  if (volumeValue.value === 0) return 'Mute'
  if (volumeValue.value < 50) return 'Microphone'
  return 'Microphone'
})

const formatTime = (seconds) => {
  if (!seconds || seconds <= 0) return '00:00'
  const m = Math.floor(seconds / 60)
  const s = Math.floor(seconds % 60)
  return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
}

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

const handleSeek = (value) => {
  if (!audioRef.value || !playerStore.duration) return
  
  let time = (value / 100) * playerStore.duration
  
  // 检查试听限制
  if (playerStore.isTrial && time > playerStore.trialDuration) {
    time = playerStore.trialDuration
    progressValue.value = (time / playerStore.duration) * 100
  }
  
  audioRef.value.currentTime = time
  playerStore.setCurrentTime(time)
}

const handleRateChange = (rate) => {
  currentRate.value = rate
  if (audioRef.value) {
    audioRef.value.playbackRate = rate
  }
}

const closePlayer = () => {
  if (audioRef.value) {
    audioRef.value.pause()
  }
  playerStore.closePlayer()
}

const goToDetail = () => {
  if (playerStore.currentAudio?.id) {
    router.push(`/audio/${playerStore.currentAudio.id}`)
  }
}

// 监听音量变化
watch(volumeValue, (val) => {
  if (audioRef.value) {
    audioRef.value.volume = val / 100
  }
  playerStore.setVolume(val / 100)
})

// 监听当前音频变化
watch(() => playerStore.currentAudio, (audio) => {
  if (audio && audioRef.value) {
    audioRef.value.src = audio.hlsPath || audio.url
    audioRef.value.load()
  }
})

// 事件处理
const onTimeUpdate = () => {
  if (!audioRef.value) return
  const time = audioRef.value.currentTime
  playerStore.setCurrentTime(time)
  
  if (playerStore.duration) {
    progressValue.value = (time / playerStore.duration) * 100
  }
  
  // 检查试听限制
  if (playerStore.isTrial && time >= playerStore.trialDuration) {
    audioRef.value.pause()
    playerStore.pause()
  }
}

const onLoadedMetadata = () => {
  if (!audioRef.value) return
  playerStore.setDuration(audioRef.value.duration)
}

const onEnded = () => {
  playerStore.pause()
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

onMounted(() => {
  if (audioRef.value) {
    audioRef.value.addEventListener('timeupdate', onTimeUpdate)
    audioRef.value.addEventListener('loadedmetadata', onLoadedMetadata)
    audioRef.value.addEventListener('ended', onEnded)
    audioRef.value.addEventListener('play', onPlay)
    audioRef.value.addEventListener('pause', onPause)
    audioRef.value.addEventListener('error', onError)
    audioRef.value.volume = playerStore.volume
  }
})

onUnmounted(() => {
  if (audioRef.value) {
    audioRef.value.removeEventListener('timeupdate', onTimeUpdate)
    audioRef.value.removeEventListener('loadedmetadata', onLoadedMetadata)
    audioRef.value.removeEventListener('ended', onEnded)
    audioRef.value.removeEventListener('play', onPlay)
    audioRef.value.removeEventListener('pause', onPause)
    audioRef.value.removeEventListener('error', onError)
  }
})
</script>

<style lang="scss" scoped>
.audio-player {
  background: var(--bg-color);
  border-top: 1px solid var(--border-lighter);
  box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.1);
}

.player-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: var(--spacing-md);
}

.audio-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-sm);
  
  .audio-cover {
    width: 48px;
    height: 48px;
    border-radius: var(--radius-sm);
    overflow: hidden;
    
    .cover-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--bg-hover);
      color: var(--text-secondary);
      
      .el-icon {
        font-size: 24px;
      }
    }
  }
  
  .audio-meta {
    flex: 1;
    min-width: 0;
    
    .audio-title {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-primary);
    }
    
    .audio-creator {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}

.player-controls {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  
  .progress-wrapper {
    flex: 1;
    display: flex;
    align-items: center;
    gap: var(--spacing-sm);
    
    .time {
      font-size: 12px;
      color: var(--text-secondary);
      min-width: 40px;
    }
    
    .progress-slider {
      flex: 1;
    }
  }
  
  .volume-control {
    display: flex;
    align-items: center;
  }
}

.trial-tip {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  background: #fdf6ec;
  border-radius: var(--radius-sm);
  margin-top: var(--spacing-sm);
  font-size: 12px;
  color: #e6a23c;
}
</style>
