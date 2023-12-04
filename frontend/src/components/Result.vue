<template>
    <div>
      <!-- 顶部导航外部容器 -->
      <div class="top-bar-wrapper">
        <!-- 内部容器 -->
        <div class="topbar">
          <!-- logo -->
          <img src="../views/logo.png" alt="profile" style="width:40px;height: 40px; margin-top: 16px; margin-left: 6%;"/>
          <!-- 左侧搜索条 -->
            <SearchZone style="width: 40%; margin-left:10%; margin-top:-45px;border: 1px solid #c4c7ce; box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)"/>
            <!-- 右侧设置及用户条 -->
          <ul class="info">
            <li>
              <el-select v-model="value" placeholder="自定义排序" >
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
                <span class="user">setting</span>
              </a>
            </li>
            <li>
              <a href="javascript:;">
                <img src="../assets/user.png" alt="profile" />
                <span class="user">Guest</span>
              </a>
            </li>
            
          </ul>
          <!-- 下部导航条 -->
          <ul class="service">
            
        <!-- 筛选>
      <el-cascader :options="options">
        <template slot-scope="{ node, data }">
          <span>{{ data.label }}</span>
          <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
        </template>
      </el-cascader> -->
          </ul>
        </div>
      </div>
  
      <div class="line"></div>
      <!-- 下部搜索结果栏 -->
      <div class="main-wrapper">
        <!-- 顶部搜索结果个数 -->
        <div class="result-num">
          <span>为您找到相关结果约100,000,000个</span>
        </div>
        <!-- 搜索结果 -->
        <ul class="results">
 
          <li
            class="result-content"
            v-for="searchResult in searchResults"
            :key="searchResult.id"
          >
            <!-- 如果返回的图片地址不为空，有图片盒子，文字盒子样式为des-text1 -->
            <div class="description" v-if="searchResult.imgURL !== null">
                  <!-- 给img的src绑定数据要用v-bind -->
                  <img :src='searchResult.imgURL' class="img"/>
                  <h3 class = title>
                    <a href="javascript:;" @click="jump(searchResult.url)">{{ searchResult.title }}</a>
                  </h3> 
                  <div class="des-text1">
                    <div class="van-multi-ellipsis--l2">
                      {{ searchResult.abstracts }}
                    </div> 
                  </div>
                  
            </div>
            <!-- 如果返回的图片地址为空，没有图片盒子，文字盒子样式des-text2-->
            <div class="description" v-else>
              <h3 class = title>
                    <a href="javascript:;" @click="jump(searchResult.url)">{{ searchResult.title }}</a>
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
            <span>相关搜索</span>
          </div>
          <div class="set">
          <li class="item">
            <el-image :src="url1"></el-image>
            <a  href="javascript:;" >百香果</a>
          </li> 
          <li class="item">
            <el-image :src="url2"></el-image>
            <a  href="javascript:;">柑橘</a>
          </li> 
          <li class="item">setting
            <el-image :src="url3"></el-image>
            <a  href="javascript:;">柠檬</a>
          </li> 
          </div>
         <div class="title"><i class="el-icon-caret-top"></i><span>热门搜索</span></div>
        <div class="set">
          <div>
            <li><span style="color:crimson">1</span><a href="javascript:;">家常菜</a></li> 
            <li><span style="color:chocolate">2</span><a href="javascript:;">早餐</a></li> 
            <li><span style="color:gold">3</span><a href="javascript:;">汤</a></li> 
            <li><span>4</span><a href="javascript:;">排骨</a></li> 
            <li><span>5</span><a href="javascript:;">白菜</a></li> 
          </div>
          <div style="margin-left: 25%;">
            <li><span>6</span><a href="javascript:;">鸡蛋</a></li> 
            <li><span>7</span><a href="javascript:;">红豆</a></li> 
            <li><span>8</span><a href="javascript:;">南瓜</a></li> 
            <li><span>9</span><a href="javascript:;">豆腐</a></li> 
            <li><span>10</span><a href="javascript:;">面食</a></li> 
          </div>
        </div>
        </ul>

      </div>

      <!-- 最底部 -->
      <div class="bottom-bar">
        <!-- 底部翻页栏 -->
        <ul class="index">
          <a href="javascript:;">
            <li>1</li>
          </a>
          <a href="javascript:;">
            <li>2</li>
          </a>
          <a href="javascript:;">
            <li>3</li>
          </a>
          <a href="javascript:;">
            <li>4</li>
          </a>
          <a href="javascript:;">
            <li>5</li>
          </a>
          <a href="javascript:;">
            <li>6</li>
          </a>
          <a href="javascript:;">
            <li>7</li>
          </a>
          <a href="javascript:;">
            <li>8</li>
          </a>
          <a href="javascript:;">
            <li>9</li>
          </a>
          <a href="javascript:;">
            <li>10</li>
          </a>
          <a href="javascript:;">
            <li class="nextPage">下一页></li>
          </a>
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
export default {
    data () {
      return {
        para:'',
        option: [{
          value: '选项1',
          label: '默认排序'
        }, {
          value: '选项2',
          label: '按时间'
        }, {
          value: '选项3',
          label: '按相关度'
        }, {
          value: '选项4',
          label: '按搜索量'
        }],
        value: '',
        options: [{
          value: 'zhinan',
          label: '指南',
          children: [{
            value: 'shejiyuanze',
            label: '设计原则',
            children: [{
              value: 'yizhi',
              label: '一致'
            }, {
              value: 'fankui',
              label: '反馈'
            }, {
              value: 'xiaolv',
              label: '效率'
            }, {
              value: 'kekong',
              label: '可控'
            }]
          }, {
            value: 'daohang',
            label: '导航',
            children: [{
              value: 'cexiangdaohang',
              label: '侧向导航'
            }, {
              value: 'dingbudaohang',
              label: '顶部导航'
            }]
          }]
        }, {
          value: 'zujian',
          label: '组件',
          children: [{
            value: 'basic',
            label: 'Basic',
            children: [{
              value: 'layout',
              label: 'Layout 布局'
            }, {
              value: 'color',
              label: 'Color 色彩'
            }, {
              value: 'typography',
              label: 'Typography 字体'
            }, {
              value: 'icon',
              label: 'Icon 图标'
            }, {
              value: 'button',
              label: 'Button 按钮'
            }]
          }, {
            value: 'form',
            label: 'Form',
            children: [{
              value: 'radio',
              label: 'Radio 单选框'
            }, {
              value: 'checkbox',
              label: 'Checkbox 多选框'
            }, {
              value: 'input',
              label: 'Input 输入框'
            }, {
              value: 'input-number',
              label: 'InputNumber 计数器'
            }, {
              value: 'select',
              label: 'Select 选择器'
            }, {
              value: 'cascader',
              label: 'Cascader 级联选择器'
            }, {
              value: 'switch',
              label: 'Switch 开关'
            }, {
              value: 'slider',
              label: 'Slider 滑块'
            }, {
              value: 'time-picker',
              label: 'TimePicker 时间选择器'
            }, {
              value: 'date-picker',
              label: 'DatePicker 日期选择器'
            }, {
              value: 'datetime-picker',
              label: 'DateTimePicker 日期时间选择器'
            }, {
              value: 'upload',
              label: 'Upload 上传'
            }, {
              value: 'rate',
              label: 'Rate 评分'
            }, {
              value: 'form',
              label: 'Form 表单'
            }]
          }, {
            value: 'data',
            label: 'Data',
            children: [{
              value: 'table',
              label: 'Table 表格'
            }, {
              value: 'tag',
              label: 'Tag 标签'
            }, {
              value: 'progress',
              label: 'Progress 进度条'
            }, {
              value: 'tree',
              label: 'Tree 树形控件'
            }, {
              value: 'pagination',
              label: 'Pagination 分页'
            }, {
              value: 'badge',
              label: 'Badge 标记'
            }]
          }, {
            value: 'notice',
            label: 'Notice',
            children: [{
              value: 'alert',
              label: 'Alert 警告'
            }, {
              value: 'loading',
              label: 'Loading 加载'
            }, {
              value: 'message',
              label: 'Message 消息提示'
            }, {
              value: 'message-box',
              label: 'MessageBox 弹框'
            }, {
              value: 'notification',
              label: 'Notification 通知'
            }]
          }, {
            value: 'navigation',
            label: 'Navigation',
            children: [{
              value: 'menu',
              label: 'NavMenu 导航菜单'
            }, {
              value: 'tabs',
              label: 'Tabs 标签页'
            }, {
              value: 'breadcrumb',
              label: 'Breadcrumb 面包屑'
            }, {
              value: 'dropdown',
              label: 'Dropdown 下拉菜单'
            }, {
              value: 'steps',
              label: 'Steps 步骤条'
            }]
          }, {
            value: 'others',
            label: 'Others',
            children: [{
              value: 'dialog',
              label: 'Dialog 对话框'
            }, {
              value: 'tooltip',
              label: 'Tooltip 文字提示'
            }, {
              value: 'popover',
              label: 'Popover 弹出框'
            }, {
              value: 'card',
              label: 'Card 卡片'
            }, {
              value: 'carousel',
              label: 'Carousel 走马灯'
            }, {
              value: 'collapse',
              label: 'Collapse 折叠面板'
            }]
          }]
        }, {
          value: 'ziyuan',
          label: '资源',
          children: [{
            value: 'axure',
            label: 'Axure Components'
          }, {
            value: 'sketch',
            label: 'Sketch Templates'
          }, {
            value: 'jiaohu',
            label: '组件交互文档'
          }]
        }],
      url1:"https://tse3-mm.cn.bing.net/th/id/OIP-C.EEFoDCyN0wjAgxArXWiAyAHaFC?w=276&h=188&c=7&r=0&o=5&dpr=1.3&pid=1.7",
      url2:"https://tse1-mm.cn.bing.net/th/id/OIP-C.MV5TPVtOw2H_XqNwCZ1jdgHaE8?w=259&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7",
      url3:"https://tse3-mm.cn.bing.net/th/id/OIP-C.EOkhk7RzVXnR22_Zn8WtJwHaE8?w=252&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7",
      searchForm: {
          searchContent: ''
        },
      searchResults: [
        {
          id:1,
          title:"青柠红茶巴斯克",
          abstracts:"像春天一样的小清新，青柠与红茶的双重口感,你只需要一只蜜桃，几片薄荷，半杯苏打水。不落俗套的少女心，都在这一杯蜜桃莫吉托里了",
          imgURL:"https://tse3-mm.cn.bing.net/th/id/OIP-C.vE-mMltbF0REfrlazDXSGQHaJ4?w=140&h=186&c=7&r=0&o=5&dpr=1.3&pid=1.7",
          url:"https://www.xiachufang.com/recipe/107106680/"
        },
        {
          id:2,
          title:"蜜桃莫吉托｜Peach Mojito",
          abstracts:"你只需要一只蜜桃，几片薄荷，半杯苏打水。不落俗套的少女心，都在这一杯蜜桃莫吉托里了！",
          imgURL:"https://tse3-mm.cn.bing.net/th/id/OIP-C.w_46wdfSgtqW04jA5rkCDAHaE8?w=253&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7",
          url:"https://www.xiachufang.com/recipe/101894403/"
        },
        {
          id:3,
          title:"蔓越莓坚果风味藜麦沙拉Cranberry Quinoa Salad ",
          abstracts:"这道菜色彩明快，口感上层次丰富，竟能吃出肉味来，是brunch的保留菜式。该菜成品为两人份量。用量参考改编自厨师Juan-Carlos Cruz菜谱",
          imgURL:"https://tse2-mm.cn.bing.net/th/id/OIP-C.AiOZF9vQ8Gh9Pnq7EtwchAHaE8?w=277&h=185&c=7&r=0&o=5&dpr=1.3&pid=1.7",
          url:"https://www.xiachufang.com/recipe/107106680/"
        },
        {
          id:4,
          title:"青柠/青桔百香果气泡水/夏日必备",
          abstracts:"夏天就要喝透心凉的饮料！酸酸的百香果和青柠，揉入甜甜的雪碧和苏打水，碰撞出绝美的火花！",
          imgURL:"https://tse2-mm.cn.bing.net/th/id/OIP-C.lr0WAejAR-7OIOMB5vK74gHaE7?w=272&h=181&c=7&r=0&o=5&dpr=1.3&pid=1.7",
          url:"https://www.xiachufang.com/recipe/100285676/"
        },
        {
          id:5,
          title:"青柠香菜虾仁",
          abstracts:"这是一道能改变你对香菜态度的神奇的菜肴。青柠，香菜和虾仁三个听上去并不搭调的食材组合起来却异常和谐。青柠的酸和清香，虾仁的鲜，和香菜独特的香味完美结合",
          imgURL:"https://tse4-mm.cn.bing.net/th/id/OIP-C.pJaEByHjUcPJJUHj6dbFfgHaE6?w=282&h=187&c=7&r=0&o=5&dpr=1.3&pid=1.7",
          url:"https://www.xiachufang.com/recipe/100546155/"
        },
        {
          id:6,
          title:"百香果青柠鸡脚",
          abstracts:"这是一道能改变你对香菜态度的神奇的菜肴。青柠，香菜和虾仁三个听上去并不搭调的食材组合起来却异常和谐。青柠的酸和清香，虾仁的鲜，和香菜独特的香味完美结合",
          imgURL:"https://tse4-mm.cn.bing.net/th/id/OIP-C.63khk0VdnVzZc5rUXQDHLgHaEK?w=283&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7",
          url:"https://www.xiachufang.com/recipe/102321347/"
        },
        {
          id:7,
          title:"青柠茉莉茶",
          abstracts:"茉莉花茶是略微有一点点苦涩的，冰镇以后苦味会降低一些，夏天喝非常消暑。建议读完贴士再做",
          imgURL:"https://tse4-mm.cn.bing.net/th/id/OIP-C.5doZJlz6ract52GiJXWQUQHaF7?w=211&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7",
          url:"https://www.xiachufang.com/recipe/105637327/"
        },
        {
          id:8,
          title:"简单易做的爽口泰式青柠鱼",
          abstracts:"爱吃泰餐里的青柠鱼，总想挑战一下自己的厨艺，结合下厨房里前辈们的几个菜谱，终于下定决心买齐了几种必须的原材料，试做一把，一次成功",
          imgURL:"https://tse4-mm.cn.bing.net/th/id/OIP-C.ELPodeFp7bnSeRqNMN4-hAHaEK?w=253&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7",
          url:"https://www.xiachufang.com/recipe/104677382/"
        },
      ]
      }
    },
    components: { SearchZone },
  
    methods: {
    onConfirm() {
      this.$refs.item.toggle();
    },
    jump(url){
      window.location.href = url;
    },
  
    //   Search () {
    //     this.$axios
    //       .post('/result', {
    //         // 把 search 页面的 input 内容映射到 searchContent 变量中并发送给后端
    //         searchContent: this.searchForm.searchContent // 通过 v-model 把 input 框输入的内容存进 searchContent 中
    //       })
    //       // .then意思是指定回调函数
    //       .then((successResponse) => {
    //         // successResponse.data[0].title：就是符合条件的搜索结果的标题。其他以此类推
    //         // console.log(successResponse.data[0].title)
    //         console.log(successResponse.data[0].imgURL) // 数组，所有符合条件的结果数组
    //         // console.log(successResponse.data[0])  // 数组第一个，里面包含id title abstracts
    //         let list = successResponse.data
    //         this.searchResults = list
    //         if (successResponse.data.length === 0) {
    //           alert('error')
    //         }
    //       })
    //       // 指定发生错误时的回调函数
    //       .catch((failResponse) => {})
    //   }
    // }
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
    color:#000;
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
  .line {
    position: relative;
    margin: 0 auto;
    width: 1200px;
    height: 1px;
    background-color: #e7e5e5;
    text-align: center;
    font-size: 16px;
    color: rgb(235, 230, 230);
  }
  
  /* ----------------顶部------------------ */
  .top-bar-wrapper {
    position: relative;
    width: 100%;
    height: 90px;
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
  
  .topbar .logoImg {
    width: 30px;
    height: 30px;
    margin-top: 20px;
    padding: 0 0 0 50px;
  }

  /* .topbar input {
    position: absolute;
    width: 500px;
    height: 14px;
    top: 14px;
    left: 137px;
    padding: 12px 48px 12px 16px;
    border: 2px solid #c4c7ce;
    border-radius: 20px 20px 20px 20px;
    /* 设置文字开始空格长度 */
    /* text-indent: 5px;
    font-size: 16px; */
    /* 取消input获得焦点时的边框 */
    /* outline: none;
  } */

  /* 设置input获得焦点时样式 */
  /* .topbar input:hover {
    border-color: #a7aab5;
    box-shadow: 0px 1px 0px 0px #e5e5e5;
  } */
  
  /* 设置input被点击时的样式 */
  /* .topbar input:focus {
    border-color: #626d9c;
    box-shadow: 0px 1px 0px 0px #e5e5e5; /*下边阴影 */
  /* } */ 

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
    height: 36px;
    margin-left: 150px;
    margin-top: 10px;
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
    line-height: 30px;
    font-size: 13px;
    color: #999999;
  }
  
  .main-wrapper .results {
    position: absolute;
    width: 560px;
    height: 1360px;
    top: 40px;
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
    padding-left:20px;
    padding-top: 20px;
    width: 400px;
    height: 450px;
    right:-10%;
    top: 25px;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
  }
  
  .main-wrapper .search-ranking .set{
    display:flex;
    flex-direction: row;
    height:128px;
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
    margin-right: 30px;
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
    bottom: 42px;
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
