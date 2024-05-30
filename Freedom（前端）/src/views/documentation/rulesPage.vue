<template>
  <div style="width: 100%;margin-left: 20%">
    <el-transfer class="edit_dev" v-model="value" :data="data" style="min-height: 1000px;height: 1000px;width: 50%"></el-transfer>
  </div>
</template>

<script>
export default {
  name: 'rulesPage',
  data() {
    const generateData = _ => {
      const data = [];
      for (let i = 1; i <= 15; i++) {
        data.push({
          key: i,
          label: `备选项 ${ i }`,
          disabled: i % 4 === 0
        });
      }
      return data;
    };
    return {
      data: generateData(),
      value: [1, 4]
    };
  }
};
</script>

<style scoped>
    .edit_dev >>> .el-transfer-panel {
    width: 300px;
    height: 500px;
    padding-right: 100px;
    float: right;
}
</style>
