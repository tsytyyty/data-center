<template>
  <div class="tab-container">
    <aside>
      异常数据处理
    </aside>

    <el-tabs  v-model="activeName" style="margin-top:15px;" type="border-card" @tab-click="handleClick">
      <el-tab-pane v-for="item in tabMapOptions" :key="item.key" :label="item.label" :name="item.key">
          <tab-pane v-if="activeName==item.key" v-show="tab1"/>
          <tab-pane2  v-if="activeName==item.key" v-show="tab2"/>
          <tab-pane3  v-if="activeName==item.key" v-show="tab3"/>
          <tab-pane4  v-if="activeName==item.key" v-show="tab4"/>
      </el-tab-pane>

      <div style="margin-top: -6%;margin-left: 12%">
        <el-col :span="4" class="text-center">
          <el-button class="pan-btn light-blue-btn" style="width: 200px;margin-left: 00%" @click="aly">
            开始分析
          </el-button>
        </el-col>
      </div>
    </el-tabs>
  </div>
</template>

<script>
import TabPane from './components/TabPane'
import TabPane2 from "./components/TabPane2";
import TabPane3 from "@/views/permission/components/TabPane3";
import TabPane4 from "@/views/permission/components/TabPane4";
import axios from "axios";

export default {
  name: 'page',
  props:['allData','a1','a2'],
  components: { TabPane , TabPane2 ,TabPane3,TabPane4},
  data() {
    return {
      tabMapOptions: [
        { label: '物流信息', key: 'wlxx' ,showd: 'true'},
        { label: '客户信息', key: 'khxx' ,showd: 'false'},
        { label: '装货表', key: 'zxb' ,showd: 'false'},
        { label: '卸货表', key: 'xhb' ,showd: 'false'},
      ],
      activeName: 'wlxx',
      createdTimes: 0,
      tab1: true,
      tab2: false,
      tab3: false,
      tab4: false,
      lastIndex: 0
    }
  },
  mounted() {
    console.log(this.$cookies.get('repeat'));
  },
  watch: {
    activeName(val) {
      this.$router.push(`${this.$route.path}?tab=${val}`)
    }
  },
  methods: {
    aly(){
      axios.post(`http://59.110.34.218:10010/dataAnalysis`).then(
        response => {
          console.log(response.data)
          if(response.data.code == 0) {
            this.$message({
              type: 'success',
              message: '分析结束!'
            });
            this.$router.push({path:'/alz/chart'});
          }else{
            this.$message({
              type: 'error',
              message: '异常错误!'
            });
          }
        }
      )
    },
    handleClick(tab, event) {
      console.log(tab.index, event);
      if(tab.index == 0){
        this.tab1=false
        this.tab2=false
        this.tab3=false
        this.tab4=false

        this.tab1 = true
      }else if(tab.index==1){
        this.tab1=false
        this.tab2=false
        this.tab3=false
        this.tab4=false

        this.tab2 = true
      }else if(tab.index==2){
        this.tab1=false
        this.tab2=false
        this.tab3=false
        this.tab4=false

        this.tab3 = true
      }else if(tab.index==3){
        this.tab1=false
        this.tab2=false
        this.tab3=false
        this.tab4=false


        this.tab4 = true
      }
    },
  }
}
</script>

<style scoped>
.tab-container {
  margin: 30px;
}
</style>


