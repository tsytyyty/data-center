<template>
  <div class="components-container">
    <aside>服务端返回JSON数据</aside>
    <div class="editor-container">
      <json-editor  ref="jsonEditor" v-model="value2" />
    </div>
  </div>
</template>

<script>
import JsonEditor from '@/components/JsonEditor'

const jsonData = '[{"items":[{"market_type":"forexdata","symbol":"XAUUSD"},{"market_type":"forexdata","symbol":"UKOIL"},{"market_type":"forexdata","symbol":"CORN"}],"name":""},{"items":[{"market_type":"forexdata","symbol":"XAUUSD"},{"market_type":"forexdata","symbol":"XAGUSD"},{"market_type":"forexdata","symbol":"AUTD"},{"market_type":"forexdata","symbol":"AGTD"}],"name":"贵金属"},{"items":[{"market_type":"forexdata","symbol":"CORN"},{"market_type":"forexdata","symbol":"WHEAT"},{"market_type":"forexdata","symbol":"SOYBEAN"},{"market_type":"forexdata","symbol":"SUGAR"}],"name":"农产品"},{"items":[{"market_type":"forexdata","symbol":"UKOIL"},{"market_type":"forexdata","symbol":"USOIL"},{"market_type":"forexdata","symbol":"NGAS"}],"name":"能源化工"}]'

export default {
  name: 'show-JSON',
  props: ['jsonStr'],
  components: { JsonEditor },

  data() {
    return {
      value: JSON.parse(jsonData),
      value2: this.jsonStr,
    }
  },

  created() {
    console.log(this.jsonStr)
  }

}
</script>

<style scoped>
.editor-container{
  position: relative;
  height: 100%;
}
</style>

