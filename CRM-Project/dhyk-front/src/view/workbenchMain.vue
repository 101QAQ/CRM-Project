<template>
    <el-container>
      <el-aside :width="isCollapse ? '60px':'200px'">
        <div class="icon-header" @click="toSummaryPage" >@{{ systemInfo.title }}</div>
        <el-menu
        active-text-color="#ffd04b"
        background-color="#384C70"
        class="el-menu-vertical-demo"
        default-active="2"
        :collapse-transition="false"
        :collapse="isCollapse"
        style="border-right: 0px solid;"
        text-color="#fff"
        :router="true"
        :unique-opened="true"
      >
        <el-sub-menu :index="''+index" v-for="(menu,index) in menuList":key=menu.id>
          <template #title>
            <el-icon><component :is="menu.icon"></component></el-icon>
            <span>{{ menu.name}}</span>
          </template>
            <el-menu-item  v-for="menuItem in menu.children":key=menuItem.id :index="menuItem.url">
              <el-icon><component :is="menuItem.icon"></component></el-icon>
              {{ menuItem.name }}
            </el-menu-item>
        </el-sub-menu>
      </el-menu>
      </el-aside>
      <el-container class="mainContent">
        <el-header>
          <el-icon size="30">
            <Expand class="extent" @click="isShow"/>
          </el-icon>
          <el-dropdown>
              <span class="el-dropdown-link">
                <el-icon class="el-icon-left"><User /></el-icon>
                {{user.name}}
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="myDetails">我的资料</el-dropdown-item>
                  <el-dropdown-item @click="passwordDialogVisible=true">修改密码</el-dropdown-item>
                  <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
        </el-header>
        <el-main>
            <router-view v-if="isRelodeShow"></router-view>
        </el-main>
        <el-footer>@ 版权所有物联22103段海涛,如有侵权请联系：{{ systemInfo.email }}</el-footer>
      </el-container>
    </el-container>


    <el-dialog v-model="passwordDialogVisible"  :append-to-body="true" :modal="false" title="修改密码" center>
        <el-form
                ref="passwordRuleFormRef"
                style="max-width: 100%"
                :model="passwordForm"
                :rules="passwordRules"
                label-width="auto"
                class="demo-ruleForm"
                >
            <el-form-item label="旧密码" prop="oldPwd">
                <el-input
                    v-model.trim="passwordForm.oldPwd"
                    style="width: 100%"
                    type="password"
                    placeholder="请输入旧密码"
                />
            </el-form-item>
            <el-form-item label="新密码" prop="newPwd">
                <el-input
                    v-model.trim="passwordForm.newPwd"
                    style="width: 100%"
                    type="password"
                    placeholder="请输入新密码"
                />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPwd">
                <el-input
                    v-model.trim="passwordForm.confirmPwd"
                    style="width: 100%"
                    type="password"
                    placeholder="请确认密码"
                />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="confirmResetPwd">确认</el-button>
                <el-button type="primary" plain @click="consoleResetPwd">取消</el-button>
            </div>
        </template>
    </el-dialog>



</template>

<script  setup>
  import {ArrowDown}from '@element-plus/icons-vue'
  import { nextTick, onMounted, reactive, ref} from 'vue';
  import { get, put } from '@/http/http';
  import { getAuthority, isPermission, messageConfirm, messageTip, refreshToken, removeToken } from '@/utils/Myutils';
  import { mitter } from '@/mitt/mitt';
