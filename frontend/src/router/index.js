import Vue from 'vue'
import VueRouter from 'vue-router'
import Serach from '../components/Search.vue'
import Result from '../components/Result.vue'
import Detail from '../components/Detail.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Serach
  },
  {
    path: '/result',
    name: 'result',
    component: Result
  },
  {
    path: '/detail',
    name: 'detail',
    component: Detail
  },
  {
    path: '/result/:para',
    name: 'result',
    component: Result
  },
  
]

const router = new VueRouter({
  routes,
  mode:'history'
})

export default router
