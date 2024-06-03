import { h } from '@logicflow/core'
import BaseNode from "./BaseNode"

class ClickNode extends BaseNode.view {
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
      href: 'images/function.svg'
    })
  }
}

class ClickNodeModel extends BaseNode.model {
  initNodeData (data) {
    super.initNodeData(data)
    this.defaultFill = 'rgb(231, 231, 174)'
    this.setProperties({
      command: 'click'
    })
  }
}

export default {
  type: 'click-node',
  model: ClickNodeModel,
  view: ClickNode
}
