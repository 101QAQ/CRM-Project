<template>
     <el-form :inline="true" :model="formInline.activity" class="demo-form-inline" :rules="rules" ref="formCondition">
        <el-form-item label="负责人">
            <el-select v-model="formInline.activity.ownerId" placeholder="请选择负责人" style="width: 130px">
                <el-option
                    v-for="item in ownerOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="活动名称">
            <el-input v-model.trim="formInline.activity.name" placeholder="请输入活动名称" clearable />
        </el-form-item>
        <el-form-item label="活动开始时间">
            <el-date-picker
                v-model="formInline.activity.startTime"
                type="date"
                placeholder="选择开始日期"
                value-format="YYYY-MM-DD HH:mm:ss"
            />
        </el-form-item>
        <el-form-item label="活动结束时间">
            <el-date-picker
                v-model="formInline.activity.endTime"
                type="date"
                placeholder="选择结束日期"
                value-format="YYYY-MM-DD HH:mm:ss"
            />
        </el-form-item>
        <el-form-item label="预算" prop="cost">
            <el-input v-model.trim="formInline.activity.cost" placeholder="请输入活动预算" clearable />
        </el-form-item>
        <el-form-item label="创建时间">
            <el-date-picker
                v-model="formInline.activity.createTime"
                type="date"
                placeholder="选择创建日期"
                value-format="YYYY-MM-DD HH:mm:ss"
            />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
            <el-button type="primary" plain @click="onReset">重置</el-button>
        </el-form-item>
    </el-form>
    <div class="activityButton">
        <el-button type="primary" @click="addActivity" v-if="isPermission(authorityList,'activity:add')">录入市场活动</el-button>
        <el-button type="danger"  @click="deleteBatch" v-if="isPermission(authorityList,'activity:delete')">批量删除</el-button>
    </div>
    <el-table :data="tableData.list" style="width: 100%" @selection-change="checkedUser" >
        <el-table-column type="selection" width="50"/>
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="ownerUser.name" label="负责人"/>
        <el-table-column property="name" label="活动名"/>
        <el-table-column property="startTime" label="活动开始时间"/>
        <el-table-column property="endTime" label="活动结束时间"/>
        <el-table-column property="cost" label="预算"/>
        <el-table-column property="createTime" label="创建时间"/>
        <el-table-column label="操作" width="230">
        <template #default="scope">
            <el-button type="primary" @click="view(scope.row.id)" v-if="isPermission(authorityList,'activity:view')">详情</el-button>
            <el-button type="success" @click="editOpenDialog(scope.row.id)"v-if="isPermission(authorityList,'activity:edit')" >编辑</el-button>
            <el-button type="danger" @click="del(scope.row.id)" v-if="isPermission(authorityList,'activity:delete')">删除</el-button>
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
</template>

<script setup>
import { deleteA, get } from '@/http/http';
import { getAuthority, messageTip, refreshToken ,isPermission, deleteBatchUtil} from '@/utils/Myutils';
import { onMounted, reactive, ref } from 'vue';
import router from '@/router/router';
import { deleteItem } from '../utils/Myutils';
    let formCondition=ref(null)
    let tableData=ref({})
    let authorityList=ref([])
    let formInline=reactive({
        activity:{
            ownerId:null,
            name:null,
            startTime:null,
            endTime:null,
            cost:null,
            createTime:null,
        }
    })

    let ownerOptions=ref([])
    const rules={
        cost:[
            {pattern:/^(0|[1-9]\d*)(\.\d{1,2})?$/,message:'必须为数字(两位小数)',trigger:'blur'},
        ]
    }

    const activityPage=(params)=>{
        get("/activity/activityPage",params).then((response)=>{
            if(response.data.code===200){
                tableData.value=response.data.data
                refreshToken()
            }else{
                messageTip("数据加载失败,"+response.data.msg,"error")
            }
        })
    }

    const addActivity=()=>{
        router.push({
            name:'addActivity'
        })
    }

    const ownerUser=()=>{
        get("/user/usersList").then((reponse)=>{
            if(reponse.data.code===200){
                ownerOptions.value=reponse.data.data
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
    }

    onMounted(async ()=>{
        const jsonActivity=JSON.stringify(formInline.activity)
        activityPage({condition:jsonActivity})
        ownerUser()
        authorityList.value=await getAuthority()
    })



    let activityIdList=[]

    const checkedUser=(activityList)=>{
        activityIdList=[]
        activityList.forEach(element => {
            activityIdList.push(element.id)
        });
        console.log(activityIdList)
    }
    const view=(id)=>{
        router.push({
            name:'activityDetails',
            params:{
                id:id
            }
        })
    }
    const editOpenDialog=(id)=>{
        router.push({
            name:'editActivity',
            params:{
                id:id
            }
        })
    }

    const deleteBatch=(activityIdList)=>{
        if(activityIdList.length<=0){
            messageTip("请选择要删除的数据",'warning')
            return
        }
        let idStr=''
        activityIdList.forEach((e,index)=>{
            if(index!=clueIdList.length-1){
                idStr+=(e+',')
            }else{
                idStr+=e
            }
        })
        deleteA("/activity/deleteBatch",{ids:idStr}).then((response)=>{
            if(response.data.code===200){
                refreshToken()
                messageTip("删除成功",'success')
                deleteBatchUtil(tableData,activityIdList)
            }else{
                messageTip(response.data.msg,"error")
            }
        })
    }

    const del=(id)=>{
        deleteA("/activity/delete",{id:id}).then((response)=>{
            if(response.data.code===200){
                messageTip("删除成功",'success')
                deleteItem(tableData,id)
            }else{
                messageTip(response.data.msg,"error")
            }
        })
    }
    const onSubmit=()=>{
        const jsonActivity=JSON.stringify(formInline.activity)
        activityPage({condition:jsonActivity})
    }
    const onReset=()=>{
        formInline.activity={}
    }
    const prev=(pageNum)=>{
        const jsonActivity=JSON.stringify(formInline.activity)
        activityPage({pageNum:pageNum,condition:jsonActivity})
    }
    const next=(pageNum)=>{
        const jsonActivity=JSON.stringify(formInline.activity)
        activityPage({pageNum:pageNum,condition:jsonActivity})
    }
    const current=(pageNum)=>{
        const jsonActivity=JSON.stringify(formInline.activity)
        activityPage({pageNum:pageNum,condition:jsonActivity})
    }
</script>

<style scoped>
.el-table{
    margin-top: 12px;
}
.activityButton{
    margin-top: 12px;
}
.el-pagination{
    margin-top: 12px;
}
</style>