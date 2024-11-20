<template>
<!--  <div>-->
<!--    <div class="search">-->
<!--      <el-select v-model="departmentId" placeholder="请选择科室" style="width: 200px">-->
<!--        <el-option v-for="item in departmentData" :key="item.id" :label="item.name" :value="item.id">-->
<!--        </el-option>-->
<!--      </el-select>-->
<!--      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>-->
<!--      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>-->
<!--    </div>-->

<!--    <div class="table" style="padding: 15px 20px">-->
<!--      <el-row :gutter="20">-->
<!--        <el-col :span="6" v-for="item in tableData" :key="item.id" style="margin-bottom: 20px">-->
<!--          <div style="text-align: center; background-color: #ecf8fd" class="card">-->
<!--            <img :src="item.avatar" alt="" style="width: 100px; height: 100px; border-radius: 50%">-->
<!--            <div style="font-weight: 550; margin-top: 10px">-->
<!--              {{item.name}} <span style="color: #383535; margin-left: 5px; font-weight: 500">{{item.departmentName}}</span>-->
<!--            </div>-->
<!--            <div style="margin-top: 20px; color: #353523; padding: 0 10px; text-align: left; overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 4;">-->
<!--              简介：{{item.description}}-->
<!--            </div>-->
<!--            <div style="margin-top: 15px">-->
<!--              挂号费：<span style="color: red; font-weight: 550; margin-right: 20px">￥{{item.price}}</span> 剩余：{{item.num}}-->
<!--            </div>-->
<!--            <div style="margin-top: 15px">-->
<!--              <el-button type="primary" size="mini" @click="reserve(item.id)" :disabled="item.num === 0">挂号</el-button>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-row>-->

<!--      <div class="pagination">-->
<!--        <el-pagination-->
<!--            background-->
<!--            @current-change="handleCurrentChange"-->
<!--            :current-page="pageNum"-->
<!--            :page-sizes="[5, 10, 20]"-->
<!--            :page-size="pageSize"-->
<!--            layout="total, prev, pager, next"-->
<!--            :total="total">-->
<!--        </el-pagination>-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
  <h1>DoctorCard</h1>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const tableData = ref([])  // 所有的数据
const pageNum = ref(1)   // 当前的页码
const pageSize = ref(10)  // 每页显示的个数
const total = ref(0)
const departmentId = ref(null)
const departmentData = ref([])

const user = JSON.parse(localStorage.getItem('xm-user') || '{}')

// 挂号
const reserve = (doctorId) => {
  if (user.role !== 'USER') {
    this.$message.warning('您的角色不支持挂号操作')
    return
  }
  let data = {
    userId: user.id,
    doctorId: doctorId
  }
  this.$request.post('/reserve/add', data).then(res => {
    if (res.code === '200') {
      this.$message.success('挂号成功')
      load(1)
    } else {
      this.$message.error(res.msg)
    }
  })
}

// 加载科室数据
const loadDepartment = () => {
  this.$request.get('/department/selectAll').then(res => {
    if (res.code === '200') {
      departmentData.value = res.data
    } else {
      this.$message.error(res.msg)
    }
  })
}

// 分页查询医生数据
const load = (pageNum) => {
  if (pageNum) pageNum.value = pageNum
  this.$request.get('/doctor/selectPage2', {
    params: {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      departmentId: departmentId.value,
    }
  }).then(res => {
    tableData.value = res.data?.list
    total.value = res.data?.total
  })
}

// 重置搜索条件
const reset = () => {
  departmentId.value = null
  load(1)
}

// 处理页码变化
const handleCurrentChange = (pageNum) => {
  load(pageNum)
}

// 加载页面数据
onMounted(() => {
  load(1)
  loadDepartment()
})
</script>

<style scoped>

</style>
