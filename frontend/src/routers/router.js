import {createRouter, createWebHashHistory} from 'vue-router';

import Login from '../components/Login.vue';
import Manager from "../components/Manager.vue";
import Register from '../components/Register.vue';
import NotFound from '../components/404.vue';
import Plan from '../components/manager/Plan.vue'
import Admin from '../components/manager/Admin.vue';
import Doctor from '../components/manager/Doctor.vue';
import Patient from '../components/manager/Patient.vue';
import AdminPerson from '../components/manager/AdminPerson.vue';
import DoctorPerson from '../components/manager/DoctorPerson.vue';
import PatientPerson from '../components/manager/PatientPerson.vue';
import Password from '../components/manager/Password.vue';
import Notice from '../components/manager/Notice.vue';
import Department from '../components/manager/Department.vue';
import Welcome from '../components/manager/Welcome.vue';
import DoctorCard from '../components/manager/DoctorCard.vue';
import Registration from '../components/manager/Registration.vue';
import Record from '../components/manager/Record.vue';
import Prescription from '../components/manager/Prescription.vue'
import Medicine from '../components/manager/Medicine.vue'
import Disease from '../components/manager/Disease.vue'

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
                    path: 'welcome',
                    name: 'Welcome',
                    meta: {name: '欢迎页'},
                    component: Welcome,
                },
                {
                    path: 'plan',
                    name: 'Plan',
                    meta: {name: '医生排班管理'},
                    component: Plan,
                },
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
                    path: 'patient',
                    name: 'Patient',
                    meta: {name: '患者信息'},
                    component: Patient,
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
                    path: 'patientPerson',
                    name: 'PatientPerson',
                    meta: {name: '个人信息'},
                    component: PatientPerson,
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
                    path: 'registration',
                    name: 'Registration',
                    meta: {name: '我的挂号'},
                    component: Registration,
                },
                {
                    path: 'record',
                    name: 'Record',
                    meta: {name: '我的就诊'},
                    component: Record,
                },
                {
                    path: 'prescription',
                    name: 'Prescription',
                    meta: {name: '处方管理'},
                    component: Prescription,
                },
                {
                    path: 'medicine',
                    name: 'Medicine',
                    meta: {name: '药品管理'},
                    component: Medicine,
                },
                {
                    path: 'disease',
                    name: 'Disease',
                    meta: {name: '疾病管理'},
                    component: Disease,
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