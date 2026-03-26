// 创作者端咨询接口
import { get, post, put } from '../../utils/request'

// 获取我的时间槽列表
export const getMySlots = (params) => get('/creator/slots/page', params)

// 批量创建时间槽
export const createSlotsBatch = (data) => post('/creator/slots', data)

// 修改时间槽状态
export const updateSlot = (id, data) => put(`/creator/slots/${id}`, data)

// 获取预约订单列表
export const getConsultOrders = (params) => get('/creator/consult/page', params)

export default {
  getMySlots,
  createSlotsBatch,
  updateSlot,
  getConsultOrders
}
