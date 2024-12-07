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
        <el-table-column prop="id" label="身份证号" width="70" align="center" sortable/>
        <el-table-column label="头像">
          <template #default="{ row }">
            <el-image
                v-if="row.photo"
                style="width: 40px; height: 40px; border-radius: 50%"
                :src="row.avatar"
                :preview-src-list="[row.avatar]"
            />
          </template>
        </el-table-column>
        <el-table-column prop="password" label="密码"/>
        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="age" label="年龄"/>
        <el-table-column prop="gender" label="性别"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="emergencyPhone" label="紧急联系人电话"/>
        <el-table-column prop="address" label="家庭住址" show-overflow-tooltip/>

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
        title="患者信息"
        v-model="formVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >

      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="身份证号" prop="id">
          <el-input v-model="form.id" placeholder="身份证号"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="密码"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age" placeholder="年龄"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-input v-model="form.gender" placeholder="性别"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="紧急联系人电话" prop="emergencyPhone">
          <el-input v-model="form.emergencyPhone" placeholder="紧急联系人电话"></el-input>
        </el-form-item>
        <el-form-item label="家庭住址" prop="address">
          <el-input v-model="form.address" placeholder="家庭住址"></el-input>
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
const tableData = ref([
  {
    id: 'doctorWang',
    password: '123456',
    name: "",
    gender: '',
    title: '',
    specialty: '',
    departmentName: '',
    consultLimit: '',
    consultDays: '',
    description: ''

  }
])
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
        params: {pageNum: pageNum.value, pageSize: pageSize.value, username: id.value},
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
        .post("/doctor/delete", {data: id})
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
        .post("/doctor/delete/batch", {data: ids.value})
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
