<template>
  <div>
    <div class="search" style="padding-bottom: 20px">
      <el-input placeholder="请输入处方id查询" style="width: 200px" v-model="id"/>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation" style="padding-bottom: 20px">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column prop="prescription.prescriptionId" label="挂号单号" width="100" align="center" sortable/>
        <el-table-column prop="patient.name" label="病人"/>
        <el-table-column prop="doctor.name" label="医生"/>
        <el-table-column prop="prescription.disease" label="疾病"/>
        <el-table-column prop="prescription.medicalAdvice" label="医嘱"/>

        <el-table-column label="操作" align="center" width="180">
          <template #default="{ row }">
<!--            <el-button size="mini" type="primary" plain @click="handleEdit(row)">编辑</el-button>-->
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
        title="处方信息"
        v-model="formVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >

      <el-form :model="form" label-width="150px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="挂号单号" prop="registrationId">
          <el-input v-model="form.prescription.prescriptionId" placeholder="挂号单号" disabled></el-input>
        </el-form-item>
        <el-form-item label="病人姓名" prop="patientName">
          <el-input v-model="form.patient.name" placeholder="病人姓名" disabled></el-input>
        </el-form-item>
        <el-form-item label="医生姓名" prop="doctorName">
          <el-input v-model="form.doctor.name" placeholder="医生姓名" disabled></el-input>
        </el-form-item>
        <el-form-item label="疾病" prop="disease">
          <el-input v-model="form.prescription.disease" placeholder="疾病"></el-input>
        </el-form-item>
        <el-form-item label="医嘱" prop="medicalAdvice">
          <el-input type="textarea" rows="5" v-model="form.prescription.medicalAdvice" placeholder="医嘱"></el-input>
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

const tableData = ref([{
  id: 1
}]);
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);

const id = ref("");

const formVisible = ref(false);
const isHandleAdd = ref(false);
const form = reactive({});
const rules = reactive({});
const ids = ref([]);

const load = (page = 1) => {
  pageNum.value = page;
  request
      .post("/prescription/selectPageForAdmin", {
        pageNum: pageNum.value, pageSize: pageSize.value, username: id.value
      })
      .then((res) => {
        tableData.value = res.data?.list || [];
        total.value = res.data?.total || 0;
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
      .post(isHandleAdd.value ? "/prescription/add" : "/prescription/update", form)
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
        .post("/prescription/delete", {id: id})
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
        .post("/prescription/delete/batch", {ids: ids.value})
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
});
</script>

<style scoped>
</style>
