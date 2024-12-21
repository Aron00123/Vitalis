<template>
    <div>
      <div class="search">
        <el-input placeholder="请输入病人id查询" style="width: 200px" v-model="id" v-if="user.role === 'DOCTOR'"/>
        <el-button type="info" plain style="margin: 10px" @click="selectById(1)" v-if="user.role === 'DOCTOR'">查询
        </el-button>
  
        <el-autocomplete
            v-model="name"
            v-if="user.role === 'PATIENT'"
            :fetch-suggestions="querySearch"
            :trigger-on-focus="false"
            clearable
            class="inline-input w-50"
            style="width: 200px"
            placeholder="请输入医生姓名查询"
        />
        <el-autocomplete
            v-model="name"
            v-else
            :fetch-suggestions="querySearch"
            :trigger-on-focus="false"
            clearable
            class="inline-input w-50"
            style="width: 200px"
            placeholder= "请输入病人姓名查询"
        />
        <el-button type="info" plain style="margin-left: 10px" @click="querySearchAdmin(1)">查询</el-button>
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
            <el-input v-model="viewContent.name" placeholder="无" disabled></el-input>
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
            <el-input type="textarea" :rows="3" v-model="viewContent.medicalAdvice" placeholder="无" disabled></el-input>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  
    <el-dialog
        title="填写处方"
        v-model="formVisible"
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
          />
        </el-form-item>
        <el-form-item prop="medicalRecord" label="医嘱">
          <el-input type="textarea" :rows="4" v-model="form.medicalRecord" placeholder="填写医嘱"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="addFormVisible = false">取 消</el-button>
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
  
  const tableData = ref([]);
  
  const pageNum = ref(1);
  const pageSize = ref(10);
  const total = ref(0);
  
  const id = ref(null);
  const name = ref(null);
  
  const formVisible = ref(false);
  const prescriptionVisible = ref(false);
  const form = reactive({});
  const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
  const ids = ref([]);
  const viewContent = ref({});
  
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
  
  const selectById = (page) => {
    if (page) pageNum.value = page;
    console.log(user)
    request
        .post("/registration/selectPageRegistration", {
          pageNum: pageNum.value, pageSize: pageSize.value, userId: id.value, doctorId: user.id
        })
        .then((res) => {
          tableData.value = res.data?.list || [];
          total.value = res.data?.total || 0;
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  };
  
  const querySearch = (queryString, cb) => {
    let results = [];
    request
        .post(user.role === 'PATIENT' ? '/doctor/querySearch' : '/patient/querySearch', {queryString: queryString})
        .then((res) => {
          if (res.code === "200") {
            results = res.data;
            results.forEach(item => {
              item.value = item.name;
            });
            cb(results)
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  }
  
  const querySearchAdmin = (page) => {
    if (page) pageNum.value = page;
    request
        .post(user.role === 'PATIENT' ? '/doctor/querySearchAdminRegistration' : '/patient/querySearchAdminRegistration', {
          pageNum: pageNum.value, pageSize: pageSize.value, id: name.value, userId: user.id
        })
        .then((res) => {
          tableData.value = res.data.list || [];
          total.value = res.data.total || 0;
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  }
  
  function reset() {
    id.value = null;
    name.value = null;
    load(1);
  }
  
  function handleEdit(row) {
    Object.assign(form, row);
    formVisible.value = true;
  }
  
  function viewPrescription(row) {
    request
        .post("/prescription/selectAndView", {id: row.id})
        .then((res) => {
          if (res.code === "200") {
            viewContent.value = {
              ...res.data.prescription,
              ...res.data.patient
            };
            viewContent.value.doctorName = res.data.doctor.name;
            prescriptionVisible.value = true;
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  }
  
  function save() {
    request
        .post("/prescription/add", {
          registrationId: form.id,
          prescriptionId: form.id,
          disease: form.disease,
          medicine: form.medicine,
          medicalAdvice: form.medicalRecord
        })
        .then((res) => {
          if (res.code === "200") {
            ElMessage.success("保存成功");
            load(1);
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
    formVisible.value = false;
  
  }
  
  const querySearchDisease = (queryString, cb) => {
    let results = [];
    request
        .post("/disease/querySearch", {queryString: queryString})
        .then((res) => {
          if (res.code === "200") {
            results = res.data;
            results.forEach(item => {
              item.value = item.name;
            });
            cb(results)
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  }
  
  const querySearchMedicine = (queryString, cb) => {
    let results = [];
    request
        .post("/medicine/querySearch", {queryString: queryString})
        .then((res) => {
          if (res.code === "200") {
            results = res.data;
            results.forEach(item => {
              item.value = item.name;
            });
            cb(results)
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
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
  