// 创作者端AI接口
import { get, post } from '../../utils/request'

// 申请AI转写
export const requestTranscript = (audioId) => post('/creator/ai/transcript', { audioId })

// 申请AI摘要
export const requestNote = (audioId) => post('/creator/ai/note', { audioId })

// 获取AI任务状态
export const getAiTaskStatus = (taskId) => get(`/creator/ai/task/${taskId}`)

export default {
  requestTranscript,
  requestNote,
  getAiTaskStatus
}
