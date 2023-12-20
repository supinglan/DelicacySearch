<template>
    <div style="width: 100%;">
        <div style="width: 70%; margin: 0 auto;">
            <el-container>
                <el-header style="height: 60px;">
                    <div style="height: 60px;">
                        <img src="../views/logo_full_black.png" alt="profile"
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
                                <img src="../views/main.png" width="100%" />
                            </div>
                            <div class="title">关键词搜索</div>
                            <div class="content">这里是关键词搜索。</div>
                        </el-tab-pane>
                        <el-tab-pane class="pane" label="关键词搜索" style="">
                            <div class="title">关键词搜索</div>
                            <div class="content">
                                您可以在搜索框中输入想要搜索的关键词。<br />
                                当您输入内容后，搜索框会自动检索相关数据，并生成智能补全建议。<br />
                                <br/>
                                
                            </div>
                            <div class="title">关键词搜索</div>
                            <div class="content">这里是关键词搜索。</div>
                            <RelationGraph root="鱼香肉丝" style="height: 90%;"/>
                        </el-tab-pane>
                        <el-tab-pane label="搜索结果处理">配置管理</el-tab-pane>
                        <el-tab-pane label="AI问答">角色管理</el-tab-pane>
                        <el-tab-pane label="知识图谱">定时任务补偿</el-tab-pane>
                    </el-tabs>
                    <!-- <el-aside width="200px">
                        
                    </el-aside>
                    <el-main>Main</el-main> -->
                </el-container>
            </el-container>

            <!-- <h1>This is Help Page.</h1>
            
        <el-radio-group v-model="tabPosition" style="margin-bottom: 30px;">
        <el-radio-button label="top">top</el-radio-button>
        <el-radio-button label="right">right</el-radio-button>
        <el-radio-button label="bottom">bottom</el-radio-button>
        <el-radio-button label="left">left</el-radio-button>
      </el-radio-group>
    
      <el-tabs :tab-position="tabPosition" style="height: 200px;">
        <el-tab-pane label="用户管理">用户管理</el-tab-pane>
        <el-tab-pane label="配置管理">配置管理</el-tab-pane>
        <el-tab-pane label="角色管理">角色管理</el-tab-pane>
        <el-tab-pane label="定时任务补偿">定时任务补偿</el-tab-pane>
      </el-tabs> -->
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