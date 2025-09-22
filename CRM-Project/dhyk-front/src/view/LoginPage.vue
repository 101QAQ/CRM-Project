<template>
     <el-container>
      <el-aside class="aside">
        <img src="../assets/loginBox.svg" class="image">
        <p class="text">欢迎使用东海云客系统</p>
      </el-aside>
        <el-main>
            <el-form
                ref="ruleFormRef"
                style="max-width: 600px"
                :model="ruleForm"
                status-icon
                :rules="rules"
                label-width="auto"
                class="demo-ruleForm"
                >
                <h1 class="title">欢迎登录东海云客系统</h1>
                <el-form-item label="账号" prop="act">
                    <el-input v-model="ruleForm.act" type="text" autocomplete="off" />
                    </el-form-item>
                    <el-form-item label="密码" prop="pass">
                        <el-input v-model="ruleForm.pass" type="password" autocomplete="off" />
                    </el-form-item>
                    <el-form-item>
                    <el-button type="primary" @click="submitForm(ruleFormRef)" :disabled="!value1">登录</el-button>
                    <el-button @click="resetForm(ruleFormRef)">重置</el-button>
                    <div style="width: 100%;"></div> 
                    <el-checkbox :value="isNotLogin" name="isNotLogin" @change="isNotLogin=!isNotLogin">
                        七天免登录
                    </el-checkbox>
                </el-form-item>
            </el-form>
        </el-main>
    </el-container>
</template>

<script setup>
    import { onMounted, reactive, ref } from 'vue'
    import {get, post} from '../http/http'
    import { getAuthority, isFree, isPermission, messageTip, removeToken} from '../utils/Myutils'
    const ruleFormRef = ref(null)
    let value1=ref(true)
    const ruleForm = reactive({
        pass: '',
        act: '',
    })
    isFree()
    const isNotLogin=ref(false)
    const rules = {
        pass: [
            {min:6,max:16,message:"密码长度为6~16为",trigger:'blur'},
            {required:true,message:"请输入密码",trigger:'blur'}
            
        ],
        act: [{required:true,message:"请输入账号",trigger:'blur'}],
    }
    

    let isLogin=false
    let authorityList=[]
    const submitForm =()=> {
        ruleFormRef.value.validate(async (valid) => {
            if (valid) {
                let formDate=new FormData()
                formDate.append("loginAct",ruleForm.act)
                formDate.append("loginPassword",ruleForm.pass)
                formDate.append("isNotLogin",''+isNotLogin.value)
                await post("/login",formDate).then((result) => {
                    if(result.data.code===200){
                        isLogin=true
                        messageTip("登录成功","success")
                        removeToken()
                        if(isNotLogin.value){
                            window.localStorage.setItem('Jwt',result.data.data)
                        }else{
                            window.sessionStorage.setItem('Jwt',result.data.data)
                        }
                    }else{
                        messageTip(result.data.msg,"error")
                    }
                })
                if(isLogin){
                    authorityList=await getAuthority()
                    setTimeout(()=>{ 
                        if(isPermission(authorityList,'admin')||isPermission(authorityList,'accountant')){
                            window.location.href='/workbenchMain/statistic'
                        }else{
                            window.location.href='/workbenchMain'
                        }
                    },500)
                }
            } else {
                console.log('error submit!', fields)
            }
        })

        
        
    }
    const resetForm =()=> {
        ruleFormRef.value.resetFields()
    }


</script>

<style scoped>
.title{
    text-align: center;
}
.aside{
    width: 40%;
    height: calc(100vh);
    background-color: black;
    text-align: center;
}
.image{
    width: 380px;
}
.text{
    color: white;
    font-size: 30px;
}
.el-form{
    margin-left:auto;
    margin-right: auto;
    margin-top: 30%;
}
</style>