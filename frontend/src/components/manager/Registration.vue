<template>
  <div>
    <div class="search">
      <el-select v-model="status" placeholder="挂号状态查询" style="width: 200px">
        <el-option v-for="item in statusData" :key="item.id" :label="item.name" :value="item.name">
        </el-option>
      </el-select>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe>
        <el-table-column prop="id" label="挂号单" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="患者姓名" width="130" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctor" label="医生姓名" width="130" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="科室" show-overflow-tooltip></el-table-column>
        <el-table-column prop="date" label="挂号时间"></el-table-column>
        <el-table-column prop="status" label="挂号状态"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="danger" size="mini" v-if="scope.row.status === '未就诊' && user.role === 'PATIENT'"
                       @click="del(scope.row.id)">取消挂号
            </el-button>
            <el-button plain type="primary" size="mini"
                       v-else-if="scope.row.status === '已就诊' && user.role === 'PATIENT'"
                       @click="searchByRegistrationId(scope.row.id)">就诊记录
            </el-button>

            <el-button plain type="warning" size="mini" v-if="scope.row.status === '未就诊' && user.role === 'DOCTOR'"
                       @click="call(scope.row)">叫号
            </el-button>
            <el-button plain type="primary" size="mini"
                       v-else-if="scope.row.status === '已就诊' && user.role === 'DOCTOR'"
                       @click="searchByRegistrationId(scope.row.id)">就诊记录
            </el-button>
            <el-button size="mini" type="primary" plain
                       v-if="user.role === 'ADMIN'"  @click="AdminEditor(scope.row)">编辑
            </el-button>
            <el-button size="mini" type="danger" plain
                       v-if="user.role === 'ADMIN'"  @click="AdminDel(scope.row.id)">删除
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

    <el-dialog
        title="挂号单信息"
        v-model="formVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >

      <el-form :model="form" label-width="150px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="挂号单" prop="id">
          <el-input v-model="form.id" placeholder="挂号单" disabled></el-input>
        </el-form-item>
        <el-form-item label="患者姓名" prop="name">
          <el-input v-model="form.name" placeholder="患者姓名" disabled></el-input>
        </el-form-item>
        <el-form-item label="医生姓名" prop="doctor">
          <el-input v-model="form.doctor" placeholder="医生姓名" disabled></el-input>
        </el-form-item>
        <el-form-item label="科室" prop="departmentName">
          <el-input v-model="form.departmentName" placeholder="科室" disabled></el-input>
        </el-form-item>
        <el-form-item label="挂号时间" prop="date">
          <el-input v-model="form.date" placeholder="挂号时间" disabled></el-input>
        </el-form-item>
        <el-form-item label="挂号状态" prop="status">
          <el-select v-model="form.status" placeholder="挂号状态">
            <el-option v-for="item in statusData" :key="item.id" :label="item.name" :value="item.name">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </template>
    </el-dialog>
  </div>

</template>

<script setup>
import {ref, onMounted, reactive} from 'vue'
import request from "../../utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {useRouter} from 'vue-router'

const router = useRouter();

// 定义响应式数据
//const tableData = ref([])  // 所有的数据
const tableData = ref([
  {
    status: '已就诊'
  },
  {
    status: '未就诊'
  }
])
const pageNum = ref(1)     // 当前的页码
const pageSize = ref(10)   // 每页显示的个数
const total = ref(0)
const formVisible = ref(false)
const form = reactive({});
const rules = {
  status: [{required: true, message: "请选择挂号单状态", trigger: "blur"}],
}
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
  reserveData.status = '已就诊'
  reserveData.registrationId = reserveData.id;
  reserveData.isAdmin = 0;
  request
      .post("/registration/update", reserveData)
      .then((res) => {
        if (res.code === "200") {
          ElMessage.success('叫号成功');

          request.post("/prescription/add", {
            registrationId: reserveData.id,
            prescriptionId: reserveData.id,
          })

          load(1);
          //record(row)
          searchByRegistrationId(reserveData.registrationId);
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
}

// 同步就诊记录
const record = (row) => {
  const data = {
    userId: row.userId,
    doctorId: row.doctorId,
  }
  request
      .post("/record/add", data)
      .then((res) => {
        if (res.code === "200") {
          ElMessage.success('数据同步成功');
        } else {
          ElMessage.error(res.msg);
        }
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
}

// 查看就诊信息
const searchByRegistrationId = (registrationId) => {
  router.push({
    path: '/record',
    query: {registrationId}, // 使用 query 方式传递参数
  });
}

// 取消挂号操作
const del = (id) => {
  ElMessageBox.confirm("您确定取消挂号吗？", "取消挂号", {
    type: "warning", confirmButtonText: "确认", cancelButtonText: "取消"
  }).then(() => {
    request
        .post("/registration/delete", {id: id})
        .then((res) => {
          if (res.code === "200") {
            ElMessage.success('取消成功');
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

const AdminEditor = (row) => {
  Object.assign(form, {...row});
  formVisible.value = true;

}

const AdminDel = (id) => {
  console.log('wcnmd\n\n\n\n\n\n\n\n')
  ElMessageBox.confirm("您确定取消挂号吗？", "取消挂号", {
    type: "warning", confirmButtonText: "确认", cancelButtonText: "取消"
  }).then(() => {
    request
        .post("/registration/delete", {id: id})
        .then((res) => {
          if (res.code === "200") {
            ElMessage.success('取消成功');
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

const save = () => {
  form.isAdmin = 1;
  request
      .post("/registration/update", form)
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
}

// 加载数据
const load = (pageNum1) => {
  if (pageNum1) pageNum.value = pageNum1
  let data = null
  if (user.role === 'PATIENT' || user.role === 'DOCTOR') {
    data = {
      userId: user.id,
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      status: status.value
    }
  } else {
    data = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      status: status.value
    }
  }
  request
      .post("/registration/selectPage", data)
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
