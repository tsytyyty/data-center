<template>
  <div>
    <el-table
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
    </el-table>
  </div>
</template>

<script>
export default {
  name: "sql4",
  props: ['list1'],
  data() {
    return {

    }
  },
}
</script>

<style scoped>

</style>
