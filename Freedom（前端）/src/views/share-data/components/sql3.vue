<template>
  <div>

    <el-table :data="tableList" stripe height="200" width="50%">
      <el-table-column
        v-for="(item, index) in tableLabel"
        :key="index"
        :label="item.label"
        :prop="item.prop"
      ></el-table-column>
    </el-table>



<!--    <el-table
      :data="columnList"
      stripe
      border
      :max-height="300"
      style="width: 100%; margin: 15px 0;"
    >
      <el-table-column label="序号" width="55" align="center">
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <template v-for="(column, index) in columnList">
        <el-table-column
          :key="index"
          :prop="column.data"
          :label="column.name"
          align="center"
          show-overflow-tooltip
        >
        </el-table-column>

      </template>
    </el-table>-->

<!--    <el-table
      :data="list1"
      style="width: 100%">
      <el-table-column
        prop="ship_companies"
        label="ship_companies"
        width="180">
      </el-table-column>
      <el-table-column
        prop="ship_name"
        label="ship_name"
        width="180">
      </el-table-column>
      <el-table-column
        prop="work_begin_time"
        label="work_begin_time">
      </el-table-column>
      <el-table-column
        prop="work_end_time"
        label="work_end_time">
      </el-table-column>
      <el-table-column
        prop="departure_time"
        label="departure_time">
      </el-table-column>
      <el-table-column
        prop="arrive_time"
        label="arrive_time">
      </el-table-column>
      <el-table-column
        prop="port"
        label="port">
      </el-table-column>
      <el-table-column
        prop="order_id"
        label="order_id">
      </el-table-column>
      <el-table-column
        prop="container_id"
        label="container_id">
      </el-table-column>
      <el-table-column
        prop="container_size"
        label="container_size">
      </el-table-column>
      <el-table-column
        prop="departure_place"
        label="departure_place">
      </el-table-column>
      <el-table-column
        prop="destination_place"
        label="destination_place">
      </el-table-column>
      <el-table-column
        prop="remark"
        label="remark">
      </el-table-column>
    </el-table>-->
  </div>
</template>

<script>
export default {
  name: "sql3",
  props: ['list1'],
  data() {
    return {
      columnIndex: ['颜色','内存'],
      columnList: [{
        name: 'n1',
        data: ['1','2','3']
      },{
        name: 'n2',
        data: 'd2'
      }],
      tableList: [
        {
          name: "Rachel",
          age: "23",
          sex: "girl",
          hobby: "eat",
        },
        {
          name: "Mike",
          age: "32",
          sex: "boy",
          hobby: "play",
        }
      ],
      tableLabel: [
        {
          label: "姓名",
          prop: "name",
        },
        {
          label: "年龄",
          prop: "age",
        },
        {
          label: "性别",
          prop: "sex",
        },
        {
          label: "爱好",
          prop: "hobby",
        },
      ],

      lableList:[{}],


    }
  },

  created() {
    /*console.log(this.tableList[0].key)*/
    let keylist = [{}]
    keylist.pop()
    let i = 0
    for (let key in this.list1[0]){
      console.log(key);
      let params = {
        label: key,
        prop: key
      }
      keylist.push(params)
    }
    console.log(keylist)


  }
}
</script>

<style scoped>

</style>
