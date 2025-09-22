<template>
    <el-button type="primary" @click="exportCustomer" v-if="isPermission(authorityList,'customer:export')">导出客户(Excel)</el-button>
    <el-button type="success" @click="exportCustomerSeleted">选择导出客户(Excel)</el-button>
    <el-table :data="tableData.list" style="width: 100%" @selection-change="checkedClue">
        <el-table-column type="selection" width="50"/>
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="clue.ownerUser.name" label="负责人"/>
        <el-table-column property="clue.activity.name" label="所属活动"/>
        <el-table-column property="clue.fullName" label="姓名"/>
        <el-table-column property="clue.appellationOD.typeValue" label="称呼"/>
        <el-table-column property="clue.phone" label="手机"/>
        <el-table-column property="clue.weixin" label="微信"/>
        <el-table-column property="clue.needLoanOD.typeValue" label="是否贷款"/>
        <el-table-column property="clue.intentionStateOD.typeValue" label="意向状态"/>
        <el-table-column property="clue.stateOD.typeValue" label="线索状态"/>
        <el-table-column property="clue.sourceOD.typeValue" label="线索来源"/>
        <el-table-column property="clue.nextContactTime" label="下次联系时间"/>
        <el-table-column label="操作" width="100">
            <template #default="scope">
                <el-button type="primary" @click="view(scope.row.id)" v-if="isPermission(authorityList,'customer:view')">详情</el-button>
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
import { getAuthority, getToken,  isPermission,  messageTip, refreshToken } from '@/utils/Myutils';
import axios from 'axios';
import { onMounted, ref } from 'vue';


let tableData=ref({})
let customerId=[]
let authorityList=ref([])
const prev=(pageNum)=>{
    customerPage(pageNum)
}
const next=(pageNum)=>{
    customerPage(pageNum)
}
const current=(pageNum)=>{
    customerPage(pageNum)
}


const customerPage=(pageNum)=>{
    get("/customer/customerPage",{pageNum:pageNum}).then((response)=>{
        if(response.data.code===200){
            tableData.value=response.data.data
        }else{
            messageTip(response.data.msg,"error")
        }
    })
}


const exportCus=(ids)=>{
    let iframe=document.createElement('iframe')
    let token=getToken()
    if(token){
       iframe.src=axios.defaults.baseURL+'/'+'customer/exportExcel?Authorization='+token
       if(ids){
            iframe.src=axios.defaults.baseURL+'/'+'customer/exportExcel?Authorization='+token+'&ids='+ids
        }
    }
    iframe.style.display='none'
    document.body.appendChild(iframe)
    refreshToken()
}

const checkedClue=(customer)=>{
    customerId=[]
    customer.forEach(element => {
        customerId.push(element.id)
    });
}

const exportCustomer=()=>{
    exportCus()
}

const view=(id)=>{
    router.push({
        name:'customerDetails',
        params:{
            id:id
        }
    })
}

const exportCustomerSeleted=()=>{

    if(customerId.length<=0){
        messageTip("请选择要导出的数据","warning")
        return
    }

    let ids=""
    customerId.forEach((element,index)=>{
        if(index<customerId.length-1){
            ids+=(element+',')
        }else{
            ids+=element
        }
    })
    exportCus(ids)
}

onMounted(async()=>{
    refreshToken()
    customerPage()
    authorityList.value=await getAuthority()
})

</script>

<style scoped>
.el-table {
  font-size: 13px;
}
</style>