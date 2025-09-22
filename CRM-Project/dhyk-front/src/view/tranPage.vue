<template>

<el-form :inline="true" :model="tranSelect" class="demo-form-inline" :rules="rules" ref="tranFromCondition">
        <el-form-item label="交易流水号">
            <el-input v-model.trim="tranSelect.tranNo" placeholder="请输入交易流水号" clearable />
        </el-form-item>
        <el-form-item label="客户姓名">
            <el-input v-model.trim="tranSelect.customerName" placeholder="请输入客户姓名" clearable />
        </el-form-item>
        <el-form-item label="交易金额" prop="money">
            <el-input v-model.trim="tranSelect.money" placeholder="请输入交易金额"  clearable style="width: 130px" />
        </el-form-item>
        <el-form-item label="创建时间">
            <el-date-picker
                v-model="tranSelect.createTime"
                type="date"
                placeholder="选择创建日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 200px"
            />
        </el-form-item>

        <el-form-item label="预计成交日期">
            <el-date-picker
                v-model="tranSelect.expectedDate"
                type="date"
                placeholder="选择预计成交日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 180px"
            />
        </el-form-item>
       
        <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
            <el-button type="primary" plain @click="onReset">重置</el-button>
        </el-form-item>
    </el-form>

  <el-table :data="tableData.list" style="width: 100%" @selection-change="checkedUser">
        <el-table-column type="selection" width="50"/>
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="tranNo" label="交易流水号"/>
        <el-table-column property="customerName" label="客户姓名"/>
        <el-table-column property="money" label="交易金额"/>
        <el-table-column property="expectedDate" label="预计成交日期"/>
        <el-table-column property="createTime" label="创建时间"/>
        <el-table-column label="操作" width="230">
            <template #default="scope">
                <el-button type="primary" @click="view(scope.row.id)" v-if="isPermission(authorityList,'tran:view')">详情</el-button>
                <el-button type="danger" @click="del(scope.row.id)" v-if="isPermission(authorityList,'tran:delete')">删除</el-button>
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
import { get } from '@/http/http';
import router from '@/router/router';
import { getAuthority, isPermission, messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
    const tranFromCondition=ref(null)


    let tableData=ref({})
    let tranSelect=ref({})
    let tranStageOptions=ref([])
    let authorityList=ref([])

    const rules={
        money:{pattern:/^\d+(\.\d+)?$/,message:'金额格式有误',trigger:'blur'}
    }


    const tranPage=(params)=>{
        get("/tran/tranPage",params).then((res)=>{
            if(res.data.code===200){
                tableData.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    onMounted(async ()=>{
        refreshToken()
        authorityList.value=await getAuthority()
        tranPage({condition:JSON.stringify(tranSelect.value)})
        get("/dicValue/DicValue",{dicCode:'stage'}).then((res)=>{
            if(res.data.code===200){
                tranStageOptions.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })

    })


    const view=(id)=>{
        router.push({
            name:'tranDetails',
            params:{
                id:id
            }
        })
    }
    const editOpenDialog=()=>{

    }
    const del=()=>{

    }

    const onSubmit=()=>{
        tranFromCondition.value.validate((valid)=>{
            if(valid){
                tranPage({condition:JSON.stringify(tranSelect.value)})
            }
        })
    }

    const onReset=()=>{
        tranSelect.value={}
        tranPage({condition:JSON.stringify(tranSelect.value)})
    }
</script>

<style>

</style>