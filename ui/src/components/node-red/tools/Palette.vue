<template>
  <div class="demo-collapse">
    <el-collapse v-model="activeNames">
      <el-collapse-item title="基础节点" name="base">
        <div
            class="red-ui-palette-node ui-draggable ui-draggable-handle"
            @mousedown="startDrag(item)"
            v-for="(item, index) in baseNodes"
            :key="index"
            :style="{ backgroundColor: item.background }"
        >
          <div class="red-ui-palette-label">{{ item.text }}</div>
          <div class="red-ui-palette-icon-container">
            <div class="red-ui-palette-icon" :style="{ backgroundImage: `url(${item.icon})`}"></div>
          </div>
          <div class="red-ui-palette-port red-ui-palette-port-input"></div>
          <div class="red-ui-palette-port red-ui-palette-port-output"></div>
        </div>
      </el-collapse-item>

      <el-collapse-item title="功能" name="base">
        <div class="red-ui-palette-node ui-draggable ui-draggable-handle" style="background-color: rgb(253, 208, 162);">
          <div class="red-ui-palette-label" @click="importLf">导入</div>
        </div>
        <div class="red-ui-palette-node ui-draggable ui-draggable-handle" style="background-color: rgb(253, 208, 162);">
          <div class="red-ui-palette-label" @click="exportLf">导出</div>
        </div>
      </el-collapse-item>

    </el-collapse>
  </div>
</template>

<script lang="ts" setup>
import LogicFlow from "@logicflow/core"

import {ref} from 'vue'

const props = defineProps({
  lf: {
    type: LogicFlow,
    required: true,
  }
})

const activeNames = ref(['base'])

const startDrag = (item) => {
  const {lf} = props;
  lf.dnd.startDrag({
    type: item.type,
    text: item.text
  })
}

const baseNodes = ref([
  {
    type: 'auto-test-node',
    text: 'node',
    background: 'rgb(253, 208, 162)',
    icon: 'images/function.svg'
  },
  {
    type: 'vue-html',
    text: 'html',
    background: 'rgb(253, 208, 162)',
    icon: 'images/function.svg'
  },
])

// create a function exportLf to export the lf graph data as a json file
const exportLf = () => {
  // export lf graph data as a json file
  const {lf} = props;
  const data = lf.getGraphData()
  const blob = new Blob([JSON.stringify(data)], {type: 'text/plain;charset=utf-8'})
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'lf.json'
  a.click()
  URL.revokeObjectURL(url)
}

const importLf = () => {
  // open import dialog to read a json file , and set to lf graph data
  let fileInput = document.createElement("input")
  fileInput.type='file'
  fileInput.style.display='none'
  fileInput.id='fileInput'
  fileInput.onchange=readFile
  document.body.appendChild(fileInput)
  fileInput.click()

}
function readFile(e) {
  let file = e.target.files[0];
  if (!file) {
    return;
  }
  let reader = new FileReader();
  reader.onload = function (e) {
    const {lf} = props;
    let contents = e.target.result;
    document.body.removeChild(document.getElementById('fileInput'))
    lf.render(JSON.parse(contents))
  }
  reader.readAsText(file)
}

</script>
<style scoped>
.demo-collapse {
  width: 150px;
}

.demo-collapse :deep(.el-collapse-item__header) {
  text-indent: 20px;
}

.red-ui-palette-node {
  cursor: move;
  background: #fff;
  margin: 10px auto;
  height: 25px;
  border-radius: 5px;
  border: 1px solid #999;
  background-position: 5% 50%;
  background-repeat: no-repeat;
  width: 120px;
  background-size: contain;
  position: relative;
}

.red-ui-palette-label {
  color: #333;
  font-size: 13px;
  margin: 4px 0 4px 32px;
  line-height: 20px;
  overflow: hidden;
  text-align: center;
  -webkit-user-select: none;
  -khtml-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

.red-ui-palette-icon-container {
  position: absolute;
  text-align: center;
  top: 0;
  bottom: 0;
  left: 0;
  width: 30px;
  border-right: 1px solid rgba(0, 0, 0, .05);
  background-color: rgba(0, 0, 0, .05);
}

.red-ui-palette-icon {
  display: inline-block;
  width: 20px;
  height: 100%;
  background-position: 50% 50%;
  background-size: contain;
  background-repeat: no-repeat;
}

.red-ui-palette-port-output {
  left: auto;
  right: -6px;
}

.red-ui-palette-port {
  position: absolute;
  top: 8px;
  left: -5px;
  box-sizing: border-box;
  -moz-box-sizing: border-box;
  background: #d9d9d9;
  border-radius: 3px;
  width: 10px;
  height: 10px;
  border: 1px solid #999;
}

.red-ui-palette-port-output {
  left: auto;
  right: -6px;
}
</style>