<template>
  <div>
    <div class="card" style="padding: 15px; font-family: Arial, sans-serif; color: #333; box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); background-color: #f9f9f9; border-radius: 5px;">
        您好，<strong>{{ user.name ? user.name : 'ADMIN' }}</strong>！欢迎使用Vitalis医疗服务系统
    </div>

    <div style="display: flex; margin: 10px 0">
      <div style="width: 100%;" class="card">
        <div style="margin-bottom: 30px; font-size: 20px; font-weight: bold">
          公告列表
        </div>
        <div>
          <el-timeline style="max-width: 600px">
            <el-timeline-item v-for="item in notices"
                              :key="item.id"
                              :timestamp="item.time"
                              placement="top">
              <el-card>
                <h4>{{ item.title }}</h4>
                <p>{{ item.content }}</p>
              </el-card>
            </el-timeline-item>

          </el-timeline>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ElMessage} from "element-plus";
import {onMounted, ref} from "vue";
import request from "../../utils/request"; // 引入封装的 axios 实例

// 初始化用户信息和公告列表
const user = ref(JSON.parse(localStorage.getItem("xm-user") || "{}"));
const notices = ref([
  // {
  //   id: 2,
  //   title: '12月12日大版本更新公告',
  //   content: '各位忍者学员：\n' +
  //       '\n' +
  //       '      《火影忍者》手游新版本来袭。聊天系统全面升级，新增战区频道，支持语音录制转文字；组织合并功能正式上线；提升修罗天赋等级；新增神器草薙剑；忍者/通灵兽/秘卷平衡性调整。\n' +
  //       '\n' +
  //       ' \n' +
  //       '\n' +
  //       '       新版本将于12月12日0点至10点全区全服（iOS和安卓）停机更新，停机时间可能因特殊情况有所延长，具体以开服时间为准。停机更新补偿金币*100，完成版本更新奖励金币*200。安卓用户请重启游戏进行更新；苹果商店更新同步可能会有延迟，如未能检测到最新版本，请您耐心等待。',
  //   time: "2024-12-12",
  //   user: "zhangsan"
  // }, {
  //   id: 1,
  //   title: '11月29日更新公告，九周年福利第一弹即将开启',
  //   content: '各位忍者学员：\n' +
  //       '       《火影忍者》手游九周年福利第一弹即将开启；新忍者黑绝，免费限次体验；新忍者马基，上架十二月限定招募；危机来临，守护木叶村；雪球大作战，冬日限定忍者闯关；宇智波斑[秽土转生·解]上架特权商店；漩涡玖辛奈[冬日限定]限时返场；十二月签到更新。',
  //   time: '2024-11-28 17:39:31',
  //   user: 'zhangsan'
  // }
])

// 获取公告数据
const fetchNotices = async () => {
  console.log('wcnmd\n\n\n\n\n\n')
  request
      .post("/notice/selectAll")
      .then((res) => {
        if (res.code === "200") {
          notices.value = res.data ? res.data : [];
          console.log(notices)
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
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


