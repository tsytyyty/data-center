<template>
  <div style="width: 94%;margin-left: 3%;margin-top: 2%">
    <aside>用户管理</aside>
    <div>
      <el-table
        :data="List"
		row-key="id"
        style="width: 100%">

        <el-table-column type="expand">
          <template slot-scope="props" >
            <el-form label-position="left" inline class="demo-table-expand" >
              <el-form-item label="用户名">
                <span>{{ props.row.username }}</span>
              </el-form-item>
              <el-form-item label="真实姓名">
                <span>{{ props.row.realName }}</span>
              </el-form-item>
              <el-form-item label="工号">
                <span>{{ props.row.idCard }}</span>
              </el-form-item>
              <el-form-item label="手机号">
                <span>{{ props.row.phone }}</span>
              </el-form-item>
              <el-form-item label="邮箱">
                <span>{{ props.row.email }}</span>
              </el-form-item>
              <el-form-item label="授权状态">
                <span>{{ props.row.active }}</span>
              </el-form-item>
            </el-form>
			<el-form v-slot="scope">
				<el-button type="primary" size="small" @click="auth('admin',props.row.id)" style="margin-left: 5%;">授权管理员</el-button>
				<el-button type="primary" size="small" @click="auth('editor',props.row.id)" style="margin-left: 5%;">授权普通用户</el-button>
			</el-form>
          </template>
        </el-table-column>
        <el-table-column
          label="用户名"
          prop="username">
        </el-table-column>
        <el-table-column
          label="真实姓名"
          prop="realName">
        </el-table-column>
        <el-table-column
          label="工号"
          prop="idCard">
        </el-table-column>
		<el-table-column
		  label="授权状态"
		  prop="active">
		</el-table-column>
      </el-table>
    </div>
  </div>

</template>

<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>

<script>
import axios from "axios";

export default {
  data() {
    return {
      List: [],
    }
  },
  created() {
    this.getList()
  },
  methods:{

	getList(){
		axios({
		    method:'post',
		    url: 'http://59.110.34.218:10010/alluser',
			params: null,
		  })
		  .then(response => {
				this.List=response.data.object
			  },
			  error => {
				console.log("error")
			  }
			)
		},



    auth(val,id){
		console.log(id);
	let params = {
		  userId: id,
		  power: val
		}
      axios({
        method: "post",
        url: 'http://59.110.34.218:10010/authorization',
        params: params,
      })
	  .then(response => {
         this.$notify({
           title: 'Success',
           message: '授权成功',
           type: 'success',
           duration: 2000
         })
        },
        error => {
			this.$notify({
              title: 'error',
              message: '授权失败',
              type: 'error',
              duration: 2000
            })
			}
      )
    }
	}
}
</script>
