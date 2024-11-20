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
          <el-breadcrumb-item :to="{ path: route.path }">{{ route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <el-button> {{ user.name || '管理员' }} </el-button>
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
<!--        <el-col :span="12">-->

<!--          <el-menu-->
<!--              default-active="2"-->
<!--              class="el-menu-vertical-demo"-->
<!--              @open="handleOpen"-->
<!--              @close="handleClose"-->
<!--          >-->
<!--            <el-menu-item index="1" >-->
<!--              <el-icon></el-icon>-->
<!--              <span>系统首页</span>-->
<!--            </el-menu-item>-->

<!--            <el-sub-menu index="2" v-if="user.role === 'USER'">-->
<!--              <template #title>-->
<!--                <el-icon><location /></el-icon>-->
<!--                <span>预约就诊</span>-->
<!--              </template>-->
<!--              <el-menu-item-group title="Group One">-->
<!--                <el-menu-item index="1-1">item one</el-menu-item>-->
<!--                <el-menu-item index="1-2">item two</el-menu-item>-->
<!--              </el-menu-item-group>-->
<!--              <el-menu-item-group title="Group Two">-->
<!--                <el-menu-item index="1-3">item three</el-menu-item>-->
<!--              </el-menu-item-group>-->
<!--              <el-sub-menu index="1-4">-->
<!--                <template #title>item four</template>-->
<!--                <el-menu-item index="1-4-1">item one</el-menu-item>-->
<!--              </el-sub-menu>-->
<!--            </el-sub-menu>-->

<!--          </el-menu>-->
<!--        </el-col>-->


        <el-menu :default-openeds="['info', 'reserve', 'user']" router style="border: none" :default-active="route.path"
                 @open="handleOpen" @close="handleClose">
                    <el-menu-item index="/home">
                      <i class="el-icon-s-home"></i>
                      <span slot="title">系统首页</span>
                    </el-menu-item>

                    <el-sub-menu index="info" v-if="user.role !== 'USER'">
                      <template #title>
                        <i class="el-icon-menu"></i><span>信息管理</span>
                      </template>
                      <el-menu-item index="/notice" v-if="user.role === 'ADMIN'">公告信息</el-menu-item>
                      <el-menu-item index="/department" v-if="user.role === 'ADMIN'">科室信息</el-menu-item>
                      <el-menu-item index="/plan">医生排班</el-menu-item>
                    </el-sub-menu>

                    <el-sub-menu index="reserve">
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

                    <el-sub-menu index="user" v-if="user.role === 'ADMIN'">
                      <template #title>
                        <i class="el-icon-menu"></i><span>用户管理</span>
                      </template>

                      <el-menu-item index="/admin">管理员信息</el-menu-item>
                      <el-menu-item index="/doctor">医生信息</el-menu-item>
                      <el-menu-item index="/user">患者信息</el-menu-item>
                    </el-sub-menu>

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
console.log(localStorage.getItem('xm-user'))
console.log('manager')
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';

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
