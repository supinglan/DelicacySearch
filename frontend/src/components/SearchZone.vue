<template>
    <div  style="border-radius: 50px;">
    
            <el-autocomplete 
      clearable 
      prefix-icon="el-icon-search" 
      v-model="toSearch"
      :fetch-suggestions="querySearch"
      placeholder="发现美食内容"
      :trigger-on-focus="false"
      @select="handleSelect"
      style="width: 100%; "
      :highlight-first-item="true"
      :size="medium"
      :popper-append-to-body="true"
      @keyup.enter.native="onNext"
    >
</el-autocomplete>
</div>

</template>

<script>
import router from '@/router';
import axios from 'axios';
export default {
    props:{
        toDo:{type:Function, required: true},
        prevRoute:'',
        toSearch:''
      
    },
    data() {
        return {

            temp: '1',
            
            
            autoComp : []
        }
    },
    methods: {
      async querySearch(queryString, cb) {
     
        const params = new URLSearchParams();  
    params.append('SearchText',this.toSearch);
      
         await axios.post("http://120.55.14.3:8088/autofill",params)
        .then(response=>{  
          this.autoComp = []

          response.data.forEach(element => {
            this.autoComp.push({"value":element})
          });

      })  
    
      .catch(error => {  
        console.error(error);  
      });
      // alert("this autoComp:" + this.autoComp[0].value)
        // var results = queryString ? this.autoComp.filter(this.createFilter(queryString)) : this.autoComp;
        var results = this.autoComp;
            // 调用 callback 返回建议列表的数据

          results.unshift({ "value": this.toSearch }) // 把当前项加到列表
        cb(results);
      },
      createFilter(queryString) {
        return (autoCompItem) => {
          return (autoCompItem.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
      loadAll() {
        
        return [];
      },
      handleSelect(item) {
        console.log(item);
        },
      onNext() {
        if(this.toSearch===''){
          router.go(-1)
          return;
        }
        if(this.toSearch===this.prevRoute){
          return;
        }

        if(this.toSearch!==this.prevRoute){
          
          this.prevRoute = this.toSearch
          if (this.$route.fullPath.includes("detail/")) {
            router.go(-1).catch();
            router.push({ path: 'result/' + this.toSearch }).catch();
          }
          else if(!this.$route.fullPath.includes("result/"))
          router.push({ path: 'result/' + this.toSearch }).catch();
          else
          router.replace({ path: this.toSearch }).catch();

          toDo();

        }
      }
    },
    mounted() {
      this.autoComp = this.loadAll();
    }
}
</script>

<style scoped>


/*搜索组件最外层div */
.input_box {
    width: 300px;
    margin-right: 15px;
    border-radius: 95px;
    background: rgba(242, 3, 3, 0.48);
}
/* el-input el-input-group el-input-group--append el-input--prefix el-input--suffix */
/*搜索input框 */
:deep(.el-input__inner) {
    background-color: rgba(255,255,255,0.9);/*覆盖原背景颜色，设置成透明 */
    border-radius: 95px;
    font-size:medium;
    border: 0;
    box-shadow: 0 0 0 0px;
}


:deep(.el-input-group) {
    background-color: #F8F8FF;/*覆盖原背景颜色，设置成透明 */
    border-radius: 95px;
    border: 0;
    box-shadow: 0 0 0 0px;
}
:deep(.el-autocomplete-suggestion__wrap) {
    background-color: #000;/*覆盖原背景颜色，设置成透明 */
    border-radius: 95px;
    border: 0;
    box-shadow: 0 0 0 0px;
}


:deep(.el-popper) {
    background-color: #000;/*覆盖原背景颜色，设置成透明 */
    border-radius: 95px;
    border: 0;
    box-shadow: 0 0 0 0px;
}
/*搜索button按钮 */
:deep(.el-input-group__append) {
    background: rgba(245, 139, 0);
    border-radius: 95px;
    border: 0;
    box-shadow: 0 0 0 0px;
}


</style>