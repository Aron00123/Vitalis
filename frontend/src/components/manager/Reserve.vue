<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入挂号状态" style="width: 200px" v-model="status"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="患者姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="挂号时间"></el-table-column>
        <el-table-column prop="status" label="挂号状态"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="danger" size="mini" v-if="scope.row.status === '未叫号' && user.role === 'USER'" @click="del(scope.row.id)">取消挂号</el-button>
            <el-button plain type="warning" size="mini" v-if="user.role === 'DOCTOR'" @click="call(scope.row)">叫号</el-button>
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
import { ref, onMounted } from 'vue'

// 定义响应式数据
const tableData = ref([])  // 所有的数据
const pageNum = ref(1)     // 当前的页码
const pageSize = ref(10)   // 每页显示的个数
const total = ref(0)
const status = ref(null)   // 挂号状态
const user = JSON.parse(localStorage.getItem('xm-user') || '{}')

// 处理“叫号”操作
const call = (row) => {
  let reserveData = JSON.parse(JSON.stringify(row))
  reserveData.status = '已叫号'
  $request.put('/reserve/update', reserveData).then(res => {
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
  $confirm('您确定取消挂号吗？这个医生不好挂哦！', '灵魂拷问', { type: 'warning' }).then(() => {
    $request.delete(`/reserve/delete/${id}`).then(res => {
      if (res.code === '200') {
        $message.success('操作成功')
        load(1)
      } else {
        $message.error(res.msg)
      }
    })
  }).catch(() => {})
}

// 加载数据
const load = (pageNum) => {
  if (pageNum) pageNum = pageNum
  $request.get('/reserve/selectPage', {
    params: {
      pageNum,
      pageSize: pageSize.value,
      status: status.value,
    }
  }).then(res => {
    tableData.value = res.data?.list
    total.value = res.data?.total
  })
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
/* 样式代码可以继续放在这里 */
</style>
