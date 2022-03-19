import Vue from 'vue'
import VueRouter from 'vue-router'
// import Home from '../views/Home.vue'
import Firstact from '../views/firstact.vue'
// import Secondact from '../views/secondact.vue'
// import Threeact from '../views/threeact.vue'
// import Fouract from '../views/fouract.vue'
// import Fiveact from '../views/fiveact.vue'
// import Sixact from '../views/sixact.vue'
// import Sevenact from '../views/sevenact.vue'
// import Eightact from '../views/eightact.vue'


Vue.use(VueRouter)

const routes = [

  {
    path: '/',
    name: 'firstact',
    component: Firstact
  },
  // {
  //   path: '/secondact',
  //   name: 'secondact',
  //   component: Secondact
  // },
  // {
  //   path: '/threeact',
  //   name: 'threeact',
  //   component: Threeact
  // },
  // {
  //   path: '/fouract',
  //   name: 'fouract',
  //   component: Fouract
  // },
  // {
  //   path: '/fiveact',
  //   name: 'fiveact',
  //   component: Fiveact
  // },
  // {
  //   path: '/sixact',
  //   name: 'sixact',
  //   component: Sixact
  // },
  // {
  //   path: '/sevenact',
  //   name: 'sevenact',
  //   component: Sevenact
  // },
  // {
  //   path: '/eightact',
  //   name: 'eightact',
  //   component: Eightact
  // },
]

const router = new VueRouter({
  routes
})

export default router
