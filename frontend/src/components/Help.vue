<template>
    <div style="width: 100%;">
        <div style="width: 70%; margin: 0 auto;">
            <el-container>
                <el-header style="height: 60px;">
                    <div style="height: 60px;">
                        <img src="../views/logo_full.png" alt="profile"
                            style="height: 50px; margin-top: 15px; margin-left: 10px; float: left;" />
                        <el-button style="float: right;height: 50px; width: 50px; margin-top: 15px;" icon="el-icon-s-home"
                            circle @click="$router.push('/')"></el-button>
                    </div>
                    <el-divider />
                </el-header>
                <el-container style="margin-top: 60px;">
                    <el-tabs :tab-position="tabPosition" style="height: 100%; width: 100%; " :stretch="true">
                        <el-tab-pane class="pane" label="引言" style=";">
                            <div class="title">欢迎</div>
                            <div class="content">
                                欢迎使用“食香传世”美食类垂直搜索引擎。<br />
                                本搜索引擎是针对美食这一特定领域的搜索引擎，专门为美食相关需求和信息而设计，专注于在这一领域提供更加精准和专业的搜索结果。
                                通过对美食这一特定领域的网页进行深入的索引和分析，建立起专有的搜索算法和数据库，以满足用户追求更高质量美食信息的信息需求。
                                不同于综合搜索引擎，美食类搜索引擎更具专注性，更加面向生活。
                                我们旨在通过收集美食领域不同来源的数据，通过多样化的搜索方式，对各种数据进行分类、聚合分析和整理，
                                力求为用户提供完整直观的数据，改善用户搜索体验，使得对菜谱、食材信息的获取更加便捷。<br />
                                同时，人工智能方兴未艾，知识图谱技术为搜索提供了无限的可能。<br />
                                通过知识图谱这一种结构化的知识表示方法，可以将各种美食相关的信息进行有机地连接和组织，形成一个完整的知识网络。<br />
                                我们还内嵌了AI问答插件，您可以通过与AI对话来解决问题。<br />
                                
                            </div>
                            <div class="title">关键词搜索</div>
                            <div class="content">这里是关键词搜索。</div>
                        </el-tab-pane>
                        <el-tab-pane class="pane" label="关键词搜索" style="">
                            <div class="title">搜索引擎主页</div>
                            <div class="content">

                                <img src="../views/main.png" width="100%" />
                            </div>
                            <div class="title">关键词搜索</div>
                            <div class="content">
                                您可以在搜索框中输入想要搜索的关键词。<br />
                                当您输入内容后，搜索框会自动检索相关数据，并生成智能补全建议。<br />
                                您可以在完成输入或者选择所需的智能补全项后按回车搜索。
                                <br />
                            </div>
                            <div class="title">拼音搜索和拼音智能补全</div>
                            <div class="content">
                                允许使用拼音搜索。<br/>
                                搜索框同样会生成智能补全建议。<br/>
                            </div> 
                            <div class="title">热门搜索</div>
                            <div class="content">
                                在搜索引擎首页，我们根据用户搜索频率提供了一些较为热门的搜索词条。<br/>
                                您可以直接点击感兴趣的词条跳转到相应的结果展示界面。<br/>
                            </div>
                            <div class="title">高级搜索</div>
                            <div class="content">
                                我们支持一些搜索逻辑，允许通过它们的组合进行高级搜索。<br/>
                                <b>or</b>：对不同关键词进行搜索，并对结果取并集<br/>
                                <b>-</b>：对左侧关键词进行搜索，并去除右侧关键词的搜索结果<br/>
                                <b>*</b>：通配符，此处允许任意个字符<br/>
                                <b>" "</b>：要求引号内部的关键词在结果中必须完整出现<br/>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane class="pane" label="搜索结果处理">
                            <div class="title">搜索结果展示</div>
                            <div class="content">
                                在输入框输入搜索关键词并发起搜索请求后，将会跳转到搜索结果展示页面。<br/>
                                <img src="../views/result.png" width="100%" />
                            </div> 
                        </el-tab-pane>
                        <el-tab-pane class="pane" label="结果详情展示">
                            <div class="title">结果详情展示页面</div>
                            <div class="content">
                                点击搜索结果展示界面的搜索项，即可跳转到对应的搜索结果详情展示页面。<br/>
                                
                                <img src="../views/detail.png" width="100%" />
                            </div>
                        </el-tab-pane>
                        <el-tab-pane class="pane" label="AI增强">
                            <div class="title">AI问答</div>
                            <div class="content">
                                我们内置了大语言模型调用窗口，您可以由此向AI咨询相关问题。<br/>
                                <br/>
                            </div> 
                            <div class="title">知识图谱</div>
                            <div class="content">
                                在输入框输入搜索关键词并发起搜索请求后，将会跳转到搜索结果展示页面。<br/>
                                搜索框同样会生成智能补全建议。<br/>
                            </div> 
                        </el-tab-pane>
                    </el-tabs>
                </el-container>
            </el-container>
        </div>

    </div>
</template>

<script>
import RelationGraph from './RelationGraph.vue';
export default {
    data() {
        return {
            tabPosition: 'left'
        };
    },
    components: { RelationGraph }
}
</script>

<style>
body,
html {
    height: 100%;
    width: 100%;
    margin: 0 0 0 0;
    overflow: hidden hidden;
}

.pane {
    margin-left: 30px;
    /* margin-right: 30px;  */
    /* border-left: 30px; */
    /* width: 100%; */
    text-align: justify;
    overflow-y: scroll;
    height: calc(95vh - 80px)
}

.title {
    font-size: 30px;
    font-weight: bold;

}

.content {
    margin-top: 15px;
    margin-bottom: 35px;
    line-height: 25px;
}

.pane::-webkit-scrollbar {
    display: none;
}
</style>