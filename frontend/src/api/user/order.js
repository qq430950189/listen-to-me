// 用户端订单接口
import { get, post, put } from '../../utils/request'

// 创建订单
export const createOrder = (data) => post('/user/order', data)

// 获取订单详情
export const getOrderDetail = (orderSn) => get(`/user/order/${orderSn}`)

// 取消订单
export const cancelOrder = (orderSn) => put(`/user/order/${orderSn}/cancel`)

export default {
  createOrder,
  getOrderDetail,
  cancelOrder
}
