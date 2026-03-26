// 公共接口
import { get, post, upload } from '../utils/request'

// 认证相关
// 登录
export const login = (data) => post('/common/auth/login', data)

// 注册
export const register = (data) => post('/common/auth/register', data)

// 刷新Token
export const refreshToken = () => post('/common/auth/refresh')

// 发送验证码
export const sendCaptcha = (data) => post('/common/auth/captcha', data)

// 文件相关
// 获取上传预签名URL
export const getUploadUrl = (fileName) => get('/common/file/url', { fileName })

// 文件上传
export const uploadFile = (file, onProgress) => upload('/common/file/upload', file, onProgress)

// 获取播放签名
export const getStreamSign = (audioId) => get('/common/file/sign', { audioId })

export default {
  login,
  register,
  refreshToken,
  sendCaptcha,
  getUploadUrl,
  uploadFile,
  getStreamSign
}
