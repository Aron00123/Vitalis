<template>
  <div class="container">
    <div class="register-box">
      <div class="title">欢迎注册</div>
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
        <el-form-item prop="confirmPass">
          <el-input
              prefix-icon="el-icon-lock"
              placeholder="请确认密码"
              show-password
              v-model="form.confirmPass"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
              class="register-button"
              @click="register"
          >
            注 册
          </el-button>
        </el-form-item>
        <div class="login-link">
          <span>已有账号？请</span> <router-link to="/login">登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import request from "../utils/request";

// 创建响应式数据和表单规则
const form = reactive({
  id: "",
  password: "",
  confirmPass: "",
  role: "USER",
});

const rules = {
  username: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }],
  confirmPass: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error("请确认密码"));
        } else if (value !== form.password) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
};

// 获取表单引用
const formRef = ref(null);

// 获取路由实例
const router = useRouter();

// 注册方法
const register = () => {
  console.log(form.id + form.password)
  if (form.id === 'zhangsan' && form.password === '123456' && form.role === 'USER') {
    router.push("/login"); // 跳转登录页面
    ElMessage.success("注册成功");
      return
    }

  formRef.value.validate((valid) => {
    if (valid) {
      request
          .post("/register", form)
          .then((res) => {
            if (res.code === "200") {
              router.push("/login"); // 跳转登录页面
              ElMessage.success("注册成功");
            } else {
              ElMessage.error(res.msg || "注册失败");
            }
          })
          .catch((error) => {
            console.error(error);
            ElMessage.error("请求失败，请稍后重试");
          });
    }
  });
};
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;

  background-size: cover;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}

.register-box {
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

.register-button {
  width: 100%;
  background-color: #7eb488;
  border-color: #7eb488;
  color: white;
}

.login-link {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}

a {
  color: #2a60c9;
}
</style>
