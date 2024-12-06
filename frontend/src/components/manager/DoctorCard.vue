<template>
  <div>
    <div class="search">
      <el-select v-model="departmentId" placeholder="请选择科室" style="width: 200px">
        <el-option v-for="item in departmentData" :key="item.id" :label="item.name" :value="item.id">
        </el-option>
      </el-select>
      <el-date-picker
          v-model="date"
          type="date"
          placeholder="请选择日期"
          :size="'default'"
      />
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table" style="padding: 15px 20px">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in tableData" :key="item.id" style="margin-bottom: 20px">
          <div style="text-align: center; background-color: #ecf8fd" class="card">
            <img :src="item.photo" alt="" style="width: 100px; height: 100px; border-radius: 50%">
            <div style="font-weight: 550; margin-top: 10px">
              {{ item.name }}
            </div>
            <div style=" margin-top: 10px">
              <span style="padding: 10px">{{ item.departmentName }}</span>
              <span>{{ item.title }}</span>

            </div>
            <div
                style="margin-top: 20px; color: #353523; padding: 0 10px; text-align: left; overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 4;">
              主治疾病：{{ item.specialty }}
            </div>

            <div style="margin-top: 15px">
              <el-button type="primary" size="mini" plain @click="open(item)">查看医生简介</el-button>
              <el-button type="primary" size="mini" @click="centerDialogVisible = true"
                         :disabled="item.consultLimit === 0">
                挂号
              </el-button>

              <el-dialog v-model="centerDialogVisible" title="确认挂号" width="500" center>
                <div style="height: 200px ;padding-left: 20px">
                  <div style="padding: 10px"><strong>预约挂号：</strong></div>
                  <div style="padding: 10px;">
                    <span><strong>{{ item.name }} {{item.title}}</strong></span>
<!--                    <span style="padding-left: 50px"><strong>{{ item.departmentName }}</strong></span>-->
                  </div>
                  <div style="padding: 10px;">
                    <span><strong>预约时间：{{item.date}}</strong></span>
                  </div>
                  <div style="padding: 10px;">
                    <span><strong>剩余号数：{{item.consultLimit}}</strong></span>
                  </div>

                </div>

                <template #footer>
                  <div class="dialog-footer">
                    <el-button @click="centerDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="reserve(item.id)">
                      确认
                    </el-button>
                  </div>
                </template>
              </el-dialog>
            </div>
          </div>
        </el-col>
      </el-row>

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

const centerDialogVisible = ref(false)

// const tableData = ref([])  // 所有的数据
const tableData = [
  {
    photo: "",    // 照片
    id: "",   // 医生id
    name: "谢晓红",
    departmentName: "呼吸内科",   // 科室
    title: "副主任医师",  // 职称
    specialty: "矽肺，慢性支气管炎，阻塞性肺气肿，支气管哮喘，支气管扩张症，过敏性哮喘，肺结核，流行性感冒，肺炎支原体肺炎，葡萄球菌肺炎，肺炎，肺炎链球菌肺炎，慢性肺源性心脏病，急性气管-支气管炎，急性上呼吸道感染",  // 主治疾病
    description: "上海市徐汇区新乐社区卫生服务中心 1975年毕业于贵阳医学院医疗系，长期以来一直在病房工作。擅长治疗消化系统呼吸系统。",    // 医生简介
    phone: "18620988651", // 联系电话
    consultDays: "星期五上午", // 坐诊日
    consultLimit: 10,
    date: "2024-12-5"
  }
]
const pageNum = ref(1)   // 当前的页码
const pageSize = ref(10)  // 每页显示的个数
const total = ref(0)
const departmentId = ref(null)
const departmentData = ref([])
let date = ref(null)

const user = JSON.parse(localStorage.getItem('xm-user') || '{}')

// 挂号
const reserve = (doctorId) => {
  if (user.role !== 'USER') {
    ElMessage.error('您的角色不支持挂号操作')
    return
  }
  let data = {
    userId: user.id,
    doctorId: doctorId
  }
  request
      .post("/reserve/add", data)
      .then((res) => {
        if (res.code === "200") {
          ElMessage.success('挂号成功');
          load(1)
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
  centerDialogVisible.value = false;
}

// 加载科室数据
const loadDepartment = () => {
  request
      .post("/department/selectAll")
      .then((res) => {
        if (res.code === "200") {
          departmentData.value = res.data
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
}


// 分页查询医生数据
const load = (pageNum1) => {
  if (pageNum1) pageNum.value = pageNum1

  // 默认选择当天
  if (!date.value) {
    date.value = new Date()
    console.log(date.value)
  }
  let year = date.value.getFullYear()
  let month = date.value.getMonth() + 1
  let day = date.value.getDate()
  let formatDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`


  request
      .post("/doctor/selectPage2", {
        params: {
          pageNum: pageNum.value,
          pageSize: pageSize.value,
          departmentId: departmentId.value,
          dateStr: formatDate
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

// 查看医生简介
const open = (item) => {
  ElMessageBox.alert(
      `<div><strong>坐诊日：${item.consultDays}</strong></div>
// <div><strong>联系电话：${item.phone}</strong></div>
// <div style="margin-top: 20px; color: #353523; padding: 0 10px; text-align: left; overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 4;">
//               简介：${item.description}</div>`,
//       item.name,
//       {
//         dangerouslyUseHTMLString: true,
//       }
  )
}

// 重置搜索条件
const reset = () => {
  departmentId.value = null
  date.value = null
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
