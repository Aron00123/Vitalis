<template>
  <div>
    <div class="search" style="padding-bottom: 20px">
      <el-input placeholder="请输入标题查询" style="width: 200px" v-model="id"/>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation" style="padding-bottom: 20px">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column prop="id" label="序号" width="70" align="center" sortable/>
        <el-table-column prop="title" label="标题"/>
        <el-table-column prop="content" label="内容" width="400"/>
        <el-table-column prop="time" label="创建时间"/>
        <el-table-column prop="user" label="创建人" width="150"/>
        <el-table-column label="操作" align="center" width="180">
          <template #default="{ row }">
            <el-button size="mini" type="primary" plain @click="handleEdit(row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination" style="padding-top: 20px">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total"
        />
      </div>
    </div>

    <el-dialog
        title="公告信息"
        v-model="formVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >

      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" :rows="5" v-model="form.content" placeholder="内容"/>
        </el-form-item>

      </el-form>

      <template #footer>
        <el-button @click="closeDialog">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from "../../utils/request"; // 替换为实际的请求工具

// const tableData = ref([]);
const tableData = ref([{
  id: 2,
  title: '12月12日大版本更新公告',
  content: '各位忍者学员：\n' +
      '\n' +
      '      《火影忍者》手游新版本来袭。聊天系统全面升级，新增战区频道，支持语音录制转文字；组织合并功能正式上线；提升修罗天赋等级；新增神器草薙剑；忍者/通灵兽/秘卷平衡性调整。\n' +
      '\n' +
      ' \n' +
      '\n' +
      '       新版本将于12月12日0点至10点全区全服（iOS和安卓）停机更新，停机时间可能因特殊情况有所延长，具体以开服时间为准。停机更新补偿金币*100，完成版本更新奖励金币*200。安卓用户请重启游戏进行更新；苹果商店更新同步可能会有延迟，如未能检测到最新版本，请您耐心等待。',
  time: "2024-12-12",
  user: "zhangsan"
}, {
  id: 1,
  title: '11月29日更新公告，九周年福利第一弹即将开启',
  content: '各位忍者学员：\n' +
      '       《火影忍者》手游九周年福利第一弹即将开启；新忍者黑绝，免费限次体验；新忍者马基，上架十二月限定招募；危机来临，守护木叶村；雪球大作战，冬日限定忍者闯关；宇智波斑[秽土转生·解]上架特权商店；漩涡玖辛奈[冬日限定]限时返场；十二月签到更新。',
  time: '2024-11-28 17:39:31',
  user: 'zhangsan'
}])
const pageNum = ref(1);
const pageSize = ref(10);
// const total = ref(0);
const total = ref(1)

const id = ref("");

const formVisible = ref(false);
const isHandleAdd = ref(false);
const form = reactive({});
const rules = reactive({
  title: [{required: true, message: "请输入公告标题", trigger: "blur"}],
  content: [{required: true, message: "请输入公告内容", trigger: "blur"}],
});
const ids = ref([]);

const load = (page = 1) => {
  pageNum.value = page;
  request
      .post("/notice/selectPage", {
        pageNum: pageNum.value, pageSize: pageSize.value, username: id.value
      })
      .then((res) => {
        tableData.value = res.data?.list || [];
        total.value = res.data?.total || 0;
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
};

const handleAdd = () => {
  Object.assign(form, {});
  formVisible.value = true;
  isHandleAdd.value = true;
};

const handleEdit = (row) => {
  Object.assign(form, {...row});
  formVisible.value = true;
};

const save = () => {
  const date = new Date()
  let year = date.getFullYear()
  let month = date.getMonth() + 1
  let day = date.getDate()
  form.time = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`
  request
      .post(isHandleAdd.value ? "/notice/add" : "/notice/update", form)
      .then((res) => {
        if (res.code === "200") {
          ElMessage.success("保存成功");
          load(1);
          formVisible.value = false;
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });

  isHandleAdd.value = false;
};

const closeDialog = () => {
  formVisible.value = false;
  isHandleAdd.value = false;
}

const del = (id) => {
  ElMessageBox.confirm("您确定删除吗？", "确认删除", {
    type: "warning", confirmButtonText: "确认", cancelButtonText: "取消"
  }).then(() => {
    request
        .post("/notice/delete", {id: id})
        .then((res) => {
          if (res.code === "200") {
            ElMessage.success("操作成功");
            load(1);
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });

  });
};

const handleSelectionChange = (rows) => {
  ids.value = rows.map((row) => row.id);
};

const delBatch = () => {
  if (!ids.value.length) {
    ElMessage.warning("请选择数据");
    return;
  }
  ElMessageBox.confirm("您确定批量删除这些数据吗？", "确认删除",
      {type: "warning", confirmButtonText: "确认", cancelButtonText: "取消"}).then(() => {
    request
        .post("/notice/delete/batch", {ids: ids.value})
        .then((res) => {
          if (res.code === "200") {
            ElMessage.success("操作成功");
            load(1);
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  });
};

const reset = () => {
  id.value = "";
  load(1);
};

const handleCurrentChange = (page) => {
  load(page);
};

onMounted(() => {
  load(1);
});
</script>

<style scoped>
</style>
