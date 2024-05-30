<template>
  <div class="board-column">
    <div class="board-column-header">
      {{ headerText }}
    </div>
    <draggable
      :list="list"
      v-bind="$attrs"
      class="board-column-content"
      :set-data="setData"
    >
      <div v-for="element in list" :key="element.id" class="board-item" @click="open(element)">
        {{ element.name }}
      </div>
    </draggable>

    <div style="margin-top: 3%" v-if="isShow">
      <el-card>
        <el-descriptions class="margin-top" title="用户信息" :column="3" :size="size" border>
          <template slot="extra">
            <el-button type="primary" size="small" @click="auth('admin')">授权管理员</el-button>
            <el-button type="primary" size="small" @click="auth('editor')">授权普通用户</el-button>
          </template>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-user"></i>
              用户名
            </template>
            {{temp.username}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-mobile-phone"></i>
              手机号
            </template>
            {{ temp.phone }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-location-outline"></i>
              邮箱
            </template>
            {{temp.email}}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-tickets"></i>
              工号
            </template>
            <el-tag size="small">{{temp.idCard}}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              <i class="el-icon-office-building"></i>
              真实姓名
            </template>
            {{temp.realName}}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

    </div>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import axios from "axios";

export default {
  name: 'DragKanbanDemo',
  components: {
    draggable
  },
  props: {
    headerText: {
      type: String,
      default: 'Header'
    },
    options: {
      type: Object,
      default() {
        return {}
      }
    },
    list: {
      type: Array,
      default() {
        return []
      }
    },
    list2: {
      type: Array,
      default() {
        return []
      }
    },

  },
  data() {
    return {
      isShow: false,
      temp:null,
    }
  },
  created() {
    console.log(this.list2)
  },
  methods: {
    setData(dataTransfer) {
      // to avoid Firefox bug
      // Detail see : https://github.com/RubaXa/Sortable/issues/1012
      dataTransfer.setData('Text', '')
    },

    open(item){
      for(let i = 0 ; i<this.list2.length;i++){
        if(item.id == this.list2[i].id){
          this.temp = this.list2[i]
        }
      }
      this.isShow = true
    },

    auth(val){
      let params = {
        userId : this.temp.id,
        power: val
      }
      axios({
        method: "post",
        url: 'http://59.110.34.218:10010/authorization',
        params: params,
      }).then(
        response => {
          console.log(response.data)
          if (response.data.code == 0) {
            this.$notify({
              title: 'Success',
              message: '授权成功',
              type: 'success',
              duration: 2000
            })
            this.isShow = false
            this.temp = null
          } else {
            this.$notify({
              title: 'error',
              message: '授权失败',
              type: 'error',
              duration: 2000
            })
          }
        },
        error => {

        }
      )
    }
  }
}
</script>
<style lang="scss" scoped>
.board-column {
  min-width: 100%;
  min-height: 100px;
  height: auto;
  overflow: hidden;
  background: #f0f0f0;
  border-radius: 3px;

  .board-column-header {
    height: 50px;
    line-height: 50px;
    overflow: hidden;
    padding: 0 20px;
    text-align: center;
    background: #333;
    color: #fff;
    border-radius: 3px 3px 0 0;
  }

  .board-column-content {
    height: auto;
    overflow: hidden;
    border: 10px solid transparent;
    min-height: 60px;
    display: flex;
    justify-content: flex-start;
    flex-direction: column;
    align-items: center;

    .board-item {
      cursor: pointer;
      width: 100%;
      height: 64px;
      margin: 5px 0;
      background-color: #fff;
      text-align: left;
      line-height: 54px;
      padding: 5px 10px;
      box-sizing: border-box;
      box-shadow: 0px 1px 3px 0 rgba(0, 0, 0, 0.2);
    }
  }
}
</style>

