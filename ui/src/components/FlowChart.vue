<script>
import {ref} from 'vue'
import LogicFlow from '@logicflow/core'
import '@logicflow/core/dist/style/index.css'
import "@logicflow/extension/lib/style/index.css";
import {Control, Menu} from '@logicflow/extension';
import NodeRedExtension from './node-red/index'
import Dagre from "./node-red/tools/dagre";
import Setting from './node-red/tools/Setting.vue'
import axios from 'axios'

import './node-red/style.css'

export default {
  components: {Setting},
  setup() {
    const count = ref(0)
    const currentNode = ref(null)
    const lf = ref(null)
    const isLoading = ref(false)
    return {
      count,
      currentNode,
      lf,
      isLoading
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
        Menu, Control, NodeRedExtension, Dagre
      ]
    })

    this.lf.extension.control.addItem({
      iconClass: "beautify-flow",
      title: "",
      text: "一键美化",
      onClick: (lf, ev) => {
        lf.extension.dagre && this.lf.extension.dagre.layout({
          nodesep: 20,
          ranksep: 20,
        });
      }
    });

    this.lf.render({
      "nodes": [{
        "id": "f676a7d4-ceea-4b4e-8d4c-57863144e061",
        "type": "start-node",
        "x": 470,
        "y": 120,
        "properties": {"ui": "node-red"},
        "text": {"x": 480, "y": 120, "value": "start"}
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

      this.isLoading = true; // Show loading div
      axios.post('http://localhost:9191/flow', result)
          .then(response => {
            console.log(response)
            this.isLoading = false; // Hide loading div
          })
          .catch(error => {
            console.log(error)
            this.isLoading = false; // Hide loading div
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
function executeNode(node, commands, edgeComment) {
  // skip start node
  if (node.type === 'start-node') {
    return;
  }
  let comment = node.properties.comment || edgeComment;
  commands.push({
    command: node.properties.command,
    target: node.properties.target,
    value: node.properties.value,
    comment: comment
  })
}

// Recursive function to execute nodes
function executeNodes(nodeId, nodes, edges, commands, edgeComment) {
  // Find and execute the current node
  const node = nodes.find(node => node.id === nodeId);
  executeNode(node, commands, edgeComment);

  // Find the edge for the current node
  const edge = edges.find(edge => edge.sourceNodeId === nodeId);
  if (edge) {
    // Execute the next node
    executeNodes(edge.targetNodeId, nodes, edges, commands, edge.text ? edge.text.value : undefined);
  }
}

</script>

<template>
  <div class="flow-chart beautify-chart">
    <div ref="container" class="container"></div>
    <Setting v-if="currentNode" @changeStyle="changeStyle" :lf="lf" :nodeData="currentNode"
             class="setting-panel"></Setting>
  </div>
  <div v-if="isLoading" class="loading">Loading...</div>
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

.loading {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2em;
}
</style>
