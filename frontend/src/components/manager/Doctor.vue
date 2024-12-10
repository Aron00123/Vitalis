<template>
  <div>
    <div class="search" style="padding-bottom: 20px">
      <el-input placeholder="请输入账号查询" style="width: 200px" v-model="id"/>
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
        <el-table-column prop="id" label="账号" width="70" align="center" sortable/>
        <el-table-column label="照片">
          <template #default="{ row }">
            <el-image
                v-if="row.photo"
                style="width: 40px; height: 40px; border-radius: 50%"
                :src="row.photo"
                :preview-src-list="[row.photo]"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="gender" label="性别"/>
        <el-table-column prop="title" label="职称"/>
        <el-table-column prop="specialty" label="主治疾病" show-overflow-tooltip/>
        <el-table-column prop="departmentName" label="科室"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="description" label="简介" show-overflow-tooltip/>
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
        title="医生信息"
        v-model="formVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >

      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="账号" prop="id">
          <el-input v-model="form.id" placeholder="账号"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="密码"/>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="姓名"/>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-input v-model="form.gender" placeholder="性别"/>
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-input v-model="form.title" placeholder="职称"/>
        </el-form-item>
        <el-form-item label="主治疾病" prop="specialty">
          <el-input type="textarea" :rows="2" v-model="form.specialty" placeholder="主治疾病"/>
        </el-form-item>
        <el-form-item label="科室" prop="departmentName">
          <el-select v-model="form.departId" placeholder="请选择科室" style="width: 100%">
            <el-option v-for="item in departmentData" :key="item.departId" :label="item.type" :value="item.departId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="电话"/>
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input type="textarea" :rows="4" v-model="form.description" placeholder="简介"/>
        </el-form-item>

        <!--        <el-form-item label="入职时间" prop="time">-->
        <!--          <el-date-picker-->
        <!--              style="width: 100%"-->
        <!--              v-model="form.time"-->
        <!--              type="date"-->
        <!--              value-format="yyyy-MM-dd"-->
        <!--              placeholder="选择日期"-->
        <!--          />-->
        <!--        </el-form-item>-->
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
const tableData = ref([])
const pageNum = ref(1);
const pageSize = ref(10);
// const total = ref(0);
const total = ref(1)

const id = ref("");

const formVisible = ref(false);
const isHandleAdd = ref(false);
const form = reactive({});
const rules = reactive({
  id: [{required: true, message: "请输入账号", trigger: "blur"}],
});
const ids = ref([]);
const departmentData = ref([]);

const loadDepartment = () => {
  request
      .post("/department/selectAll")
      .then((res) => {
        if (res.code === "200") {
          departmentData.value = res.data;
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
        params: {pageNum: pageNum.value, pageSize: pageSize.value, userId: id.value},
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
  Object.keys(form).forEach((key) => {
    form[key] = null;
  })
  formVisible.value = true;
  isHandleAdd.value = true;
};

const handleEdit = (row) => {
  Object.assign(form, {...row});
  formVisible.value = true;
};

const save = () => {
  form.role = 'DOCTOR';
  if (form.password === "") {
    form.password = null;
  }
  request
      .post(isHandleAdd.value ? "/register" : "/updatePassword", {
        id: form.id,
        role: form.role,
        password: form.password,
        newPassword: form.password
      })
      .then()
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
  request
      .post(isHandleAdd.value ? "/doctor/add" : "/doctor/update", form)
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
        .post("/doctor/delete", {id: id})
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
        .post("/doctor/delete/batch", {ids: ids.value})
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
  loadDepartment();
});
</script>

<style scoped>
</style>
