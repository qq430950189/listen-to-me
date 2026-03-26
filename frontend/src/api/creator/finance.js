// 创作者端财务接口
import { get, post } from '../../utils/request'

// 获取收益概览
export const getFinanceSummary = () => get('/creator/finance/summary')

// 获取账单流水
export const getFinanceLogPage = (params) => get('/creator/finance/log', params)

// 申请提现
export const requestWithdraw = (data) => post('/creator/finance/withdraw', data)

export default {
  getFinanceSummary,
  getFinanceLogPage,
  requestWithdraw
}
