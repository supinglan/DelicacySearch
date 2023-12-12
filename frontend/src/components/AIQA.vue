<template>
    <div class="box" style="width: 100%; height: 100%;background-color: white;">
        <div style="font-weight: bold; font-size: 30px; padding-top: 15px;">AI问答</div>
        <div class="chat-content" id="chat" ref="mainScroll" style=" min-height: 90%;max-height: 90%; width: 100%;overflow-y: scroll;">
            <!-- recordContent 聊天记录数组-->
            <div v-for="item in messages.slice(1)">
                <!-- 对方 -->
                <div class="word" v-if="item.role == 'assistant'">
                    <img src="../views/logo.png">
                    <div class="info">
                        <p class="time">食香传世AI</p>
                        <div class="info-content">{{ item.content }}</div>
                    </div>
                </div>
                <!-- 我的 -->
                <div class="word-my" v-else>
                    <div class="info">
                        <p class="time">我</p>
                        <div class="info-content">{{ item.content }}</div>
                    </div>
                    <img src="../views/user.png">
                </div>
            <div id="chat_end" style="height: 1px; width: 1px;"></div>
            </div>
        </div>
        <!-- <div v-for="item in messages">
            {{ item.role }}
            {{ item.content }}
        </div> -->
        <div style="padding-bottom: 15px; height: 10%;">
            <el-input v-model="content" placeholder="请输入您的问题" style="width: 80%;"></el-input>
            <el-button @click="Ask" :disabled="!this.ready" style="margin-left: 10px;" type="primary">发送</el-button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            messages: [],
            content: "",
            ready: true

        }
    },
    methods: {
        get_access_token() {
            return "24.b047bae8d59111dca71775ab800e68b3.2592000.1704892280.282335-44778753"
        },
        async Ask() {
            const url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/qianfan_chinese_llama_2_13b?access_token=" + this.get_access_token();
            const config = { headers: { 'Content-Type': 'application/json' } };

            this.messages.push({ "role": "user", "content": this.content })
            // document.getElementById('chat').scrollTop = document.getElementById('chat').scrollHeight
            document.getElementById('chat').scrollIntoView(0,document.getElementById('chat').scrollHeight)
            // this.$refs.mainScroll.scrollTo({top:this.$refs.scrollHeight, behavior:'smooth'})
            this.content = ""
            let payload = {
                "messages": this.messages
            }
            // const params = new URLSearchParams();
            // params.append('messages', this.messages);
            // console.log(params)
            this.ready = false
            await axios.post(url, payload, config)
                .then(response => {
                    console.log(response)
                    this.messages.push({ "role": "assistant", "content": response.data.result })
                    document.getElementById('chat').scrollIntoView(0,document.getElementById('chat').scrollHeight)
                    // this.$refs.mainScroll.scrollTo({top:this.$refs.scrollHeight, behavior:'smooth'})
                    // document.querySelector('#chat_end').scrollIntoView({behavior:'smooth'})
                    // document.getElementById('chat').scrollTop = document.getElementById('chat').scrollHeight
                })
                .catch(error => {
                    console.log(error)
                })
            this.ready = true
        }
    },
    mounted() {
         const url = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/qianfan_chinese_llama_2_13b?access_token=" + this.get_access_token();
            const config = { headers: { 'Content-Type': 'application/json' } };
        this.messages.push({"role": "user",
            "content": "我希望你担任一名专业的美食烹饪和营养专家，回答我关于美食的疑惑。你可以做的有提供菜谱、食材以及分析它们的营养等。接下来，请回复我“您好，欢迎来到食香传世美食类垂直搜索引擎，请问有什么可以帮到您？”",
        })
        let payload = {
                "messages": this.messages
            }
            console.log(payload)
            // const params = new URLSearchParams();
            // params.append('messages', this.messages);
            // console.log(params)
            axios.post(url, payload, config)
                .then(response => {
                    console.log(response)
                    this.messages.push({ "role": "assistant", "content": response.data.result })
                })
                .catch(error => {
                    console.log(error)
                })
    }
}
</script>

<style>
.box{
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
    text-align: center;
}
.chat-content {
    width: 100%;
    padding: 20px;
   

    .word {
        display: flex;
        margin-bottom: 20px;

        img {
            width: 40px;
            height: 40px;
            border-radius: 50%;
        }

        .info {
            margin-left: 10px;
            max-width: 70%;
            text-align: left;
            .time {
                font-size: 12px;
                color: rgba(51, 51, 51, 0.8);
                margin: 0;
                height: 20px;
                line-height: 20px;
                margin-top: -5px;
            }

            .info-content {
                padding: 10px;
                font-size: 14px;
                background: #fff;
                position: relative;
                margin-top: 8px;
                text-align: left;
                background: #9EA0A1;
            }

            .info-content::before {
                position: absolute;
                left: -8px;
                top: 8px;
                content: '';
                border-right: 10px solid #9EA0A1;
                border-top: 8px solid transparent;
                border-bottom: 8px solid transparent;
                
            }
        }
    }

    .word-my {
        display: flex;
        justify-content: flex-end;
        margin-bottom: 20px;

        img {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            padding-right: 30px;
        }

        .info {
            width: 90%;
            margin-left: 10px;
            text-align: right;

            .time {
                font-size: 12px;
                color: rgba(51, 51, 51, 0.8);
                margin: 0;
                height: 20px;
                line-height: 20px;
                margin-top: -5px;
                margin-right: 10px;
            }

            .info-content {
                max-width: 70%;
                padding: 10px;
                font-size: 14px;
                float: right;
                margin-right: 10px;
                position: relative;
                margin-top: 8px;
                background: #A3C3F6;
                text-align: left;
            }

            .info-content::after {
                position: absolute;
                right: -8px;
                top: 8px;
                content: '';
                border-left: 10px solid #A3C3F6;
                border-top: 8px solid transparent;
                border-bottom: 8px solid transparent;
            }
        }
    }
}
.chat-content::-webkit-scrollbar {
    display: none;
  }
</style>