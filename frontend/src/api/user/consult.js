// 用户端咨询接口
import { get, post } from '../../utils/request'

// 获取创作者可用时间槽
export const getAvailableSlots = (creatorId, params) => get(`/user/consult/slots/${creatorId}`, params)

// 预约咨询
export const createConsult = (data) => post('/user/consult', data)

// 获取我的咨询订单
export const getMyConsultOrders = (params) => get('/user/consult/my', params)

export default {
  getAvailableSlots,
  createConsult,
  getMyConsultOrders
}
