import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'gprotoView',
    component: () => import('../views/GprotoView.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
