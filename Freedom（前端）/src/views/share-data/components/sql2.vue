<template>
  <div style="min-height: 1000px">

    <el-table :data="list1" stripe min-height="50" width="50%">
      <el-table-column
        v-for="(item, index) in lableList"
        :key="index"
        :label="item.label"
        :prop="item.prop"
      ></el-table-column>
    </el-table>


<!--    <el-table
      :data="list1"
      style="width: 100%">
      <el-table-column
        prop="id"
        label="id"
        width="180">
      </el-table-column>
      <el-table-column
        prop="owner"
        label="owner"
        width="180">
      </el-table-column>
      <el-table-column
        prop="owner_id"
        label="owner_id">
      </el-table-column>
      <el-table-column
        prop="company_id"
        label="company_id">
      </el-table-column>
      <el-table-column
        prop="container_id"
        label="container_id">
      </el-table-column>
      <el-table-column
        prop="goods_name"
        label="goods_name">
      </el-table-column>
      <el-table-column
        prop="weight"
        label="weight">
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
  name: "sql2",
  props: ['list1'],
  data() {
    return {
      lableList:[{}],
    }
  },
  created() {
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
    this.lableList = keylist
    console.log(keylist)
  }
}
</script>

<style scoped>

</style>
