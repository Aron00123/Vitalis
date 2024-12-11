<template>
  <div>
    <el-card style="width: 60%; ; margin: 0 auto;">
      <el-form :model="user" label-width="130px" style="padding-right: 50px">
        <div style="margin: 15px; text-align: center">
          <!-- 上传图片按钮 -->
          <el-upload
              class="avatar-uploader"
              :show-file-list="false"
              :before-upload="uploadToThirdParty"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar"/>
            <el-icon v-if="!imageUrl" class="avatar-uploader-icon">
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
          <el-input v-model="user.consultDays" placeholder="坐诊日" disabled/>
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
          imageUrl.value = user.photo;
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });

};

const imageUrl = ref(""); // 存储返回的图片 URL

// 上传到第三方网站
const uploadToThirdParty = async (file) => {
  console.log("Received file:", file);
  if (!file) {
    console.error("File is invalid or was rejected by validation rules.");
    return false;
  }
  const formData = new FormData();
  formData.append("smfile", file); // 上传文件字段名为 smfile

  try {
    // 使用 fetch 将图片上传到 SM.MS
    const response = await fetch("http://localhost:9290/uploadImage", {
      method: "POST",
      //   headers: {
      //     "Authorization": "QqM8QZ7fWLfMWKETCFC5eyYGWpDdY1pJ", // 替换为实际的 API Token
      //   },
      body: formData,
    });

    const result = await response.json();
    console.log(result);

    if (result.success) {
      imageUrl.value = result.data.url; // 从响应中获取图片 URL
      ElMessage.success("图片上传成功！");
      request
          .post("/doctor/update", {
            id: user.id,
            photo: imageUrl.value
          })
          .then((res) => {
          })
          .catch((err) => {
            ElMessage.error("请求失败，请稍后重试");
          });
    } else {
      ElMessage.error(`上传失败：${result.message}`);
    }
  } catch (error) {
    console.error("上传失败:", error);
    ElMessage.error("图片上传失败，请稍后重试！");
  }

  return false; // 阻止 el-upload 默认上传行为
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