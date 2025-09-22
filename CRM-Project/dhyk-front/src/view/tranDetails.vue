<template>
      <el-button
      type="primary"
      text
      @click="router.back()"
    >
    <el-icon><Back /></el-icon>返回
    </el-button>
  <el-descriptions v-if="tranDetails"
    :title="tranDetails.tranNo+'订单的详情'"
    direction="vertical"
    border
    style="margin-top: 20px"
  >
        <el-descriptions-item label="订单号">{{ tranDetails.tranNo }}</el-descriptions-item>
        <el-descriptions-item label="客户姓名">{{ tranDetails.customerName}}</el-descriptions-item>
        <el-descriptions-item label="交易金额">{{ tranDetails.money}}</el-descriptions-item>
        <el-descriptions-item label="预计成交日期">{{ tranDetails.expectedDate}}</el-descriptions-item>
        <el-descriptions-item label="职业">{{ tranDetails.description}}</el-descriptions-item>
        <el-descriptions-item label="交易阶段">{{ tranDetails.stageValue}}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ tranDetails.createTime}}</el-descriptions-item>
        <el-descriptions-item label="下次联系时间">{{ tranDetails.nextContactTime}}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ tranDetails.createUserName}}</el-descriptions-item>
        <el-descriptions-item label="编辑时间">{{ tranDetails.editTime}}</el-descriptions-item>
        <el-descriptions-item label="编辑人">{{ tranDetails.editUserName}}</el-descriptions-item>
  </el-descriptions>
  <el-divider content-position="center">交易历史记录</el-divider>
  <el-form :model="tranStage" class="demo-form-inline" :rules="rules" ref="tranStageRef">
        <el-form-item label="交易进程" prop="stage">
            <el-select v-model="tranStage.stage" placeholder="请选择交易阶段" style="width: 100%">
                <el-option
                    v-for="item in tranStageOptions"
                    :key="item.id"
                    :label="item.typeValue"
                    :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="交易金额" prop="money">
            <el-input v-model.trim="tranStage.money" placeholder="请输入交易金额"  clearable style="width: 100%" />
        </el-form-item>
        <el-form-item label="预计成交日期">
            <el-date-picker
                v-model="tranStage.expectedDate"
                type="date"
                placeholder="选择预计成交日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
            />
        </el-form-item>
        <el-form-item>
            <el-button class="tranStage" type="primary" @click="onSubmit" >修改交易进程</el-button>
            <el-button type="primary" plain @click="tranStage={}">重置</el-button>
        </el-form-item>
    </el-form>
    <el-table :data="tableData.list" style="width: 100%" >
        <el-table-column property="stageValue" label="交易阶段"/>
        <el-table-column property="money" label="交易金额"/>
        <el-table-column property="expectedDate" label="预计成交日期"/>
        <el-table-column property="createTime" label="创建时间"/>
        <el-table-column property="createUserName" label="创建者"/>
        <el-table-column label="操作" width="230">
            <template #default="scope">
                <el-button type="danger" @click="del(scope.row.id)" >删除</el-button>
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
  
    
    <el-divider content-position="center">交易跟踪记录</el-divider>

    <el-form :model="tranTrace" class="demo-form-inline" :rules="tranTraceRules" ref="tranTraceRef">
        <el-form-item label="跟踪方式" prop="noteWay">
            <el-select v-model="tranTrace.noteWay" placeholder="请选择跟踪方式" style="width: 100%">
                <el-option
                    v-for="item in tranTraceOptions"
                    :key="item.id"
                    :label="item.typeValue"
                    :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="交易跟踪记录">
            <el-input
                    v-model.trim="tranTrace.noteContent"
                    style="width: 100%"
                    :rows="3"
                    type="textarea"
                    placeholder="请输入记录"
                />
        </el-form-item>
        <el-form-item>
            <el-button class="tranStage" type="primary" @click="onSubmitTrace" >添加跟踪记录</el-button>
            <el-button type="primary" plain @click="tranTrace={}">重置</el-button>
        </el-form-item>
    </el-form>
    
    <el-table :data="tableDataTrace.list" style="width: 100%" >
        <el-table-column property="noteWayName" label="跟踪方式"/>
        <el-table-column property="noteContent" label="跟踪内容"/>
        <el-table-column property="createTime" label="跟踪时间"/>
        <el-table-column property="createUserName" label="跟踪人"/>
        <el-table-column property="editTime" label="编辑时间"/>
        <el-table-column property="editUserName" label="编辑人"/>
        <el-table-column label="操作" width="230">
            <template #default="scope">
                <el-button type="danger" @click="del(scope.row.id)" >删除</el-button>
            </template>
        </el-table-column>
    </el-table>
    <el-pagination background layout="prev, pager, next" 
                :page-size="tableDataTrace.pageSize" 
                :total="tableDataTrace.total" 
                @prevClick="prev"            
                @nextClick="next"            
                @current-change="current"
                />
