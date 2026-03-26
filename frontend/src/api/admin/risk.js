// 管理员端风控接口
import { get, post, put } from '../../utils/request'

// 获取敏感词列表
export const getSensitiveWords = (params) => get('/admin/sensitive/page', params)

// 添加敏感词
export const addSensitiveWord = (data) => post('/admin/sensitive', data)

// 删除敏感词
export const deleteSensitiveWord = (id) => del(`/admin/sensitive/${id}`)

// 封禁/解封用户
export const updateUserStatus = (data) => put('/admin/user/status', data)

export default {
  getSensitiveWords,
  addSensitiveWord,
  deleteSensitiveWord,
  updateUserStatus
}
