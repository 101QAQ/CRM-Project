<template>
    <el-button
      type="primary"
      text
      @click="router.back()"
    >
    <el-icon><Back /></el-icon>返回
    </el-button>
  <el-descriptions v-if="customerDetails"
    :title="customerDetails.clue.fullName+'的详情'"
    direction="vertical"
    border
    style="margin-top: 20px"
  >
        <el-descriptions-item label="负责人">{{ customerDetails.clue.ownerUser?.name||''}}</el-descriptions-item>
        <el-descriptions-item label="所属活动">{{customerDetails.clue.activity?.name||''}}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ customerDetails.clue.fullName}}</el-descriptions-item>
        <el-descriptions-item label="称呼">{{ customerDetails.clue.appellationOD?.typeValue||''}}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ customerDetails.clue.phone }}</el-descriptions-item>
        <el-descriptions-item label="微信">{{ customerDetails.clue.weixin}}</el-descriptions-item>
        <el-descriptions-item label="QQ号">{{ customerDetails.clue.qq}}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ customerDetails.clue.email}}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{customerDetails.clue.age}}</el-descriptions-item>
        <el-descriptions-item label="职业">{{ customerDetails.clue.job}}</el-descriptions-item>
        <el-descriptions-item label="年收入">{{ customerDetails.clue.yearIncome}}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ customerDetails.clue.address}}</el-descriptions-item>
        <el-descriptions-item label="是否贷款">{{ customerDetails.clue.needLoanOD?.typeValue||''}}</el-descriptions-item>
        <el-descriptions-item label="选购产品">{{ customerDetails.shoppingProduct?.name||''}}</el-descriptions-item>
        <el-descriptions-item label="客户描述">{{ customerDetails.description}}</el-descriptions-item>
        <el-descriptions-item label="下次联系时间">{{ customerDetails.nextContactTime}}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ customerDetails.createTime}}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ customerDetails.createUser?.name||''}}</el-descriptions-item>
        <el-descriptions-item label="编辑时间">{{ customerDetails.editTime}}</el-descriptions-item>
        <el-descriptions-item label="编辑人">{{ customerDetails.editUser?.name||''}}</el-descriptions-item>
  </el-descriptions>

    <el-form
        ref="desFormRef"
        style="max-width: 100%"
        :model="des"
        status-icon
        :rules="rules"
        label-width="auto"
        class="demo-ruleForm"
    >
        <el-form-item label="跟踪方式" prop="noteWay">
            <el-select v-model="des.noteWay" placeholder="请选择跟踪方式" style="width: 100%">
                <el-option
                    v-for="item in noteWayOptions"
                    :key="item.id"
                    :label="item.typeValue"
                    :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="线索跟踪记录" prop="noteContent">
            <el-input
                v-model.trim="des.noteContent"
                style="width: 100%"
                :rows="3"
                type="textarea"
                placeholder="请输入客户跟踪记录"
            />
        </el-form-item>
        <el-form-item class="button">
            <el-button type="primary" @click="onSubmit">添加</el-button>
            <el-button type="success" @click="createTran">创建交易</el-button>
            <el-button type="primary" plain @click="desFormRef.resetFields()">重置</el-button>
        </el-form-item>
    </el-form>

  <el-table :data="tableData.list" style="width: 100%" >
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="noteContent" label="内容"/>
        <el-table-column property="notWayDO.typeValue" label="跟踪方式"/>
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

    

    <el-dialog v-model="remarkdialogVisible"  :append-to-body="true" :modal="false" title="修改跟踪记录">
        <el-form
                ref="editRuleFormRef"
                style="max-width: 100%"
                :model="editDes"
                status-icon
                :rules="rules"
                label-width="auto"
                class="demo-ruleForm"
                >
            <el-form-item label="跟踪方式" prop="noteWay">
                <el-select v-model="editDes.noteWay" placeholder="请选择跟踪方式" style="width: 100%">
                    <el-option
                        v-for="item in noteWayOptions"
                        :key="item.id"
                        :label="item.typeValue"
                        :value="item.id"
                    />
                </el-select>
            </el-form-item>

            <el-form-item label="线索跟踪记录" prop="noteContent">
                <el-input
                    v-model.trim="editDes.noteContent"
                    style="width: 100%"
                    :rows="3"
                    type="textarea"
                    placeholder="请输入记录"
                />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="handleSubmit">修改</el-button>
                <el-button type="primary" plain @click="remarkdialogVisible = false">取消</el-button>
            </div>
        </template>
    </el-dialog>


    <el-dialog v-model="tranDialogVisible"  :append-to-body="true" :modal="false" title="创建交易" center>
        <el-form
                ref="tranRuleFormRef"
                style="max-width: 100%"
                :model="tran"
                :rules="tranRules"
                label-width="auto"
                class="demo-ruleForm"
                >

            
            <el-form-item label="交易金额" prop="money">
                <el-input
                    v-model.trim="tran.money"
                    style="width: 100%"
                    type="text"
                    placeholder="请输入交易金额"
                />
            </el-form-item>

            <el-form-item label="预计成交日期" prop="expectedDate">
                <el-date-picker
                    v-model="tran.expectedDate"
                    style="width: 100%"
                    type="date"
                    placeholder="选择成交日期"
                    value-format="YYYY-MM-DD HH:mm:ss"
                />
            </el-form-item>

            <el-form-item label="交易阶段" prop="stage">
                <el-select v-model="tran.stage" placeholder="请选择交易阶段" style="width: 100%">
                    <el-option
                        v-for="item in tranStageOptions"
                        :key="item.id"
                        :label="item.typeValue"
                        :value="item.id"
                    />
                </el-select>
            </el-form-item>

            <el-form-item label="交易描述">
                <el-input
                    v-model.trim="tran.description"
                    style="width: 100%"
                    :rows="3"
                    type="textarea"
                    placeholder="请输入交易描述"
                />
            </el-form-item>


            <el-form-item label="下次联系时间">
                <el-date-picker
                    v-model="tran.nextContactTime"
                    style="width: 100%"
                    type="date"
                    placeholder="选择下次联系时间"
                    value-format="YYYY-MM-DD HH:mm:ss"
                />
            </el-form-item>


        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" plain @click="tranDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="createTranConfirm">创建</el-button>
            </div>
        </template>
    </el-dialog>



