<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import { Delete, Edit } from "@element-plus/icons-vue"
import {hadoopList} from "@/api/article";

// 定义状态变量
const result = ref([])  // 用于存储 Hadoop 作业的结果
const jobStatus = ref('')  // 存储作业状态
const jobResult = ref('')  // 存储作业结果信息

// 控制点击按钮时发起请求
const runHadoopJob = async () => {
  try {
    //clearData();
    // 向后端发送请求，获取 Hadoop 作业的结果
    const response = await hadoopList();
    // 获取并存储作业结果
    result.value = Object.entries(response.data).map(([word,cnt]) => ({word,cnt}))
    jobResult.value = `Hadoop作业成功，结果为: ${JSON.stringify(result.value)}`
    jobStatus.value = '成功'
    ElMessage.success('Hadoop作业已成功完成！')
  } catch (error) {
    jobStatus.value = '失败'
    jobResult.value = `错误: ${error.message}`
    ElMessage.error('Hadoop作业失败！')
  }
}

// 清空结果数据
const clearData = () => {
  result.value = []
  jobStatus.value = ''
  jobResult.value = ''
}
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>Hadoop 作业</span>
      </div>
    </template>
    <div class="extra">
      <el-button type="primary" @click="runHadoopJob">启动 Hadoop 作业</el-button>
    </div>
    <el-divider></el-divider>

    <!-- 显示作业状态 -->
    <div v-if="jobStatus">
      <h3>作业状态: {{ jobStatus }}</h3>
      <p v-if="jobResult">结果: {{ jobResult }}</p>
    </div>

    <!-- 显示 Hadoop 作业结果 -->
    <el-table :data="result" style="width: 100%">
      <el-table-column label="文章单词" prop="word" width="200"></el-table-column>
      <el-table-column label="单词词频" prop="cnt" width="200"></el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
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