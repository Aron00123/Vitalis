<template>
  <div>
    <div class="card" style="padding: 15px">
      您好，{{ user?.name }}！欢迎使用本系统
    </div>

    <div style="display: flex; margin: 10px 0">
      <div style="width: 50%;" class="card">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold">
          公告列表
        </div>
        <div>
          <el-timeline reverse>
            <el-timeline-item
                v-for="item in notices"
                :key="item.id"
                :timestamp="item.time"
            >
              <el-popover
                  placement="right"
                  width="200"
                  trigger="hover"
                  :content="item.content"
              >
                <span>{{ item.title }}</span>
              </el-popover>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
// import request from "../../utils/request"; // 引入封装的 axios 实例

// 初始化用户信息和公告列表
const state = reactive({
  user: JSON.parse(localStorage.getItem("xm-user") || "{}"),
  notices: [],
});

// 获取公告数据
const fetchNotices = async () => {
  // state.notices = [{
  //   id: 1,
  //   title: "title",
  //   content:"content",
  //   time:"2024-5-6",
  //   user:"zhangsan"
  //
  // }]
  try {
    const res = await request.get("/notice/selectAll");
    state.notices = res.data || [];
  } catch (error) {
    console.error("公告数据获取失败：", error);
  }
};

// 在组件挂载时调用公告数据获取方法
onMounted(() => {
  fetchNotices();
});
</script>

<style scoped>
.card {
  background-color: #fff;
  border-radius: 5px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 15px;
  margin: 10px;
}
</style>
