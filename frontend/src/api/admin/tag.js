// 管理员端标签接口
import { get, post, put, del } from '../../utils/request'

// 获取标签列表
export const getTagList = (params) => get('/admin/tag/page', params)

// 创建标签
export const createTag = (data) => post('/admin/tag', data)

// 更新标签
export const updateTag = (data) => put('/admin/tag', data)

// 删除标签
export const deleteTag = (id) => del(`/admin/tag/${id}`)

export default {
  getTagList,
  createTag,
  updateTag,
  deleteTag
}
