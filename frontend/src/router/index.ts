import type {Router, RouteRecordRaw} from 'vue-router'
import {createRouter, createWebHistory} from 'vue-router'

const routes: RouteRecordRaw[] = [
    {
        path: '',
        redirect: '/test'
    },
    {
        path: '/home',
        component: () => import('@/view/Home/index.vue')
    },
    {
        path: '/test',
        component: () => import('@/view/Test/index.vue')
    }
]

export const router: Router = createRouter({
    history: createWebHistory(),
    routes
})