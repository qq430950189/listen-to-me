// 创作者端路由模块
import CreatorLayout from '@/components/Layout/CreatorLayout.vue'

export default [
  {
    path: '/creator',
    component: CreatorLayout,
    meta: { requiresAuth: true, requiresCreator: true },
    children: [
      {
        path: '',
        redirect: '/creator/audio'
      },
      {
        path: 'audio',
        name: 'CreatorAudio',
        component: () => import('@/views/creator/audio/AudioList.vue'),
        meta: { title: '音频管理' }
      },
      {
        path: 'audio/create',
        name: 'CreatorAudioCreate',
        component: () => import('@/views/creator/audio/AudioEdit.vue'),
        meta: { title: '发布音频' }
      },
      {
        path: 'audio/edit/:id',
        name: 'CreatorAudioEdit',
        component: () => import('@/views/creator/audio/AudioEdit.vue'),
        meta: { title: '编辑音频' }
      },
      {
        path: 'transcript',
        name: 'CreatorTranscript',
        component: () => import('@/views/creator/ai/Transcript.vue'),
        meta: { title: 'AI转写' }
      },
      {
        path: 'finance',
        name: 'CreatorFinance',
        component: () => import('@/views/creator/finance/Summary.vue'),
        meta: { title: '收益概览' }
      },
      {
        path: 'finance/log',
        name: 'CreatorFinanceLog',
        component: () => import('@/views/creator/finance/FinanceLog.vue'),
        meta: { title: '账单流水' }
      },
      {
        path: 'slots',
        name: 'CreatorSlots',
        component: () => import('@/views/creator/consult/SlotList.vue'),
        meta: { title: '咨询时间管理' }
      }
    ]
  }
]
