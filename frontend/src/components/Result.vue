<template>
  <div>
    <!-- 顶部导航外部容器 -->
    <div class="top-bar-wrapper">
      <!-- 内部容器 -->
      <div class="topbar">
        <!-- logo -->
        <img src="../views/logo.png" alt="profile" style="width:40px;height: 40px; margin-top: 16px; margin-left: 6%;"/>
        <!-- 左侧搜索条 -->
          <SearchZone @keyup.enter.native="onNext" :prevRoute="this.$route.params.para" :toSearch="this.$route.params.para" style="width: 40%; margin-left:10%; margin-top:-45px;border: 1px solid #c4c7ce; box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)"/>
          <!-- 右侧设置及用户条 -->
        <ul class="info">
          <li>
            <el-select v-model="value" placeholder="自定义排序" @change = "updateSort">
              <el-option
                v-for="item in option"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </li>
          <li>
            <a href="javascript:;">
              <img src="../assets/setting.png" alt="profile" />
              <span class="user">Setting</span>
            </a>
          </li>
          <li>
            <a href="javascript:;">
              <LoginDialog></LoginDialog>
            </a>
          </li>
        </ul>
        
        <!-- 下部导航条 -->
        <ul class="service">
          <el-radio-group v-model="radio" style="margin-top: 20px;" size = "medium" @change = "updateType">
              <el-radio-button  label="常规搜索"></el-radio-button>
              <el-radio-button  label="按食材搜索"></el-radio-button>
              <el-radio-button  label="按食谱搜索"></el-radio-button>
          </el-radio-group>
        </ul>
      </div>       
    </div>

    <!-- 下部搜索结果栏 -->
    <div class="main-wrapper">
      <!-- 顶部搜索结果个数 -->
      <div class="result-num">
        <span>为您找到相关结果约{{total}}个</span>
      </div>
      <!-- 搜索结果 -->
      <ul class="results">
      <el-collapse v-model="activeNames">
        <el-collapse-item title="自定义筛选" name="1">
          <Selection :updateSelect="this.updateSelect"></Selection>
        </el-collapse-item>
      </el-collapse>
      <!-- <AIQA style="height: 500px; width:600px"/> -->
        <li
          class="result-content"
          v-for="searchResult in searchResults"
          :key="searchResult.index"
        >
          <!-- 如果返回的图片地址不为空，有图片盒子，文字盒子样式为des-text1 -->
          <div class="description" v-if="searchResult.imgURL !== null">
                <!-- 给img的src绑定数据要用v-bind -->
                <img :src='searchResult.imgURL' class="img"/>
                <h3 class = title>
                  <a href="javascript:;" @click="jumpToInfo(searchResult.id)">{{ searchResult.title }}</a>
                </h3> 
                <div class="des-text1">
                  <div class="van-multi-ellipsis--l2">
                    {{searchResult.abstracts}}
                  </div> 
                </div>
                
          </div>
          <!-- 如果返回的图片地址为空，没有图片盒子，文字盒子样式des-text2-->
          <div class="description" v-else>
            <h3 class = title>
                  <a href="javascript:;" @click="jumpToInfo(searchResult.id)">{{ searchResult.title }}</a>
            </h3> 
            <div class="des-text2">
              <div class="van-multi-ellipsis--l2">{{ searchResult.abstracts }}</div>
            </div>
          </div>
          <el-divider></el-divider>
        </li>

      </ul>
      <!-- 相关搜索 -->
      <ul class="search-ranking">
        <div class="title">
          <i class="el-icon-search"></i>
          <span>猜你喜欢</span>
        </div>
        <div class="set">
        <li class="item" v-on:click="handleClick(Recommend[0])">
          <a  style="font-weight: bold;" href="javascript:;">{{Recommend[0]}}</a>
        </li> 
        <li class="item" v-on:click="handleClick(Recommend[1])">
          <a  style="font-weight: bold;" href="javascript:;">{{Recommend[1]}}</a>
        </li> 
        <li class="item" v-on:click="handleClick(Recommend[2])">
          <a  style="font-weight: bold;" href="javascript:;">{{Recommend[2]}}</a>
        </li> 
        </div>
        
       <div class="title" style="margin-top: 50px; margin-bottom:20px ;">
        <i class="el-icon-caret-top"></i>
        <span>热门搜索</span>
        
      </div>
       
      <div class="set" >
        <div style="margin-left: 7%;">
          <li><span style="color:crimson">1</span><a href="javascript:;" v-on:click="handleClick(Hot[0])">{{Hot[0]}}</a></li> 
          <li><span style="color:chocolate">2</span><a href="javascript:;" v-on:click="handleClick(Hot[1])">{{Hot[1]}}</a></li> 
          <li><span style="color:gold">3</span><a href="javascript:;" v-on:click="handleClick(Hot[2])">{{Hot[2]}}</a></li> 
          <li><span>4</span><a href="javascript:;" v-on:click="handleClick(Hot[3])">{{Hot[3]}}</a></li> 
        </div>
        <div style="margin-left: 25%;">
          <li><span>5</span><a href="javascript:;" v-on:click="handleClick(Hot[4])">{{Hot[4]}}</a></li> 
          <li><span>6</span><a href="javascript:;" v-on:click="handleClick(Hot[5])">{{Hot[5]}}</a></li> 
          <li><span>7</span><a href="javascript:;" v-on:click="handleClick(Hot[6])">{{Hot[6]}}</a></li> 
          <li><span>8</span><a href="javascript:;" v-on:click="handleClick(Hot[7])">{{Hot[7]}}</a></li>
        </div>
      </div>
      
      
      </ul>

    </div>
    <!-- 最底部 -->
    <div class="bottom-bar">
      <!-- 底部翻页栏 -->
        <ul class="index">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :current-page="currentPage"
            :page-size="8"
            @current-change="handleCurrentChange">
          </el-pagination>
      </ul>
      <!-- 最底部功能栏 -->
      <ul class="bottom-tools">
        <li>帮助</li>
        <li>用户反馈</li>
      </ul>
    </div>
  </div>
