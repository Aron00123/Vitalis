<template>
    <div>
      <div class="search" style="padding-bottom: 20px">
        <el-input placeholder="请输入账号查询" style="width: 200px" v-model="id"/>
        <el-button type="info" plain style="margin: 10px" @click="load(1)">查询</el-button>
        <el-autocomplete
            v-model="name"
            :fetch-suggestions="querySearchDoctor"
            :trigger-on-focus="false"
            clearable
            class="inline-input w-50"
            style="width: 200px"
            placeholder="请输入医生姓名查询"
        />
        <el-button type="info" plain style="margin-left: 10px" @click="querySearchAdmin(1)">查询</el-button>
        <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
      </div>
  
      <div class="table">
        <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
          <el-table-column prop="id" label="序号" width="200" align="center" sortable/>
          <el-table-column prop="name" label="医生姓名" width="140"/>
          <el-table-column prop="departmentName" label="科室"/>
          <el-table-column prop="consultLimit" label="就诊限额"/>
          <el-table-column prop="consultDays" label="坐诊日"/>
          <el-table-column label="操作" align="center" width="180">
            <template #default="{ row }">
              <el-button size="mini" type="primary" plain @click="handleEdit(row)">编辑</el-button>
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
          title="医生排班信息"
          v-model="formVisible"
          width="40%"
          :close-on-click-modal="false"
          destroy-on-close
      >
  
        <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
          <el-form-item label="选择医生" prop="doctor">
            <el-select v-model="form.id" placeholder="请选择医生" style="width: 100%">
              <el-option
                  v-for="item in doctorData"
                  :key="item.id"
                  :label="`${item.name} - ${item.departmentName}`"
                  :value="item.id"
              ></el-option>
            </el-select>
  
          </el-form-item>
          <el-form-item label="就诊限额" prop="consultLimit">
            <el-input v-model="form.consultLimit" placeholder="就诊限额"></el-input>
          </el-form-item>
  
          <el-form-item label="坐诊日" prop="consultDays">
            <el-checkbox
                v-model="checkAll"
                :indeterminate="isIndeterminate"
                @change="handleCheckAllChange"
            >
              全选
            </el-checkbox>
            <el-checkbox-group
                v-model="checkedDays"
                @change="handleCheckedCitiesChange"
            >
              <el-checkbox v-for="day in weeks" :key="day" :label="day" :value="day">
                {{ day }}
              </el-checkbox>
            </el-checkbox-group>
          </el-form-item>
  
        </el-form>
  
        <template #footer>
          <el-button @click="addFormVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </template>
  
  <script setup>
  import {ref, reactive, onMounted} from "vue";
  import {ElMessage, ElMessageBox} from "element-plus";
  import request from "../../utils/request"; // 替换为实际的请求工具
  
  const tableData = ref([]);
  const pageNum = ref(1);
  const pageSize = ref(10);
  const total = ref(0);
  const doctorData = ref([]);
  const id = ref("");
  const name = ref("");
  
  const formVisible = ref(false);
  const form = reactive({});
  const rules = reactive({
    doctor: [{required: true, message: "请选择医生", trigger: "blur"}],
    consultLimit: [{required: true, message: "请输入就诊限额", trigger: "blur"}],
  });
  
  const ids = ref([]);
  const checkAll = ref(false)
  const isIndeterminate = ref(true)
  const checkedDays = ref([])
  const weeks = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  
  const handleCheckAllChange = (val) => {
    checkedDays.value = val ? weeks : []
    isIndeterminate.value = false
  }
  const handleCheckedCitiesChange = (value) => {
    const checkedCount = value.length
    checkAll.value = checkedCount === weeks.length
    isIndeterminate.value = checkedCount > 0 && checkedCount < weeks.length
  }
  
  const loadDoctor = () => {
    request
        .post("/doctor/selectAll", {
          pageNum: pageNum.value, pageSize: pageSize.value, username: id.value
        })
        .then((res) => {
          if (res.code === "200") {
            doctorData.value = res.data;
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  };
  
  const load = (page = 1) => {
    pageNum.value = page;
    request
        .post("/doctor/selectPage", {
          pageNum: pageNum.value, pageSize: pageSize.value, userId: id.value
        })
        .then((res) => {
          if (res.code === "200") {
            tableData.value = res.data?.list || [];
            total.value = res.data?.total || 0;
          } else {
            ElMessage.error(res.msg);
          }
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  };
  
  const querySearchDoctor = (queryString, cb) => {
    let results = [];
    request
        .post("/doctor/querySearch", {queryString: queryString})
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
        .post("/doctor/querySearchAdmin", {
          pageNum: pageNum.value, pageSize: pageSize.value, id: name.value
        })
        .then((res) => {
          tableData.value = res.data.list || [];
          total.value = res.data.total || 0;
        })
        .catch((err) => {
          ElMessage.error("请求失败，请稍后重试");
        });
  }
  
  const handleEdit = (row) => {
    Object.assign(form, {...row});
    formVisible.value = true;
  };
  
  const save = () => {
    request
        .post("/doctor/update", {
          id: form.id,
          consultLimit: form.consultLimit,
          consultDays: checkedDays.value.join('，')
        })
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
  };
  
  const handleSelectionChange = (rows) => {
    ids.value = rows.map((row) => row.id);
  };
  
  const reset = () => {
    id.value = "";
    name.value = "";
    load(1);
  };
  
  const handleCurrentChange = (page) => {
    load(page);
  };
  
  onMounted(() => {
    load(1);
    loadDoctor();
  });
  </script>
  
  <style scoped>
  </style>
  