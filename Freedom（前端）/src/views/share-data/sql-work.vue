<template>
  <div>
    <div style="width: 94%;margin-left: 3%;margin-top: 2%">
      <aside>达梦数据库-SQL工作台</aside>
    </div>

    <div style="margin-left: 3%">
      数据表选择：
      <el-select v-model="value" placeholder="请选择">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
    </div>



    <div style="width: 94%;margin-left: 3%;margin-top: 7%;">
      <sql-editor
        ref="sqleditor"
        :value="sqlText"
        style="height: 300px; margin: 20px 0;"
        @changeTextarea="changeTextarea($event)"
      />
    </div>

    <div>
      <div style="margin-top: 30px;margin-right: 3%;">
        <div style="float: right">
          <el-col :span="4" class="text-center">
            <el-button class="pan-btn green-btn" @click="sendSql" style="width: 200px;margin-left: 00%">
              运行
            </el-button>
          </el-col>
        </div>

        <div style="margin-right: 1%;float: right">
          <el-col :span="4" class="text-center">
            <el-button class="pan-btn green-btn" @click="sendData" style="width: 200px;margin-left: 00%">
              报表配置
            </el-button>
          </el-col>
        </div>

        <div style="float: left">
          <div style="margin-left: 30%">
            <el-col :span="4" class="text-center">
              <el-button class="pan-btn pink-btn" @click="refreshData" style="width: 150px;margin-left: 00%">
                重置
              </el-button>
            </el-col>
          </div>
          <div style="margin-left: 140%">
            <el-col :span="4" class="text-center">
              <el-button class="pan-btn pink-btn" @click="refreshData" style="width: 150px;margin-left: 00%">
                格式化
              </el-button>
            </el-col>
          </div>
        </div>
      </div>
    </div>

    <div style="margin-top: 8%;margin-left: 3%;width: 94%;min-height: 1000px">
      <el-card>
      <el-row>
        <el-col>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="信息" name="first">
          <pre>{{sqlConsole}}</pre>
        </el-tab-pane>
        <el-tab-pane label="结果" name="second">
<!--          <sql1 v-if="sql_1" :list1="tableData"></sql1>-->
          <sql2 v-if="sql_2" :list1="tableData"></sql2>
<!--          <sql3 v-if="sql_3" :list1="tableData"></sql3>
          <sql4 v-if="sql_4" :list1="tableData"></sql4>-->
        </el-tab-pane>
        <el-tab-pane label="JSON" name="third" @click="jsongive">
          <json-editor  ref="jsonEditor" v-model="jsonStr"/>
        </el-tab-pane>
		<el-tab-pane label="数据地图" name="fourth" @click="datamap">
			<div class="tab-map" style="margin-left: 20px;">
				<el-tabs :tab-position="tabPosition" style="height: 100%;font-weight: 600;font-size: 30px;">
						<el-tab-pane label="customer_information">
							<el-table :data="customer_information" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
								<el-table-column
									  prop="date"
									  label="字段名"
									  width="500px">
								</el-table-column>
								<el-table-column
									  prop="name"
									  label="注释"
									  width="500px">
								</el-table-column>
							</el-table>
						</el-tab-pane>
						<el-tab-pane label="goods_data">
						<el-table :data="goods_data" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="goods_port_put">
						<el-table :data="goods_port_put" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="goods_throughput">
						<el-table :data="goods_throughput" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="goods_time">
						<el-table :data="goods_time" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="loading_table">
						<el-table :data="loading_table" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="logistics_information">
						<el-table :data="logistics_information" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="port_data">
						<el-table :data="port_data" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="port_goods_data">
						<el-table :data="port_goods_data" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="port_goods_trend">
						<el-table :data="port_goods_trend" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="port_throughput">
						<el-table :data="port_throughput" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
						<el-tab-pane label="unloading_table">
						<el-table :data="unloading_table" border stripe style="width: 1001px;margin-top: 10px;margin-left: 5px;">
							<el-table-column
								  prop="date"
								  label="字段名"
								  width="500px">
							</el-table-column>
							<el-table-column
								  prop="name"
								  label="注释"
								  width="500px">
							</el-table-column>
						</el-table>
						</el-tab-pane>
				</el-tabs>
			</div>
		</el-tab-pane>
      </el-tabs>
        </el-col>
      </el-row>
      </el-card>
    </div>

  </div>
</template>

<script>

import JsonEditor from '@/components/JsonEditor'
import sqlEditor from "@/components/SqlEditor";
import axios from "axios";
import Sql1 from "@/views/share-data/components/sql1";
import Sql2 from "@/views/share-data/components/sql2";
import Sql3 from "@/views/share-data/components/sql3";
import Sql4 from "@/views/share-data/components/sql4";

