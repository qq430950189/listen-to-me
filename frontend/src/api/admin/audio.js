// 管理员端音频接口
import { get, put } from '../../utils/request'

// 获取待审音频分页列表
export const getAuditAudioPage = (params) => get('/admin/audio/audit/page', params)

// 执行音频审核
export const auditAudio = (data) => put('/admin/audio/audit', data)

// 手动调整热度
export const updateAudioHot = (audioId, viewCount) => put('/admin/audio/hot', { audioId, viewCount })

// 下架音频
export const offlineAudio = (id) => put(`/admin/audio/${id}/offline`)

export default {
  getAuditAudioPage,
  auditAudio,
  updateAudioHot,
  offlineAudio
}
