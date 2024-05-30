<template>
  <div>
    <el-table
      :data="list1"
      style="width: 100%">
      <el-table-column
        prop="name"
        label="name"
        width="180">
      </el-table-column>
      <el-table-column
        prop="id"
        label="id"
        width="180">
      </el-table-column>
      <el-table-column
        prop="phone_number"
        label="phone_number">
      </el-table-column>
      <el-table-column
        prop="address"
        label="address">
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
  name: "sql1",
  props: ['list1'],
  data() {
    return {

    }
  },
}
</script>

<style scoped>

</style>
