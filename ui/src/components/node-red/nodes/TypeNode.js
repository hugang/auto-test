import { h } from '@logicflow/core'
import BaseNode from "./BaseNode"

class TypeNode extends BaseNode.view {
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

class TypeNodeModel extends BaseNode.model {
  initNodeData (data) {
    super.initNodeData(data)
    this.defaultFill = 'rgb(231, 231, 174)'
  }
  getData () {
    const data = super.getData()
    data.properties.command = 'type'
    return data
  }
}

export default {
  type: 'type-node',
  model: TypeNodeModel,
  view: TypeNode
}
