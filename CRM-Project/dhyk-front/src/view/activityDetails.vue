<template>
    <el-button
      type="primary"
      text
      @click="router.back()"
    >
    <el-icon><Back /></el-icon>返回
    </el-button>
    <el-descriptions v-if="activityDetails"
    :title="activityDetails.name+'的详情'"
    direction="vertical"
    border
    style="margin-top: 20px"
  >
        <el-descriptions-item label="负责人">{{ activityDetails.ownerUser.name}}</el-descriptions-item>
        <el-descriptions-item label="活动名称">{{activityDetails.name}}</el-descriptions-item>
        <el-descriptions-item label="活动开始时间">{{ activityDetails.startTime}}</el-descriptions-item>
        <el-descriptions-item label="活动结束时间">{{ activityDetails.endTime}}</el-descriptions-item>
        <el-descriptions-item label="活动预算">{{ activityDetails.cost }}</el-descriptions-item>
        <el-descriptions-item label="活动描述">{{ activityDetails.description}}</el-descriptions-item>
        <el-descriptions-item label="活动创建时间">{{ activityDetails.createTime}}</el-descriptions-item>
        <el-descriptions-item label="活动创建人">{{ activityDetails.createUser.name}}</el-descriptions-item>
        <el-descriptions-item label="活动修改时间">{{activityDetails.editTime}}</el-descriptions-item>
        <el-descriptions-item label="活动修改人">{{ activityDetails.editUser.name}}</el-descriptions-item>
  </el-descriptions>
  <el-form
    ref="ruleFormRef"
    style="max-width: 100%"
    :model="des"
    status-icon
    :rules="rules"
    label-width="auto"
    class="demo-ruleForm"
    >   
        <el-form-item label="活动备注" prop="noteContent">
            <el-input
                v-model.trim="des.noteContent"
                style="width: 100%"
                :rows="3"
                type="textarea"
                placeholder="请输入活动备注"
            />
        </el-form-item>
        <el-form-item class="button">
            <el-button type="primary" @click="onSubmit">添加</el-button>
            <el-button type="primary" plain @click="onReset">重置</el-button>
        </el-form-item>
    </el-form>
    <el-table :data="tableData.list" style="width: 100%" >
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="noteContent" label="内容"/>
        <el-table-column property="createTime" label="备注时间"/>
        <el-table-column property="createUser.name" label="备注人"/>
        <el-table-column property="editTime" label="编辑时间"/>
        <el-table-column property="editUser.name" label="编辑人"/>
        <el-table-column label="操作" width="230">
            <template #default="scope">
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
    <el-dialog v-model="dialogVisible"  :append-to-body="true" :modal="false" title="修改备注">
        <el-form
                ref="editRuleFormRef"
                style="max-width: 100%"
                :model="editDes"
                status-icon
                :rules="editRules"
                label-width="auto"
                class="demo-ruleForm"
                >
            <el-form-item label="账户是否启用" prop="noteContent">
                <el-input
                    v-model.trim="editDes.noteContent"
                    style="width: 100%"
                    :rows="3"
                    type="textarea"
                    placeholder="请输入活动备注"
                />
                </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">修改</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { deleteA, get, post, put } from '@/http/http';
import { deleteItem, messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { mitter } from '@/mitt/mitt';
import router from '../router/router'

    

    const ruleFormRef=ref(null)
    const editRuleFormRef=ref(null)
    let activityDetails=ref(null)
    const route=useRoute()
    const activityId=route.params.id
    let dialogVisible=ref(false)
    let des=ref({
        activityId:activityId,
        noteContent:''
    })

    let editDes=ref({
        id:null,
        noteContent:''
    })

    const rules={
        description:{required:true,message:"备注不能为空",trigger:'blur'},
    }
    const editRules={
        noteContent:{required:true,message:"备注不能为空",trigger:'blur'}
    }
    let tableData=ref({})


    const getRemark=(pageNum)=>{
        get("/activityRemark/remarkPage",{activityId:activityId,pageNum:pageNum}).then((reponse)=>{
            if(reponse.data.code===200){
                tableData.value=reponse.data.data
                tableData.value.list.forEach((remark)=>{
                    if(remark.editUser==null){
                        remark.editUser={name:'null'}
                    }
                    if(remark.createUser==null){
                        remark.createUser={name:'null'}
                    }
                })
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
    }

    onMounted(()=>{
        get("/activity/details",{id:activityId}).then((response)=>{
            if(response.data.code===200){
                activityDetails.value=response.data.data
                if(activityDetails.value.editUser==null){
                    activityDetails.value.editUser={name:null}
                }
                if(activityDetails.value.createUser==null){
                    activityDetails.value.createUser={name:null}
                }
                refreshToken()
            }else{
                messageTip(response.data.msg,"error")
            }
        })
        getRemark()
    })
    const onSubmit=()=>{
        ruleFormRef.value.validate((valid)=>{
            if(valid){
                post("/activityRemark/add",des.value).then((reponse)=>{
                    if(reponse.data.code===200){
                        refreshToken()
                        messageTip("添加成功","success")
                        des.value={}
                        mitter.emit("relode")
                    }else{
                        messageTip(reponse.data.msg,"error")
                    }
                })
            }
        })
    }

    const prev=(pageNum)=>{
        getRemark(pageNum)
    }

    const next=(pageNum)=>{
        getRemark(pageNum)
    }
    const current=(pageNum)=>{
        getRemark(pageNum)
    }
    const onReset=()=>{

    }
    const editOpenDialog=(id)=>{
        editDes.value.id=id
        get("/activityRemark/remark",{id:id}).then((response)=>{
            if(response.data.code===200){
                editDes.value.noteContent=response.data.data.noteContent
            }
        })
        dialogVisible.value=true
    }

    const handleSubmit=()=>{
        editRuleFormRef.value.validate((valid)=>{
            if(valid){
                put("/activityRemark/edit",editDes.value).then((reponse)=>{
                    if(reponse.data.code===200){
                        refreshToken()
                        messageTip("修改成功",'success')
                        mitter.emit("relode")
                    }else{
                        messageTip(reponse.data.msg,"error")
                    }
                })
            }else{
                messageTip("验证不通过",'warning')
            }
        })
    }

    const del=(id)=>{
        deleteA("/activityRemark/delete",{id:id}).then((reponse)=>{
            if(reponse.data.code===200){
                messageTip("删除成功","success")
                deleteItem(tableData,id)
                refreshToken()
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
    }
</script>

<style scoped>
.demo-ruleForm{
    margin-top: 20px;
}
.collapse-container {
  height: 155px; /* 根据需求调整高度 */
  overflow-y: auto; /* 内容超出时显示滚动条（自动模式，无内容时不显示） */
  /* 或使用 overflow-y: scroll; 强制显示滚动条 */
  margin-top: 20px; /* 可选：根据页面布局添加间距 */
}

/* 可选：自定义滚动条样式（非必需，仅美化用） */
.collapse-container::-webkit-scrollbar {
  width: 8px; /* 滚动条宽度 */
}
.collapse-container::-webkit-scrollbar-track {
  background-color: #f0f2f5; /* 轨道背景色 */
}
.collapse-container::-webkit-scrollbar-thumb {
  background-color: #dcdfe6; /* 滚动条滑块颜色 */
  border-radius: 4px; /* 滑块圆角 */
}
</style>