<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.url" placeholder="地址" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.type" placeholder="数据库类型" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
      </el-select>
      <el-select v-model="listQuery.sort" style="width: 140px" class="filter-item" @change="handleFilter">
        <el-option v-for="item in sortOptions" :key="item.key" :label="item.label" :value="item.key" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="test">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">
        新建数据源
      </el-button>
      <el-button v-waves :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">
        导出
      </el-button>

<!--      <el-button size="medium" v-waves class="filter-item" type="danger" @click="cleanData">
        清除数据
      </el-button>-->
      <el-button size="medium" v-waves class="filter-item" type="success" @click="open(row)">
        数据采集
      </el-button>

    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="序号" prop="number" sortable="custom" align="center" width="80" :class-name="getSortClass('number')">
        <template slot-scope="{row}">
          <span>{{ row.number }}</span>
        </template>
      </el-table-column>
      <el-table-column label="日期" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.timestamp | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据源名称" min-width="120px" align="center">
        <template slot-scope="{row}">
          <span @click="handleUpdate(row)">{{ row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column label="地址" min-width="150px">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.url }}</span>
          <el-tag>{{ row.type | typeFilter }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="同步状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag type="success">
            已同步
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
<!--          <el-button size="mini" type="success" @click="handleModifyStatus(row,'published')">
            连接
          </el-button>-->
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>

          <el-button type="success" size="mini" @click="testConnect(row)">
            测试连接
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="类型" prop="type">
          <el-select v-model="temp.type" class="filter-item" placeholder="Please select">
            <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据源名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="日期" prop="timestamp">
          <el-date-picker v-model="temp.timestamp" type="datetime" placeholder="Please pick a date" />
        </el-form-item>
        <el-form-item label="地址" prop="url">
          <el-input v-model="temp.url" />
        </el-form-item>
        <el-form-item label="数据库名" prop="databaseName" v-if="temp.type=='mysql'">
          <el-input v-model="temp.databaseName"/>
        </el-form-item>
        <el-form-item label="用户名" prop="username" v-if="temp.type!='hdfs'">
          <el-input v-model="temp.username"/>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="temp.type!='hdfs'">
          <el-input v-model="temp.password"/>
        </el-form-item>
        <el-form-item label="桶名称" prop="bucketName" v-if="temp.type=='minio'">
          <el-input v-model="temp.bucketName"/>
        </el-form-item>
        <el-form-item label="数据路径" prop="path" v-if="temp.type=='hdfs'">
          <el-input v-model="temp.path"/>
        </el-form-item>



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="success" @click="testConnect2(temp)">
          测试连接
        </el-button>
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


    <el-dialog title="数据清洗" :visible.sync="dialogFormVisible1" width="40%">
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="startCollectData">开始清洗</el-button>
      </div>
    </el-dialog>

    <div>
      <el-dialog
        v-loading="loading"
        element-loading-text="拼命采集中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        style="width: 100%"
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center>
        <span>正在采集中</span>
        <span slot="footer" class="dialog-footer">
        <el-button type="primary">确定</el-button>
        </span>
      </el-dialog>
    </div>

    <div>
      <el-dialog
        v-loading="loading1"
        element-loading-text="拼命治理中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        style="width: 100%"
        title="提示"
        :visible.sync="centerDialogVisible1"
        width="30%"
        center>
        <span>正在治理中</span>
        <span slot="footer" class="dialog-footer">
        <el-button type="primary">确定</el-button>
        </span>
      </el-dialog>
    </div>

    <div>
      <el-dialog
        v-loading="loading2"
        element-loading-text="拼命治理中"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        style="width: 100%"
        title="提示"
        :visible.sync="testVis"
        width="30%"
        center>
        <span>测试连接...</span>
        <span slot="footer" class="dialog-footer">
        <el-button type="primary">确定</el-button>
        </span>
      </el-dialog>
    </div>

    </div>
</template>

<script>
import { fetchList, fetchPv, createArticle, updateArticle } from '@/api/article'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import {dataSourceSave, dataSourceConnectionTest, dataAcquisition} from '@/api/user'
import axios from "axios";
import permission from '@/directive/permission/index.js' // 权限判断指令
import checkPermission from '@/utils/permission' // 权限判断函数

const calendarTypeOptions = [
  { key: 'mysql', display_name: 'MySQL' },
  { key: 'minio', display_name: 'Minio' },
  { key: 'hdfs', display_name: 'Hdfs' }
]

// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'Documentation',
  components: { Pagination},
  directives: { waves,permission },
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
      loading2: false,
      testVis: false,
      isConnected: false,
      loading1: false,
      centerDialogVisible1: false,
      centerDialogVisible: false,
      loading: false,
      checkList: [],
      isHdfs: true,
      tableKey: 0,
      /*list: null,*/
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        url: undefined,
        type: undefined,
        sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID 升序', key: '+id' }, { label: 'ID 降序', key: '-id' }],
      statusOptions: ['connectable', 'unknown', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        number: null,
        name: '',
        timestamp: new Date(),
        url: '',
        type: 'minio',
        status: 'connectable',
        username: '',
        password: '',
        bucketName:'',
        path:'',
        databaseName:'',
      },
      list: [{
        id: 1,
        number: null,
        name: '用户信息库',
        timestamp: new Date(),
        url: 'mysqla-mysqld.damenga-zone.svc:3306',
        type: 'mysql',
        status: 'unknown',
        username: 'mysqluser',
        password: 'Dameng123',
        databaseName:'cnsoft',
      },{
        id: 2,
        number: null,
        name: '集装箱动态库',
        timestamp: new Date(),
        url: 'http://10.104.171.187',
        type: 'minio',
        status: 'unknown',
        username: 'cnsof14013305',
        password: 'Cnsoft18266086979',
        bucketName: 'data'
      },{
        id: 3,
        number: null,
        name: '物流信息-装货-卸货表',
        timestamp: new Date(),
        url: 'hdfs://hadoopa-namenode.damenga-zone.svc:9000',
        type: 'hdfs',
        status: 'unknown',
        username: null,
        password: null,
        path:'/'
      }],
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        name: [{ required: true, message: '', trigger: 'blur' }],
        url: [{ required: true, message: '', trigger: 'blur' }],
        username: [{ required: true, message: '', trigger: 'blur' }],
        password: [{ required: true, message: '', trigger: 'blur' }],
        path: [{ required: true, message: '', trigger: 'blur' }],
        bucketName: [{ required: true, message: '', trigger: 'blur' }],
        databaseName: [{ required: true, message: '', trigger: 'blur' }]
      },
      downloadLoading: false,
      dialogFormVisible1: false,
      allConnectable: false
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /*sourceSave() {
      for (let i = 0; i <= 2; i++) {

        let params = {
          type: this.list[i].type,
          url: this.list[i].url,
          username: this.list[i].username,
          password: this.list[i].password,
          path: this.list[i].path,
          bucketName: this.list[i].bucketName,
        }

        axios({
          method: "post",
          url: 'http://59.110.34.218:10010/acquisition-server/dataSourceSave',
          params: params,
        }).then(
          response => {
            console.log(response.data)
            if (response.data.code == 0) {
              this.list[i].status = 'connectable'
            }
          },
          error => {
          }
        )
      }

    },*/
    getList() {
      this.listLoading = true
      axios({
        method: "get",
        url: 'http://59.110.34.218:10010/acquisition-server/dataSource/list',
      }).then(
        response => {
          console.log(response.data)
          if (response.data.code == 0) {
            this.listLoading = false
            this.list = response.data.object
            for(let i =0;i<this.list.length;i++) {
              this.list[i].status = 'unknown'
              this.list[i].timestamp = new Date()
              this.list[i].number = i + 1;
            }
          } else {
            this.$message({
              type: 'error',
              message: '获取失败!'
            });
          }
        },
        error => {
          console.log('error', error.message)
          this.listLoading = false
          this.$message({
            type: 'error',
            message: '连接失败!'
          });
        }
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
        url: '',
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
      if(this.isConnected == true) {
        //添加数据源
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            let params = {
              type: this.temp.type,
              url: this.temp.url,
              name: this.temp.name,
              username: this.temp.username,
              password: this.temp.password,
              path: this.temp.path,
              bucketName: this.temp.bucketName,
              databaseName: this.temp.databaseName
            }

            /*let isConnect = await this.testConnect2(params)*/

            if (true) {
              console.log(params)
              axios({
                method: "post",
                url: 'http://59.110.34.218:10010/acquisition-server/dataSource/add',
                params: params,
              }).then(
                response => {
                  console.log(response.data)
                  if (response.data.code == 0) {
                    this.$message({
                      type: 'success',
                      message: '数据源添加成功!'
                    });
                    //返回一个id

                    this.temp.id = 0
                    this.temp.number = this.list.length + 2
                    this.temp.status = 'connectable'
                    console.log(this.temp)
                    this.list.push(this.temp)

                    //清除
                    this.isConnected = false
                  } else {
                    this.$message({
                      type: 'error',
                      message: '数据源添加失败!'
                    });
                  }
                },
                error => {
                  console.log('error', error.message)
                  this.$message({
                    type: 'error',
                    message: '数据源添加失败!'
                  });
                }
              )
            } else {
              this.$message({
                type: 'error',
                message: '测试连接失败!'
              });
            }
          }
        })
      }else{
        this.$message({
          type: 'error',
          message: '请先测试连接'
        });
      }

