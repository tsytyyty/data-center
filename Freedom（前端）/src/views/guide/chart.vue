<template>
  <div class="app-container">
	<div class="login" style="margin-bottom: 20px;">
		<el-alert title="账号:tsytyyty" type="success"
		 description="密码:admin123"
		 />
	</div>
    <div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div>
            <el-statistic group-separator="," :precision="2" :value="value2" :title="title"></el-statistic>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic title="男女比">
              <template slot="formatter"> 456/2 </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic group-separator="," :precision="2" decimal-separator="." :value="value1" :title="title">
              <template slot="prefix">
                <i class="el-icon-s-flag" style="color: red"></i>
              </template>
              <template slot="suffix">
                <i class="el-icon-s-flag" style="color: blue"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic :value="like ? 521 : 520" title="Feedback">
              <template slot="suffix">
              <span @click="like = !like" class="like">
                <i class="el-icon-star-on" style="color:red" v-show="!!like"></i>
                <i class="el-icon-star-off" v-show="!like"></i>
              </span>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-row>
    </div>


    <div style="margin-left: 1%;margin-top: 3%;text-align: left;float: left;display: inline-block" v-for="(item,index) in list">
      <el-row style="text-align: left;float: left;margin-left: 1%;display: inline-block" >
        <el-col :span="8">
            <el-card  :body-style="{ padding: '0px' }" style="width: 330px;height: 245px;margin-top: 5%;margin-left: 0%;float: left">
              <img @click="openUrl(item.url)" style="width: 100%;height:200px " :src="item.src" class="image">
              <div style="padding: 14px;background-color: whitesmoke">
                <span style="font-size: 16px;">{{ item.title }}
                  <span style="float:right"><i style="color: #7d7d7f;font-size: 18px" class="el-icon-picture-outline-round"></i></span>
                </span>
              </div>
            </el-card>
        </el-col>
      </el-row>

<!--      <el-row style="text-align: left;float: left;margin-left: 1%">
        <el-col :span="8">
          <el-card :body-style="{ padding: '0px' }" style="width: 330px;height: 285px;margin-top: 5%;margin-left: 0%;float: left">
            <img style="width: 350px;height:200px " src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
            <div style="padding: 14px;">
              <span>好吃的汉堡</span>
              <div class="bottom clearfix">
                <time class="time">{{ currentDate }}</time>
                <el-button type="text" class="button">操作按钮</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="text-align: left;float: left;margin-left: 1%">
        <el-col :span="8">
          <el-card :body-style="{ padding: '0px' }" style="width: 330px;height: 285px;margin-top: 5%;margin-left: 0%;float: left">
            <img style="width: 350px;height:200px " src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
            <div style="padding: 14px;">
              <span>好吃的汉堡</span>
              <div class="bottom clearfix">
                <time class="time">{{ currentDate }}</time>
                <el-button type="text" class="button">操作按钮</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-row style="text-align: left;float: left;margin-left: 1%">
        <el-col :span="8">
          <el-card :body-style="{ padding: '0px' }" style="width: 330px;height: 285px;margin-top: 5%;margin-left: 0%;float: left">
            <img style="width: 350px;height:200px " src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
            <div style="padding: 14px;">
              <span>好吃的汉堡</span>
              <div class="bottom clearfix">
                <time class="time">{{ currentDate }}</time>
                <el-button type="text" class="button">操作按钮</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>


      <el-row style="text-align: left;float: left;margin-left: 1%;margin-top: 30px">
        <el-col :span="8">
          <el-card :body-style="{ padding: '0px' }" style="width: 330px;height: 285px;margin-top: 5%;margin-left: 0%;float: left">
            <img style="width: 350px;height:200px " src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image">
            <div style="padding: 14px;">
              <span>好吃的汉堡</span>
              <div class="bottom clearfix">
                <time class="time">{{ currentDate }}</time>
                <el-button type="text" class="button">操作按钮</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>-->


    </div>
  </div>
</template>

