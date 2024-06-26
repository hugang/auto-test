import {createApp} from 'vue';
import StartNode from "./nodes/StartNode";
import FlowLink from "./FlowLink";
import Palette from './tools/Palette.vue';
import VueHtmlNode from "./nodes/VueHtmlNode";
import AutoTestNode from "./nodes/AutoTestNode";
import OpenNode from "./nodes/OpenNode";
import TypeNode from "./nodes/TypeNode";
import ClickNode from "./nodes/ClickNode";

class NodeRedExtension {
  static pluginName = 'NodeRedExtension'
  constructor ({ lf }) {
    lf.register(StartNode);
    lf.register(FlowLink);
    lf.register(AutoTestNode)
    lf.register(OpenNode)
    lf.register(TypeNode)
    lf.register(ClickNode)
    lf.register(VueHtmlNode);
    lf.setDefaultEdgeType('flow-link');
    this.app = createApp(Palette, {
      lf
    })
  }
  render(lf, domOverlay) {
    const node = document.createElement('div')
    node.className = 'node-red-palette'
    domOverlay.appendChild(node)
    this.app.mount(node)
  }
}

export default NodeRedExtension