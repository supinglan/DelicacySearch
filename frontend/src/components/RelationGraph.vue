<template>
    <div class="box" style="height: 90%; width: 100%; text-align: center;">
        <div style="padding-top: 20px; padding-bottom: 20px; font-weight: bold; font-size: large;">
            <span style="color: blue; ">
                {{ this.root }}
            </span>
            的关系图谱
        </div>

        <RelationGraph ref="graphRef" :options="graphOptions" :on-node-click="onNodeClick" :on-line-click="onLineClick">

        </RelationGraph>
        <div style="float: right; padding-right: 20px;">
            对
            <span style="color: blue;">
                {{ this.currentNodeText }}</span>
            进行搜索
            <el-button icon="el-icon-search" circle type="primary" size="small"
                @click="$router.replace('/result/' + currentNodeText)" />
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import RelationGraph from 'relation-graph';
export default {
    components: { RelationGraph },
    props: {
        root: ''
    },
    watch:{
        root()
        {        
        this.showSeeksGraph();
        this.currentNodeText = this.root}
    },
    data() {
        return {
            currentNodeText: '',
            prevNode: '',
            prevNodeColor: '',
            isShowCodePanel: false,
            isShowNodeMenuPanel: false,
            graphOptions: {
                "disableLineClickEffect": true,
                "backgroundImageNoRepeat": true,
                "downloadImageFileName": "my-graph",
                "moveToCenterWhenRefresh": false,
                "useAnimationWhenRefresh": true,
                "allowAutoLayoutIfSupport": false,
                "backgrounImageNoRepeat": true,
                //   "isMoveByParentNode": true,
                "defaultNodeBorderWidth": 5,
                "defaultNodeColor": "#67C23A",
                "defaultNodeBorderColor": "#90EE90",
                "defaultLineWidth": 3,
                "defaultLineShape": 5,
                "hideNodeContentByZoom": true,
                "placeOtherGroup": true,
                "defaultLineMarker": {
                    "markerWidth": 0,
                    "markerHeight": 0,
                    "refX": 0,
                    "refY": 0,
                    "data": ""
                },
                "layouts": [
                    {
                        "label": "中心",
                        "layoutName": "force",
                        "maxLayoutTimes": 900,
                        "force_node_repulsion": 1,
                        "force_line_elastic": 1
                    }
                ]

                // 这里可以参考"Graph 图谱"中的参数进行设置
            }
        };
    },
    async mounted() {
        this.currentNodeText = this.root
        this.showSeeksGraph();
    },
    beforeDestroy() {
        console.log('beforeDestroy stop layout');
        this.$refs.graphRef.getInstance().stopAutoLayout();
    },
    methods: {
        async showSeeksGraph() {
            if(this.root=='') return
            const params = new URLSearchParams();
            params.append('rootText', this.root);
            await axios.post('http://120.55.14.3:8088/getKG', params)
                .then(
                    response => {
                        let graphJsonData = response.data
                        this.$refs.graphRef.setJsonData(graphJsonData, (graphInstance) => {
                            graphInstance.setZoom(30);
                        });
                    }
                )
                .catch()
            // const graphJsonData = {
            //     'rootId': 'a',
            //     'nodes': [{ 'id': 'a', 'text': 'a', 'color':'#38419D' }, { 'id': 'b', 'text': 'b', 'color':'#3887BE' }, { 'id': 'b1', 'text': 'b1', 'color':'#52D3D8' }, { 'id': 'b1-1', 'text': 'b1-1' }, { 'id': 'b1-2', 'text': 'b1-2' }, { 'id': 'b1-3', 'text': 'b1-3' }, { 'id': 'b1-4', 'text': 'b1-4' }, { 'id': 'b1-5', 'text': 'b1-5' }, { 'id': 'b1-6', 'text': 'b1-6' }, { 'id': 'b2', 'text': 'b2' }, { 'id': 'b2-1', 'text': 'b2-1' }, { 'id': 'b2-2', 'text': 'b2-2' }, { 'id': 'b2-3', 'text': 'b2-3' }, { 'id': 'b2-4', 'text': 'b2-4' }, { 'id': 'b3', 'text': 'b3' }, { 'id': 'b3-1', 'text': 'b3-1' }, { 'id': 'b3-2', 'text': 'b3-2' }, { 'id': 'b3-3', 'text': 'b3-3' }, { 'id': 'b3-4', 'text': 'b3-4' }, { 'id': 'b3-5', 'text': 'b3-5' }, { 'id': 'b3-6', 'text': 'b3-6' }, { 'id': 'b3-7', 'text': 'b3-7' }, { 'id': 'b4', 'text': 'b4' }, { 'id': 'b4-1', 'text': 'b4-1' }, { 'id': 'b4-2', 'text': 'b4-2' }, { 'id': 'b4-3', 'text': 'b4-3' }, { 'id': 'b4-4', 'text': 'b4-4' }, { 'id': 'b4-5', 'text': 'b4-5' }, { 'id': 'b4-6', 'text': 'b4-6' }, { 'id': 'b4-7', 'text': 'b4-7' }, { 'id': 'b4-8', 'text': 'b4-8' }, { 'id': 'b4-9', 'text': 'b4-9' }, { 'id': 'b5', 'text': 'b5' }, { 'id': 'b5-1', 'text': 'b5-1' }, { 'id': 'b5-2', 'text': 'b5-2' }, { 'id': 'b5-3', 'text': 'b5-3' }, { 'id': 'b5-4', 'text': 'b5-4' }, { 'id': 'b6', 'text': 'b6' }, { 'id': 'b6-1', 'text': 'b6-1' }, { 'id': 'b6-2', 'text': 'b6-2' }, { 'id': 'b6-3', 'text': 'b6-3' }, { 'id': 'b6-4', 'text': 'b6-4' }, { 'id': 'b6-5', 'text': 'b6-5' }, { 'id': 'c', 'text': 'c' }, { 'id': 'c1', 'text': 'c1' }, { 'id': 'c1-1', 'text': 'c1-1' }, { 'id': 'c1-2', 'text': 'c1-2' }, { 'id': 'c1-3', 'text': 'c1-3' }, { 'id': 'c1-4', 'text': 'c1-4' }, { 'id': 'c1-5', 'text': 'c1-5' }, { 'id': 'c1-6', 'text': 'c1-6' }, { 'id': 'c1-7', 'text': 'c1-7' }, { 'id': 'c2', 'text': 'c2' }, { 'id': 'c2-1', 'text': 'c2-1' }, { 'id': 'c2-2', 'text': 'c2-2' }, { 'id': 'c3', 'text': 'c3' }, { 'id': 'c3-1', 'text': 'c3-1' }, { 'id': 'c3-2', 'text': 'c3-2' }, { 'id': 'c3-3', 'text': 'c3-3' }, { 'id': 'd', 'text': 'd' }, { 'id': 'd1', 'text': 'd1' }, { 'id': 'd1-1', 'text': 'd1-1' }, { 'id': 'd1-2', 'text': 'd1-2' }, { 'id': 'd1-3', 'text': 'd1-3' }, { 'id': 'd1-4', 'text': 'd1-4' }, { 'id': 'd1-5', 'text': 'd1-5' }, { 'id': 'd1-6', 'text': 'd1-6' }, { 'id': 'd1-7', 'text': 'd1-7' }, { 'id': 'd1-8', 'text': 'd1-8' }, { 'id': 'd2', 'text': 'd2' }, { 'id': 'd2-1', 'text': 'd2-1' }, { 'id': 'd2-2', 'text': 'd2-2' }, { 'id': 'd3', 'text': 'd3' }, { 'id': 'd3-1', 'text': 'd3-1' }, { 'id': 'd3-2', 'text': 'd3-2' }, { 'id': 'd3-3', 'text': 'd3-3' }, { 'id': 'd3-4', 'text': 'd3-4' }, { 'id': 'd3-5', 'text': 'd3-5' }, { 'id': 'd4', 'text': 'd4' }, { 'id': 'd4-1', 'text': 'd4-1' }, { 'id': 'd4-2', 'text': 'd4-2' }, { 'id': 'd4-3', 'text': 'd4-3' }, { 'id': 'd4-4', 'text': 'd4-4' }, { 'id': 'd4-5', 'text': 'd4-5' }, { 'id': 'd4-6', 'text': 'd4-6' }, { 'id': 'e', 'text': 'e' }, { 'id': 'e1', 'text': 'e1' }, { 'id': 'e1-1', 'text': 'e1-1' }, { 'id': 'e1-2', 'text': 'e1-2' }, { 'id': 'e1-3', 'text': 'e1-3' }, { 'id': 'e1-4', 'text': 'e1-4' }, { 'id': 'e1-5', 'text': 'e1-5' }, { 'id': 'e1-6', 'text': 'e1-6' }, { 'id': 'e2', 'text': 'e2' }, { 'id': 'e2-1', 'text': 'e2-1' }, { 'id': 'e2-2', 'text': 'e2-2' }, { 'id': 'e2-3', 'text': 'e2-3' }, { 'id': 'e2-4', 'text': 'e2-4' }, { 'id': 'e2-5', 'text': 'e2-5' }, { 'id': 'e2-6', 'text': 'e2-6' }, { 'id': 'e2-7', 'text': 'e2-7' }, { 'id': 'e2-8', 'text': 'e2-8' }, { 'id': 'e2-9', 'text': 'e2-9' }],
            //     'lines': [{ 'from': 'a', 'to': 'b', text: '食材' }, { 'from': 'b', 'to': 'b1' }, { 'from': 'b1', 'to': 'b1-1' }, { 'from': 'b1', 'to': 'b1-2' }, { 'from': 'b1', 'to': 'b1-3' }, { 'from': 'b1', 'to': 'b1-4' }, { 'from': 'b1', 'to': 'b1-5' }, { 'from': 'b1', 'to': 'b1-6' }, { 'from': 'b', 'to': 'b2' }, { 'from': 'b2', 'to': 'b2-1' }, { 'from': 'b2', 'to': 'b2-2' }, { 'from': 'b2', 'to': 'b2-3' }, { 'from': 'b2', 'to': 'b2-4' }, { 'from': 'b', 'to': 'b3' }, { 'from': 'b3', 'to': 'b3-1' }, { 'from': 'b3', 'to': 'b3-2' }, { 'from': 'b3', 'to': 'b3-3' }, { 'from': 'b3', 'to': 'b3-4' }, { 'from': 'b3', 'to': 'b3-5' }, { 'from': 'b3', 'to': 'b3-6' }, { 'from': 'b3', 'to': 'b3-7' }, { 'from': 'b', 'to': 'b4' }, { 'from': 'b4', 'to': 'b4-1' }, { 'from': 'b4', 'to': 'b4-2' }, { 'from': 'b4', 'to': 'b4-3' }, { 'from': 'b4', 'to': 'b4-4' }, { 'from': 'b4', 'to': 'b4-5' }, { 'from': 'b4', 'to': 'b4-6' }, { 'from': 'b4', 'to': 'b4-7' }, { 'from': 'b4', 'to': 'b4-8' }, { 'from': 'b4', 'to': 'b4-9' }, { 'from': 'b', 'to': 'b5' }, { 'from': 'b5', 'to': 'b5-1' }, { 'from': 'b5', 'to': 'b5-2' }, { 'from': 'b5', 'to': 'b5-3' }, { 'from': 'b5', 'to': 'b5-4' }, { 'from': 'b', 'to': 'b6' }, { 'from': 'b6', 'to': 'b6-1' }, { 'from': 'b6', 'to': 'b6-2' }, { 'from': 'b6', 'to': 'b6-3' }, { 'from': 'b6', 'to': 'b6-4' }, { 'from': 'b6', 'to': 'b6-5' }, { 'from': 'a', 'to': 'c' }, { 'from': 'c', 'to': 'c1' }, { 'from': 'c1', 'to': 'c1-1' }, { 'from': 'c1', 'to': 'c1-2' }, { 'from': 'c1', 'to': 'c1-3' }, { 'from': 'c1', 'to': 'c1-4' }, { 'from': 'c1', 'to': 'c1-5' }, { 'from': 'c1', 'to': 'c1-6' }, { 'from': 'c1', 'to': 'c1-7' }, { 'from': 'c', 'to': 'c2' }, { 'from': 'c2', 'to': 'c2-1' }, { 'from': 'c2', 'to': 'c2-2' }, { 'from': 'c', 'to': 'c3' }, { 'from': 'c3', 'to': 'c3-1' }, { 'from': 'c3', 'to': 'c3-2' }, { 'from': 'c3', 'to': 'c3-3' }, { 'from': 'a', 'to': 'd' }, { 'from': 'd', 'to': 'd1' }, { 'from': 'd1', 'to': 'd1-1' }, { 'from': 'd1', 'to': 'd1-2' }, { 'from': 'd1', 'to': 'd1-3' }, { 'from': 'd1', 'to': 'd1-4' }, { 'from': 'd1', 'to': 'd1-5' }, { 'from': 'd1', 'to': 'd1-6' }, { 'from': 'd1', 'to': 'd1-7' }, { 'from': 'd1', 'to': 'd1-8' }, { 'from': 'd', 'to': 'd2' }, { 'from': 'd2', 'to': 'd2-1' }, { 'from': 'd2', 'to': 'd2-2' }, { 'from': 'd', 'to': 'd3' }, { 'from': 'd3', 'to': 'd3-1' }, { 'from': 'd3', 'to': 'd3-2' }, { 'from': 'd3', 'to': 'd3-3' }, { 'from': 'd3', 'to': 'd3-4' }, { 'from': 'd3', 'to': 'd3-5' }, { 'from': 'd', 'to': 'd4' }, { 'from': 'd4', 'to': 'd4-1' }, { 'from': 'd4', 'to': 'd4-2' }, { 'from': 'd4', 'to': 'd4-3' }, { 'from': 'd4', 'to': 'd4-4' }, { 'from': 'd4', 'to': 'd4-5' }, { 'from': 'd4', 'to': 'd4-6' }, { 'from': 'a', 'to': 'e' }, { 'from': 'e', 'to': 'e1' }, { 'from': 'e1', 'to': 'e1-1' }, { 'from': 'e1', 'to': 'e1-2' }, { 'from': 'e1', 'to': 'e1-3' }, { 'from': 'e1', 'to': 'e1-4' }, { 'from': 'e1', 'to': 'e1-5' }, { 'from': 'e1', 'to': 'e1-6' }, { 'from': 'e', 'to': 'e2' }, { 'from': 'e2', 'to': 'e2-1' }, { 'from': 'e2', 'to': 'e2-2' }, { 'from': 'e2', 'to': 'e2-3' }, { 'from': 'e2', 'to': 'e2-4' }, { 'from': 'e2', 'to': 'e2-5' }, { 'from': 'e2', 'to': 'e2-6' }, { 'from': 'e2', 'to': 'e2-7' }, { 'from': 'e2', 'to': 'e2-8' }, { 'from': 'e2', 'to': 'e2-9' }]
            // };
            // this.$refs.graphRef.setJsonData(graphJsonData, (graphInstance) => {
            //                 graphInstance.setZoom(30);
            //             });

        },
        onNodeClick(nodeObject, $event) {
            if (this.prevNode != '') {
                this.prevNode.color = this.prevNodeColor;
            }
            this.prevNode = nodeObject;
            this.prevNodeColor = nodeObject.color;
            nodeObject.color = 'orange';
            this.$refs.graphRef.getNodes().forEach(node => {
                if (nodeObject.targetNodes.includes(node) || node == nodeObject || node.id == this.root)
                    node.opacity = 1
                else
                    node.opacity = 0.4
            });
            this.currentNodeText = nodeObject.text;
            console.log('onNodeClick:', nodeObject);
        },
        onLineClick(lineObject, linkObject, $event) {
            console.log('onLineClick:', lineObject);
        }
    }
}
</script>

<style >
.box {
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
</style>


