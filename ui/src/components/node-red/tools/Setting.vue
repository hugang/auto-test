<template>
  <div class="setting-panel">
    <div>设置({{nodeData.text.value}})</div>
    <div class="setting-item-title">选择风格</div>
    <div class="style-wrapper">
      <div class="style-item" @click="$emit('changeStyle', item)" v-for="(item, index) in styleConfig" :key="index" :style="item"></div>
    </div>

    <div v-if="nodeData.type !== 'start-node'">
      <div class="setting-item-title">command</div>
      <select id="command" @change="updateNodeData" :value="nodeData.text.value">
        <option value="click">click</option>
        <option value="type">type</option>
        <option value="wait">wait</option>
      </select>
      <div id="tips"></div>
      <div class="setting-item-title">description</div>
      <input id="description" type="text" :value="nodeData.properties.description" @blur="updateNodeData">
      <hr>
      <div class="setting-item-title">target</div>
      <input id="target" type="text" :value="nodeData.properties.target" @blur="updateNodeData">
      <div class="setting-item-title">value</div>
      <input id="value" type="text" :value="nodeData.properties.value" @blur="updateNodeData">
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
  if (e.target.id === 'value') {
    props.lf.setProperties(props.nodeData.id, {
      value: target
    })
  }else if (e.target.id === 'target') {
    props.lf.setProperties(props.nodeData.id, {
      target: target
    })
  }else if (e.target.id === 'description') {
    props.lf.setProperties(props.nodeData.id, {
      description: target
    })
  }else if (e.target.id === 'command') {
    // update node text
    props.lf.updateText(props.nodeData.id,target)
    // show command tips in tips div
    const tips = document.getElementById('tips')
    if (target === 'click') {
      tips.innerHTML = 'click: Clicks on a link, button, checkbox, or radio button.'
    } else if (target === 'type') {
      tips.innerHTML = 'type: Types a string of text into the target.'
    } else if (target === 'wait') {
      tips.innerHTML = 'wait: Waits for a number of milliseconds.'
    }
  }
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