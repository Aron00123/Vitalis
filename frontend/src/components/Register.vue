<template>
  <div class="container">

    <div class="login-signup">
      <div class="signin-panel">
        <h1>Vitalis</h1>
        <el-form :model="form" :rules="rules" ref="formRef" label-width="130px" style="padding-right: 30px">
          <el-form-item label="身份证号" prop="id">
            <el-input v-model="form.id" placeholder="请输入账号"></el-input>
          </el-form-item>

          <el-form-item label="姓名" prop="name">
            <el-input v-model="form.name" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input v-model="form.age" placeholder="年龄"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-input v-model="form.gender" placeholder="性别"></el-input>
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
                type="primary"
                class="signin-btn"
                @click="onRegister"
            >
              注册
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="signup-panel">
        <h2>欢迎回来！</h2>
        <p>为了与我们保持联系，请使用您的个人信息登录系统。</p>
        <el-button type="primary" class="signup-btn" @click="goToSignin">
          去登录
        </el-button>
      </div>
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
  name: "",
  age: "",
  gender: "",
  phone: "",
  emergencyPhone: "",
  email: "",
  address: "",
  role: "PATIENT",
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
  if (form.id === 'zhangsan' && form.password === '123456' && form.role === 'PATIENT') {
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

const goToSignin = () => {
  router.push("/login");
};
</script>

<style scoped>
.container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: url("https://bpic.588ku.com/back_pic/06/39/09/5064337d6783a0a.jpg") no-repeat center;
  background-size: cover;
}

.login-signup {
  display: flex;
  width: 950px;
  height: 700px;
  background: white;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
}

.signin-panel,
.signup-panel {
  width: 50%;
  padding: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.signin-panel {
  background: #fff;
}

.signup-panel {
  background: linear-gradient(to right, #4284db, #29eac4);
  color: white;
  text-align: center;
}

.signup-panel h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.signup-panel p {
  margin-bottom: 20px;
}

.signin-btn,
.signup-btn {
  background-color: #4284db;
  color: white;
  width: 100%;
}

</style>
