import axios from 'axios';
import { useRouter } from 'vue-router';

// 创建 axios 实例
const request = axios.create({
    // baseURL: import.meta.env.VITE_APP_BASEURL
    baseURL: "http://localhost:9090",
    timeout: 30000, // 请求超时 30s
});

// 获取路由实例
const router = useRouter();

// request 拦截器
request.interceptors.request.use(
    (config) => {
        // 设置请求头
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
        // 获取用户信息
        const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
        if (user.token) {
            config.headers['token'] = user.token; // 添加 token 到请求头
        }
        return config;
    },
    (error) => {
        console.error('Request error:', error); // Debug 信息
        return Promise.reject(error);
    }
);

// response 拦截器
request.interceptors.response.use(
    (response) => {
        let res = response.data;

        // 处理字符串类型的响应数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }

        // 如果返回状态码是 401，跳转到登录页
        if (res.code === '401') {
            router.push('/login');
        }
        return res;
    },
    (error) => {
        console.error('Response error:', error); // Debug 信息
        return Promise.reject(error);
    }
);

export default request;
