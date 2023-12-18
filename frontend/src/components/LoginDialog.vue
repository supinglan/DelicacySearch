<template>
<div>
<img src="../assets/user.png" style="width:25px; height: 25px;" alt="profile" />
<el-link  @click="popLoginForm">{{user}}</el-link>
<el-dialog title="用户登录" :visible.sync="loginFormVisible" @close="clearLoginForm">
      <el-dialog title="用户注册" :visible.sync="registerFormVisible" append-to-body @close="clearRegForm">
        <el-form :model="registerForm" status-icon :rules="rules" ref="registerForm" label-width="100px" >
          <el-form-item label="用户名" prop="username">
            <el-input  v-model="registerForm.username" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="registerForm.password" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="checkpassword">
            <el-input type="password" v-model="registerForm.checkpassword" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input  v-model="registerForm.email" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="margin-top: 30px;" type="primary" @click="Register" >注册</el-button>
            <el-button style="margin-right: 100px;" @click="registerFormVisible = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
  <el-form :model="loginForm" status-icon ref="loginForm" label-width="100px">
  <el-form-item label="用户名">
    <el-input  v-model="loginForm.username" autocomplete="off"></el-input>
  </el-form-item>
  <el-form-item label="密码">
    <el-input type="password" v-model="loginForm.password" autocomplete="off"></el-input>
  </el-form-item>
  <el-link type="primary" @click="registerFormVisible = true;">没有账户？点击注册</el-link>
  <el-form-item>
  
    <el-button style="margin-top: 30px;" type="primary" @click="Login" >确认</el-button>
    <el-button style="margin-right: 100px;" @click="loginFormVisible = false">取消</el-button>
  </el-form-item>
</el-form>
</el-dialog>
</div>
</template>
<script>
import axios from 'axios';
  export default {
    data() {
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.registerForm.checkpassword !== '') {
            this.$refs.registerForm.validateField('checkpassword');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.registerForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      var checkEmail = (rule, value, callback) => {
        if (!value) {
          return callback(new Error('邮箱不能为空'));
        }else{
          callback();
        }
      }
      return {
        user:"Guest",
        loginFormVisible: false,
        registerFormVisible: false,
        loginForm: {
          username: '',
          password: ''
        },
        registerForm:{
          username:'',
          password:'',
          checkpassword:'',
          email:''
        },
        rules: {
          username: { required: true, message: '请输入用户名', trigger: 'blur' },
          password: [
            { required: true, validator: validatePass, trigger: 'blur' }
          ],
          checkpassword: [
            { required: true, validator: validatePass2, trigger: 'blur' }
          ],
          email: [
            { required: true, validator: checkEmail, trigger: 'blur' }
          ]
        },

      };
    },
    methods: {
      clearLoginForm(){
        this.loginForm.username='';
        this.loginForm.password='';
      },
      clearRegForm(){
        this.loginForm.username='';
        this.loginForm.password='';
        this.loginForm.checkpassword='';
        this.loginForm.email='';
      },
      popLoginForm(){
        if(this.user =="Guest"){
          this.loginFormVisible = true;
        }
      },
      async Login() {
        this.$message({
            type: 'success',
            message: '登录成功!'
          });
        this.user = this.loginForm.username;
        this.loginFormVisible = false;
      //   var para = new URLSearchParams();
      //   para.append("username",this.loginForm.username);
      //   para.append("password",this.loginForm.password);
      //   await axios.post('http://localhost:8088/user/login',para)
      // .then(response=>{
      //   if(response.data.success){
      //     this.$message({
      //       type: 'success',
      //       message: '登录成功!'
      //     });
      //   }else{
      //     this.$message({
      //       type: 'error',
      //       message: '登陆失败!'
      //     });
      //   }
        
      // })
      },
      async Register() {
        this.$message({
            type: 'success',
            message: '注册成功!'
          });
        this.registerFormVisible = false;
      //   var para = new URLSearchParams();
      //   para.append("username",this.registerForm.username);
      //   para.append("password",this.registerForm.password);
      //   para.append("email",this.registerForm.email)
      //   await axios.post('http://localhost:8088/user/register',para)
      // .then(response=>{
      //   if(response.data.success){
      //     this.$message({
      //       type: 'success',
      //       message: '注册成功!'
      //     });
      //   }else{
      //     this.$message({
      //       type: 'error',
      //       message: '注册失败!'
      //     });
      //   }
        
      // })
      },
      
    }
  }
</script>

<style>

.el-link{
  margin-left: 50%;
}