import router from '@/router/router';

  const passwordRuleFormRef=ref(null)
  let user=reactive({})

  let userId=ref('')

  let isCollapse=ref(false)

  let menuList=ref([])

  let systemInfo=ref({})

  let passwordForm=reactive({
      oldPwd:'',
      newPwd:'',
      confirmPwd:''
  })


  let passwordDialogVisible=ref(false)


  const confirmPwd=(rule,value,callback)=>{
      if(value){
        if(passwordForm.newPwd!='' &&passwordForm.newPwd!=null){
          if(passwordForm.newPwd==value){
            return callback()
          }else{
            return callback(new Error('确认密码错误'))
          }
        }else{
          return callback(new Error('请输入新密码'))
        }
      }else{
        return callback(new Error('请确认密码'))
      }
  }

  const passwordRules={
    oldPwd:[
      {required:true,message:"请输入旧密码",trigger:'blur'},
      {min:6,max:16,message:"密码长度为6~16为",trigger:'blur'},
    ],
    newPwd:[
      {required:true,message:"请输入新密码",trigger:'blur'},
      {min:6,max:16,message:"密码长度为6~16位",trigger:'blur'},
    ],
    confirmPwd:[
      {required:true,message:"请确认密码",trigger:'blur'},
      {validator:confirmPwd,trigger:'blur'}
    ]
  }

  function isShow(){
    isCollapse.value=!isCollapse.value
  }
  

  
  const consoleResetPwd=()=>{
    passwordDialogVisible.value=false
    passwordRuleFormRef.value.resetFields()
  }

  const confirmResetPwd=()=>{
    passwordRuleFormRef.value.validate((valid)=>{
        if(valid){
          put("/user/resetPwd",passwordForm).then((res)=>{
            if(res.data.code===200){
              removeToken()
              messageTip("修改成功")
              messageConfirm("密码已修改请重新登录").then(()=>{
                window.location.href='/'
              }).catch(()=>{
                messageTip("登录状态无效")
              })
            }else{
              messageTip(res.data.msg,'error')
            }
          })
        }
    })
  }

  let isRelodeShow=ref(true)

  mitter.on("relode",()=>{
    isRelodeShow.value=false,
    nextTick(()=>{
      isRelodeShow.value=true
    })
  })


  const myDetails=()=>{
    router.push({
      name:'myDetails',
      params:{
        id:userId.value
      }
    })
  }


  let authorityList=[]

  const toSummaryPage=async ()=>{
    authorityList=await getAuthority()
    if(isPermission(authorityList,'admin')||isPermission(authorityList,'accountant')){
      router.push({
        name:'statistic',
      })
    }
  }

  const logout=function(){
    get("/user/logout").then((reponse)=>{
      if(reponse.data.code===200){
        removeToken()
        setTimeout(()=>{
          messageTip("退出成功",'success')
           window.location.href='/'
        },500) 
      }else{
        messageConfirm(reponse.data.msg+".退出异常是否强行退出").then(()=>{
          removeToken()
          window.location.href='/'
        })
      }
    })
  }

  get("/user/getInfor",null).then((result)=>{
    const data=result.data.data
        if(result.data.code===200){
          user=data.principal
          userId.value=user.id
          refreshToken()
        }else{
            messageTip(result.data.msg,"error")
        }
        console.log(userId.value)
  })

  onMounted(()=>{
      get("/permission/menu").then((response)=>{
          if(response.data.code===200){
            menuList.value=response.data.data
          }else{
            messageTip("数据加载失败","error")
          }
      })

      get("/system/infor").then((res)=>{
        if(res.data.code===200){
            systemInfo.value=res.data.data
        }else{
            messageTip(res.data.msg,'error')
        }
      })

  })
</script>

<style scoped>
.icon-header{
  color:white;
  text-align: center;
  line-height: 40px;
  height: 40px;
  cursor: pointer;
}
.el-header{
  padding-left: 5px;
  height: 40px;
  line-height: 60px;
  background-color: rgb(229, 255, 246);
}
.el-aside{
  background-color: black;
}
.mainContent{
  height: calc(100vh);  
}


.el-footer{
  text-align: center;
  line-height: 60px;
  color: black;
  background-color: #f5f7fa;
}
.extent{
  cursor: pointer;
}
.el-dropdown{
  line-height: 40px;
  float: right;
}
.el-icon-left{
  line-height: 40px;
}
</style>