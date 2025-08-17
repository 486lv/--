<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import {updatePasswordService, userInfoUpdateService} from '@/api/user.js'

const form = ref({
  oldPwd: '',
  newPwd: '',
  rePwd: ''
})

const rules = {
  oldPwd: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPwd: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
  rePwd: [{ required: true, message: '请确认新密码', trigger: 'blur' }]
}

const updatePassword = async () => {
  if (form.value.newPwd !== form.value.rePwd) {
    ElMessage.error('新密码和确认新密码不一致')
    return
  }

    let result = await updatePasswordService({
      old_pwd: form.value.oldPwd,
      new_pwd: form.value.newPwd,
      re_pwd: form.value.rePwd
    })
    ElMessage.success(result.msg? result.msg : '修改成功');
}
</script>

<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>重置密码</span>
      </div>
    </template>
    <el-row>
      <el-col :span="12">
        <el-form :model="form" :rules="rules" label-width="100px" size="large">
          <el-form-item label="原密码" prop="oldPwd">
            <el-input v-model="form.oldPwd" type="password"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="newPwd">
            <el-input v-model="form.newPwd" type="password"></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="rePwd">
            <el-input v-model="form.rePwd" type="password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updatePassword">提交修改</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>