<template>
  <div>
    <div class="search">
      <el-select v-model="status" placeholder="挂号状态查询" style="width: 200px">
        <el-option v-for="item in statusData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="挂号单" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="患者姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctor" label="医生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="date" label="挂号时间"></el-table-column>
        <el-table-column prop="status" label="挂号状态"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="danger" size="mini" v-if="scope.row.status === '未就诊' && user.role === 'PATIENT'"
                       @click="del(scope.row.id)">取消挂号
            </el-button>
            <el-button plain type="primary" size="mini"
                       v-else-if="scope.row.status === '已就诊' && user.role === 'PATIENT'"
                       @click="">查看就诊信息
            </el-button>
            <el-button plain type="warning" size="mini" v-if="user.role === 'DOCTOR'" @click="call(scope.row)">叫号
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>

</template>

<script setup>
import {ref, onMounted} from 'vue'
import request from "../../utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

// 定义响应式数据
//const tableData = ref([])  // 所有的数据
const tableData = ref([
  {
    id: 12062,
    name: 'zhangsan',
    doctor: 'doctorWang',
    date: "2024-12-3 15:23",
    status: "已就诊"
  }, {
    id: 14197,
    name: 'zhangsan',
    doctor: 'doctorLi',
    date: "2024-12-4 10:52",
    status: "未就诊"
  }
])
const pageNum = ref(1)     // 当前的页码
const pageSize = ref(10)   // 每页显示的个数
const total = ref(0)
const statusData = [
  {
    id: 1,
    name: '未就诊'
  },
  {
    id: 2,
    name: '已就诊'
  }
]
const status = ref(null)   // 挂号状态
const user = JSON.parse(localStorage.getItem('xm-user') || '{}')

// 处理“叫号”操作
const call = (row) => {
  let reserveData = JSON.parse(JSON.stringify(row))
  reserveData.status = '已叫号'
  $request.put('/registration/update', reserveData).then(res => {
    if (res.code === '200') {
      $message.success('叫号成功')
      load(1)
      record(row)
    }
  })
}

// 同步就诊记录
const record = (row) => {
  const data = {
    userId: row.userId,
    doctorId: row.doctorId,
  }
  $request.post('/record/add', data).then(res => {
    if (res.code === '200') {
      $message.success('数据同步成功')
    } else {
      $message.error(res.msg)
    }
  })
}

// 取消挂号操作
const del = (id) => {
  ElMessageBox.confirm("您确定取消挂号吗？", "取消挂号", {
    type: "warning", confirmButtonText: "确认", cancelButtonText: "取消"
  }).then(() => {
    request
        .post("/registration/delete", id)
        .then((res) => {
          if (res.code === "200") {
            ElMessage.error('取消成功');
            load(1);
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  });
}

// 加载数据
const load = (pageNum1) => {
  if (pageNum1) pageNum.value = pageNum1
  request
      .post("/registration/selectPage", {
        params: {
          pageNum,
          pageSize: pageSize.value,
          status: status.value,
          id: user.value.id
        }
      })
      .then((res) => {
        if (res.code === "200") {
          tableData.value = res.data?.list
          total.value = res.data?.total
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
}

// 重置
const reset = () => {
  status.value = null
  load(1)
}

// 处理页码变化
const handleCurrentChange = (pageNum) => {
  load(pageNum)
}

// 初始加载
onMounted(() => {
  load(1)
})
</script>

<style scoped>

.search {
  padding-bottom: 20px;
}

.table {
  padding-bottom: 20px;
}

.pagination {
  padding-top: 20px;
}
</style>
