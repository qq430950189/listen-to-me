// 用户端路由模块
import UserLayout from '@/components/Layout/UserLayout.vue'

export default [
  {
    path: '/',
    component: UserLayout,
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/user/audio/AudioList.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'hot',
        name: 'Hot',
        component: () => import('@/views/user/audio/HotList.vue'),
        meta: { title: '热门' }
      },
      {
        path: 'audio/:id',
        name: 'AudioDetail',
        component: () => import('@/views/user/audio/AudioDetail.vue'),
        meta: { title: '音频详情' }
      },
      {
        path: 'my',
        name: 'MyAudio',
        component: () => import('@/views/user/profile/MyAudio.vue'),
        meta: { title: '我的音频', requiresAuth: true }
      },
      {
        path: 'history',
        name: 'History',
        component: () => import('@/views/user/profile/History.vue'),
        meta: { title: '播放历史', requiresAuth: true }
      },
      {
        path: 'order/:orderSn',
        name: 'OrderDetail',
        component: () => import('@/views/user/order/OrderDetail.vue'),
        meta: { title: '订单详情', requiresAuth: true }
      },
      {
        path: 'consult',
        name: 'ConsultOrder',
        component: () => import('@/views/user/consult/ConsultOrder.vue'),
        meta: { title: '预约咨询', requiresAuth: true }
      }
    ]
  }
]