export default {
  name: 'sql-work',
  components: {
    Sql1,
    Sql2,
    Sql3,
    Sql4,
    sqlEditor,
    JsonEditor
  },
  data() {
    return {
      sql_1: false,
      sql_2: false,
      sql_3: false,
      sql_4: false,
      options: [{
        value: 'customer_information',
        label: '客户信息'
      }, {
        value: 'logistics_information',
        label: '物流信息'
      }, {
        value: 'loading_table',
        label: '装货表'
      }, {
        value: 'unloading_table',
        label: '卸货表'
      }],

      value: 'logistics_information',
      // 数据源数据字典
      sourceOptions: [],
      sqlDataSource: undefined,
      sqlText: undefined,
      sqlExecuting: false,
      activeTabName: 'table0',
      sqlExecutorId: undefined,
      sqlConsole: '暂无信息',
      executeResultInfo: undefined,
      activeName: 'first',
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }],
	  customer_information: [{
	            date: 'name',
	            name: '客户名称',
	          }, {
	            date: 'id',
	            name: '客户编号',
	          }, {
	            date: 'phone_number',
	            name: '手机号',
	          }, {
	            date: 'address',
	            name: '省市区',
	          },{
	            date: 'remark',
	            name: '备注',
	  		}],
	  goods_data: [{
	            date: 'goods',
	            name: '货物',
	          }, {
	            date: 'year',
	            name: '年份',
	          }, {
	            date: 'month',
	            name: '月份',
	          }, {
	            date: 'throughput',
	            name: '吞吐量',
	          }],
	  goods_port_put: [{
	            date: 'goods',
	            name: '货物',
	          }, {
	            date: 'port',
	            name: '港口',
	          }, {
	            date: 'put',
	            name: '流量',
	          }, {
	            date: 'percentage',
	            name: '占比',
	          }],
	  goods_throughput: [{
	            date: 'goods',
	            name: '货物',
	          }, {
	            date: 'percentage',
	            name: '占比',
	          }, {
	            date: 'throughput',
	            name: '吞吐量',
	          }],
	  goods_time: [{
	            date: 'goods',
	            name: '货物',
	          }, {
	            date: 'time',
	            name: '平均流转时间',
	          }],
	  loading_table: [{
	            date: 'ship_companies',
	            name: '船公司',
	          }, {
	            date: 'ship_name',
	            name: '船名称',
	          }, {
	            date: 'work_begin_time',
	            name: '作业开始时间',
	          },{
	  		  date: 'departure',
	  		  name: '始发时间',
	  		},{
	  		  date: 'arrive_time',
	  		  name: '到达时间',
	  		},{
	  		  date: 'port',
	  		  name: '作业港口',
	  		},{
	  		  date: 'order_id',
	  		  name: '提单号',
	  		},{
	  		  date: 'container_id',
	  		  name: '集装箱号',
	  		},{
	  		  date: 'container_size',
	  		  name: '箱尺寸',
	  		},{
	  		  date: 'departure_place',
	  		  name: '启运地',
	  		},{
	  		  date: 'destination_place',
	  		  name: '目的地',
	  		},{
	  		  date: 'remar',
	  		  name: '备注',
	  		}],
	  logistics_information: [{
	            date: 'id',
	            name: '提单号',
	          }, {
	            date: 'owner',
	            name: '货主名称',
	          }, {
	            date: 'owner_id',
	            name: '货主代码',
	          },{
	  		  date: 'company_id',
	  		  name: '物流公司',
	  		},{
	  		  date: 'container_id',
	  		  name: '集装箱号',
	  		},{
	  		  date: 'goods_name',
	  		  name: '货物名称',
	  		},{
	  		  date: 'weight',
	  		  name: '货重',
	  		},{
	  		  date: 'remar',
	  		  name: '备注',
	  		}],
	  port_data: [{
	            date: 'port',
	            name: '港口',
	          }, {
	            date: 'year',
	            name: '年份',
	          }, {
	            date: 'month',
	            name: '月份',
	          }, {
	            date: 'throughput',
	            name: '吞吐量',
	          },{
	            date: 'yoy',
	            name: '同比',
	          }, {
	            date: 'qoq',
	            name: '环比',
	          }],
	  port_goods_data: [{
	            date: 'port',
	            name: '港口',
	          }, {
	            date: 'goods',
	            name: '货物',
	          }, {
	            date: 'year',
	            name: '年份',
	          }, {
	            date: 'month',
	            name: '月份',
	          }, {
	            date: 'throughput',
	            name: '吞吐量',
	          }],
	  port_goods_trend: [{
	            date: 'port',
	            name: '港口',
	          }, {
	            date: 'goods',
	            name: '货物',
	          }, {
	            date: 'trend',
	            name: '趋势',
	          }],
	  port_throughput: [{
	            date: 'port',
	            name: '港口',
	          }, {
	            date: 'percentage',
	            name: '占比',
	          }, {
	            date: 'throughput',
	            name: '吞吐量',
	          }],
	  unloading_table: [{
	            date: 'ship_companies',
	            name: '船公司',
	          }, {
	            date: 'ship_name',
	            name: '船名称',
	          }, {
	            date: 'work_begin_time',
	            name: '作业开始时间',
	          },{
	  		  date: 'departure',
	  		  name: '始发时间',
	  		},{
	  		  date: 'arrive_time',
	  		  name: '到达时间',
	  		},{
	  		  date: 'port',
	  		  name: '作业港口',
	  		},{
	  		  date: 'order_id',
	  		  name: '提单号',
	  		},{
	  		  date: 'container_id',
	  		  name: '集装箱号',
	  		},{
	  		  date: 'container_size',
	  		  name: '箱尺寸',
	  		},{
	  		  date: 'departure_place',
	  		  name: '启运地',
	  		},{
	  		  date: 'destination_place',
	  		  name: '目的地',
	  		},{
	  		  date: 'remar',
	  		  name: '备注',
	  		}],
      jsonStr: undefined,
      finalValue: null,
    }
  },
  created() {
    this.finalValue = this.$cookies.get('username') + Math.random().toString(36).slice(-6)
    console.log(this.finalValue)
  },
  methods: {
    changeTextarea(val) {
      this.sqlText = val
    },
    formaterSql() {
      if (!this.sqlText) {
        return
      }
      this.$refs.sqleditor.editor.setValue(sqlFormatter.format(this.$refs.sqleditor.editor.getValue()))
    },
    refreshData() {
      if (!this.sqlText) {
        return
      }
      this.sqlExecuting = false
      this.activeTabName = 'table0'
      this.sqlExecutorId = undefined
      this.sqlText = undefined
      this.$refs.sqleditor.editor.setValue('')
      this.sqlConsole = []
      this.executeResultInfo = undefined
    },

    handleClick(tab, event) {
      console.log(tab, event);
    },

    clear(){
      this.jsonStr = null
      this.sql_1 = false
      this.sql_2 = false
      this.sql_3 = false
      this.sql_4 = false
    },

    setShow(){
      this.sql_2 = true
/*      if(this.value == 'customer_information'){
        this.sql_1 = true
      }

      if(this.value == 'logistics_information'){
        this.sql_2 = true
      }

      if(this.value == 'loading_table'){
        this.sql_3 = true
      }

      if(this.value == 'unloading_table'){
        this.sql_4 = true
      }*/
    },

    sendData(){
      axios({
        method: "post",
        url: `http://59.110.34.218:10010/saveResult?username=${this.finalValue}`,
        params: null,
      }).then(
        response => {
          this.sqlExecuting = true
          console.log(response.data)
          if (response.data.code == 0) {
            this.$confirm(`跳转至报表配置网站!配置账号:tsytyyty。密码:admin123。当前表名:${this.finalValue}`, '提示', {
              confirmButtonText: '跳转',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              //打开一个新页面
              window.open('http://120.55.190.237:8015/page/auth/login.html',"_blank")
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消'
              });
            });
          } else {

          }
        },
      error => {

      }
      )
    },

    sendSql(){
      this.clear();
      this.sqlExecuting = true
      let params = this.sqlText;
      console.log(params)
      console.log(`http://59.110.34.218:10010/selectTable?username=${this.finalValue}&sql=${params}`)

      axios({
        method: "post",
        url: `http://59.110.34.218:10010/selectTable?username=${this.finalValue}&sql=${params}`,
        params: null,
      }).then(
        response => {
          this.sqlExecuting = true
          console.log(response.data)
          if (response.data.code == 0) {
            this.tableData = response.data.object
            this.$message({
              message: '操作成功',
              type: 'success'
            })
            this.sqlExecuting = false

            this.sqlConsole = this.sqlText + '\n'
            this.sqlConsole = this.sqlConsole + '> 状态：成功\n> 耗时：0.005s'
            this.jsonStr = response.data

            this.setShow();
          }else{
            this.$message({
              message: response.data.object,
              type: 'error'
            })

            this.executeResultInfo = response.data.object

            this.sqlConsole = this.sqlText + '\n'
            this.sqlConsole = this.sqlConsole + response.data.object
            this.sqlExecuting = false
            this.jsonStr = response.data
          }
        },
        error => {
        }
      )
    },
    jsongive(){
      let abc = this.jsonStr
      this.jsonStr = null
      this.jsonStr = abc
    }
  }
}
</script>

<style lang="scss" scoped>
.CodeMirror {
  border: 1px solid black;
  font-size: 13px;
}

.CodeMirror-hints{
  z-index: 9999 !important;
}
</style>
