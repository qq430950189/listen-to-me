// 用户端音频接口
import { get, post, put } from '../../utils/request'

// 获取音频分页列表
export const getAudioPage = (params) => get('/user/audio/page', params)

// 获取热门音频列表
export const getHotAudioList = (limit = 10) => get('/user/audio/hot', { limit })

// 获取音频详情
export const getAudioDetail = (id) => get(`/user/audio/${id}`)

// 获取我的已购音频
export const getMyPurchasedAudio = (params) => get('/user/audio/my/purchased', params)

// 收藏/取消收藏音频
export const toggleAudioAction = (data) => post('/user/audio/action', data)

// 同步播放进度
export const syncPlayProgress = (data) => put('/user/history', data)

// 获取播放历史
export const getPlayHistory = (params) => get('/user/history/page', params)

export default {
  getAudioPage,
  getHotAudioList,
  getAudioDetail,
  getMyPurchasedAudio,
  toggleAudioAction,
  syncPlayProgress,
  getPlayHistory
}
