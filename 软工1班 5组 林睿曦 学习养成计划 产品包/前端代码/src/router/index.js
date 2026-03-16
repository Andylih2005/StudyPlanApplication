import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  {
    path: '/main',
    component: () => import('../views/Main.vue')
  },
  {
    path: '/register',
    component: () => import('../views/Register.vue')
  }, {
    path: '/home',
    component: () => import('../views/home.vue')
  },
  {
    path: '/pdf-viewer',
    name: 'PdfViewer',
    component: () => import('@/views/PdfViewer.vue'), // 假設你放在 views 資料夾
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