</template>

<script>
import SearchZone from './SearchZone.vue';
import Selection from './Selection.vue';
import AIQA from './AIQA.vue';
import LoginDialog from './LoginDialog.vue';
import axios from 'axios';
export default {
  data () {
    return {
      currentPage:1,
      total:1,
      radio:"常规搜索",
      Hot:["家常菜","早餐","汤","排骨","白菜","鸡蛋","红豆","南瓜"],
      Recommend:['百香果','柑橘','柠檬'],
      activeNames: [],
      Username:"Guest",
      Method:0,
      Taste:0,
      Scene:0,
      Category:0,
      Sort:0,
      Type:0,
      option: [{
        value: '选项1',
        label: '综合排序'
      }, {
        value: '选项2',
        label: '相关度排序'
      }, {
        value: '选项3',
        label: '点击量排序'
      }],
      value: '',
    url1:"https://tse3-mm.cn.bing.net/th/id/OIP-C.EEFoDCyN0wjAgxArXWiAyAHaFC?w=276&h=188&c=7&r=0&o=5&dpr=1.3&pid=1.7",
    url2:"https://tse1-mm.cn.bing.net/th/id/OIP-C.MV5TPVtOw2H_XqNwCZ1jdgHaE8?w=259&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7",
    url3:"https://tse3-mm.cn.bing.net/th/id/OIP-C.EOkhk7RzVXnR22_Zn8WtJwHaE8?w=252&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7",
    searchForm: {
        searchContent: ''
      },
    searchResults: [
      
    ]
    }
  },
  components: { SearchZone,Selection,AIQA,LoginDialog },

  methods: {
  onConfirm() {
    this.$refs.item.toggle();
  },
  jump(url){
    window.location.href = url;
  },
  handleCurrentChange(val){
    this.currentPage = val;
    this.Search();
    console.log("current page:"+val);
  },
  async updateHot(){
    await axios.post("http://localhost:8088/hot")
    .then(response => {
        this.Hot = response.data;
      }
    ).catch(error => {  
      console.error(error);  
    });
  },
  async updateRecommend(){
    const para = new URLSearchParams();
    para.append("username","test");  
    await axios.post("http://localhost:8088/recommend",para)
    .then(response => {
      console.log(response.data);
        this.Recommend = response.data;
        
      }
    ).catch(error => {  
      console.error(error);  
    });
  },
  async Search(){
  const params = new URLSearchParams();  
  params.append('SearchText',this.$route.params.para);
  params.append('Method',this.Method);
  params.append('Taste',this.Taste);
  params.append('Scene',this.Scene);
  params.append('Category',this.Category);
  params.append('sortType',this.Sort);
  params.append('type',this.Type);
  params.append('currentPage',this.currentPage);
  params.append('username',"spl");
    await axios.post('http://localhost:8088/search',params)
    .then(response=>{
       this.searchResults=[];
       let i = (this.currentPage-1)*8+1;
       this.total = response.data.first;
      response.data.second.forEach(element => {
        if (element.abstract.length>140)
        element.abstract = element.abstract.substring(0,135)+"..."
        if(element.origin!=="食谱秀"){
          this.searchResults.push({
            "id":element.id,
            "index": i,
            "title":element.title,
            "abstracts":element.abstract,
            "imgURL":element.pict_url,
            "url":element.html_url
          })
          i++
        }
      });
      // let list = response.data
      // this.searchResults = list
      console.log("complete search");
    })
    .catch(error => {  
      console.error(error);  
    });
  },
  onNext(){
    this.Search();
  },
  handleClick(val){
    self.location.href = 'http://localhost:8080/result/'+val;
  },
  jumpToInfo(val){
    console.log(val);
    self.location.href = 'http://localhost:8080/detail/'+val;
  },
  updateType(){
    switch(this.radio){
      case "常规搜索":
        this.Type = 0;
        break;
      case "按食材搜索":
        this.Type = 1;
        break;
      case "按食谱搜索":
        this.Type = 2;
        break;
    }
    console.log(this.Type);
    this.Search();
  },
  updateSort(val){
    switch(val){
      case "选项1":
        this.Sort = 0;
        break;
      case "选项2":
        this.Sort = 1;
        break;
      case "选项3":
        this.Sort = 2;
        break;
    }
    console.log(this.Sort);
    this.Search();
  },
  updateSelect(data){
    let tag = data.tag;
    let key = data.key;
    console.log(tag);
    console.log(key);
    if(key === 0){
      switch(tag){
        case "Null":
          this.Method = 0;
          break;
        case "煎":
          this.Method = 1;
          break;
        case "蒸":
          this.Method = 2;
          break;
        case "炖":
          this.Method = 3;
          break;
        case "烧":
          this.Method = 4;
          break;
        case "炸":
          this.Method = 5;
          break;
        case "卤":
          this.Method = 6;
          break;
        case "干锅":
          this.Method = 7;
          break;
        case "火锅":
          this.Method = 8;
          break;

      }
    }else if(key === 1){
      switch(tag){
        case "Null":
          this.Taste = 0;
          break;
        case "辣":
          this.Taste = 1;
          break;
        case "咖喱":
          this.Taste = 2;
          break;
        case "蒜香":
          this.Taste = 3;
          break;
        case "酸甜":
          this.Taste = 4;
          break;
        case "奶香":
          this.Taste = 5;
          break;
        case "孜然":
          this.Taste = 6;
          break;
        case "鱼香":
          this.Taste = 7;
          break;
        case "五香":
          this.Taste = 8;
          break;
        case "清淡":
          this.Taste = 9
          break;
      }
    }else if(key === 2){
      switch(tag){
        case "Null":
          this.Scene = 0;
          break;
        case "早餐":
          this.Scene = 1;
          break;
        case "下午茶":
          this.Scene = 2;
          break;
        case "二人世界":
          this.Scene = 3;
          break;
        case "正餐":
          this.Scene = 4;
      }
    }else if(key === 3){
      switch(tag){
        case "Null":
          this.Category = 0;
          break;
        case "烘焙":
          this.Category = 1;
          break;
        case "汤羹":
          this.Category = 2;
          break;
        case "主食":
          this.Category = 3;
          break;
        case "小吃":
          this.Category = 4;
          break;
        case "荤菜":
          this.Category = 5;
          break;
        case "素菜":
          this.Category = 6;
          break;
        case "凉菜":
          this.Category = 7;
          break;
      }
    }
  }
},
created(){
  this.updateHot();
  this.updateRecommend();
  this.Search();
  console.log(this.Hot);
  
},

}
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  text-decoration: none;
  list-style: none;
  text-align: left;
}

