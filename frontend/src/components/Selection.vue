<template>
    <div>
      <el-card>
        <el-row :gutter="24">
          <el-col :span="10" style="color: black">
            已选条件：
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <el-tag v-if="this.selectTags.method !== ''"
              closable size="small" @close="handleClose(0)">
              {{this.selectTags.method}}
            </el-tag>
            <el-tag v-if="this.selectTags.taste !== ''"
                    closable size="small" @close="handleClose(1)">
              {{this.selectTags.taste}}
            </el-tag>
            <el-tag v-if="this.selectTags.scene !== ''"
                    closable size="small" @close="handleClose(2)">
              {{this.selectTags.scene}}
            </el-tag>
            <el-tag v-if="this.selectTags.category !== ''"
              closable size="small" @close="handleClose(3)">
              {{this.selectTags.category}}
            </el-tag>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="10" style="color: black">
            烹调方式：
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <el-button type="text" plain v-for="method in method" :key="method"
                       size="medium" @click="selectKey(method,0)">{{method}}</el-button>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="10" style="color: black">
            口味：
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="30">
            <el-button type="text" plain v-for="taste in taste" :key="taste"
                       size="medium"  @click="selectKey(taste,1)">{{taste}}</el-button>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="10" style="color: black">
            场景：
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <el-button type="text" plain v-for="scene in scene" :key="scene"
                       size="medium"  @click="selectKey(scene,2)">{{scene}}</el-button>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="10" style="color: black">
            种类：
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <el-button type="text" plain v-for="category in category" :key="category"
                       size="medium"  @click="selectKey(category,3)">{{category}}</el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </template>
   
  <script>
    export default {
      props:{
        updateSelect: { 
          type: Function,
          required: true
        }
      },
      data() {
        return {
          //烹调方法
          method: ['煎', '蒸', '炖', '烧', '炸', '卤','干锅','火锅'],
          //口味
          taste: ['辣', '咖喱', '蒜香', '酸甜','奶香','孜然','鱼香','五香','清淡'],
          //场景
          scene: ['早餐','下午茶','二人世界','正餐'],
          //种类
          category:['烘焙','汤羹','主食','小吃','荤菜','素菜','凉菜'],
          //筛选参数
          selectTags: {
            method: '',
            taste: '',
            scene: '',
            category:''
          }
      }
    },
      methods: {
        //筛选
        selectKey(tag,key) {
          console.log("select key");
          if(key === 0)
            this.selectTags.method = tag
          else if(key === 1)
            this.selectTags.taste = tag
          else if(key === 2)
            this.selectTags.scene = tag
          else if(key === 3)
            this.selectTags.category = tag
          let data = {
            tag: tag,
            key: key
          }
          
          this.updateSelect(data);
        },
        //删掉已选中的选项
        handleClose(key) {
          console.log("close");
          if(key === 0)
            this.selectTags.method = ''
          else if(key === 1)
            this.selectTags.taste = ''
          else if(key === 2)
            this.selectTags.scene = ''
          else if(key === 3)
            this.selectTags.category = ''
          let data = {
            tag: "Null",
            key: key
          }
          this.updateSelect(data);
        },
        
      }
    }

  </script>
   
  <style scoped>
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



  </style>