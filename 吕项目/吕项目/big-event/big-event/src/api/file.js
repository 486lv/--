import request from '@/utils/request.js'

export const fileListService = (params) => {
    return request.get('/api/files', { params })
}

export const fileUploadService = (formData) => {
    return request.post('/api/files', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
    })
}

export const fileDeleteService = (filename) => {
    return request.delete(`/api/files/${filename}`)
}

// 正确导出下载服务
export const fileDownloadService = (filename) => {
    return request({
        url: `/api/files/${encodeURIComponent(filename)}`,
        method: 'GET',
        responseType: 'blob'
    })
}

// src/api/file.js
export const filePreviewService = (filename) => {
    return request({
        url: `/api/files/preview/${encodeURIComponent(filename)}`,
        method: 'GET',
        responseType: 'blob'
    })
}