<template>
        <el-form
                ref="ruleFormRef"
                style="max-width: 100%"
                :model="activity"
                status-icon
                :rules="rules"
                label-width="auto"
                class="demo-ruleForm"
            >
        <el-form-item label="负责人" prop="ownerId">
            <el-select v-model="activity.ownerId" placeholder="请选择负责人" >
                <el-option
                    v-for="item in ownerOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="活动名称" prop="name">
            <el-input v-model.trim="activity.name" placeholder="请输入活动名称" clearable />
        </el-form-item>
        <el-form-item label="活动开始时间" prop="startTime">
            <el-date-picker
                v-model="activity.startTime"
                type="date"
                placeholder="选择开始日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
            />
        </el-form-item>
        <el-form-item label="活动结束时间" prop="endTime">
            <el-date-picker
                v-model="activity.endTime"
                type="date"
                placeholder="选择结束日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
            />
        </el-form-item>
        <el-form-item label="预算" prop="cost">
            <el-input v-model.trim="activity.cost" placeholder="请输入活动预算" clearable />
        </el-form-item>
        <el-form-item label="活动描述">
            <el-input
                v-model="activity.description"
                style="width: 100%"
                :rows="10"
                type="textarea"
                placeholder="请输入活动描述"
            />
        </el-form-item>
        <el-form-item class="button">
            <el-button type="primary" @click="onSubmit">{{ activityId?'修改':'录入' }}</el-button>
            <el-button type="primary" plain @click="onBack">返回</el-button>
        </el-form-item>
    </el-form>
</template>

<script setup>
import { get, post, put } from '@/http/http';
import router from '@/router/router';
import { messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
    const ruleFormRef=ref(null)
    const route=useRoute()
    const activityId =route.params.id
    let activity=ref({
        ownerId:null,
        name:null,
        startTime:null,
        endTime:null,
        cost:null,
        description:null
    })
    let ownerOptions=ref([])
    const rules={
        ownerId:{required:true,message:"请选择负责人",trigger:'blur'},
        name:{required:true,message:"请输入活动名",trigger:'blur'},
        startTime:[
            {required:true,message:"请选择开始日期",trigger:'blur'},
            {validator:checkDateTime,trigger:'blur'}
        ],
        endTime:[
            {required:true,message:"请选择结束日期",trigger:'blur'},
            {validator:checkDateTime,trigger:'blur'}
        ],
        cost:[
            {required:true,message:"请输入活动预算",trigger:'blur'},
            {pattern:/^(0|[1-9]\d*)(\.\d{1,2})?$/,message:'必须为数字(两位小数)',trigger:'blur'}
        ],
    }

    function checkDateTime(rule,value,callback){
        if(value){
            if(rule.field=='endTime'){
                if(activity.value.startTime >=value){
                    return callback(new Error('结束日期不能比开始日期小'))
                }else{
                    return callback()
                }
            }else{
                if(activity.value.endTime <=value){
                    return callback(new Error('开始日期不能比结束日期大'))
                }else{
                    return callback()
                }
            }
        }else{
            return callback(new Error('请输入号码'))
        }
    }


    onMounted(()=>{
        get("/user/usersList").then((reponse)=>{
            if(reponse.data.code===200){
                ownerOptions.value=reponse.data.data
                refreshToken()
            }else{
                messageTip(reponse.data.msg,'warning')
            }
        })
        if(activityId){
           get("/activity/details",{id:activityId}).then((response)=>{
                if(response.data.code===200){
                    activity.value=response.data.data
                }else{
                    messageTip(response.data.msg,"error")
                }
           }) 
        }
    })
    const onSubmit=()=>{
        ruleFormRef.value.validate((valid)=>{
            if(valid){
               (activityId?put("/activity/edit",activity.value):post("/activity/add",activity.value))
               .then((reponse)=>{
                    if(reponse.data.code===200){
                        messageTip(activityId?"修改成功":"录入成功",'success')
                        refreshToken()
                        activity.value={}
                    }else{
                        messageTip(reponse.data.msg,"error")
                    }
                })
            }else{
                messageTip("表单数据验证不通过",'warning')
            }
        })
    }
    const onBack=()=>{
        router.back()
    }


</script>

<style scoped>
.button{
    margin-top: 12px;
    margin-left: 20px;
}
</style>