<template>
  <div class="setting-panel">
    <div>设置({{nodeData.properties.command}})</div>
    <div class="setting-item-title">选择风格</div>
    <div class="style-wrapper">
      <div class="style-item" @click="$emit('changeStyle', item)" v-for="(item, index) in styleConfig" :key="index" :style="item"></div>
    </div>

    <div v-if="nodeData.type !== 'start-node'">
      <div class="setting-item-title">command</div>
      <select class="setting-input" id="command" @change="updateNodeData" :value="nodeData.properties.command">
        <option v-for="option in selectOptions" :value="option.value">{{ option.label }}</option>
      </select>
      <div class="setting-item-title">target</div>
      <input class="setting-input" id="target" type="text" :value="nodeData.properties.target" @blur="updateNodeData">
      <div class="setting-item-title">value</div>
      <input class="setting-input" id="value" type="text" :value="nodeData.properties.value" @blur="updateNodeData">
      <div class="setting-item-title">description</div>
      <input class="setting-input" id="description" type="text" :value="nodeData.properties.description" @blur="updateNodeData">
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

const selectOptions = ref([
  {value: 'addSelection', label: 'addSelection'},
  {value: 'answerOnNextPrompt', label: 'answerOnNextPrompt'},
  {value: 'assertAlert', label: 'assertAlert'},
  {value: 'assertConfirmation', label: 'assertConfirmation'},
  {value: 'assertPrompt', label: 'assertPrompt'},
  {value: 'assertChecked', label: 'assertChecked'},
  {value: 'assert', label: 'assert'},
  {value: 'assertEditable', label: 'assertEditable'},
  {value: 'assertElementNotPresent', label: 'assertElementNotPresent'},
  {value: 'assertElementPresent', label: 'assertElementPresent'},
  {value: 'assertNotChecked', label: 'assertNotChecked'},
  {value: 'assertNotEditable', label: 'assertNotEditable'},
  {value: 'assertNotSelectedLabel', label: 'assertNotSelectedLabel'},
  {value: 'assertNotSelectedValue', label: 'assertNotSelectedValue'},
  {value: 'assertNotText', label: 'assertNotText'},
  {value: 'assertSelectedLabel', label: 'assertSelectedLabel'},
  {value: 'assertSelectedValue', label: 'assertSelectedValue'},
  {value: 'assertText', label: 'assertText'},
  {value: 'assertTitle', label: 'assertTitle'},
  {value: 'assertValue', label: 'assertValue'},
  {value: 'check', label: 'check'},
  {value: 'chooseCancelOnNextConfirmation', label: 'chooseCancelOnNextConfirmation'},
  {value: 'chooseCancelOnNextPrompt', label: 'chooseCancelOnNextPrompt'},
  {value: 'chooseOkOnNextConfirmation', label: 'chooseOkOnNextConfirmation'},
  {value: 'chooseOkOnNextPrompt', label: 'chooseOkOnNextPrompt'},
  {value: 'clickAt', label: 'clickAt'},
  {value: 'click', label: 'click'},
  {value: 'close', label: 'close'},
  {value: 'doubleClickAt', label: 'doubleClickAt'},
  {value: 'doubleClick', label: 'doubleClick'},
  {value: 'dragAndDropToObject', label: 'dragAndDropToObject'},
  {value: 'echo', label: 'echo'},
  {value: 'editContent', label: 'editContent'},
  {value: 'executeAsyncScript', label: 'executeAsyncScript'},
  {value: 'executeScript', label: 'executeScript'},
  {value: 'mouseDownAt', label: 'mouseDownAt'},
  {value: 'mouseDown', label: 'mouseDown'},
  {value: 'mouseMoveAt', label: 'mouseMoveAt'},
  {value: 'mouseOut', label: 'mouseOut'},
  {value: 'mouseOver', label: 'mouseOver'},
  {value: 'mouseUpAt', label: 'mouseUpAt'},
  {value: 'mouseUp', label: 'mouseUp'},
  {value: 'open', label: 'open'},
  {value: 'pause', label: 'pause'},
  {value: 'wait', label: 'wait'},
  {value: 'sleep', label: 'sleep'},
  {value: 'removeSelection', label: 'removeSelection'},
  {value: 'runCase', label: 'runCase'},
  {value: 'run', label: 'run'},
  {value: 'runScript', label: 'runScript'},
  {value: 'screenshot', label: 'screenshot'},
  {value: 'select', label: 'select'},
  {value: 'selectFrame', label: 'selectFrame'},
  {value: 'selectWindow', label: 'selectWindow'},
  {value: 'sendKeys', label: 'sendKeys'},
  {value: 'setSpeed', label: 'setSpeed'},
  {value: 'setWindowSize', label: 'setWindowSize'},
  {value: 'storeAttribute', label: 'storeAttribute'},
  {value: 'store', label: 'store'},
  {value: 'storeJson', label: 'storeJson'},
  {value: 'storeText', label: 'storeText'},
  {value: 'storeTitle', label: 'storeTitle'},
  {value: 'storeValue', label: 'storeValue'},
  {value: 'storeWindowHandle', label: 'storeWindowHandle'},
  {value: 'storeXpathCount', label: 'storeXpathCount'},
  {value: 'submit', label: 'submit'},
  {value: 'type', label: 'type'},
  {value: 'uncheck', label: 'uncheck'},
  {value: 'verifyChecked', label: 'verifyChecked'},
  {value: 'verify', label: 'verify'},
  {value: 'verifyEditable', label: 'verifyEditable'},
  {value: 'verifyElementNotPresent', label: 'verifyElementNotPresent'},
  {value: 'verifyElementPresent', label: 'verifyElementPresent'},
  {value: 'verifyNotChecked', label: 'verifyNotChecked'},
  {value: 'verifyNotEditable', label: 'verifyNotEditable'},
  {value: 'verifyNotSelectedLabel', label: 'verifyNotSelectedLabel'},
  {value: 'verifyNotSelectedValue', label: 'verifyNotSelectedValue'},
  {value: 'verifyNotText', label: 'verifyNotText'},
  {value: 'verifySelectedLabel', label: 'verifySelectedLabel'},
  {value: 'verifySelectedValue', label: 'verifySelectedValue'},
  {value: 'verifyText', label: 'verifyText'},
  {value: 'verifyTitle', label: 'verifyTitle'},
  {value: 'verifyValue', label: 'verifyValue'},
  {value: 'waitForElementEditable', label: 'waitForElementEditable'},
  {value: 'waitForElementNotEditable', label: 'waitForElementNotEditable'},
  {value: 'waitForElementNotPresent', label: 'waitForElementNotPresent'},
  {value: 'waitForElementNotVisible', label: 'waitForElementNotVisible'},
  {value: 'waitForElementPresent', label: 'waitForElementPresent'},
  {value: 'waitForElementVisible', label: 'waitForElementVisible'},
  {value: 'waitForText', label: 'waitForText'},
  {value: 'webdriverAnswerOnVisiblePrompt', label: 'webdriverAnswerOnVisiblePrompt'},
  {value: 'webdriverChooseCancelOnVisibleConfirmation', label: 'webdriverChooseCancelOnVisibleConfirmation'},
  {value: 'webdriverChooseCancelOnVisiblePrompt', label: 'webdriverChooseCancelOnVisiblePrompt'},
  {value: 'webdriverChooseOkOnVisibleConfirmation', label: 'webdriverChooseOkOnVisibleConfirmation'},
  {value: 'callApi', label: 'callApi'},
  {value: 'exportDb', label: 'exportDb'},
  {value: 'generateCode', label: 'generateCode'},
  {value: 'gitHistory', label: 'gitHistory'},
  {value: 'increaseNumber', label: 'increaseNumber'},
  {value: 'jenkinsJob', label: 'jenkinsJob'},
  {value: 'ocr', label: 'ocr'},
  {value: 'readProperties', label: 'readProperties'},
  {value: 'saveProperties', label: 'saveProperties'},
  {value: 'scroll', label: 'scroll'},
  {value: 'scrollIntoView', label: 'scrollIntoView'},
  {value: 'setProperty', label: 'setProperty'},
  {value: 'sftp', label: 'sftp'},
  {value: 'stepCount', label: 'stepCount'}
])

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
    props.lf.setProperties(props.nodeData.id, {
      command: target
    })
    props.lf.updateText(props.nodeData.id,target)
  }
}

</script>

<style scoped>
.setting-panel {
  padding: 10px;
  background: #FFFFFF;
  border-left: 1px solid #dadce0;
  border-bottom: 1px solid #dadce0;
  width: 400px;
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
.setting-input {
  width: 100%;
  height: 30px;
  border: 1px solid #dadce0;
  margin-top: 5px;
}
</style>