<template>
  <div class="app-container">
    <el-row>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>数据可视化展示</span>
          <el-row :gutter="40" class="panel-group">
            <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="chart" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    Charts
                  </div>
                  <count-to :start-val="0" :end-val="7" :duration="2600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
            <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
              <div class="card-panel" @click="handleSetLineChartData('shoppings')">
                <div class="card-panel-icon-wrapper icon-shopping">
                  <svg-icon icon-class="eye-open" class-name="card-panel-icon" />
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">
                    eye
                  </div>
                  <count-to :start-val="0" :end-val="10" :duration="3600" class="card-panel-num" />
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <div style="margin-bottom:50px;">
          <el-col :span="4" class="text-center">
            <el-button class="pan-btn blue-btn" style="width: 250px" @click="chart('https://datav.dameng.com/dataview/publish/page.html?pageId=1676986925539405826&isTemplate=0')">
              港口的吞吐量分析
            </el-button>
          </el-col>
          <el-col :span="4" class="text-center">
            <el-button class="pan-btn light-blue-btn" to="/icon/index" style="width: 250px;margin-left: 145%" @click="chart('https://datav.dameng.com/dataview/publish/page.html?pageId=1668944483850395650&isTemplate=0')">
              港口不同类型货物吞吐趋势
            </el-button>
          </el-col>
          <el-col :span="4" class="text-center">
            <el-popover
              placement="top-start"
              width="300"
              trigger="hover">
              <aside><a href="https://datav.dameng.com/dataview/publish/page.html?pageId=1677920488711548930&isTemplate=0" target="_blank">2021港口货物吞吐同比环比</a></aside>
              <aside><a href="https://datav.dameng.com/dataview/publish/page.html?pageId=1677937560606765057&isTemplate=0" target="_blank">2022港口货物吞吐同比环比</a></aside>
              <el-button slot="reference" class="pan-btn pink-btn" to="/excel/export-excel" style="width: 250px;margin-left: 290%">
                港口货物吞吐同比环比
              </el-button>
            </el-popover>

          </el-col>
          <br><br><br>
          <el-col :span="4" class="text-center">
            <el-button class="pan-btn green-btn" to="/table/complex-table" style="width: 250px" @click="chart('https://datav.dameng.com/dataview/publish/page.html?pageId=1676492321459281921&isTemplate=0')">
              不同货物吞吐占比
            </el-button>
          </el-col>
          <el-col :span="4" class="text-center">
            <el-button class="pan-btn tiffany-btn" to="/example/create" style="width: 250px;margin-left: 145%" @click="chart('https://datav.dameng.com/dataview/publish/page.html?pageId=1676466769046282241&isTemplate=0')">
              不同货物流向分析
            </el-button>
          </el-col>
          <el-col :span="4" class="text-center">
            <el-button class="pan-btn yellow-btn" to="/theme/index" style="width: 270px;margin-left: 285%" @click="chart('https://datav.dameng.com/dataview/publish/page.html?pageId=1676988149647028226&isTemplate=0')">
              不同类型货物堆场流转周期分析
            </el-button>
          </el-col>
        </div>
      </el-card>
      <br>
      <el-col :span="4" class="text-center">
        <router-link class="pan-btn green-btn" to="/documentation/index" style="width: 250px;margin-left: 9%">
          重新提取
        </router-link>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import 'driver.js/dist/driver.min.css'
import CountTo from "vue-count-to"; // import driver.js css

export default {
  name: 'Guide',
  props: ['showd'],
  components: {
    CountTo
  },
  data() {
    return {

    }
  },
  mounted() {
    if(this.showd === true){
      console.log('success')
    }
  },

  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    },
    chart(url){
      console.log('???')
      window.location.href= url;
    }
  }
}
</script>

<style lang="scss" scoped>
.mixin-components-container {
  background-color: #f0f2f5;
  padding: 30px;
  min-height: calc(100vh - 84px);
}
.component-item{
  min-height: 100px;
}

.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
