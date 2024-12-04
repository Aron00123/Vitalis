import {createRouter, createWebHashHistory} from 'vue-router';

import Front from '../components/Front.vue';
 import Login from '../components/Login.vue';
import Manager from "../components/Manager.vue";
import Register from '../components/Register.vue';
import NotFound from '../components/404.vue';

import NoAuth from '../components/manager/NoAuth.vue';
import Admin from '../components/manager/Admin.vue';
import Doctor from '../components/manager/Doctor.vue';
import User from '../components/manager/User.vue';
import AdminPerson from '../components/manager/AdminPerson.vue';
import DoctorPerson from '../components/manager/DoctorPerson.vue';
import UserPerson from '../components/manager/UserPerson.vue';
import Password from '../components/manager/Password.vue';
import Notice from '../components/manager/Notice.vue';
import Department from '../components/manager/Department.vue';
import Welcome from '../components/manager/Welcome.vue';
import DoctorCard from '../components/manager/DoctorCard.vue';
import Reserve from '../components/manager/Reserve.vue';
import Record from '../components/manager/Record.vue';

import FrontHome from '../components/front/Home.vue';
import Person from '../components/front/Person.vue';



const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: '/',
            name: 'Manager',
            component: Manager,
            redirect: 'welcome',
            children: [
                {
                    path: '403',
                    name: 'NoAuth',
                    meta: {name: '无权限'},
                    component: NoAuth,
                },
                {
                    path: 'welcome',
                    name: 'Welcome',
                    meta: {name: '欢迎页'},
                    component: Welcome,
                },
                // {
                //     path: 'home',
                //     name: 'Home',
                //     meta: {name: '系统首页'},
                //     component: Home,
                // },
                {
                    path: 'admin',
                    name: 'Admin',
                    meta: {name: '管理员信息'},
                    component: Admin,
                },
                {
                    path: 'doctor',
                    name: 'Doctor',
                    meta: {name: '医生信息'},
                    component: Doctor,
                },
                {
                    path: 'user',
                    name: 'User',
                    meta: {name: '患者信息'},
                    component: User,
                },
                {
                    path: 'adminPerson',
                    name: 'AdminPerson',
                    meta: {name: '个人信息'},
                    component: AdminPerson,
                },
                {
                    path: 'doctorPerson',
                    name: 'DoctorPerson',
                    meta: {name: '个人信息'},
                    component: DoctorPerson,
                },
                {
                    path: 'userPerson',
                    name: 'UserPerson',
                    meta: {name: '个人信息'},
                    component: UserPerson,
                },
                {
                    path: 'password',
                    name: 'Password',
                    meta: {name: '修改密码'},
                    component: Password,
                },
                {
                    path: 'notice',
                    name: 'Notice',
                    meta: {name: '公告信息'},
                    component: Notice,
                },
                {
                    path: 'department',
                    name: 'Department',
                    meta: {name: '科室信息'},
                    component: Department,
                },
                {
                    path: 'doctorCard',
                    name: 'DoctorCard',
                    meta: {name: '预约挂号'},
                    component: DoctorCard,
                },
                {
                    path: 'reserve',
                    name: 'Reserve',
                    meta: {name: '我的挂号'},
                    component: Reserve,
                },
                {
                    path: 'record',
                    name: 'Record',
                    meta: {name: '我的就诊'},
                    component: Record,
                },
            ],
        },
        {
            path: '/front',
            name: 'Front',
            component: Front,
            children: [
                {
                    path: 'home',
                    name: 'Home',
                    meta: {name: '系统首页'},
                    component: FrontHome,
                },
                {
                    path: 'person',
                    name: 'Person',
                    meta: {name: '个人信息'},
                    component: Person,
                },
            ],
        },
        {
            path: '/login',
            name: 'Login',
            meta: {name: '登录'},
            component: Login,
        },
        {
            path: '/register',
            name: 'Register',
            meta: {name: '注册'},
            component: Register,
        },
        {
            path: '/:catchAll(.*)',
            name: 'NotFound',
            meta: {name: '无法访问'},
            component: NotFound,
        },
    ]

});

// router.beforeEach((to, from) => {
//     const user = JSON.parse(localStorage.getItem("xm-user") || '{}');
//     console.log(user)
//     if (to.path === '/') {
//         if (user.role) {
//             if (user.role === 'USER') {
//                 return '/front/home';
//             } else {
//                 return '/home';
//             }
//             //return '/';
//         } else {
//             return '/login';
//         }
//     }
//     return true;
// });

export default router;