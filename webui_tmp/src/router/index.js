import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/elementuiTest',
    name: 'elementuiTest',
    component: () => import('../views/ElementuiTest.vue')
  },
  {
    path: '/gprotoView',
    name: 'gprotoView',
    component: () => import('../views/GprotoView.vue')
  },
  {
    path: '/treeView',
    name: 'treeView',
    component: () => import('../views/TreeView.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
