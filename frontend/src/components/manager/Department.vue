<template>
  <div>
    <div class="search" style="padding-bottom: 20px">
      <el-input placeholder="请输入科室类型查询" style="width: 200px" v-model="id"/>
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
        <el-table-column prop="type" label="科室类型" />
        <el-table-column prop="directorId" label="科室主任身份证号" />
        <el-table-column prop="deputyId" label="科室副主任身份证号"/>
        <el-table-column prop="docNum" label="医生数量"/>
        <el-table-column prop="floor" label="所属楼层"/>
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
        title="科室信息"
        v-model="formVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >

      <el-form :model="form" label-width="150px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="科室类型" prop="type">
          <el-input v-model="form.type" placeholder="科室类型"></el-input>
        </el-form-item>
        <el-form-item label="科室主任身份证号" prop="directorId">
          <el-input v-model="form.directorId" placeholder="科室主任身份证号"></el-input>
        </el-form-item>
        <el-form-item label="科室副主任身份证号" prop="deputyId">
          <el-input v-model="form.deputyId" placeholder="科室副主任身份证号"></el-input>
        </el-form-item>
        <el-form-item label="医生数量" prop="docNum">
          <el-input v-model="form.docNum" placeholder="医生数量"></el-input>
        </el-form-item>
        <el-form-item label="所属楼层" prop="floor">
          <el-input v-model="form.floor" placeholder="所属楼层"></el-input>
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
const rules = reactive({
  type: [{required: true, message: "科室类型", trigger: "blur"}],
  directorId: [{required: true, message: "请输入科室主任身份证号", trigger: "blur"}],
  docNum: [{required: true, message: "请输入医生数量", trigger: "blur"}],
  floor: [{required: true, message: "请输入所处楼层", trigger: "blur"}],
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
      .post("/department/selectPage", {
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
      .post(isHandleAdd.value ? "/department/add" : "/department/update", form)
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
        .post("/department/delete", {data: id})
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
        .post("/department/delete/batch", {data: ids.value})
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
