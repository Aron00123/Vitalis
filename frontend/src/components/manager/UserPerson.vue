<template>
  <div>
    <el-card style="width: 60%; ; margin: 0 auto;"  >
      <el-form :model="user" label-width="130px" style="padding-right: 50px">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              :action="`${$baseUrl}/files/upload`"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.avatar" :src="user.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="身份证号" prop="id">
          <el-input v-model="user.id" placeholder="身份证号" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名" disabled></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="user.age" placeholder="年龄" disabled></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-input v-model="user.sex" placeholder="性别" disabled></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="紧急联系人电话" prop="emergencyPhone">
          <el-input v-model="user.emergencyPhone" placeholder="紧急联系人电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="家庭住址" prop="address">
          <el-input v-model="user.address" placeholder="家庭住址"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import request from "../../utils/request.js";
import {ElMessage} from "element-plus";

// Reactive states
const user = reactive(JSON.parse(localStorage.getItem('xm-user') || '{}'));
const fromVisible = ref(false);

// Fetch user data
const getPerson = () => {
  request
      .post("/user/selectById", user)
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
  this.$request.put('/user/update', user).then(res => {
    if (res.code === '200') {
      fromVisible.value = false;
      this.$message.success('保存成功');
      localStorage.setItem('xm-user', JSON.stringify(user));
      this.$emit('update:user');
    } else {
      this.$message.error(res.msg);
    }
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
/deep/.el-form-item__label {
  font-weight: bold;
}
/deep/.el-upload {
        border-radius: 50%;
      }
.avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        border-radius: 50%;
      }
.avatar-uploader .el-upload:hover {
        border-color: #409EFF;
      }
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}
</style>