.el-divider{
  top:-25px;
  width:350px;
  height:2px;
  color:#000;
}

.el-row {
    margin: 0px 0 15px 10px;
  }
  .el-tag {
    margin-right: 10px;
  }

  .el-button--medium {
    padding: 0px 10px;
    font-size: 16px;
    border-radius: 4px;
  }

  .el-card{
    padding: 30%;
  }


  .el-radio{
    margin-right: 20px;
    margin-bottom: 5px;
  }
  .el-dropdown{
    left:0;
    margin-left: 10px;
    margin-top: 5px;
    margin-right: 3%;
  }
  .el-dropdown-link {
    cursor: pointer;
    color: #5e6872;
  }
  .el-icon-arrow-down {
    font-size: 12px;
  }
  .demonstration {
    display: block;
    color: #8492a6;
    font-size: 14px;
    margin-bottom: 20px;
  }
.el-icon-search{
  margin-right: 5px;
  font-size:20px;
  font-weight: bold;
}

.el-icon-caret-top{
  margin-right: 5px;
  font-size:20px;
  font-weight: bold;
}
.el-divider{
    background-color: grey;
    margin-top:35px;
  }

  .el-image{
    position:grid;
    align:bottom;
    width:100px;
    height: 100px;
    border-radius: 30px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
  }
  .el-select{
    width: 150px;
    right:180px;
    bottom:30px;
  }

