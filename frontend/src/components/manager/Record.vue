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
        <el-table-column
            prop="id"
            label="挂号单"
            width="80"
            align="center"
            sortable
        ></el-table-column>
        <el-table-column
            prop="name"
            label="患者姓名"
            show-overflow-tooltip
        ></el-table-column>
        <el-table-column
            prop="doctor"
            label="医生姓名"
            show-overflow-tooltip
        ></el-table-column>
        <el-table-column prop="date" label="就诊时间"></el-table-column>
        <el-table-column prop="medicalRecord" label="医嘱病历">
          <template #default="{ row }">
            <el-button
                type="primary"
                size="mini"
                plain
                @click="viewEditor(row.medicalRecord)"
            >查看病历</el-button
            >
          </template>
        </el-table-column>

        <el-table-column prop="prescription" label="处方">
          <template #default="{ row }">
            <el-button
                type="primary"
                size="mini"
                plain
                @click="viewEditor(row.prescription)"
            >查看处方</el-button
            >
          </template>
        </el-table-column>


        <el-table-column label="操作" width="180" align="center" v-if="user.role !== 'PATIENT'">
          <template #default="{ row }">
            <el-button
                plain
                type="primary"
                v-if="user.role === 'DOCTOR'"
                @click="handleEdit(row)"
                size="mini"
            >填写医嘱病历</el-button
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
        title="医嘱病历填写"
        v-model:visible="fromVisible"
        width="60%"
        :close-on-click-modal="false"
        destroy-on-close
        @close="cancel"
    >
      <el-form
          label-width="100px"
          style="padding-right: 50px"
          :model="form"
          :rules="rules"
          ref="formRef"
      >
        <el-form-item prop="medicalRecord" label="医嘱病历">
          <div id="editor"></div>
        </el-form-item>
        <el-form-item prop="inhospital" label="是否住院">
          <el-select v-model="form.inhospital" placeholder="请选择" style="width: 100%;">
            <el-option label="是" value="是"></el-option>
            <el-option label="否" value="否"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </template>
    </el-dialog>

    <el-dialog
        title="医嘱病历"
        v-model:visible="editorVisible"
        width="50%"
        :close-on-click-modal="false"
        destroy-on-close
    >
      <div v-html="viewContent" class="w-e-text"></div>
    </el-dialog>
  </div>

</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import E from 'wangeditor';
import request from "../../utils/request.js";
import {ElMessage} from "element-plus";

const editor = ref(null);
//const tableData = ref([]);
const tableData = ref([
  {
    id: "15306",
    name: "zhangsan",
    doctor: "doctorWang",
    date: "2024-12-4 16:31",
    medicalRecord: "medicalRecord",
    prescription: "prescription"
  }
])
const pageNum = ref(1);
const pageSize = ref(10);
const total = ref(0);
const userName = ref(null);
const fromVisible = ref(false);
const editorVisible = ref(false);
const form = reactive({});
const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
const rules = reactive({
  inhospital: [{ required: true, message: '请选择是否住院', trigger: 'blur' }],
});
const ids = ref([]);
const viewContent = ref(null);

function initWangEditor(content) {
  setTimeout(() => {
    if (!editor.value) {
      editor.value = new E('#editor');
      editor.value.config.placeholder = '请输入内容';
      editor.value.config.uploadFileName = 'file';
      editor.value.config.uploadImgServer = 'http://localhost:9090/files/wang/upload';
      editor.value.create();
    }
    editor.value.txt.html(content);
  }, 0);
}

function load(pageNumVal = 1) {
  pageNum.value = pageNumVal;
  // Replace this with your actual API call logic
  // e.g., this.$request.get('/record/selectPage', { params: { pageNum, pageSize, userName } })
  request
      .post("/registration/selectPage", {
        params: {
          pageNum: pageNum.value,
          pageSize: pageSize.value,
          id: user.value.id
        }
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
  console.log('Load data for page', pageNum.value);
}

function reset() {
  userName.value = null;
  load(1);
}

function handleEdit(row) {
  Object.assign(form, row);
  initWangEditor(row.medicalRecord || '');
  fromVisible.value = true;
}

function viewEditor(content) {
  viewContent.value = content;
  editorVisible.value = true;
}

function save() {
  form.medicalRecord = editor.value.txt.html();
  // Replace this with actual save logic
  console.log('Save form', form);
}

function cancel() {
  fromVisible.value = false;
  location.href = '/record';
}

function handleSelectionChange(rows) {
  ids.value = rows.map((v) => v.id);
}

function registration(row) {
  console.log('Registration logic', row);
}

onMounted(() => {
  //load(1);
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
