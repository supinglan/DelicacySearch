import Vue from 'vue'
import VueRouter from 'vue-router'
import Serach from '../components/Search.vue'
import Result from '../components/Result.vue'
import Detail from '../components/Detail.vue'
import Help from '@/components/Help.vue'
import SerachZone from '../components/SearchZone.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/home',
    name: 'home',
    component: Serach
  },
  {
    path: '/result',
    name: 'result',
    component: Result
  },
  {
    path: '/detail/:para',
    name: 'detail',
    component: Detail
  },
  {
    path: '/result/:para',
    name: 'result',
    component: Result
  },
  {
    path: '/help',
    name: 'help',
    component: Help
  }
  
]

const router = new VueRouter({
  routes,
  mode:'history'
})

export default router