</template>

<script setup>
    import { get, post, put } from '@/http/http';
import { messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import router from '@/router/router';
import { mitter } from '@/mitt/mitt';
    const tranId=useRoute().params.id
    let tranDetails=ref(null)
    let tranStageRef=ref(null)
    let tranTraceRef=ref(null)
    let tranStageOptions=ref([])
    let tranStage=ref({})
    let tranTrace=ref({})
    let tranTraceOptions=ref([])
    let tableData=ref({})
    let tableDataTrace=ref({})

    const rules={
        stage:{required:true,message:"交易阶段不能为空",trigger:'blur'},
        money:{pattern:/^\d+(\.\d+)?$/,message:'金额格式有误',trigger:'blur'}
    }

    const tranTraceRules={
        noteWay:{required:true,message:"跟踪不能为空",trigger:'blur'}
    }

    const tranHistoryPage=(pageNum)=>{
        get("/tranHistory/historyPage",{tranId,pageNum}).then((res)=>{
            if(res.data.code===200){
                tableData.value=res.data.data
            }else{
                messageTip(res.data.msgm,'error')
            }
        })
    }

    const getDicValue=(dicCode,option)=>{
        get("/dicValue/DicValue",{dicCode}).then((res)=>{
            if(res.data.code===200){
                option.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    const tranTracePage=(pageNum)=>{
        get("/tranRemark/tranRemakePage",{tranId,pageNum}).then((res)=>{
            if(res.data.code===200){
                tableDataTrace.value=res.data.data
            }else{
                messageTip(res.data.msgm,'error')
            }
        })
    }

    onMounted(()=>{
        refreshToken()
        tranHistoryPage()
        tranTracePage()
        get("/tran/details",{id:tranId}).then((res)=>{
            if(res.data.code===200){
                tranDetails.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
       getDicValue('stage',tranStageOptions)
       getDicValue('noteWay',tranTraceOptions)
       
    })

    const onSubmit=()=>{
        tranStageRef.value.validate((valid)=>{
            if(valid){
                put('/tran/editStage',{id:tranId,...tranStage.value}).then((res)=>{
                    if(res.data.code===200){
                        refreshToken()
                        messageTip("修改成功",'success')
                        mitter.emit("relode")
                    }else{
                        messageTip(res.data.msg,'error')
                    }
                })
            }
        })
    }


    const onSubmitTrace=()=>{
        tranTraceRef.value.validate((valid)=>{
            if(valid){
                post("/tranRemark/add",{tranId,...tranTrace.value}).then((res)=>{
                    if(res.data.code===200){
                        refreshToken()
                        messageTip("添加成功",'success')
                        mitter.emit("relode")
                    }else{
                        messageTip(res.data.msg,'error')
                    }
                })
            }
        })
        
    }

</script>

<style scoped>
.el-form{
    margin-top: 12px;
}
</style>