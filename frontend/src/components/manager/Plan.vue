<template>
  <div>
    <div class="search" style="padding-bottom: 20px">
      <el-input placeholder="请输入关键字查询" style="width: 200px" v-model="id"/>
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
        <el-table-column prop="name" label="医生姓名"/>
        <el-table-column prop="department" label="科室"/>
        <el-table-column prop="consultLimit" label="就诊限额"/>
        <el-table-column prop="consultDays" label="坐诊日"/>
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
        <el-button @click="formVisible = false">取消</el-button>
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
  id: 1
}])
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const doctorData = ref([]);
const id = ref("");

const formVisible = ref(false);
const isHandleAdd = ref(false);
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
      .post("/plan/selectPage", {
        params: {pageNum: pageNum.value, pageSize: pageSize.value, username: id.value},
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
  request
      .post(isHandleAdd.value ? "/plan/add" : "/plan/update", {
        id: form.id.value,
        consultLimit: form.consultLimit.value,
        dateStr: checkedDays.value.join(',')
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

  isHandleAdd.value = false;
};


const del = (id) => {
  ElMessageBox.confirm("您确定删除吗？", "确认删除", {
    type: "warning", confirmButtonText: "确认", cancelButtonText: "取消"
  }).then(() => {
    request
        .post("/plan/delete", {data: id})
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
        .post("/plan/delete/batch", {data: ids.value})
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
  loadDoctor();
});
</script>

<style scoped>
</style>
