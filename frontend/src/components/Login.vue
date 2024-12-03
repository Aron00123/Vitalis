<template>
  <div class="container">
    <div class="login-box">
      <div class="title">欢迎登录医院预约挂号系统</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input
              prefix-icon="el-icon-user"
              placeholder="请输入账号"
              v-model="form.id"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
              prefix-icon="el-icon-lock"
              placeholder="请输入密码"
              show-password
              v-model="form.password"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%;">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="医生" value="DOCTOR"></el-option>
            <el-option label="患者" value="USER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
              style="width: 100%; background-color: #7eb488; border-color: #7eb488; color: white"
              @click="onLogin"
          >
            登 录
          </el-button>
        </el-form-item>
        <div class="register-link">
          <span>还没有账号？点此</span>
          <router-link to="/register">注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import request from "../utils/request.js";

// 定义响应式数据
const form = reactive({
  id: "",
  password: "",
  role: "",
});

const rules = {
  username: [{required: true, message: "请输入账号", trigger: "blur"}],
  password: [{required: true, message: "请输入密码", trigger: "blur"}],
};

const formRef = ref(null);
const router = useRouter();

// 登录方法
const onLogin = () => {
  console.log(form)

  // if (form.username === 'admin' && form.password === '123456' && form.role === 'ADMIN') {
  //   localStorage.setItem("xm-user", JSON.stringify({
  //     id: 1,
  //     username: 'admin',
  //     password: '123456',
  //     name: 'admin',
  //     role: 'ADMIN'
  //   }));
  //   router.push("/");
  //   ElMessage.success("登录成功");
  //   return
  // }
  if (form.id === 'zhangsan' && form.password === '123456' && form.role === 'USER') {
    localStorage.setItem("xm-user", JSON.stringify({
      id: 1,
      username: 'zhangsan',
      password: '123456',
      name: 'zhangsan',
      role: 'USER'
    }));
    router.push("/");
    ElMessage.success("登录成功");
    return
  }

  formRef.value.validate((valid) => {
    if (valid) {
      // 发起登录请求
      request
          .post("/login", form)
          .then((res) => {
            if (res.code === "200") {
              // 存储用户数据到本地
              localStorage.setItem("xm-user", JSON.stringify(res.data));
              // 跳转主页
              router.push("/");
              ElMessage.success("登录成功");
            } else {
              ElMessage.error(res.msg || "登录失败");
            }
          })
          .catch((err) => {
            ElMessage.error("登录请求失败，请稍后重试");
            console.error(err);
          });

    } else {
      ElMessage.warning("请填写完整的登录信息");
    }
  });
};
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
  background-color: #f5f5f5;
}

.login-box {
  width: 400px;
  padding: 30px;
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  font-size: 20px;
  margin-bottom: 20px;
  color: #333;
}

.register-link {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}

a {
  color: #2a60c9;
}
</style>
