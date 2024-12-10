<template>
  <div class="container">

    <div class="login-signup">
      <div class="signin-panel">
        <h1>Vitalis</h1>
        <el-form :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="id">
            <el-input
                prefix-icon="User"
                placeholder="用户名"
                v-model="form.id"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                prefix-icon="Lock"
                placeholder="密码"
                show-password
                v-model="form.password"
            ></el-input>
          </el-form-item>

          <el-form-item>
            <el-button
                type="primary"
                class="signin-btn"
                @click="onLogin"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="signup-panel">
        <h2>你好，朋友！</h2>
        <p>填写您的信息，立即注册，开启便捷的医疗服务之旅！</p>
        <el-button type="primary" class="signup-btn" @click="goToRegister">
          注册
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

const form = reactive({
  id: "",
  password: "",
});

const rules = {
  id: [{required: true, message: "请输入账号", trigger: "blur"}],
  password: [{required: true, message: "请输入密码", trigger: "blur"}],
};

const formRef = ref(null);
const router = useRouter();

// 登录方法
const onLogin = () => {
  if (form.id === 'zhangsan' && form.password === '123456') {
    localStorage.setItem("xm-user", JSON.stringify({
      id: 'zhangsan',
      username: 'zhangsan',
      password: '123456',
      name: 'zhangsan',
      role: 'PATIENT'
    }));
    router.push("/");
    ElMessage.success("登录成功");
    return
  }
  if (form.id === 'admin' && form.password === '123456') {
    localStorage.setItem("xm-user", JSON.stringify({
      id: 'admin',
      username: 'admin',
      password: '123456',
      name: 'admin',
      role: 'ADMIN'
    }));
    router.push("/");
    ElMessage.success("登录成功");
    return
  }
  if (form.id === 'doctor' && form.password === '123456') {
    localStorage.setItem("xm-user", JSON.stringify({
      id: 'doctor',
      username: 'doctor',
      password: '123456',
      name: 'doctor',
      role: 'DOCTOR'
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
              let user = res.data.roleInfo;
              user.password = res.data.account.password;
              user.role = res.data.account.role;
              console.log(user)
              localStorage.setItem("xm-user", JSON.stringify(user));
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

const goToRegister = () => {
  router.push("/register");
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
  width: 850px;
  height: 500px;
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
