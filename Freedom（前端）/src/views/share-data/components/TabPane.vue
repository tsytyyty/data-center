<template>
  <div class="app-container">
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list1"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="提单号" prop="id" sortable="custom" align="center" width="190" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货主名称" prop="id" sortable="custom" align="center" width="80" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.owner }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货主代码" prop="id" sortable="custom" align="center" width="200" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.ownerId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物流公司" prop="id" sortable="custom" align="center" width="210" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.companyId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="集装箱箱号" prop="id" sortable="custom" align="center" width="180" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.containerId }}</span>
        </template>
      </el-table-column>
      <el-table-column label="货物名称" prop="id" sortable="custom" align="center" width="160" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.goodsName}}</span>
        </template>
      </el-table-column>
      <el-table-column label="货重(吨)" prop="id" sortable="custom" align="center" width="80" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.weight }}</span>
        </template>
      </el-table-column>


      <el-table-column label="操作" align="center" width="184" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="block">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        @prev-click="prev"
        @next-click="next"
        :current-page.sync="currentPage1"
        :page-size="20"
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
    </div>

    <br>
    <div class="filter-container">
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
        导出
      </el-button>
    </div>


    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="110px" style="width: 400px; margin-left:50px;">
        <el-form-item label="提单号" prop="id">
          <el-input v-model="temp.id"/>
        </el-form-item>
        <el-form-item label="货主名称" prop="owner">
          <el-input v-model="temp.owner"/>
        </el-form-item>
        <el-form-item label="货主代码" prop="ownerId">
          <el-input v-model="temp.ownerId"/>
        </el-form-item>
        <el-form-item label="物流公司" prop="companyId">
          <el-input v-model="temp.companyId"/>
        </el-form-item>
        <el-form-item label="集装箱箱号" prop="containerId">
          <el-input v-model="temp.containerId"/>
        </el-form-item>
        <el-form-item label="货物名称" prop="goodsName">
          <el-input v-model="temp.goodsName"/>
        </el-form-item>
        <el-form-item label="货重(吨)" prop="weight">
          <el-input v-model="temp.weight"/>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          添加
        </el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">
      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">
        <el-table-column prop="key" label="Channel" />
        <el-table-column prop="pv" label="Pv" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogPvVisible = false">Confirm</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination'
import axios from "axios"; // secondary package based on el-pagination



export default {
  name: 'Documentation',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      currentPage1: 1,
      isHdfs: true,
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
      statusOptions: ['success', 'error', 'deleted'],
      showReviewer: false,
      temp: {
        id: '',
        owner: '',
        ownerId: '',
        companyId: '',
        containerId:'',
        goodsName: '',
        weight: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        id: [{ required: true, message: 'is required', trigger: 'change' }],
        owner: [{ required: true, message: 'is required', trigger: 'change' }],
        ownerId: [{ required: true, message: 'is required', trigger: 'change' }],
        companyId: [{ required: true, message: 'is required', trigger: 'change' }],
        containerId: [{ required: true, message: 'is required', trigger: 'change' }],
        goodsName: [{ required: true, message: 'is required', trigger: 'change' }],
        weight: [{ required: true, message: 'is required', trigger: 'change' }],
      },
      downloadLoading: false,
      /*list1: [{
        id: 'TKNG2924820',
        owner: '区克',
        ownerId: '511824199602123275',
        companyId: '乾宇国际货运代理有限公司',
        containerId:'YWCM8054',
        goodsName: '大豆粉',
        weight: '473',
        remark: '',
      },{
        id: 'TKNG29248202',
        owner: '区克2',
        ownerId: '5118241996021232752',
        companyId: '乾宇国际货运代理有限公司2',
        containerId:'YWCM80542',
        goodsName: '大豆粉2',
        weight: '4732',
        remark: '',
      }],*/
      list1:[{}],
    }
  },
  props: {
    type: {
      type: String,
      default: 'CN'
    }
  },
  created() {
    this.getTotal()
    this.getList()
  },

  mounted() {

  },
  methods: {
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage1 = val;
      this.getList()
    },
    prev() {
      if (this.currentPage1 > 1) {
      this.currentPage1 = this.currentPage1 - 1
        this.getList()
    }
    },
    next(){
      this.currentPage1 +=1;
      this.getList()
    },
    getTotal(){
      axios.post(`http://59.110.34.218:10010/displayData/errorTotal?type=logistics`).then(
        response => {
          console.log(response.data.object)
          if(response.data.code == 0) {
            this.total = response.data.object
          }else{

          }
        },
      )
    },
    getList() {
      axios.post(`http://59.110.34.218:10010/displayData/logistics?page=${this.currentPage1}&number=20`).then(
        response => {
          console.log(response.data)
          if(response.data.code == 0) {
            this.list1 = response.data.object
          }
        },
      )
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },

    //连接
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作Success',
        type: 'success'
      })
      row.status = "success"
    },


    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        importance: 1,
        remark: '',
        timestamp: new Date(),
        title: '',
        status: 'published',
        type: ''
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
          createArticle(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
/*      this.temp.timestamp = new Date(this.temp.timestamp)*/
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateArticle(tempData).then(() => {
            const index = this.list1.findIndex(v => v.id === this.temp.id)
            this.list1.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$notify({
        title: 'Success',
        message: 'Delete Successfully',
        type: 'success',
        duration: 2000
      })
      this.list1.splice(index, 1)
    },
    handleFetchPv(pv) {
      fetchPv(pv).then(response => {
        this.pvData = response.data.pvData
        this.dialogPvVisible = true
      })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    }
  }
}
</script>



