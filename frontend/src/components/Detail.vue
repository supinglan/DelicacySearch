<template>
  <div style="overflow: scroll;height: 100vh;">
  <el-container style="height: 2000px;border: 1px solid #eee ;">
    <el-container >
      <el-header style="text-align: left; font-size: 12px;display: flex;">
        <img src="../views/logo_full.png" style="width: 10%; margin-top: 10px; height:70%;">
          <SearchZone style="width:30%;margin-top:10px"></SearchZone>
      </el-header>
     
      <el-main style="height:1000px">
        <el-row style="margin-bottom:10px">
          <el-col :offset="4">
        <el-breadcrumb separator-class="el-icon-arrow-right">
    <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item >搜索结果</el-breadcrumb-item>
    <el-breadcrumb-item>{{result.title}}</el-breadcrumb-item>
  </el-breadcrumb>
  </el-col>
  </el-row>
        <el-row>
          <el-col :span="6" :offset="2">
          <div style="font-size: 32px; font-weight:1000;">{{ result.title }}</div>
          </el-col>
        </el-row>
        <el-row>
    <el-col :span="8" :offset="4">
      <img :src='result.picUrl' style="width: 100%; margin-top: 10px; height:100%;">
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="16" :offset="4" style="text-align: left;margin-top: 15px;margin-bottom:15px;">
      <span class="tag-group__title" style="margin-right: 10px;font-weight: bold;">相关分类:</span>
      <span class="tag-group">
    <el-tag
      v-for="tag in result.tag"
      type="primary"
      style="margin-right: 10px;"
      >
      <div>{{tag}}</div>
    </el-tag>
  </span>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="16" :offset="4" style="text-align: left;">
    <span style="font-weight:bold;margin-right:5px">点击量:</span>
    <span>{{ result.clicks }}</span>
    <span style="font-weight:bold;margin-right:5px;margin-left:25px">食谱链接:</span>
    <el-link @click="jump(result.url)" style="margin-bottom:2px" type="primary">{{ result.title }}</el-link>
  </el-col>
  </el-row>
  <el-row>
    <el-col :span="16" :offset="4">
      <el-divider></el-divider>
    </el-col>
    
  <el-col :span="16" :offset="4">
      <div style="text-align: left;">{{ result.abstract }}</div>
    </el-col>
    <el-col :span="16" :offset="4">
      <el-divider></el-divider>
    </el-col>
  </el-row>
  
  <!-- <el-row>
    <el-col :span="8" :offset="4">
      <div style="margin-top:10px;text-align:left;font-size: 28px; font-weight:1000;">食材</div>
    </el-col>
  </el-row> -->
  
  <el-row :style="borderProgress" >
    <el-col :span="10" :offset="4" style="text-align: left;">
      <div class="border" :style="borderProgress">
      <div class="border-title">
        <span style="font-size: 28px; font-weight:1000;" >食材</span>
      </div>
      <span>
        <el-link style="font-size: 20px; font-weight: bold; margin-left: 40px;margin-top: 30px;"
        v-for="ingredient in result.ingredient">
        <span @click="search(ingredient)">{{ ingredient }}</span>
      </el-link>
      </span>
    </div>
  </el-col>
  </el-row>
  <el-row :style="stepProgress" style="margin-top: 55px;">
    <el-col :span="16" :offset="4" style="text-align: left;">
      <el-divider content-position="left">
        <span style="font-size: 28px; font-weight:1000;" >步骤</span>
      </el-divider>
      <div style="margin-top: 35px;">
      <div 
      v-for="(step,index) in result.step"
      style="margin-top: 25px;font-size: 18px;">
      <span>{{index+1}}.</span>
      <span>{{ step }}</span>
    </div>
    </div>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="16" :offset="4" style="text-align: left;margin-top: 30px;">
      <RelationGraph :root="result.title" :currentNodeText="result.title" style="height:600px"></RelationGraph>
    </el-col>
  </el-row>
      </el-main>
    </el-container>
  </el-container>
  </div>
  </template>
  
  <script>
  import axios from 'axios';
  import LoginDialog from './LoginDialog.vue';
  import SearchZone from './SearchZone.vue';
  import RelationGraph from './RelationGraph.vue';
  export default{
    data: function () {
      return {
        id:this.$route.params.para,
        result:{
          id:1,
          picUrl:"",
          url:"",
          title:"",
          abstract:"",
          ingredient:[],
          step:[],
          source:"",
          tag:[],
          clicks:1,
        },
        borderHeight:100,
        stepHeight:55,
        display:"relative"
      }
    },
    computed:{
          borderProgress(){
            const style = {}
            style.height = this.borderHeight +'px';
            return style
          },
          stepProgress(){
            const style ={}
            style.display = this.display;
            return style;
          }
        },
    components:{
      LoginDialog,
      SearchZone,
      RelationGraph
  },
    methods:{
      jump(url){
        window.location.href = url;
      },
      search(val){
        self.location.href = 'http://localhost:8080/result/'+val;
      },
      async SearchInfo(){
        const para = new URLSearchParams();
        para.append("id",this.id);
        await axios.post("http://localhost:8088/getInfo",para)
        .then(response =>{
          this.result={
            id:response.data.id,
            picUrl:response.data.pict_url,
            url:response.data.html_url,
            title:response.data.title,
            abstract:response.data.abstract,
            ingredient:response.data.ingredient,
            step:response.data.steps,
            source:response.data.source,
            tag:response.data.tag,
            clicks:response.data.clicks,
          };
          console.log(response.data);
          if(this.result.step == null){
            this.display = "none";
          }
        })
        .catch(error =>{
          console.error(error);  
        })
      }
    },
    created(){
        this.borderHeight = ((this.result.ingredient.length/12)+1)*80;
        this.SearchInfo();
      },
  }
  
  </script>
  
  <style>
    .el-header {
      background-color: #B3C0D1;
      color: #333;
      background-image: url('../views/Search_background.jpg');
      opacity:0.8;
    }
  
    
    .el-divider{
      width:980px;
  
    }
  
  .border {
    width: 980px;
    margin-top: 30px;
    margin-left: 0px;
    border: 1px solid black;
    border-radius: 4px;
  }
  .border-title {
    width: 110px;
    height: 30px;
    background: white;
    text-align: center;
    margin-top: -20px;
    margin-left: 10px;
  }
  </style>
  