a {
  color: #000;
}

a:hover {
  color: #a7aab5;
}

/* ----------------顶部------------------ */
.top-bar-wrapper {
  position: relative;
  width: 100%;
  height: 120px;
  /* background-color: #bfc; */
  text-decoration: underline;
}

.topbar {
  /* background: url("../views/Search_background.jpg") fixed no-repeat;
  background-size: cover; */
  position: relative;
  width: 100%;
  height: 100%;
  text-decoration: underline;
}


.info {
  position: relative;
  float: right;
  margin: 0px 150px 0 0;
  bottom:40px;
}

.info li {
  float: left;
  margin: 30px 0 0 50px;
  font-size: 14px;
}

.info img {
  width: 24px;
  height: 24px;
  padding: 2px;
  margin: -5px 0 0 0;
  border-radius: 50%;
}

.topbar .service {
  width: 100%;
  height: 50px;
  margin-left: 150px;
  margin-top: 0px;
}

.topbar .service li {
  float: left;
  margin: 3px 15px 0 0;
  width: 100px;
  font-size: 15px;
}

/* ---------------------------主页面------------------------------ */
.main-wrapper {
  position: relative;
  width: 1088px;
  height: 1440px;
  /* background-color: rgb(130, 187, 187); */
  margin-left: 150px;
}

.main-wrapper .result-num {
  position: absolute;
  width: 538px;
  line-height: 15px;
  font-size: 13px;
  color: #999999;
}

.main-wrapper .results {
  position: absolute;
  width: 650px;
  height: 1360px;
  top: 25px;
  /* background-color: #bfc; */
}

.main-wrapper .results h3 a {
  display: grid;
  width: 600px;
  height: 28px;
  margin-bottom: 5px;

  color: #02040a;
  font-weight: normal;
}



