// 创作者端音频接口
import { get, post, put, del } from '../../utils/request'

// 获取我的音频列表
export const getMyAudioPage = (params) => get('/creator/audio/page', params)

// 获取音频详情
export const getAudioDetail = (id) => get(`/creator/audio/${id}`)

// 发布音频
export const createAudio = (data) => post('/creator/audio', data)

// 修改音频
export const updateAudio = (data) => put('/creator/audio', data)

// 删除音频
export const deleteAudio = (id) => del(`/creator/audio/${id}`)

export default {
  getMyAudioPage,
  getAudioDetail,
  createAudio,
  updateAudio,
  deleteAudio
}