/*      this.$refs['dataForm'].validate((valid) => {
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
      })*/
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      if(this.isConnected == true) {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            let params = {
              id: this.temp.id,
              type: this.temp.type,
              url: this.temp.url,
              name: this.temp.name,
              username: this.temp.username,
              password: this.temp.password,
              path: this.temp.path,
              bucketName: this.temp.bucketName,
              databaseName: this.temp.databaseName
            }
              console.log(params)
              axios({
                method: "post",
                url: 'http://59.110.34.218:10010/acquisition-server/dataSource/update',
                params: params,
              }).then(
                response => {
                  console.log(response.data)
                  if (response.data.code == 0) {
                    this.$message({
                      type: 'success',
                      message: '数据源更新成功!'
                    });
                    this.temp.status = 'connectable'
                    //清除
                    this.isConnected = false
                    const tempData = Object.assign({}, this.temp)
                    tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
                    updateArticle(tempData).then(() => {
                      const index = this.list.findIndex(v => v.id === this.temp.id)
                      this.list.splice(index, 1, this.temp)
                      this.dialogFormVisible = false
                      this.$notify({
                        title: 'Success',
                        message: 'Update Successfully',
                        type: 'success',
                        duration: 2000
                      })
                    })

                  } else {
                    this.$message({
                      type: 'error',
                      message: '数据源更新失败!'
                    });
                  }
                },
                error => {
                  console.log('error', error.message)
                  this.$message({
                    type: 'error',
                    message: '连接失败!'
                  });
                }
              )
          }
        })
      }else{
        this.$message({
          type: 'error',
          message: '请先测试连接'
        });
      }
    },
    handleDelete(row, index) {

      if(row.id == 1){
        this.$message({
          type: 'success',
          message: '禁止删除!'
        });
      }else {
        let params = row
        console.log(row.id)
        axios({
          method: "post",
          url: 'http://59.110.34.218:10010/acquisition-server/dataSource/delete',
          params: row
        }).then(
          response => {
            console.log(response.data)
            if (response.data.code == 0) {
              this.list.splice(index, 1)
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
            } else {
              this.$message({
                type: 'error',
                message: '删除失败!'
              });
            }
          },
          error => {
            console.log('error', error.message)
            this.listLoading = false
            this.$message({
              type: 'error',
              message: '连接失败!'
            });
          }
        )
      }

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
        const tHeader = ['timestamp', 'url', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'url', 'type', 'importance', 'status']
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
    },

    //开始清洗数据
    startCollectData(){

      this.centerDialogVisible1 = true
      this.loading1 = true

        //Post请求
      axios.get(`http://59.110.34.218:10010/analysis-server/data/clean`).then(
        response => {
          console.log(response.data)
          if(response.data.code == 0) {
            this.$message({
              type: 'success',
              message: '清洗成功!'
            });
            this.loading1 = false
            this.centerDialogVisible1 = false
            this.dialogFormVisible1 = false

            // this.$cookies.set('repeat', a2)
            // this.$cookies.set('examine', a1)
            this.$router.push({name: '异常数据处理'});
          }else{
            this.$message({
              type: 'error',
              message: '数据清洗失败!'
            });
          }
        },
        error =>{
          console.log('error',error.message)
          this.$message({
            type: 'error',
            message: '数据清洗失败!'
          });
        }
      )
        //结束后关闭该对话框
        this.dialogFormVisible1 = false;

    },

    //测试连接
    testConnect(row){
      this.testVis = true
      this.loading2 = true
        let params = {
          id: row.id,
          type: row.type,
          name: row.name,
          url: row.url,
          username: row.username,
          password: row.password,
          bucketName: row.bucketName,
          dataAndType: row.dataAndType
        }

        console.log(row)
      axios({
        method: "post",
        url: 'http://59.110.34.218:10010/acquisition-server/dataSource/test',
        data: row,
        header: {
          'content-type': 'application/x-www-form-urlencoded'
        },
      }).then(
        response => {
          console.log(response.data)
          if(response.data.code == 0) {
            this.$message({
              type: 'success',
              message: '测试成功!'
            });
            console.log(this.list)
            this.list[row.number - 1].status = "connectable"
            this.testVis = false
            this.loading2 = false
          }else{
            this.$message({
              type: 'error',
              message: '测试失败!'
            });
            this.testVis = false
            this.loading2 = false
          }
        },
        error =>{
          console.log('error',error.message)
          this.$message({
            type: 'error',
            message: '测试失败!'
          });
          this.testVis = false
          this.loading2 = false
        }
      )
    },

   testConnect2(temp){
     let params = {
       id: temp.id,
       type: temp.type,
       url: temp.url,
       name: temp.name,
       username: temp.username,
       password: temp.password,
       path: temp.path,
       bucketName: temp.bucketName,
       dbName:temp.databaseName
     }


     axios({
       method: "post",
       url: 'http://59.110.34.218:10010/acquisition-server/test/beforeAdd',
       data: params,
     }).then(
       response => {
         console.log(response.data)
         if(response.data.code == 0) {
           this.$message({
             type: 'success',
             message: '测试成功!'
           });
           console.log(this.list)

           //可添加更新的标识符
           this.isConnected = true
         }else{
           this.$message({
             type: 'error',
             message: '测试失败!'
           });
         }
       },
       error =>{
         console.log('error',error.message)
         this.$message({
           type: 'error',
           message: '测试失败!'
         });
       }
     )
    },

    checkConnectable(){
      for(let i = 0;i< this.list.length;i++){
        if(this.list[i].type != 'mysql'){
          if(this.list[i].status == 'connectable'){
            this.allConnectable = true
          }else{
            this.allConnectable = false
            break;
          }
        }
      }
    },

    openDialog(){
      this.centerDialogVisible = true
      this.loading = true
    },


    //数据采集
    open() {
      this.checkConnectable();
      if(!this.allConnectable){
        this.$alert('请先测试连接通过', '标题', {
          confirmButtonText: '确定',
          callback: action => {
          }
        });
      }else {
        this.$confirm('是否开始数据采集?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(() => {
          this.openDialog();
            axios({
              method: "get",
              url: 'http://59.110.34.218:10010/acquisition-server/data/acquisition',
              timeout: 60000
            }).then(
              response => {
                console.log(response.data)
                if(response.data.code == 0) {
                  this.$message({
                    type: 'success',
                    message: '采集成功!'
                  });
                  this.loading = false
                  this.centerDialogVisible = false
                  this.dialogFormVisible1 = true
                }else{
                  this.$message({
                    type: 'error',
                    message: '数据采集失败!'
                  });
                }
              },
              error =>{
                console.log('error',error.message)
                this.$message({
                  type: 'error',
                  message: '数据采集失败!'
                });
              }
            )
        }
        ).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消采集'
          });
        });
      }
    },

    cleanData(){
      axios({
        method: "post",
        url: 'http://59.110.34.218:10010/acquisition-server/dataClear',
      }).then(
        response => {
          if(response.data.code == 0) {
            console.log(response.data);
            this.$message({
              type: 'success',
              message: '数据清除成功!'
            });
          }else{
            this.$message({
              type: 'error',
              message: '数据清除失败!'
            });
          }
        },
        error =>{
          console.log('error',error.message)
          this.$message({
            type: 'error',
            message: '数据采集失败!'
          });
        }
      )
    },

    test(){
      this.$router.push({name: '异常数据处理',params: {wlxx: '1234',}});
    }

  }
}
</script>

