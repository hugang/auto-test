<script>
import {ref} from 'vue'
import LogicFlow from '@logicflow/core'
import '@logicflow/core/dist/style/index.css'
import "@logicflow/extension/lib/style/index.css";
import {Menu} from '@logicflow/extension';
import NodeRedExtension from './node-red/index'
import Setting from './node-red/tools/Setting.vue'
import axios from 'axios'

import './node-red/style.css'

export default {
  components: {Setting},
  setup() {
    const count = ref(0)
    const currentNode = ref(null)
    const lf = ref(null)
    return {
      count,
      currentNode,
      lf
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
        Menu, NodeRedExtension
      ]
    })
    this.lf.render({
      "nodes": [{
        "id": "f676a7d4-ceea-4b4e-8d4c-57863144e061",
        "type": "start-node",
        "x": 470,
        "y": 240,
        "properties": {"ui": "node-red"},
        "text": {"x": 480, "y": 240, "value": "start"}
      }], "edges": []
    })
    this.lf.on('node-red:start', () => {
      const commands = []
      const result = {
        "name": "test",
        "tests": [
          {
            "name": "test1",
            "commands": commands
          }
        ]
      }
      let data = this.lf.getGraphData();

      // Find the start node
      const startNode = data.nodes.find(node => node.type === 'start-node');
      result.name = startNode.id

      // Start executing nodes from the start node
      executeNodes(startNode.id, data.nodes, data.edges, commands);

      console.log(JSON.stringify(result))
      axios.post('http://localhost:9191/flow', result)
          .then(response => {
            console.log(response)
          })
          .catch(error => {
            console.log(error)
          })
    })

    this.lf.on('vue-node:click', (data) => {
      this.lf.setProperties(data.id, {
        t: ++data.val
      })
    })
    this.lf.on('node:click', ({data}) => {
      this.currentNode = data
    })
    this.lf.on('blank:click', ({data}) => {
      this.currentNode = null
    })
  },
  methods: {
    changeStyle(style) {
      this.lf.setProperties(this.currentNode.id, {
        style
      })
    }
  }
}

// Function to execute a node
function executeNode(node, commands) {
  // skip start node
  if (node.type === 'start-node') {
    return;
  }
  commands.push({
    command: node.properties.command,
    target: node.properties.target,
    value: node.properties.value
  })
}

// Recursive function to execute nodes
function executeNodes(nodeId, nodes, edges, commands) {
  // Find and execute the current node
  const node = nodes.find(node => node.id === nodeId);
  executeNode(node, commands);

  // Find the edge for the current node
  const edge = edges.find(edge => edge.sourceNodeId === nodeId);
  if (edge) {
    // Execute the next node
    executeNodes(edge.targetNodeId, nodes, edges, commands);
  }
}

</script>

<template>
  <div class="flow-chart">
    <div ref="container" class="container"></div>
    <Setting v-if="currentNode" @changeStyle="changeStyle" :lf="lf" :nodeData="currentNode"
             class="setting-panel"></Setting>
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
