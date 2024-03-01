// Imports
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import AdminPanelView from "@/views/AdminPanelView.vue";
import ProfileView from "@/views/ProfileView";
import NotFound from "@/views/NotFound";
import CreateAccountView from "@/views/CreateAccountView";
import LaptopDetails from "@/components/LaptopDetails";
import LaptopTable from "@/components/LaptopTable";
import qs from "qs";

const routes = [
    {
        path: '/laptops',
        name: 'home',
        // mode: 'history',
        base: process.env.BASE_URL,
        component: HomeView,
        children: [
            {
                path: '', // default component
                name: 'search',
                component: LaptopTable
            },
            {
                path: ':ean',
                name: 'laptop',
                component: LaptopDetails
            },
            {
                path: 'new',
                name: 'create',
                component: LaptopDetails
            },
            {
                path: ':catchAll(.*)',
                name: 'fallback',
                component: HomeView
            }
        ]

    },
    {
        path: '/',
        name: 'login',
        component: LoginView
    },
    {
        path: '/profile',
        name: 'profile',
        component: ProfileView
    },
    {
        path: '/admin',
        name: 'admin',
        component: AdminPanelView
    },
    {
        path: '/:catchAll(.*)',
        name: '404',
        component: NotFound
    },

    // {
    //   path: '/about',
    //   name: 'about',
    //   // route level code-splitting
    //   // this generates a separate chunk (about.[hash].js) for this route
    //   // which is lazy-loaded when the route is visited.
    //   component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    // }
    {
        path: '/account-aanmaken',
        name: 'CreateAccount',
        component: CreateAccountView
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
    encodeValuesOnly: true,
    parseQuery(query) {
        return qs.parse(query)
    },
    stringifyQuery(query) {
        var result = qs.stringify(query, { encodeValuesOnly: true, encode: true });
        return result ? ('?' + result) : ''
    }
})

export default router
