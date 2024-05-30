<template>
  <div class="tab-container">

    <aside>
      治理后数据
    </aside>

    <el-tabs  v-model="activeName" style="margin-top:15px;" type="border-card" @tab-click="handleClick">
      <el-tab-pane v-for="item in tabMapOptions" :key="item.key" :label="item.label" :name="item.key">
        <tab-pane v-if="activeName==item.key" v-show="tab1"/>
        <tab-pane2  v-if="activeName==item.key" v-show="tab2"/>
        <tab-pane3  v-if="activeName==item.key" v-show="tab3"/>
        <tab-pane4  v-if="activeName==item.key" v-show="tab4"/>
      </el-tab-pane>

<!--      <div style="margin-top: -6%;margin-left: 12%">
        <el-col :span="4" class="text-center">
          <router-link class="pan-btn pink-btn" to="/excel/export-excel" style="width: 200px;margin-left: -20%">
            重新治理数据
          </router-link>
        </el-col>
        <el-col :span="4" class="text-center">
          <router-link class="pan-btn light-blue-btn" to="/excel/export-excel" style="width: 200px;margin-left: 00%">
            开始分析
          </router-link>
        </el-col>
      </div>-->
    </el-tabs>
  </div>
</template>

<script>
import TabPane from './components/TabPane'
import TabPane2 from "./components/TabPane2";
import TabPane3 from "@/views/share-data/components/TabPane3";
import TabPane4 from "@/views/share-data/components/TabPane4";

export default {
  name: 'page',
  components: { TabPane , TabPane2 ,TabPane3,TabPane4},
  data() {
    return {
      tabMapOptions: [
        { label: '物流信息', key: 'wlxx' ,showd: 'true'},
        { label: '客户信息', key: 'khxx' ,showd: 'false'},
        { label: '卸货表', key: 'zxb' ,showd: 'false'},
        { label: '装货表', key: 'xhb' ,showd: 'false'},
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
  watch: {
    activeName(val) {
      this.$router.push(`${this.$route.path}?tab=${val}`)
    }
  },
  methods: {
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


