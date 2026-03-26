// 管理员端路由模块
import AdminLayout from '@/components/Layout/AdminLayout.vue'

export default [
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/stat/Dashboard.vue'),
        meta: { title: '数据大盘' }
      },
      {
        path: 'audit',
        name: 'AdminAudit',
        component: () => import('@/views/admin/audio/AuditList.vue'),
        meta: { title: '内容审核' }
      },
      {
        path: 'audit/:id',
        name: 'AdminAuditDetail',
        component: () => import('@/views/admin/audio/AuditDetail.vue'),
        meta: { title: '审核详情' }
      },
      {
        path: 'tags',
        name: 'AdminTags',
        component: () => import('@/views/admin/tag/TagList.vue'),
        meta: { title: '标签管理' }
      }
    ]
  }
]
