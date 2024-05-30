<template>
  <div style="margin-left: 20%;margin-right: 20%;margin-top: 8%">
    <aside>
      治理后数据存储记录
    </aside>
    <el-table
      :data="tableData"
      border
      style="width: 100%">
      <el-table-column
        fixed
        prop="id"
        label="ID"
        width="400">
      </el-table-column>
      <el-table-column
        prop="date"
        label="日期"
        width="350">
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="80">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  name: "index1",
  methods: {
    handleClick(row) {
      this.$router.push({name: 'dataStorage',params: {id: '1',}});
      console.log(row);
    }
  },

  data() {
    return {
      tableData: [{
        id: '1',
        date: '2016-05-04',
      }, {
        id: '2',
        date: '2016-05-04',
      }, {
        id: '3',
        date: '2016-05-04',
      }, {
        id: '4',
        date: '2016-05-04',
      }]
    }
  }
}
</script>

<style scoped>

</style>
