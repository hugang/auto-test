import { h } from '@logicflow/core'
import BaseNode from "./BaseNode"

class AutoTestNode extends BaseNode.view {
  getIcon () {
    const {
      width,
      height,
    } = this.props.model;
    return h('image', {
      width: 30,
      height: 30,
      x: - width / 2,
      y: - height / 2,
      href: 'images/fetch.svg'
    })
  }
}

class AutoTestNodeModel extends BaseNode.model {
  initNodeData (data) {
    super.initNodeData(data)
    this.defaultFill = 'rgb(231, 231, 174)'
  }
}

export default {
  type: 'auto-test-node',
  model: AutoTestNodeModel,
  view: AutoTestNode
}
