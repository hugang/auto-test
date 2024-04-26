<script>
import {ref} from 'vue'
import LogicFlow from '@logicflow/core'
import '@logicflow/core/dist/style/index.css'
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
        NodeRedExtension
      ]
    })
    this.lf.render({"nodes":[{"id":"dc1b3b0a-c530-4ccc-a9b4-fd9efd38f608","type":"open-node","x":370,"y":220,"properties":{"target":"aaa","ui":"node-red"},"text":{"x":380,"y":220,"value":"open"}},{"id":"adda8faf-9d26-4bc8-91db-05ead3769f8d","type":"start-node","x":250,"y":220,"properties":{"ui":"node-red"},"text":{"x":260,"y":220,"value":"start"}},{"id":"144fa5be-2eec-40fa-abde-a34fea53d578","type":"open-node","x":500,"y":220,"properties":{"target":"bbb","ui":"node-red"},"text":{"x":510,"y":220,"value":"open"}}],"edges":[{"id":"1d0ff359-0cea-4504-b6a8-7299660cfc57","type":"flow-link","sourceNodeId":"adda8faf-9d26-4bc8-91db-05ead3769f8d","targetNodeId":"dc1b3b0a-c530-4ccc-a9b4-fd9efd38f608","startPoint":{"x":300,"y":220},"endPoint":{"x":320,"y":220},"properties":{},"pointsList":[{"x":300,"y":220},{"x":400,"y":220},{"x":220,"y":220},{"x":320,"y":220}]},{"id":"954f7b36-f445-4c03-8a95-66f8c5c034b0","type":"flow-link","sourceNodeId":"dc1b3b0a-c530-4ccc-a9b4-fd9efd38f608","targetNodeId":"144fa5be-2eec-40fa-abde-a34fea53d578","startPoint":{"x":420,"y":220},"endPoint":{"x":450,"y":220},"properties":{},"pointsList":[{"x":420,"y":220},{"x":520,"y":220},{"x":350,"y":220},{"x":450,"y":220}]}]})
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
      console.log(JSON.stringify(data))

      // Find the start node
      const startNode = data.nodes.find(node => node.type === 'start-node');
      console.log(JSON.stringify(startNode))
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
    command: node.type.replace('-node', ''),
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
    console.log(`Executing edge: ${edge.targetNodeId}`)
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