</template>

<script setup>
import { get, put ,deleteA, post} from '@/http/http';
import { messageConfirm, messageTip, refreshToken,deleteItem } from '@/utils/Myutils';
import { effect, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import router from '../router/router'
import { mitter } from '@/mitt/mitt';
    const customerId=useRoute().params.id

    const desFormRef=ref(null)

    const tranRuleFormRef=ref(null)
    let customerDetails=ref(null)
    

    let des=ref({})

    let tran=ref({})

    let remarkdialogVisible=ref(false)
    let tranDialogVisible=ref(false)
    let editDes=ref({})

    let tableData=ref({})

    let noteWayOptions=ref([])
    let tranStageOptions=ref([])
    let rules={
        noteWay:{required:true,message:"跟踪方式不能为空",trigger:'blur'},
        noteContent:[
            
            {pattern:/^[\u4e00-\u9fa5]+$/,message:'请输入中文',trigger:'blur'}
        ]
    }
    
    let tranRules={
        money:[
            {pattern:/^\d+(\.\d+)?$/,message:'金额格式有误',trigger:'blur'},
            {required:true,message:"交易金额不能为空",trigger:'blur'}
        ],
        expectedDate: {required:true,message:"预计成交日期不能为空",trigger:'blur'},
        stage:{required:true,message:"交易员阶段不能为空",trigger:'blur'}
    }

    

    const customerRemarkPage=(pageNum)=>{
        get("/customerRemark/remarkPage",{pageNum,customerId}).then((res)=>{
            if(res.data.code===200){
                tableData.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    onMounted(()=>{
        refreshToken()
        customerRemarkPage()
        get("/customer/details",{id:customerId}).then((res)=>{
            if(res.data.code===200){
                customerDetails.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
        get("/dicValue/DicValue",{dicCode:'noteWay'}).then((response)=>{
            if(response.data.code===200){
                noteWayOptions.value=response.data.data
            }else{
                messageTip(response.data.msg,"error")
            }
        })

    })


    let customerRemarkId
    const editOpenDialog=(id)=>{
        customerRemarkId=id
        get("/customerRemark/remarkDetails",{id:id}).then((res)=>{
            if(res.data.code===200){
                editDes.value=res.data.data
                remarkdialogVisible.value=true
                console.log(editDes.value)
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    const onSubmit=()=>{

        desFormRef.value.validate((valid)=>{
            if(valid){
                post("/customerRemark/add",{customerId,...des.value}).then((res)=>{
                    if(res.data.code===200){
                        refreshToken()
                        messageTip("添加成功",'success')
                        mitter.emit('relode')
                    }else{
                        messageTip(res.data.msg,'error')
                    }
                })
            }else{
                messageTip("数据验证不通过")
            }
        })
        
    }

    const createTran=()=>{
        get('/dicValue/DicValue',{dicCode:'stage'}).then((res)=>{
            if(res.data.code===200){
                tranStageOptions.value=res.data.data
                tranDialogVisible.value=true
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    const createTranConfirm=()=>{
        tranRuleFormRef.value.validate((valid)=>{
            if(valid){
                post("/tran/add",{customerId,...tran.value}).then((res)=>{
                    if(res.data.code===200){
                        refreshToken()
                        messageTip('创建成功','success')
                        tran.value={}
                        tranDialogVisible.value=false
                    }else{
                        messageTip(res.data.msg,'error')
                    }
                })
            }
        })
    }

    const handleSubmit=()=>{
        put("/customerRemark/edit",editDes.value).then((res)=>{
            if(res.data.code===200){
                refreshToken()
                editDes.value={}
                mitter.emit("relode")
                messageTip("修改成功",'success')
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    const del=(id)=>{
        messageConfirm("确定要删除吗").then(()=>{
            deleteA("/customerRemark/delete",{id:id}).then((reponse)=>{
                if(reponse.data.code===200){
                    refreshToken()
                    messageTip("删除成功",'success')
                    deleteItem(tableData,id)
                }else{
                    messageTip(reponse.data.msg,"error")
                }
            })
        })
        
    }




    const prev=()=>{

    }

    const next=()=>{

    }

    const current=()=>{

    }
</script>

<style>

</style>