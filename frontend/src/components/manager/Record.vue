<template>
  <div>
    <div class="search" v-if="user.role !== 'PATIENT'">
      <el-input
          placeholder="请输入患者姓名查询"
          style="width: 200px"
          v-model="userName"
      ></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">
        查询
      </el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">
        重置
      </el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column prop="id" label="挂号单" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="name" label="患者姓名" width="130" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctor" label="医生姓名" width="130" show-overflow-tooltip></el-table-column>
        <el-table-column prop="departmentName" label="科室" show-overflow-tooltip></el-table-column>
        <el-table-column prop="date" label="就诊时间"></el-table-column>

        <el-table-column prop="prescription" label="处方">
          <template #default="{ row }">
            <el-button
                type="primary"
                size="mini"
                plain
                @click="viewPrescription(row)"
            >查看处方
            </el-button
            >
          </template>
        </el-table-column>


        <el-table-column label="操作" width="180" align="center" v-if="user.role !== 'PATIENT'">
          <template #default="{ row }">
            <el-button
                plain
                type="warning"
                v-if="user.role === 'DOCTOR'"
                @click="handleEdit(row)"
                size="mini"
            >填写处方
            </el-button
            >
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
            :total="total"
        >
        </el-pagination>
      </div>
    </div>

    <el-dialog
        title="处方"
        v-model="prescriptionVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >
      <el-form
          label-width="150px"
          style="padding-right: 50px"
          :model="viewContent"
          ref="formRef"
      >
        <el-form-item label="挂号单号：">
          <el-input v-model="viewContent.registrationId" placeholder="无" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名：">
          <el-input v-model="viewContent.patientName" placeholder="无" disabled></el-input>
        </el-form-item>
        <el-form-item label="年龄：">
          <el-input v-model="viewContent.age" placeholder="无" disabled></el-input>
        </el-form-item>
        <el-form-item label="性别：">
          <el-input v-model="viewContent.gender" placeholder="无" disabled></el-input>
        </el-form-item>
        <div style="padding: 10px"></div>
        <el-form-item label="处方编号：">
          <el-input v-model="viewContent.prescriptionId" placeholder="无" disabled></el-input>
        </el-form-item>
        <el-form-item label="医生姓名：">
          <el-input v-model="viewContent.doctorName" placeholder="无" disabled></el-input>
        </el-form-item>
        <el-form-item label="所诊疾病：">
          <el-input v-model="viewContent.disease" placeholder="无" disabled></el-input>
        </el-form-item>
        <el-form-item label="所开药品：">
          <el-input v-model="viewContent.medicine" placeholder="无" disabled></el-input>
        </el-form-item>
        <el-form-item label="医嘱：">
          <el-input type="textarea" :rows="3" v-model="viewContent.medicalRecord" placeholder="无" disabled></el-input>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>

  <el-dialog
      title="填写处方"
      v-model="fromVisible"
      width="40%"
      :close-on-click-modal="false"
      destroy-on-close
  >
    <el-form
        label-width="100px"
        style="padding-right: 50px"
        :model="form"
        ref="formRef"
    >
      <el-form-item prop="disease" label="所诊疾病">
        <el-autocomplete
            v-model="form.disease"
            :fetch-suggestions="querySearchDisease"
            :trigger-on-focus="false"
            clearable
            class="inline-input w-50"
            placeholder="输入疾病"
            @select="handleSelect"
        />
      </el-form-item>

      <el-form-item prop="medicine" label="所开药品">
        <el-autocomplete
            v-model="form.medicine"
            :fetch-suggestions="querySearchMedicine"
            :trigger-on-focus="false"
            clearable
            class="inline-input w-50"
            placeholder="输入药品"
            @select="handleSelect"
        />
      </el-form-item>
      <el-form-item prop="medicalRecord" label="医嘱">
        <el-input type="textarea" :rows="4" v-model="form.medicalRecord" placeholder="填写医嘱"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="fromVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>

</template>

<script setup>
import {ref, reactive, onMounted} from 'vue';
import request from "../../utils/request.js";
import {ElMessage} from "element-plus";
import {useRoute} from 'vue-router'

const route = useRoute()

const editor = ref(null);
//const tableData = ref([]);
const tableData = ref([
  {
    id: "15306",
    name: "zhangsan",
    doctor: "doctorWang",
    date: "2024-12-4 16:31",
    departmentName: "中医科",
    medicalRecord: "医嘱",
    prescription: "处方"
  }
])

const diseaseData = ref([
  {
    id: 1,
    name: '感冒'
  },
  {
    id: 2,
    name: '发烧'
  }
]);
const medicineData = ref([
  {
    id: 1,
    name: '布洛芬'
  },
  {
    id: 2,
    name: '口服液'
  }
]);
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const userName = ref(null);
const fromVisible = ref(false);
const prescriptionVisible = ref(false);
const form = reactive({});
const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
const ids = ref([]);
const viewContent = ref(null);

function loadByRegistrationId(registrationId) {
  request
      .post("/registration/selectByIdWithPage", {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        registrationId: registrationId,
        status: '已就诊'
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

function load(pageNumVal = 1) {
  pageNum.value = pageNumVal;
  request
      .post("/registration/selectPage", {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        userId: user.id,
        status: '已就诊'
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

function reset() {
  userName.value = null;
  load(1);
}

function handleEdit(row) {
  Object.assign(form, row);
  fromVisible.value = true;
}

function viewPrescription(row) {
  viewContent.value = row;
  prescriptionVisible.value = true;
}

function save() {
  form.medicalRecord = editor.value.txt.html();
  request
      .post("/patient/update", form)
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
}

const querySearchDisease = (queryString, cb) => {
  let results = [];
  if (!queryString) {
    request
        .post("/disease/querySearch", {queryString: queryString})
        .then((res) => {
          if (res.code === "200") {
            results = res.data;
            results.forEach(item => {
              item.value = item.name;
            });
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  }
  cb(results)
}

const querySearchMedicine = (queryString, cb) => {
  let results = [];
  if (queryString.value !== "") {
    request
        .post("/medicine/querySearch", {queryString: queryString})
        .then((res) => {
          if (res.code === "200") {
            results = res.data;
            results.forEach(item => {
              item.value = item.name;
            });
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  }
  cb(results)
}

const handleSelect = (item) => {
  console.log(item)
}

function handleSelectionChange(rows) {
  ids.value = rows.map((v) => v.id);
}

function registration(row) {
  console.log('Registration logic', row);
}

onMounted(() => {
  const registrationId = route.query.registrationId; // 获取传递的 registrationId
  if (registrationId) {
    loadByRegistrationId(registrationId)

    // Object.assign(form, tableData);
    // fromVisible.value = true;
  } else {
    load(1);
  }
});
</script>

<style scoped>

.search {
  padding-bottom: 20px;
}

.pagination {
  padding-top: 20px;
}
</style>
