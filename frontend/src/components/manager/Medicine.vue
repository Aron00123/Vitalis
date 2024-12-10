<template>
  <div>
    <div class="search" style="padding-bottom: 20px">
      <el-input placeholder="请输入疾病名称查询" style="width: 200px" v-model="id"/>
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
        <el-table-column prop="name" label="药品名称"/>
        <el-table-column prop="expiryDate" label="有效日期"/>
        <el-table-column prop="dosage" label="使用计量"/>
        <el-table-column prop="effects" label="药品作用"/>
        <el-table-column prop="stock" label="库存量"/>
        <el-table-column prop="price" label="价格"/>

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
        title="疾病信息"
        v-model="addFormVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >

      <el-form :model="form" label-width="150px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="药品id" prop="id">
          <el-input v-model="form.id" placeholder="药品id"></el-input>
        </el-form-item>
        <el-form-item label="药品名称" prop="name">
          <el-input v-model="form.name" placeholder="药品名称"></el-input>
        </el-form-item>
        <el-form-item label="有效日期" prop="expiryDate">
          <el-input v-model="form.expiryDate" placeholder="有效日期"></el-input>
        </el-form-item>
        <el-form-item label="使用计量" prop="dosage">
          <el-input v-model="form.dosage" placeholder="使用计量"></el-input>
        </el-form-item>
        <el-form-item label="药品作用" prop="effects">
          <el-input type="textarea" rows="3" v-model="form.effects" placeholder="药品作用"></el-input>
        </el-form-item>
        <el-form-item label="库存量" prop="stock">
          <el-input v-model="form.stock" placeholder="库存量"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="form.price" placeholder="价格"></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
        title="疾病信息"
        v-model="updateFormVisible"
        width="40%"
        :close-on-click-modal="false"
        destroy-on-close
    >

      <el-form :model="form" label-width="150px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="药品id" prop="id">
          <el-input v-model="form.id" placeholder="药品id" disabled></el-input>
        </el-form-item>
        <el-form-item label="药品名称" prop="name">
          <el-input v-model="form.name" placeholder="药品名称"></el-input>
        </el-form-item>
        <el-form-item label="有效日期" prop="expiryDate">
          <el-input v-model="form.expiryDate" placeholder="有效日期"></el-input>
        </el-form-item>
        <el-form-item label="使用计量" prop="dosage">
          <el-input v-model="form.dosage" placeholder="使用计量"></el-input>
        </el-form-item>
        <el-form-item label="药品作用" prop="effects">
          <el-input type="textarea" rows="3" v-model="form.effects" placeholder="药品作用"></el-input>
        </el-form-item>
        <el-form-item label="库存量" prop="stock">
          <el-input v-model="form.stock" placeholder="库存量"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="form.price" placeholder="价格"></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="updateFormVisible = false">取消</el-button>
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

const addFormVisible = ref(false);
const updateFormVisible = ref(false);
const isHandleAdd = ref(false);
const form = reactive({});
const rules = {
  id: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error("请确认药品id"));
        } else if (value.length !== 7) {
          callback(new Error("药品id不合规"));
        } else {
          callback();
        }
      },
      trigger: "blur",
      required: true
    },
  ],
}
const ids = ref([]);

const load = (page = 1) => {
  pageNum.value = page;
  request
      .post("/medicine/selectPage", {
        pageNum: pageNum.value, pageSize: pageSize.value, username: id.value
      })
      .then((res) => {
        tableData.value = res.data.list || [];
        total.value = res.data.total || 0;
      })
      .catch((err) => {
        ElMessage.error("请求失败，请稍后重试");
      });
};

const handleAdd = () => {
  Object.assign(form, {});
  addFormVisible.value = true;
  isHandleAdd.value = true;
};

const handleEdit = (row) => {
  Object.assign(form, {...row});
  updateFormVisible.value = true;
};

const save = () => {
  request
      .post(isHandleAdd.value ? "/medicine/add" : "/medicine/update", form)
      .then((res) => {
        if (res.code === "200") {
          ElMessage.success("保存成功");
          load(1);
          addFormVisible.value = false;
          updateFormVisible.value = false;
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
        .post("/medicine/delete", {id: id})
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
        .post("/medicine/delete/batch", {ids: ids.value})
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
