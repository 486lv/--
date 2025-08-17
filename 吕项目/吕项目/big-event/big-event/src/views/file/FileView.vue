<script setup>
import { ref } from 'vue'
const previewUrl = ref('')
const previewType = ref('')
const previewVisible = ref(false)
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import { fileListService, fileUploadService, fileDeleteService,fileDownloadService, filePreviewService} from '@/api/file.js'
import request from '@/utils/request.js'


const files = ref([])
const pageNum = ref(1)
const pageSize = ref(5)
const total = ref(0)
const searchName = ref('')
const opStatus = ref('')
const opResult = ref('')


// 获取文件列表，增加 filename 参数
const fetchFiles = async () => {
  try {
    const res = await fileListService({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      filename: searchName.value
    })
    files.value = res?.items || []
    total.value = res?.total || 0
    opStatus.value = '文件列表加载成功'
    opResult.value = ''
  } catch (e) {
    opStatus.value = '文件列表加载失败'
    opResult.value = e.message || (e.response && e.response.data && e.response.data.message) || '未知错误'
  }
}

// 搜索
const onSearch = () => {
  pageNum.value = 1
  fetchFiles()
}
// 上传文件
const uploadFile = async (option) => {
  const formData = new FormData()
  formData.append('file', option.file)
  try {
    await fileUploadService(formData)
    ElMessage.success('上传成功')
    fetchFiles()
    opStatus.value = '上传成功'
    opResult.value = ''
  } catch (e) {
    ElMessage.error('上传失败')
    opStatus.value = '上传失败'
    opResult.value = e.message
  }
}

//下载文件
// 下载文件
const downloadFile = async (filename) => {
  try {
    const response = await fileDownloadService(filename)
    const blob = response.data || response
    if (blob.type === 'application/json') {
      const text = await blob.text()
      const error = JSON.parse(text)
      throw new Error(error.message || '下载失败')
    }
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', filename)
    document.body.appendChild(link)
    link.click()
    link.remove()
    window.URL.revokeObjectURL(url)
    opStatus.value = '下载成功'
    opResult.value = ''
  } catch (e) {
    ElMessage.error('下载失败')
    opStatus.value = '下载失败'
    opResult.value = e.message
  }
}
// 删除文件
const deleteFile = (row) => {
  ElMessageBox.confirm(
      '你确认要删除该文件吗?',
      '温馨提示',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        await fileDeleteService(row.filename)
        ElMessage.success('删除成功')
        fetchFiles()
        opStatus.value = '删除成功'
        opResult.value = ''
      })
      .catch(() => {
        ElMessage.info('用户取消了删除')
      })
}

// FileView.vue
const previewFile = async (filename) => {
  try {
    const response = await filePreviewService(filename)
    const blob = response.data || response
    if (blob.type === 'application/json') {
      const text = await blob.text()
      const error = JSON.parse(text)
      throw new Error(error.message || '预览失败')
    }
    previewUrl.value = window.URL.createObjectURL(blob)
    const ext = filename.split('.').pop().toLowerCase()
    if (['png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp'].includes(ext)) {
      previewType.value = 'image'
    } else if (['pdf'].includes(ext)) {
      previewType.value = 'pdf'
    } else if (['txt', 'md', 'log', 'json', 'js', 'css', 'html'].includes(ext)) {
      previewType.value = 'text'
    } else {
      previewType.value = 'other'
    }
    previewVisible.value = true
  } catch (e) {
    ElMessage.error('预览失败')
    opStatus.value = '预览失败'
    opResult.value = e.message
  }
}

// 分页事件
const onSizeChange = (size) => {
  pageSize.value = size
  fetchFiles()
}
const onCurrentChange = (num) => {
  pageNum.value = num
  fetchFiles()
}

fetchFiles()
</script>

<template>
  <el-card class="page-container">
    <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>文件管理</span>
        <div class="extra">
          <el-upload
              :show-file-list="false"
              :http-request="uploadFile"
              accept="*"
          >
            <el-button type="primary" :icon="Plus">上传文件</el-button>
          </el-upload>
        </div>
      </div>
    </template>
      <div style="margin-bottom: 10px;">
        <el-input
            v-model="searchName"
            placeholder="搜索文件名"
            style="width: 200px; margin-right: 10px;"
            @keyup.enter="onSearch"
        />
        <el-button type="primary" @click="onSearch">搜索</el-button>
      </div>
      <!-- 其余内容不变 -->

    <el-table :data="files" style="width: 100%">
      <el-table-column label="文件名" prop="filename"></el-table-column>
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-button type="primary" @click="downloadFile(row.filename)">下载</el-button>
          <el-button type="success" @click="previewFile(row.filename)">预览</el-button>
          <el-button :icon="Delete" circle plain type="danger" @click="deleteFile(row)"></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :page-sizes="[5, 10, 20]"
        layout="jumper, total, sizes, prev, pager, next"
        background
        :total="total"
        @size-change="onSizeChange"
        @current-change="onCurrentChange"
        style="margin-top: 20px; justify-content: flex-end"
    />
    <div v-if="opStatus" style="margin-top: 10px;">
      <el-alert :title="opStatus" :description="opResult" type="info" show-icon />
    </div>
    <!-- 预览弹窗 -->
    <el-dialog v-model="previewVisible" title="文件预览" width="60%" :close-on-click-modal="false">
      <template v-if="previewType === 'image'">
        <img :src="previewUrl" style="max-width: 100%;" />
      </template>
      <template v-else-if="previewType === 'pdf'">
        <iframe :src="previewUrl" width="100%" height="600px"></iframe>
      </template>
      <template v-else-if="previewType === 'text'">
        <iframe :src="previewUrl" width="100%" height="400px" style="border:none"></iframe>
      </template>
      <template v-else>
        <p>暂不支持该类型预览，可下载后查看。</p>
      </template>
    </el-dialog>
  </el-card>
  </el-card>
</template>

<style lang="scss" scoped>
.page-container {
  min-height: 100%;
  box-sizing: border-box;

  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}
</style>