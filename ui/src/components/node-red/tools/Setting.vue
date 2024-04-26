<template>
  <div class="setting-panel">
    <div>设置({{nodeData.text.value}})</div>
    <div class="setting-item-title">选择风格</div>
    <div class="style-wrapper">
      <div class="style-item" @click="$emit('changeStyle', item)" v-for="(item, index) in styleConfig" :key="index" :style="item"></div>
    </div>

    <div v-if="nodeData.type === 'open-node'">
      <input id="openUrl" type="text" :value="nodeData.properties.target" @blur="updateNodeData">
    </div>
  </div>
</template>

<script lang="ts" setup>
// type NodeData = {}
import { ref } from 'vue'
const props = defineProps({
  nodeData: {
    type: Object,
    default: () => {
      return {}
    }
  },
  lf: Object || String
})

const styleConfig = ref([
  {
    backgroundColor: 'rgb(255, 255, 255)',
    borderColor: 'rgb(42, 42, 42)',
    borderWidth: '1px'
  },
  {
    backgroundColor: 'rgb(245, 245, 245)',
    borderColor: 'rgb(102, 102, 102)',
    borderWidth: '1px'
  },
  {
    backgroundColor: 'rgb(218, 232, 252)',
    borderColor: 'rgb(108, 142, 191)',
    borderWidth: '1px'
  },
  {
    backgroundColor: 'rgb(213, 232, 212)',
    borderColor: 'rgb(130, 179, 102)',
    borderWidth: '1px'
  },
  {
    backgroundColor: 'rgb(248, 206, 204)',
    borderColor: 'rgb(184, 84, 80)',
    borderWidth: '1px'
  }
])

const updateNodeData = (e: any) => {
  const target = e.target.value
  props.lf.setProperties(props.nodeData.id, {
    target: target
  })

  console.log(JSON.stringify(props.lf.getGraphData()))

}

</script>

<style scoped>
.setting-panel {
  padding: 10px;
  background: #FFFFFF;
  border-left: 1px solid #dadce0;
  border-bottom: 1px solid #dadce0;
  width: 200px;
}
.style-wrapper {
  display: flex;
}
.style-item {
  width: 20px;
  height: 20px;
  border: 1px solid #FFF;
  margin-right: 5px;
  cursor: pointer;
}
.setting-item-title {
  font-size: 14px;
  color: #333;
  margin-top: 10px;
}
</style>