// 管理员端统计接口
import { get } from '../../utils/request'

// 获取数据大盘
export const getDashboard = (params) => get('/admin/stat/dashboard', params)

export default {
  getDashboard
}
