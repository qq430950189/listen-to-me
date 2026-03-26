// 用户端评论接口
import { get, post } from '../../utils/request'

// 获取评论分页列表
export const getCommentPage = (params) => get('/user/comment/page', params)

// 发表评论
export const createComment = (data) => post('/user/comment', data)

// 点赞评论
export const likeComment = (commentId) => post(`/user/comment/${commentId}/like`)

export default {
  getCommentPage,
  createComment,
  likeComment
}
