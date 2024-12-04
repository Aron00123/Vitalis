<template>
  <div class="container">
    <div class="register-box">
      <div class="title">欢迎注册</div>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="130px" style="padding-right: 50px">
        <el-form-item label="身份证号" prop="id">
          <el-input v-model="form.id" placeholder="请输入账号"></el-input>
        </el-form-item>

        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age" placeholder="年龄"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-input v-model="form.sex" placeholder="性别"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="紧急联系人电话" prop="emergencyPhone">
          <el-input v-model="form.emergencyPhone" placeholder="紧急联系人电话"></el-input>
        </el-form-item>
        <el-form-item label="家庭住址" prop="address">
          <el-input v-model="form.address" placeholder="家庭住址"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input show-password v-model="form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPass">
          <el-input show-password v-model="form.confirmPass" placeholder="请确认账号"></el-input>
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
          <span>已有账号？请</span>
          <router-link to="/login" style="text-decoration: none;">登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import request from "../utils/request";

// 创建响应式数据和表单规则
const form = reactive({
  id: "",
  password: "",
  confirmPass: "",
  name: "",
  age: "",
  sex: "",
  phone: "",
  emergencyPhone: "",
  email: "",
  address: "",
  role: "USER",
});

const rules = {
  id: [{
    validator: (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入身份证号"));
      } else if (value.length !== 18) {
        callback(new Error("请输入正确的身份证号"));
      } else {
        callback();
      }
    }, required: true, trigger: "blur"
  }],
  name: [{required: true, message: "请输入姓名", trigger: "blur"}],
  age: [{required: true, message: "请输入年龄", trigger: "blur"}],
  sex: [{required: true, message: "请输入性别", trigger: "blur"}],
  phone: [{required: true, message: "请输入电话", trigger: "blur"}],
  emergencyPhone: [{required: true, message: "请输入紧急联系人电话", trigger: "blur"}],

  address: [{required: true, message: "请输入家庭住址", trigger: "blur"}],
  password: [{required: true, message: "请输入密码", trigger: "blur"}],
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
      required: true
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
  width: 500px;
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
  background-color: #4F66D5FF;
  border-color: #4F66D5FF;
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
