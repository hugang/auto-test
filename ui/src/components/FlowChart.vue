<script>
import { ref } from 'vue'
import LogicFlow from '@logicflow/core'
import '@logicflow/core/dist/style/index.css'
import NodeRedExtension from './node-red/index'
import Setting from './node-red/tools/Setting.vue'

import './node-red/style.css'
export default {
  components: {Setting},
  setup() {
    const count = ref(0)
    const currentNode = ref(null)
    return {
      count,
      currentNode
    }
  },
  mounted() {
    this.lf = new LogicFlow({
      container: this.$refs.container,
      grid: {
        visible: true,
        type: 'mesh',
        size: 10,
        config: {
          color: '#eeeeee'
        }
      },
      // adjustEdge: false,
      hoverOutline: false,
      edgeSelectedOutline: false,
      keyboard: {
        enabled: true,
      },
      // keyboard: true,
      plugins: [
        NodeRedExtension
      ]
    })
    this.lf.render({})
    this.lf.on('node-red:start', () => {
      // todo: 让流程跑起来
      // console.log('我要开始执行流程了')
      // log graph data
      console.log(JSON.stringify(this.lf.getGraphData()))

    })
    this.lf.on('vue-node:click', (data) => {
      this.lf.setProperties(data.id, {
        t: ++data.val
      })
    })
    this.lf.on('node:click', ({ data }) => {
      this.currentNode = data
    })
    this.lf.on('blank:click', ({ data }) => {
      this.currentNode = null
    })
  },
  methods: {
    changeStyle (style) {
      this.lf.setProperties(this.currentNode.id, {
        style
      })
    },
    changeProperties (data) {
      console.log(data.target.value)
      this.lf.setProperties(this.currentNode.id, {
        target: data.target.value
      })
    }
  }
}
</script>

<template>
  <div class="flow-chart">
    <div ref="container" class="container"></div>
    <Setting v-if="currentNode" @changeProperties="changeProperties" @changeStyle="changeStyle" :nodeData="currentNode" class="setting-panel"></Setting>
  </div>
</template>

<style scoped>
.container {
  width: 100%;
  height: 100%;
}
.flow-chart {
  position: relative;
  width: 100%;
  height: 100%;
}
.flow-chart :deep(.lf-red-node), .flow-chart :deep(.lf-element-text) {
  cursor: move;
}
.flow-chart :deep(svg) {
  display: block;
}
.flow-chart-palette {
  position: absolute;
  left: 0;
  top: 0;
  z-index: 1;
}
.setting-panel {
  position: absolute;
  top: 0;
  right: 0;
}
</style>
