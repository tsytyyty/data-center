<template>
  <div>
    <div style="margin-left: 3%;width: 94%;margin-top: 2%">
      <aside>
        数据集操作
      </aside>
    </div>

    <div style="margin-left: 3%;width: 94%">
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


      <el-table-column height="1000" label="序号" prop="id" sortable="custom" align="center" width="100" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据集名称" prop="id" sortable="custom" align="center" width="399" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="id" sortable="custom" align="center" width="250" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <el-tag type="success">
            可用
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="id" sortable="custom" align="center" width="350" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}')}}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            操作
          </el-button>
        </template>
      </el-table-column>
    </el-table>

      <div class="block" style="margin-top: 2%">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          @prev-click="prev"
          @next-click="next"
          :current-page.sync="currentPage1"
          :page-size="20"
          layout="total, prev, pager, next"
          :total="7">
        </el-pagination>
      </div>
    </div>


  </div>
</template>

<script>
import Pagination from "@/components/Pagination";
import waves from "@/directive/waves";
import axios from "axios";
import {createArticle, fetchPv, updateArticle} from "@/api/article";
import {parseTime} from "@/utils";

export default {

    name: "dataDetails",
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
          id: 'null',
          name: 'null',
          status: 'null',
          createTime: new Date(),
          dataSourceName: null
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
        list1: [{
          id: '1',
          name: '港口吞吐量分析',
          status: '成功',
          createTime: new Date(),
          dataSourceName: null
        },{
          id: '2',
          name: '港口不同类型货物吞吐趋势',
          status: '成功',
          createTime: new Date(),
          dataSourceName: null
        },{
          id: '3',
          name: '2022年港口货物吞吐同比环比',
          status: '成功',
          createTime: new Date(),
          dataSourceName: null
        },{
          id: '4',
          name: '2021年港口货物吞吐同比环比',
          status: '成功',
          createTime: new Date(),
          dataSourceName: null
        },{
          id: '5',
          name: '不同货物吞吐占比',
          status: '成功',
          createTime: new Date(),
          dataSourceName: null
        },{
          id: '6',
          name: '不同货物流向分析',
          status: '成功',
          createTime: new Date(),
          dataSourceName: null
        },{
          id: '7',
          name: '不同类型货物堆场流转周期分析',
          status: '成功',
          createTime: new Date(),
          dataSourceName: null
        }]
      }
    },
    props: {
      type: {
        type: String,
        default: 'CN'
      }
    },
    created() {

    },

    mounted() {

    },
    methods: {
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
      updateData() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.temp.repeat = true
            this.temp.examine = true
            axios({
              method: "post",
              url: 'http://59.110.34.218:10010/updateLogistics',
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
                }else{
                  this.$message({
                    type: 'error',
                    message: '提交错误!'
                  });
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

<style scoped>

</style>
