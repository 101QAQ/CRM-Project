<template>
      <el-button
      type="primary"
      text
      @click="router.back()"
    >
    <el-icon><Back /></el-icon>返回
    </el-button>
   <el-descriptions v-if="userDetails"
    :title="userDetails.name+'的资料'"
    direction="vertical"
    border
    style="margin-top: 20px"
  >
        <el-descriptions-item label="账号">{{ userDetails.loginAct}}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ userDetails.name }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ userDetails.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ userDetails.email }}</el-descriptions-item>
        <el-descriptions-item label="账户是否未过期">{{ userDetails.accountNoExpired===1? '是':'否'}}</el-descriptions-item>
        <el-descriptions-item label="凭据是否未过期">{{ userDetails.credentialsNoExpired===1? '是':'否' }}</el-descriptions-item>
        <el-descriptions-item label="账户是否未锁定">{{ userDetails.accountNoLocked===1? '是':'否' }}</el-descriptions-item>
        <el-descriptions-item label="账户是否启用">{{ userDetails.accountEnabled===1? '是':'否'}}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ userDetails.createTime}}</el-descriptions-item>
        <el-descriptions-item label="修改时间">{{ userDetails.editTime }}</el-descriptions-item>
        <el-descriptions-item label="修改者">{{ userDetails.editUser.name }}</el-descriptions-item>
        <el-descriptions-item label="创建者">{{ userDetails.createUser.name }}</el-descriptions-item>
        <el-descriptions-item label="角色">{{ role }}</el-descriptions-item>
  </el-descriptions>
</template>

<script setup>
import { get } from '@/http/http';
import { messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import router from '../router/router'
    let userDetails=ref(null)
    let role=ref('')
    const id=useRoute().params.id
    onMounted(()=>{
        refreshToken()
        get("/user/details",{id:id}).then((reponse)=>{
            console.log('正在发送mydetails请求')
            if(reponse.data.code===200){
                userDetails.value=reponse.data.data
                if(userDetails.value.editUser==null){
                    userDetails.value.editUser={name:'null'}
                }
                if(userDetails.value.createUser==null){
                    userDetails.value.createUser={name:'null'}
                }
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
        get("/role/getRoles",{userId:id}).then((res)=>{
            if(res.data.code===200){
                res.data.data.forEach((value,index) => {
                    if(index!=res.data.data.length-1){
                        role.value+=(value.roleName+',')
                    }else{
                        role.value+=value.roleName
                    }
                });
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    })
</script>

<style>

</style>