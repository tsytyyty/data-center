<template>
  <div class="components-container board">
    <div><Kanban @click="open" :key="2" :list2="list" :list="list2" :group="group" class="kanban working" header-text="待处理请求" /></div>



  </div>

</template>
<script>
import Kanban from '@/views/user/reg/Kanban/index'
import axios from "axios";

export default {
  name: 'index',
  components: {
    Kanban
  },
  data() {
    return {
      isShow: true,
      group: 'mission',
      list2: [{}],
      list:[{}]
    }
  },

  created() {
    this.list2.pop()
    this.getList()
  },

  methods:{
    open(){

    },
    getList(){
      axios({
        method: "post",
        url: 'http://59.110.34.218:10010/needPowerUser',
        params: null,
      }).then(
        response => {
          console.log(response.data)
          if (response.data.code == 0) {
            this.list = response.data.object
            for(let i = 0 ;i < this.list.length; i++){
              console.log('???')
              let params = {
                name: `工号:  ${this.list[i].idCard}  ------  员工:  ${this.list[i].realName}  ------  注册申请确认`,
                id: this.list[i].id
              }
              this.list2.push(params)
            }

            console.log(this.list2)
          } else {

          }
        },
        error => {

        }
      )
    }
  }
}
</script>
<style lang="scss">

.kanban {
  &.todo {
    .board-column-header {
      background: #4A9FF9;
    }
  }
  &.working {
    .board-column-header {
      background: #f9944a;
    }
  }
  &.done {
    .board-column-header {
      background: #2ac06d;
    }
  }
}
</style>

