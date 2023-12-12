<template>
    <div class="Searching" style="height: 100%; width: 100%; ">
        <div style="width: 40%; min-width: 500px;margin: 50% auto; margin-top:25vh">
            <img src="../views/logo_full.png" style="width: 70%; ">
            <SearchZone style="margin-top: 2.5vh;" />

            <AIQA style="height: 30vh;"/>

            <div style="margin-top: 20px; padding-bottom: 40px; background-color: rgba(255,255,255,0.3); border-radius: 30px;">
                <div
                    style="font-weight: bold; color: #fff;  font-size: 25px; padding: 10px; margin-left: 20px;">
                    热门搜索</div>
                <div style="">
                    <div style="display: inline-block; ">
                    <div v-for="item in hotKeyWords" key="id" style="display: flex;">
                            <div v-show="item.id == 1" style="color: red; margin-top: 5px; 
                                     font-size: large; margin-top: 15px;">
                                <span style="font-weight: bold;">{{ item.id }}</span>
                                <router-link :to="'result/' + item.title" style="text-decoration: none; margin-left: 10px; color: white; ">
                                    {{ item.title }}
                                </router-link>
                            </div>
                            <div v-show="item.id == 2" style="color: orangered; margin-top: 5px; 
                                     font-size: large; margin-top: 15px;">
                                <span style="font-weight: bold;">{{ item.id }}</span>
                                <router-link :to="'result/' + item.title" style="text-decoration: none; margin-left: 10px; color: white; ">
                                    {{ item.title }}
                                </router-link>
                            </div>
                            <div v-show="item.id == 3" style="color: orange; margin-top: 5px; 
                                     font-size: large; margin-top: 15px;">
                                <span style="font-weight: bold;">{{ item.id }}</span>
                                <router-link :to="'result/' + item.title" style="text-decoration: none; margin-left: 10px; color: white; ">
                                    {{ item.title }}
                                </router-link>
                            </div>
                            <div v-show="item.id == 4" style="color: white; margin-top: 5px; 
                                     font-size: large; margin-top: 15px;">
                                <span style="font-weight: bold;">{{ item.id }}</span>
                                <router-link :to="'result/' + item.title" style="text-decoration: none; margin-left: 10px; color: white; ">
                                    {{ item.title }}
                                </router-link>
                            </div>
                    </div>
                </div>
                <div style="display: inline-block; margin-left: 8vw;">
                    <div v-for="item in hotKeyWords" key="id" style="display: flex;">
                            <div v-show="item.id > 4" style="color: white; margin-top: 5px; 
                                     font-size: large; margin-top: 15px;">
                                <span style="font-weight: bold;">{{ item.id }}</span>
                                <router-link :to="'result/' + item.title" style="text-decoration: none; margin-left: 10px; color: white; ">
                                    {{ item.title }}
                                </router-link>
                            </div>
                    </div>
                </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import SearchZone from './SearchZone.vue';
import AIQA from './AIQA.vue';
import axios from 'axios';
export default {
    data: function () {
        return {
            hotKeyWords: [
                { id: 1, title: '求是月饼' },
                { id: 2, title: '启真湖水' },
                { id: 3, title: '浙大烤鸡腿' },
                { id: 4, title: '老妈妈卷饼' },
                { id: 5, title: '红烧大排' },
                { id: 6, title: '鱼香鸡蛋' },
                { id: 7, title: '杭椒麻婆豆腐' },
                { id: 8, title: '西湖醋鱼' },
            ]
        }
    },
    components: { SearchZone,AIQA },
    methods: {
        GetHotKeywords() {
            
        }
    }
}

</script>

<style>
.Searching {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
    width: 100%;
    min-height: 100%;
    background: url("../views/Search_background.jpg") fixed no-repeat;
    background-size: cover;
}
</style>