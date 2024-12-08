<template>
  <div>
    <el-card style="width: 60%; ; margin: 0 auto;">
      <el-form :model="user" label-width="130px" style="padding-right: 50px">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              :action="`${$baseUrl}/files/upload`"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.photo" :src="user.photo" class="avatar"/>
            <el-icon v-else class="avatar-uploader-icon">
              <Plus/>
            </el-icon>
          </el-upload>
        </div>
        <el-form-item label="账号" prop="id">
          <el-input v-model="user.id" placeholder="账号" disabled/>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名" disabled/>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-input v-model="user.gender" placeholder="性别" disabled/>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="user.title" placeholder="职称" disabled/>
        </el-form-item>
        <el-form-item label="主治疾病" prop="specialty">
          <el-input type="textarea" :rows="2" v-model="user.specialty" placeholder="主治疾病"/>
        </el-form-item>
        <el-form-item label="科室" prop="departmentName">
          <el-input v-model="user.departmentId" placeholder="科室" disabled/>
        </el-form-item>
        <el-form-item label="就诊限额" prop="consultLimit">
          <el-input v-model="user.consultLimit" placeholder="就诊限额" disabled/>
        </el-form-item>
        <el-form-item label="坐诊日" prop="consultDays">
          <el-input v-model="user.consultLimit" placeholder="坐诊日" disabled/>
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input type="textarea" :rows="4" v-model="user.description" placeholder="简介"/>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>

  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue';
import request from "../../utils/request.js";
import {ElMessage} from "element-plus";

// Reactive states
const user = reactive(JSON.parse(localStorage.getItem('xm-user') || '{}'));
const fromVisible = ref(false);

// Fetch user data
const getPerson = () => {
  request
      .post("/doctor/selectById", user)
      .then((res) => {
        if (res.code === "200") {
          Object.assign(user, res.data);
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });

};


// Update user info on the server
const update = () => {
  request
      .post("/doctor/update", user)
      .then((res) => {
        if (res.code === "200") {
          fromVisible.value = false;
          ElMessage.success('保存成功');
          localStorage.setItem('xm-user', JSON.stringify(user));
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
};

// Handle avatar upload success
const handleAvatarSuccess = (response, file, fileList) => {
  user.avatar = response.data;
};

// On mounted lifecycle hook
onMounted(() => {
  getPerson();
});
</script>


<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 50%;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>