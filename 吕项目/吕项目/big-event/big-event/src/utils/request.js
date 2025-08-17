// src/utils/request.js
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useTokenStore } from '@/stores/token.js'
import router from '@/router'

const baseURL = '/api'
const instance = axios.create({ baseURL })

// 请求拦截器
instance.interceptors.request.use(
    config => {
        const tokenStore = useTokenStore()
        if (tokenStore.token) {
            config.headers.Authorization = tokenStore.token
        }
        return config
    },
    err => Promise.reject(err)
)

// 响应拦截器
instance.interceptors.response.use(
    response => {
        // 关键：下载接口返回完整 response
        if (response.config.responseType === 'blob') {
            return response
        }
        // 只处理 HTTP 层面，业务错误不 reject
        return response.data
    },
    err => {
        if (err.response && err.response.status === 401) {
            ElMessage.error('请先登录')
            router.push('/login')
        }
        return Promise.reject(err)
    }
)

export default instance