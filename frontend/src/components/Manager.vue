<template>
  <div class="manager-container">
    <!-- 头部 -->
    <div class="manager-header">
      <div class="manager-header-left">

        <div class="title">医院预约挂号系统</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: route.path }">
            {{ route.meta.name }}
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img
                :src="
                user.avatar ||
                'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
              "
            />
            <div style="color: #666666">{{ user.name || "管理员" }}</div>
          </div>
          <el-dropdown-menu>
            <el-dropdown-item @click="goToPerson">个人信息</el-dropdown-item>
            <el-dropdown-item @click="goToPassword">修改密码</el-dropdown-item>
            <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 主体 -->
    <div class="manager-main">
      <!-- 侧边栏 -->
      <div class="manager-main-left">
        <el-menu
            :default-openeds="['info', 'reserve', 'user']"
            router
            style="border: none"
            :default-active="route.path"
        >
          <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <el-submenu index="info" v-if="user.role !== 'USER'">
            <template #title>
              <i class="el-icon-menu"></i><span>信息管理</span>
            </template>
            <el-menu-item index="/notice" v-if="user.role === 'ADMIN'">
              公告信息
            </el-menu-item>
            <el-menu-item index="/department" v-if="user.role === 'ADMIN'">
              科室信息
            </el-menu-item>
            <el-menu-item index="/plan">医生排班</el-menu-item>
          </el-submenu>
          <el-submenu index="reserve">
            <template #title>
              <i class="el-icon-menu"></i><span>预约就诊</span>
            </template>
            <el-menu-item index="/doctorCard">预约挂号</el-menu-item>
            <el-menu-item index="/reserve">我的挂号</el-menu-item>
            <el-menu-item index="/record">我的就诊</el-menu-item>
          </el-submenu>
          <el-submenu index="user" v-if="user.role === 'ADMIN'">
            <template #title>
              <i class="el-icon-menu"></i><span>用户管理</span>
            </template>
            <el-menu-item index="/admin">管理员信息</el-menu-item>
            <el-menu-item index="/doctor">医生信息</el-menu-item>
            <el-menu-item index="/user">患者信息</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!-- 数据表格 -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";

// 初始化用户信息
const state = reactive({
  user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
});

// 路由实例
const router = useRouter();
const route = useRoute();

// 用户更新逻辑
const updateUser = () => {
  state.user = JSON.parse(localStorage.getItem("xm-user") || "{}");
};

// 页面挂载时检查用户状态
onMounted(() => {
  if (!state.user.id) {
    router.push("/login");
  }
});

// 方法：跳转到个人信息页
const goToPerson = () => {
  if (state.user.role === "ADMIN") {
    router.push("/adminPerson");
  } else if (state.user.role === "DOCTOR") {
    router.push("/doctorPerson");
  } else if (state.user.role === "USER") {
    router.push("/userPerson");
  }
};

// 方法：跳转到修改密码页
const goToPassword = () => {
  router.push("/password");
};

// 方法：退出登录
const logout = () => {
  localStorage.removeItem("xm-user");
  router.push("/login");
};
</script>

<style scoped>

</style>
