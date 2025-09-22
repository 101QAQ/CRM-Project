<template>
    <el-button
      type="primary"
      text
      @click="back"
    >
    <el-icon><Back /></el-icon>返回
    </el-button>
    <el-descriptions v-if="userDetails"
    :title="userDetails.name+'的详情'"
    direction="vertical"
    border
    style="margin-top: 20px"
  >
        <el-descriptions-item label="账号">{{ userDetails.loginAct}}</el-descriptions-item>
        <el-descriptions-item label="密码">******</el-descriptions-item>
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
        <el-descriptions-item label="角色">{{ roleStr }}</el-descriptions-item>
  </el-descriptions>



  <el-form
            ref="permissionFormRef"
            style="max-width: 100%"
            :model="role"
            status-icon
            :rules="roleRules"
            label-width="auto"
            class="demo-ruleForm"
            >
            <el-form-item label="角色" prop="id">
                <el-select v-model="role.id" placeholder="请选要关联的角色" style="width: 100%">
                    <el-option
                        v-for="item in roleOptions"
                        :key="item.id"
                        :label="item.roleName"
                        :value="item.id"
                    />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button class="tranStage" type="primary" @click="onRelated" >关联角色</el-button>
                    <el-button type="primary" plain @click="role={}">重置</el-button>
                </el-form-item>
            </el-form>
            <el-table :data="roles" style="width: 100%" >
                <el-table-column property="name" label="角色"/>
                <el-table-column property="roleName" label="角色名"/>
                <el-table-column label="操作" width="300">
                <template #default="scope">
                    <el-button type="danger" @click="disassociate(scope.row.id)">解除关联</el-button>
                </template>
                </el-table-column>
            </el-table>

</template>

<script setup>
import { useRoute } from 'vue-router';
import { computed, ref, watch } from 'vue';
import { deleteA, get, post } from '@/http/http';
import { messageConfirm, messageTip, refreshToken } from '@/utils/Myutils';
import router from '../router/router'
import { mitter } from '@/mitt/mitt';
    const userId=useRoute().params.id
    const userDetails=ref(null)
    const roleRules={
        id:{required:true,message:"不能为空",trigger:'blur'},
    }
    let roleOptions=ref([])
    let roles=ref([])

    let role=ref({})
    let roleStr=ref('')

    get("/user/details",{id:userId}).then((reponse)=>{
        if(reponse.data.code===200){
            userDetails.value=reponse.data.data
            if(userDetails.value.editUser==null){
                userDetails.value.editUser={name:'null'}
            }
            if(userDetails.value.createUser==null){
                userDetails.value.createUser={name:'null'}
            }
            refreshToken()
        }else{
            messageTip(reponse.data.msg,"error")
        }
    })
    
    get("/role/notRelated",{userId}).then((res)=>{
        if(res.data.code===200){
            roleOptions.value=res.data.data
        }
    })

    get("/role/getRoles",{userId}).then((res)=>{
            if(res.data.code===200){
                roles.value=res.data.data
                roles.value.forEach((value,index) => {
                    if(index!=roles.value.length-1){
                        roleStr.value+=(value.roleName+',')
                    }else{
                        roleStr.value+=value.roleName
                    }
                });

            }else{
                messageTip(res.data.msg,'error')
            }
        })
    const back=()=>{
        router.back()
    }

    const onRelated=()=>{
        post("/userRole/add",{userId,roleId:role.value.id}).then((res)=>{
            if(res.data.code===200){
                refreshToken()
                messageTip("关联成功",'success')
                mitter.emit("relode")
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    const disassociate=(roleId)=>{
        messageConfirm("确定解除关联吗").then(()=>{
            deleteA("/userRole/delete",{userId,roleId}).then((res)=>{
                if(res.data.code===200){
                    refreshToken()
                    messageTip("解除成功",'success')
                    mitter.emit("relode")
                }else{
                    messageTip(res.data.msg,'error')
                }
            })
        })
        
    }
</script>

<style scoped>
.el-form{
    margin-top: 12px;
}
</style>