.main-wrapper .results h3 a:hover {
  text-decoration: underline;
  color: rgb(222, 109, 109);
}

.main-wrapper .results .result-content {
  height:115px;
  margin-bottom: 40px;
  margin-top:10px;
}

.main-wrapper .results .description {
  display: grid;
  width: 560px;
  height: 100px;
  grid-template-columns: 155px;
  grid-template-rows: 30px;
  grid-template-areas: 
  "img title"
  "img abstract";
  margin-top: 6px;
}

.main-wrapper .results .description .title{
  margin-left: 5px;
  grid-area: title;
}
.main-wrapper .results .description img {
  grid-area: img;
  width: 150px;
  height: 120px; 
  left: 0;
  border: 1px solid #f2f2f2;
  border-radius: 10px;
}

.main-wrapper .results .description .des-text1 {
  grid-area: abstract;
  margin-left: 5px;
  right: 0;
  width: 400px;
  font-size: 14px;
  color: #636262;
}
.main-wrapper .results .description .des-text2 {
  grid-area: img;
  margin-left: 5px;
  width: 400px;
  font-size: 14px;
  color: #636363;
}

.main-wrapper .search-ranking {
  position: absolute;
  padding-left:30px;
  padding-top: 40px;
  width: 400px;
  height: 450px;
  right:-10%;
  top: 25px;
  border-radius: 4px;
  box-shadow: 0px 12px 12px 0px rgba(0, 0, 0, 0.1)
}

.main-wrapper .search-ranking .set{
  display:flex;
  flex-direction: row;
  height:20px;
  margin-bottom: 20px;
}
.main-wrapper .search-ranking li {
  display: flex;
  width: 100px;
  height: 32px;
  padding: 5px 1px 5px 0;
}

.main-wrapper .search-ranking .title {
  grid-area: "title";
  width: 272px;
  height: 22px;
  margin-bottom: 10px;
  font-size: 17px;
  font-weight: 600;
}

.main-wrapper .search-ranking .logoImg {
  width: 23px;
  height: 24px;
  padding-right: 5px;
  margin-bottom: -5px;
  font-size: 14px;
}

.main-wrapper .search-ranking li span {
  display: block;
  width: 18px;
  height: 22px;
  margin-top:4px;
  margin-right: 8px;
  color: #ad95a3;
  font-size: 15px;
}
.main-wrapper .search-ranking .item{
  display: flex;
  flex-direction: column;
  height:110px;
  margin-top: 8px;
  margin-bottom: 8px;
  margin-right: 10px;
}

.main-wrapper .search-ranking .el-image{
  width:100px;
  height:90px;

}
.main-wrapper .search-ranking a {
  color: #090a0c;
  font-weight: 500;
  text-align: center;

}

.main-wrapper .search-ranking a:hover {
  color: #707ba0;
}


/* ----------------------最底部-------------------------- */
.bottom-bar {
  position: absolute;
  width: 100%;
}

.bottom-bar .index {
  position: absolute;
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 36px;
  padding: 14px 0 14px 150px;
  bottom: 60px;
}

.bottom-bar .index li {
  text-align: center;
  width: 36px;
  height: 36px;
  line-height: 36px;
  margin-right: 15px;
  border-radius: 20%;
  font-size: 15px;
  color: #000001;
}

.bottom-bar .index a:first-child li {
  color: #fff;
  background-color: rgb(222, 109, 109);
}

.bottom-bar .index li:hover {
  color: #fff;
  background-color:  rgb(222, 109, 109);
}

.bottom-bar .index a:last-child li {
  width: 80px;
  border-radius: 7px;
}

.bottom-bar .bottom-tools {
  display: flex;
  position: absolute;
  bottom: 0;
  width: 100%;
  line-height: 42px;
  padding-left: 150px;
  color: #9195a3;
}

.bottom-bar .bottom-tools li {
  padding: 0 12px;
  font-size: 14px;
}

.bottom-bar .bottom-tools li:first-child {
  padding-left: 0;
}
</style>