<script>
import DataChartList from './DataChartList'

export default {
  name: 'chart',
  components: { DataChartList },
  data() {
    return {
      options: {
        data: {},
        showList: true
      },
      currentDate: new Date(),
      list: [
        {
          src: require('@/assets/chartPic/01.jpg'),
          title: '不同货物流向分析',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=1676466769046282241&isTemplate=0',
        },{
          src: require('@/assets/chartPic/00.jpg'),
          title: '港口不同类型货物吞吐趋势',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=1676945417352269826&isTemplate=0',
        },{
          src: require('@/assets/chartPic/03.jpg'),
          title: '港口吞吐量分析',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=1676986925539405826&isTemplate=0',
        },{
          src: require('@/assets/chartPic/04.jpg'),
          title: '不同类型货物堆场流转周期分析',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=1676988149647028226&isTemplate=0',
        },{
          src: require('@/assets/chartPic/02.jpg'),
          title: '不同货物吞吐占比',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=1676492321459281921&isTemplate=0',
        },{
          src: require('@/assets/chartPic/06.jpg'),
          title: '2018港口货物吞吐同比环比',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=3372f22ac4f70ae85d893f9d28070612&isTemplate=0',
        },{
          src: require('@/assets/chartPic/05.jpg'),
          title: '2019港口货物吞吐同比环比',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=1677515204667142145&isTemplate=0',
        },{
          src: require('@/assets/chartPic/07.jpg'),
          title: '2020港口货物吞吐同比环比',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=70eca3ee66ac2352fee87048ca3b7eb2&isTemplate=0',
        },{
          src: require('@/assets/chartPic/08.jpg'),
          title: '2021港口货物吞吐同比环比',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=84716dee8daf69f0ca53f65987d44f1d&isTemplate=0',
        },{
          src: require('@/assets/chartPic/09.jpg'),
          title: '2022港口货物吞吐同比环比',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=9b9f2be4b535e07092b6e81dce4f3043&isTemplate=0',
        },{
          src: require('@/assets/chartPic/20.jpg'),
          title: '苏州港综合信息报表',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=59818ecdd643d832af12acc79bf79641&isTemplate=0',
        },{
          src: require('@/assets/chartPic/21.jpg'),
          title: '南通港综合信息报表',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=88508b4011a025d5b4abdd04e61f0329&isTemplate=0',
        },{
          src: require('@/assets/chartPic/22.jpg'),
          title: '芜湖港综合信息报表',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=1689318252989276162&isTemplate=0',
        },{
          src: require('@/assets/chartPic/23.jpg'),
          title: '杭州港综合信息报表',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=afaaf72941ed83499fe5829e73773681&isTemplate=0',
        },{
          src: require('@/assets/chartPic/24.jpg'),
          title: '镇江港综合信息报表',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=fb424f6e0d41fbfc0ad2dc6f689782d7&isTemplate=0',
        },{
          src: require('@/assets/chartPic/25.jpg'),
          title: '泰州港综合信息报表',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=5263dd6e52295469c93bc3634c7f8677&isTemplate=0',
        },{
          src: require('@/assets/chartPic/26.jpg'),
          title: '铜陵港综合信息报表 ',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=88d6fd87be03bccaf178314f09673733&isTemplate=0',
        },{
          src: require('@/assets/chartPic/27.jpg'),
          title: '马鞍山港综合信息报表',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=1cf6ccec06c2cc8f89fbf67b337b6ebb&isTemplate=0',
        },{
          src: require('@/assets/chartPic/28.jpg'),
          title: '太仓港综合信息报表',
          url: 'http://120.55.190.237:8015/dataview/publish/page.html?pageId=9fedbcabf83d2d244280f6abd7016489&isTemplate=0',
        }
      ]
    }
  },
  methods: {
    showCard(data) {
      Object.assign(this.options, data)
    },
    openUrl(url){
      console.log('?????')
      window.open(url,"_blank")
    }
  }
}
</script>

<style lang="scss" scoped>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>
