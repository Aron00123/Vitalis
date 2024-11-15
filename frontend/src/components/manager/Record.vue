<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入患者姓名查询" style="width: 200px" v-model="userName"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" stripe @selection-change="handleSelectionChange">
        <el-table-column prop="id" label="序号" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="userName" label="患者姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="doctorName" label="医生姓名" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="就诊时间"></el-table-column>
        <el-table-column prop="medicalRecord" label="医嘱病历">
          <template v-slot="scope">
            <el-button type="primary" size="mini" @click="viewEditor(scope.row.medicalRecord)">查看病历</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="inhospital" label="是否住院"></el-table-column>
        <el-table-column prop="inhostpitalRecord" label="是否住院登记"></el-table-column>

        <el-table-column label="操作" width="180" align="center">
          <template v-slot="scope">
            <el-button plain type="primary" v-if="user.role === 'DOCTOR'" @click="handleEdit(scope.row)" size="mini">填写医嘱病历</el-button>
            <el-button plain type="primary" v-if="user.role === 'ADMIN' && scope.row.inhospital === '是' && scope.row.inhostpitalRecord === '否'" @click="registration(scope.row)" size="mini">住院登记</el-button>
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
            :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="医嘱病历填写" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close @close="cancel">
      <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="医嘱病历" :visible.sync="editorVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div v-html="viewContent" class="w-e-text"></div>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import E from 'wangeditor'

// 响应式数据
const tableData = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const userName = ref(null)
const fromVisible = ref(false)
const form = ref({})
const user = JSON.parse(localStorage.getItem('xm-user') || '{}')
const rules = {
  inhospital: [
    { required: true, message: '请选择是否住院', trigger: 'blur' }
  ]
}
const ids = ref([])
const viewContent = ref(null)
const editorVisible = ref(false)

// 编辑器初始化
let editor
function initWangEditor(content) {
  setTimeout(() => {
    if (!editor) {
      editor = new E('#editor')
      editor.config.placeholder = '请输入内容'
      editor.config.uploadFileName = 'file'
      editor.config.uploadImgServer = 'http://localhost:9090/files/wang/upload'
      editor.create()
    }
    editor.txt.html(content)
  }, 0)
}

// 请求相关操作
const registration = (row) => {
  let data = JSON.parse(JSON.stringify(row))
  data.inhostpitalRecord = '是'
  $request.put('/record/update', data).then(res => {
    if (res.code === '200') {
      load(1)
      toRegistration(row.userId)
    }
  })
}

const toRegistration = (userId) => {
  const data = { userId, hosStatus: '住院中' }
  $request.post('/registration/add', data).then(res => {
    if (res.code === '200') {
      $message.success('登记成功')
    } else {
      $message.error(res.msg)
    }
  })
}

const viewEditor = (content) => {
  viewContent.value = content
  editorVisible.value = true
}

const cancel = () => {
  fromVisible.value = false
  location.href = '/record'
}

const handleEdit = (row) => {
  form.value = JSON.parse(JSON.stringify(row))
  initWangEditor(form.value.medicalRecord || '')
  fromVisible.value = true
}

const save = () => {
  form.value.medicalRecord = editor.txt.html()
  $request.put('/record/update', form.value).then(res => {
    if (res.code === '200') {
      $message.success('保存成功')
      load(1)
      fromVisible.value = false
    } else {
      $message.error(res.msg)
    }
  })
}

const del = (id) => {
  $confirm('您确定删除吗？', '确认删除', { type: "warning" }).then(() => {
    $request.delete(`/record/delete/${id}`).then(res => {
      if (res.code === '200') {
        $message.success('操作成功')
        load(1)
      } else {
        $message.error(res.msg)
      }
    })
  }).catch(() => {})
}

const handleSelectionChange = (rows) => {
  ids.value = rows.map(v => v.id)
}

const delBatch = () => {
  if (!ids.value.length) {
    $message.warning('请选择数据')
    return
  }
  $confirm('您确定批量删除这些数据吗？', '确认删除', { type: "warning" }).then(() => {
    $request.delete('/record/delete/batch', { data: ids.value }).then(res => {
      if (res.code === '200') {
        $message.success('操作成功')
        load(1)
      } else {
        $message.error(res.msg)
      }
    })
  }).catch(() => {})
}

const load = (pageNum) => {
  if (pageNum) pageNum = pageNum
  $request.get('/record/selectPage', {
    params: {
      pageNum,
      pageSize: pageSize.value,
      userName: userName.value,
    }
  }).then(res => {
    tableData.value = res.data?.list
    total.value = res.data?.total
  })
}

const reset = () => {
  userName.value = null
  load(1)
}

const handleCurrentChange = (pageNum) => {
  load(pageNum)
}

// 初始化加载数据
onMounted(() => {
  load(1)
})
</script>

<style scoped>
/* 样式代码可以继续放在这里 */
</style>
