<template>
  <div class="manager-container">
    <!-- 头部 -->
    <div class="manager-header">
      <div class="manager-header-left">

        <div class="title">Vitalis医疗服务系统</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/welcome' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: route.path }" v-if="route.meta.name !== '欢迎页'">{{
              route.meta.name
            }}
          </el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: route.path }" v-else></el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <!--            <img :src="user.avatar" || />-->
            <el-avatar :size="40" :src="user.avatar" v-if="user.avatar"/>
            <el-avatar :size="40" :src="'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" v-else/>
            <el-button> {{ user.name || '管理员' }}</el-button>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="goToPerson">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

      </div>
    </div>

    <!-- 主体 -->
    <div class="manager-main">
      <!-- 侧边栏 -->
      <div class="manager-main-left">
        <el-menu :default-openeds="['info', 'reserve', 'user']" router style="border: none" :default-active="route.path"
                 @open="handleOpen" @close="handleClose">
          <el-menu-item index="/welcome">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>

          <!--  病人   -->
          <el-sub-menu index="reserve" v-if="user.role === 'USER'">
            <template #title>
              <el-icon></el-icon>
              <span>预约就诊</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/doctorCard">预约挂号</el-menu-item>
              <el-menu-item index="/reserve">我的挂号</el-menu-item>
              <el-menu-item index="/record">我的就诊</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>

          <!--  医生   -->
          <el-sub-menu index="reserve" v-if="user.role === 'DOCTOR'">
            <template #title>
              <el-icon></el-icon>
              <span>诊断治疗</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/reserve">待处理挂号</el-menu-item>
              <el-menu-item index="/record">诊断记录</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>

          <!-- 管理员   -->
          <el-sub-menu index="user" v-if="user.role === 'ADMIN'">
            <template #title>
              <el-icon></el-icon>
              <i class="el-icon-menu"></i><span>用户信息管理</span>
            </template>
            <el-menu-item index="/doctor">医生管理</el-menu-item>
            <el-menu-item index="/user">患者管理</el-menu-item>
            <el-menu-item index="/admin">管理员管理</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="info" v-if="user.role === 'ADMIN'">
            <template #title>
              <el-icon></el-icon>
              <i class="el-icon-menu"></i><span>其他管理</span>
            </template>
            <el-menu-item index="/notice">公告信息</el-menu-item>
            <el-menu-item index="/department">科室信息</el-menu-item>
            <el-menu-item index="/plan">医生排班</el-menu-item>
          </el-sub-menu>


        </el-menu>
      </div>

      <!-- 数据表格 -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser"/>
      </div>

      <!-- 返回顶部 -->
      <el-backtop :bottom="100" :right="150">
        <div
            style="
        height: 100%;
        width: 100%;
        background-color: var(--el-bg-color-overlay);
        box-shadow: var(--el-box-shadow-lighter);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      "
        >
          UP
        </div>
      </el-backtop>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import {useRouter, useRoute} from 'vue-router';

// 路由实例
const router = useRouter();
const route = useRoute();

// 用户信息
const user = ref(JSON.parse(localStorage.getItem('xm-user') || '{}'));

// 页面初始化检查用户登录状态
onMounted(() => {
  if (!user.value.id) {
    router.push('/login');
  }
});

// 更新用户信息
function updateUser() {
  user.value = JSON.parse(localStorage.getItem('xm-user') || '{}');
}

// 跳转到个人信息页面
function goToPerson() {
  if (user.value.role === 'ADMIN') {
    router.push('/adminPerson');
  } else if (user.value.role === 'DOCTOR') {
    router.push('/doctorPerson');
  } else if (user.value.role === 'USER') {
    router.push('/userPerson');
  }
}

// 用户退出登录
function logout() {
  localStorage.removeItem('xm-user');
  router.push('/login');
}
</script>

<style scoped>
@import "../assets/css/manager.css";
</style>
