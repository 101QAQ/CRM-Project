<template>
      <el-button
      type="primary"
      text
      @click="router.back()"
    >
    <el-icon><Back /></el-icon>返回
    </el-button>
  <div class="herder"><h1>系统角色</h1></div>


    <div class="activityButton">
        <el-button type="primary" @click="addDialog">新增角色</el-button>
    </div>
  <el-table :data="tableData.list" style="width: 100%" >
        <el-table-column property="role" label="角色"/>
        <el-table-column property="roleName" label="角色名"/>
        <el-table-column label="操作" width="300">
        <template #default="scope">
            <el-button type="primary" @click="view(scope.row.id)">关联权限</el-button>
            <el-button type="success" @click="editOpenDialog(scope.row.id)" >编辑</el-button>
            <el-button type="danger" @click="del(scope.row.id)">删除</el-button>
        </template>
        </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next" 
                :page-size="tableData.pageSize" 
                :total="tableData.total" 
                @prevClick="prev"            
                @nextClick="next"            
                @current-change="current"
                />

    <el-dialog v-model="roleDialogVisible"  :append-to-body="true" :modal="false" :title="role.edit?'修改角色':'添加角色'" center>
        <el-form
                ref="roleFormRef"
                style="max-width: 100%"
                :model="role"
                status-icon
                :rules="rules"
                label-width="auto"
                class="demo-ruleForm"
                >
                <el-form-item label="角色" prop="role">
                    <el-input v-model="role.role" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="角色名" prop="roleName">
                    <el-input v-model="role.roleName" type="text" autocomplete="off" />
                </el-form-item>
            </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="roleDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="addRole" >{{ role.edit?'修改':'添加' }}</el-button>
            </div>
        </template>
  </el-dialog>




  <el-dialog v-model="permissionDialogVisible"  :append-to-body="true" :modal="false" title="关联权限" center>
        <el-form
                ref="permissionFormRef"
                style="max-width: 100%"
                :model="permission"
                status-icon
                :rules="permissionRules"
                label-width="auto"
                class="demo-ruleForm"
                >
                <el-form-item label="权限" prop="id">
                    <el-select v-model="permission.id" placeholder="请选要关联的权限" style="width: 100%">
                        <el-option
                            v-for="item in permissionOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button class="tranStage" type="primary" @click="onRelated" >关联权限</el-button>
                    <el-button type="primary" plain @click="permission={}">重置</el-button>
                </el-form-item>
            </el-form>


            <el-table :data="permissionTableData.list" style="width: 100%" >
                <el-table-column property="name" label="权限名"/>
                <el-table-column label="操作" width="300">
                <template #default="scope">
                    <el-button type="danger" @click="disassociate(scope.row.id)">解除关联</el-button>
                </template>
                </el-table-column>
            </el-table>
            <el-pagination background layout="prev, pager, next" 
                        :page-size="permissionTableData.pageSize" 
                        :total="permissionTableData.total" 
                        @prevClick="permissionPrev"            
                        @nextClick="permissionNext"            
                        @current-change="permissionCurrent"
                        />


        <template #footer>
            <div class="dialog-footer">
                <el-button @click="permissionDialogVisible = false">关闭</el-button>
            </div>
        </template>
  </el-dialog>


</template>

<script setup>
import { deleteA, get, post, put } from '@/http/http';
import { mitter } from '@/mitt/mitt';
import router from '@/router/router';
import { deleteItem, messageConfirm, messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
    const roleFormRef=ref(null)
    let permissionFormRef=ref(null)
    let role=ref({})

    let permission=ref({})

    let roleDialogVisible=ref(false)
    let permissionDialogVisible=ref(false)
    let tableData=ref({})
    let permissionTableData=ref({})
    let permissionOptions=ref([])

    onMounted(()=>{
        refreshToken()
        get("/role/rolePage").then((res)=>{
            if(res.data.code===200){
                tableData.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    })


    const addDialog=()=>{
        role.value={}
        roleDialogVisible.value=true
    }

    const rules={
        role:{required:true,message:"不能为空",trigger:'blur'},
        roleName:[
            {required:true,message:"不能为空",trigger:'blur'},
            {pattern:/^[\u4e00-\u9fa5]+$/,message:'请输入中文',trigger:'blur'}
        ]
    }

    const permissionRules={
        id:{required:true,message:"不能为空",trigger:'blur'},
    }

    const addRole=()=>{
        roleFormRef.value.validate((valid)=>{
            if(valid){
                (role.value.edit?put("/role/edit",role.value):post("/role/add",role.value)).then((res)=>{
                    if(res.data.code===200){
                        refreshToken()
                        messageTip(role.value.edit?'修改成功':'添加成功','success')
                        mitter.emit('relode')
                    }else{
                        messageTip(res.data.msg,'error')
                    }
                })
            }
        })
    }


    const permissionPage=(params) =>{
        get("/permission/isRelated",params).then((res)=>{
            if(res.data.code===200){
                permissionTableData.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    let roleId
     const  view=async (id)=>{
        roleId=id
        permissionPage({roleId:id})
        await get("/permission/notRelated",{roleId:id}).then((res)=>{
            if(res.data.code===200){
                permissionOptions.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
        permissionDialogVisible.value=true
   }
   const editOpenDialog=(id)=>{
        get("/role/getRole",{id:id}).then((res)=>{
            if(res.data.code===200){
                role.value=res.data.data
                role.value.edit=true
                roleDialogVisible.value=true
            }else{
                messageTip(res.data.msg,'error')
            }
        })
   }


   const disassociate=(permissionId)=>{
        messageConfirm("确定要解除关联吗").then(()=>{
            deleteA("/rolePermission/delete",{roleId,permissionId}).then((res)=>{
                if(res.data.code===200){
                    refreshToken()
                    messageTip('解除成功','success')
                    deleteItem(permissionTableData,permissionId)
                }else{
                    messageTip(res.data.msg,'error')
                }
            })
        })
   }

   const onRelated=()=>{
    permissionFormRef.value.validate((valid)=>{
        if(valid){
            post("/rolePermission/add",{permissionId:permission.value.id,roleId}).then((res)=>{
                if(res.data.code===200){
                    refreshToken()
                    messageTip("关联成功",'success')
                    permissionDialogVisible.value=false
                }else{
                    messageTip(res.data.msg,'error')
                }
            })
        }
    })
   }

   const del=()=>{

   }
   const prev=()=>{

   }
   const permissionPrev=(pageNum)=>{
        permissionPage({pageNum,roleId})
   }
   const next=(pageNum)=>{
    
   }

   const permissionNext=(pageNum)=>{
    permissionPage({pageNum,roleId})
   }

   const current=()=>{

   }

   const permissionCurrent=(pageNum)=>{
    permissionPage({pageNum,roleId})
   }

</script>

<style scoped>
.herder{
    text-align: center;
}
.el-table{
    margin-top: 12px;
}
.el-pagination{
    margin-top: 12px;
}
</style>