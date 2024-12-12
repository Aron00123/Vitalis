<template>
  <div>
    <div class="search">
      <el-select v-model="departmentId" placeholder="请选择科室" style="width: 200px">
        <el-option v-for="item in departmentData" :key="item.departId" :label="item.type" :value="item.departId">
        </el-option>
      </el-select>
      <el-date-picker
          v-model="date"
          type="date"
          placeholder="请选择日期"
          :size="'default'"
          :disabled-date="disabledDate"
      />
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table" style="padding: 15px 20px">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in tableData" :key="item.id" style="margin-bottom: 20px">
          <div
              style="text-align: center; background-color: #f9fbfd; border-radius: 12px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); padding: 20px; width: 250px; margin: 10px auto;">
            <el-image
                style="width: 100px; height: 100px; border-radius: 50%; margin: 0 auto; border: 2px solid #dfe6ed;"
                :src="item.photo ? item.photo : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"
                :zoom-rate="1.2"
                :max-scale="7"
                :min-scale="0.2"
                :preview-src-list="item.photo ? [item.photo] : ['https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png']"
                :initial-index="0"
                fit="cover"
            />
            <div style="font-weight: 550; margin-top: 15px; font-size: 16px; color: #2c3e50;">
              {{ item.name }}
            </div>
            <div style="margin-top: 10px; font-size: 14px; color: #546e7a;">
              <span style="padding: 5px; background-color: #e3f2fd; border-radius: 6px; margin-right: 5px;">{{
                  item.departmentName
                }}</span>
              <span style="padding: 5px; background-color: #f1f8e9; border-radius: 6px;">{{ item.title }}</span>
            </div>
            <div
                style="margin-top: 20px; color: #455a64; padding: 10px; text-align: left; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 4; font-size: 13px; line-height: 1.6; background-color: #fafafa; border-radius: 8px;">
              主治疾病：{{ item.specialty }}
            </div>
            <div
                style="margin-top: 10px; color: #455a64; padding: 10px; text-align: left; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 4; font-size: 13px; line-height: 1.6; background-color: #fafafa; border-radius: 8px;">
              坐诊日：{{ item.consultDays }}
            </div>
            <div
                style="margin-top: 10px; color: #455a64; padding: 10px; text-align: left; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-box-orient: vertical; -webkit-line-clamp: 4; font-size: 13px; line-height: 1.6; background-color: #fafafa; border-radius: 8px;">
              就诊限额：{{ item.consultLimit }}
            </div>
            <div style="margin-top: 15px; display: flex; justify-content: space-between;">
              <el-button type="primary" size="mini" plain @click="open(item)"
                         style="border-radius: 8px; padding: 5px 10px;">
                查看医生简介
              </el-button>
              <el-button
                  type="primary"
                  size="mini"
                  @click="item.centerDialogVisible = true"
                  :disabled="item.consultLimit === 0"
                  style="border-radius: 8px; padding: 5px 10px;">
                挂号
              </el-button>
            </div>
            <el-dialog
                v-model="item.centerDialogVisible"
                title="确认挂号"
                width="500"
                center
                style="border-radius: 12px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15); background-color: #ffffff;">
              <div style="height: auto; padding: 20px; font-size: 14px; line-height: 1.6; color: #37474f;">
                <div style="margin-bottom: 15px; font-size: 16px; font-weight: bold; color: #1e88e5;">预约挂号</div>
                <div style="margin-bottom: 10px;">
                  <span style="font-size: 15px; font-weight: bold;">医生：</span>{{ item.name }} {{ item.title }}
                </div>
                <div style="margin-bottom: 10px;">
                  <span style="font-size: 15px; font-weight: bold;">科室：</span>{{ item.departmentName }}
                </div>
                <div style="margin-bottom: 10px;">
                  <span style="font-size: 15px; font-weight: bold;">预约时间：</span>{{ item.date }}
                </div>
                <div style="margin-bottom: 10px;">
                  <span style="font-size: 15px; font-weight: bold;">剩余号数：</span>{{ item.consultLimit }}
                </div>
              </div>
              <template #footer>
                <div class="dialog-footer" style="text-align: center;">
                  <el-button @click="item.centerDialogVisible = false" style="border-radius: 8px;">取消</el-button>
                  <el-button type="primary" @click="reserve(item.id, item.centerDialogVisible)"
                             style="border-radius: 8px;">
                    确认
                  </el-button>
                </div>
              </template>
            </el-dialog>
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

const tableData = ref([]);  // 所有的数据
const pageNum = ref(1);   // 当前的页码
const pageSize = ref(8);  // 每页显示的个数
const total = ref(0);
const departmentId = ref(null);
const departmentData = ref([]);
let date = ref(null);
let formatDate = '';

const user = JSON.parse(localStorage.getItem('xm-user') || '{}')

// 限制日期选择
const disabledDate = (time) => {
  const today = new Date();
  const nextWeek = new Date();
  nextWeek.setDate(today.getDate() + 7);
  today.setHours(0, 0, 0, 0);

  return time < today || time > nextWeek;
};

// 挂号
const reserve = (doctorId, centerDialogVisible) => {
  console.log(centerDialogVisible)
  if (user.role !== 'PATIENT') {
    ElMessage.error('您的角色不支持挂号操作')
    return
  }
  let data = {
    patientId: user.id,
    doctorId: doctorId,
    appointDate: formatDate
  }
  request
      .post("/registration/add", data)
      .then((res) => {
        if (res.code === "200") {
          if (res.data === 1) {
            ElMessage.error('当天您已经预约过该医生');
          } else {
            ElMessage.success('挂号成功');
          }
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
  }
  let year = date.value.getFullYear()
  let month = date.value.getMonth() + 1
  let day = date.value.getDate()
  formatDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`

  request
      .post("/doctor/selectPage2", {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        departmentId: departmentId.value,
        dateStr: formatDate
      })
      .then((res) => {
        if (res.code === "200") {
          tableData.value = res.data.list
          total.value = res.data.total
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
      `<div>${item.description}</div>`,
      '医生简介',
      {
        dangerouslyUseHTMLString: true,
      }
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
