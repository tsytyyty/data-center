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
      <el-table-column label="客户名称"  prop="id" sortable="custom" align="center" width="160" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span :key="row.id">{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="客户编号" prop="id" sortable="custom" align="center" width="280" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号" prop="id" sortable="custom" align="center" width="250" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.phoneNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column label="省市区" prop="id" sortable="custom" align="center" width="210" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.address }}</span>
        </template>
      </el-table-column>

      <el-table-column label="原因" prop="id" sortable="custom" align="center" width="200" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.remark }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="184" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            修改
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
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="90px" style="width: 400px; margin-left:50px;">
        <el-form-item label="客户名称" prop="name">
          <el-input v-model="temp.name"/>
        </el-form-item>
        <el-form-item label="客户编号" prop="id">
          <el-input v-model="temp.id"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phoneNumber">
          <el-input v-model="temp.phoneNumber"/>
        </el-form-item>
        <el-form-item label="省市区" prop="address">
          <el-input v-model="temp.address"/>
        </el-form-item>
        <el-form-item label="原因" prop="address">
          <el-input v-model="temp.remark"/>
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
  name: 'TabPane2',
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
        name: null,
        id: null,
        phoneNumber: null,
        address: null,
        remark: null
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
        name: [{ required: true, message: 'is required', trigger: 'change' }],
        id: [{ required: true, message: 'is required', trigger: 'change' }],
        phoneNumber: [{ required: true, message: 'is required', trigger: 'change' }],
        address: [{ required: true, message: 'is required', trigger: 'change' }],
      },
      downloadLoading: false,
/*      list1: [{
        name: 'null',
        id: 'null',
        phoneNumber: 'null',
        address: 'null',
        remark: 'null'
      }],*/
      list1:[{}],
      currentList: [{}],
      currentPage: 1
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
/*    console.log(this.list1)
    this.$bus.$on('customer',item => {
      console.log(item[0])
      this.list1[0] = item[0]
      console.log(this.list1[0],this.list1[0].shipName)
      //监测到提交事件上传

    })*/
  },

  mounted() {
  },
  methods: {
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.currentPage1 = val;
      this.currentPage = val
      this.getList()
      /*let a =0
      for(let i = this.currentPage * 20;i < this.currentPage * 20 + 20 ; i++){
        this.currentList[a] = this.list1[i]
        a++
      }
      this.tableKey = 10000*/
    },
    prev() {
      if (this.currentPage1 > 1) {
        this.currentPage1 = this.currentPage1 - 1
        this.currentPage -=1;
        this.getList()
        /*let a = 0
        for(let i = this.currentPage * 20;i < this.currentPage * 20 + 20 ; i++){
          this.currentList[a] = this.list1[i]
          a++
        }*/
      }
/*      this.tableKey = 9999*/
    },
    next(){
      this.currentPage1 += 1;
      this.currentPage +=1;
      this.getList();
      /*let a = 0
      for(let i = this.currentPage * 20;i < this.currentPage * 20 + 20 ; i++){
        console.log(i,a)
        this.currentList[a] = this.list1[i]
        a++
      }
      this.tableKey = 9999*/
    },
    getTotal(){
      axios.post(`http://59.110.34.218:10010/analysis-server/error/data/errorTotal?type=customerError`).then(
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
      this.listLoading = true
      axios.get(`http://59.110.34.218:10010/analysis-server/error/data/list/customerError?page=${this.currentPage}&number=20`).then(
        response => {
          console.log(response.data.object)
          if(response.data.code == 0) {
            if(response.data.object.length != 0) {
              this.list1 = response.data.object
              this.tableKey = 8848
              this.listLoading = false
/*              for(let i = 0;i < 20;i++){
                this.currentList[i] = this.list1[i]
                console.log(this.currentList,i)
              }*/
            }else{
              this.listLoading = false
            }
          }else{
            this.listLoading = false
          }
        },
      )
    },
    handleFilter() {
      this.listQuery.page = 1
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
    formatDate(value) {
      var date = new Date(value);
      var y = date.getFullYear(),
        m = date.getMonth() + 1,
        d = date.getDate();

      if (m < 10) { m = '0' + m; }
      if (d < 10) { d = '0' + d; }
      var t = y + '-' + m + '-' + d;
      return t;
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.repeat = this.$cookies.get('repeat')
          this.temp.examine = this.$cookies.get('examine')
          axios({
            method: "post",
            url: 'http://59.110.34.218:10010/updateCustomerData',
            params: this.temp,
            timeout: 60000
          }).then(
            response => {
              console.log(response.data)
              if(response.data.code == 0) {
                this.$message({
                  type: 'success',
                  message: '提交成功!'
                });
                console.log(response.data)
                this.getList()
              }else{
                this.$alert(response.data.message, '错误的提交', {
                  confirmButtonText: '确定',
                  callback: action => {
                    this.$message({
                      type: 'info',
                      message: `action: ${ action }`
                    });
                  }
                });
                this.getList()
              }
            },
            error =>{
              console.log('error',error.message)
              this.$message({
                type: 'error',
                message: '提交错误!'
              });
            }
          )
/*          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          updateArticle(tempData).then(() => {
            const index = this.list1.findIndex(v => v.name === this.temp.name)
            this.list1.splice(index, 1, this.temp)
            this.dialogFormVisible = false
            this.$notify({
              title: 'Success',
              message: 'Update Successfully',
              type: 'success',
              duration: 2000
            })
          })*/
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



