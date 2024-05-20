import {HtmlNode, HtmlNodeModel} from "@logicflow/core";
import {createApp, h} from 'vue';
import VueNode from './VueNode.vue';

class VueHtmlNode extends HtmlNode {
    constructor(props) {
        super(props)
        this.isMounted = false
        this.r = h(VueNode, {
            properties: props.model.getProperties(),
        })
        this.app = createApp({
            render: () => this.r
        })
    }

    setHtml(rootEl) {
        if (!this.isMounted) {
            this.isMounted = true
            const node = document.createElement('div')
            rootEl.appendChild(node)
            this.app.mount(node)
        } else {
            this.r.component.props.properties = this.props.model.getProperties()
        }
    }

    getText() {
        return null
    }
}

class VueHtmlNodeModel extends HtmlNodeModel {
    initNodeData(data) {
        super.initNodeData(data);
        this.setProperties({
            "style": {
                "backgroundColor": "rgb(255, 255, 255)",
                "borderColor": "rgb(42, 42, 42)",
                "borderWidth": "1px"
            }
        })
    }

    setAttributes() {
        this.width = 250;
        this.height = 100;
        this.text.editable = false;
        this.properties = this.getProperties()
    }

    getOutlineStyle() {
        const style = super.getOutlineStyle();
        style.stroke = 'none';
        style.hover.stroke = 'none';
        return style;
    }

    /**
     * 重写定义锚点
     */
    getDefaultAnchor() {
        const {x, y, id, width} = this;
        return [
            {
                x: x + width / 2,
                y: y,
                id: `${id}_right`,
                type: "right"
            },
            {
                x: x - width / 2,
                y: y,
                id: `${id}_left`,
                type: "left"
            }
        ];
    }
}

export default {
    type: 'vue-html',
    model: VueHtmlNodeModel,
    view: VueHtmlNode
}