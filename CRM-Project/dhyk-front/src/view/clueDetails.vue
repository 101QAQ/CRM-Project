<template>
  <el-button
      type="primary"
      text
      @click="router.back()"
    >
    <el-icon><Back /></el-icon>返回
    </el-button>
    <el-descriptions v-if="clueDetails"
    :title="clueDetails.id+'号线索关于'+clueDetails.fullName+'的详情'"
    direction="vertical"
    border
    style="margin-top: 20px"
  >
        <el-descriptions-item label="负责人">{{ clueDetails.ownerUser?.name||''}}</el-descriptions-item>
        <el-descriptions-item label="所属活动">{{clueDetails.activity?.name||''}}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{ clueDetails.fullName}}</el-descriptions-item>
        <el-descriptions-item label="称呼">{{ clueDetails.appellationOD?.typeValue||''}}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ clueDetails.phone }}</el-descriptions-item>
        <el-descriptions-item label="微信">{{ clueDetails.weixin}}</el-descriptions-item>
        <el-descriptions-item label="QQ号">{{ clueDetails.qq}}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ clueDetails.email}}</el-descriptions-item>
        <el-descriptions-item label="年龄">{{clueDetails.age}}</el-descriptions-item>
        <el-descriptions-item label="职业">{{ clueDetails.job}}</el-descriptions-item>
        <el-descriptions-item label="年收入">{{ clueDetails.yearIncome}}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ clueDetails.address}}</el-descriptions-item>
        <el-descriptions-item label="是否贷款">{{ clueDetails.needLoanOD?.typeValue||''}}</el-descriptions-item>
        <el-descriptions-item label="意向状态">{{ clueDetails.intentionStateOD?.typeValue||''}}</el-descriptions-item>
        <el-descriptions-item label="意向产品">{{ clueDetails.intentionProductOD?.name||''}}</el-descriptions-item>
        <el-descriptions-item label="线索状态">{{ clueDetails.stateOD?.typeValue||''}}</el-descriptions-item>
        <el-descriptions-item label="线索来源">{{ clueDetails.sourceOD?.typeValue||''}}</el-descriptions-item>
        <el-descriptions-item label="线索描述">{{ clueDetails.description}}</el-descriptions-item>
        <el-descriptions-item label="下次联系时间">{{ clueDetails.nextContactTime}}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ clueDetails.createTime}}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ clueDetails.createUser?.name||''}}</el-descriptions-item>
        <el-descriptions-item label="编辑时间">{{ clueDetails.editTime}}</el-descriptions-item>
        <el-descriptions-item label="编辑人">{{ clueDetails.editUser?.name||''}}</el-descriptions-item>
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
                placeholder="请输入线索跟踪记录"
            />
        </el-form-item>
        <el-form-item class="button">
            <el-button type="primary" @click="onSubmit">添加</el-button>
            <el-button type="success" @click="onTransfer" v-if="isShowTranser>0">转为客户</el-button>
            <el-button type="primary" plain @click="ruleFormRef.resetFields()">重置</el-button>
        </el-form-item>
    </el-form>
    <el-table :data="tableData.list" style="width: 100%" >
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="noteContent" label="内容"/>
        <el-table-column property="noteWayOD.typeValue" label="跟踪方式"/>
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
                <el-button type="primary" @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">修改</el-button>
            </div>
        </template>
    </el-dialog>



    <el-dialog v-model="customerdialogVisible"  :append-to-body="true" :modal="false" title="线索转客户" center>
        <el-form
                ref="transferRuleFormRef"
                style="max-width: 100%"
                :model="customer"
                status-icon
                :rules="customerRules"
                label-width="auto"
                class="demo-ruleForm"
                >
            <el-form-item label="意向产品" prop="product">
                <el-select v-model="customer.product" placeholder="请选择客户意向产品" style="width: 100%">
                    <el-option
                        v-for="item in intentionProductOptions"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id"
                    />
                </el-select>
            </el-form-item>

            <el-form-item label="客户描述" prop="description">
                <el-input
                    v-model.trim="customer.description"
                    style="width: 100%"
                    :rows="3"
                    type="textarea"
                    placeholder="请输入客户描述"
                />
            </el-form-item>

            <el-form-item label="下次联系时间">
                <el-date-picker
                    v-model="customer.nextContactTime"
                    type="date"
                    placeholder="选择下次联系时间"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    style="width: 100%;"
                />
            </el-form-item>

        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="toTransfer">转换</el-button>
                <el-button type="primary" plain @click="customerdialogVisible=false">取消</el-button>
            </div>
        </template>
    </el-dialog>


</template>

<script setup>
import { deleteA, get, post, put } from '@/http/http';
import router from '@/router/router';
import { deleteItem, messageTip, refreshToken } from '@/utils/Myutils';
import { computed, onMounted, ref } from 'vue';
import { mitter } from '@/mitt/mitt';
import { useRoute } from 'vue-router';
    const ruleFormRef=ref(null)
    const transferRuleFormRef=ref(null)
    const editRuleFormRef=ref(null)
    const clueId=useRoute().params.id 
    let intentionProductOptions=ref([])
    let customer=ref({})
    const rules={
        noteWay:{required:true,message:"跟踪方式不能为空",trigger:'blur'},
        noteContent:[
            {pattern:/^[\u4e00-\u9fa5]+$/,message:'请输入中文',trigger:'blur'},
            {required:true,message:"备注内容不能为空",trigger:'blur'}
        ]
    }
    const editRules={
        noteWay:{required:true,message:"跟踪方式不能为空",trigger:'blur'},
        noteContent:[
            {pattern:/^[\u4e00-\u9fa5]+$/,message:'请输入中文',trigger:'blur'},
            {required:true,message:"备注内容不能为空",trigger:'blur'}
        ]
    }

    const customerRules={
        product:{required:true,message:"跟踪方式不能为空",trigger:'blur'},
        description:[
            {pattern:/^[\u4e00-\u9fa5]+$/,message:'请输入中文',trigger:'blur'},
        ],

    }

    let dialogVisible=ref(false)
    let customerdialogVisible=ref(false)
    let tableData=ref({})
    let des=ref({
        noteWay:'',
        noteContent:''
    })
    let editDes=ref({
        noteWay:'',
        noteContent:''
    })
    let clueDetails=ref({state:1})
    let noteWayOptions=ref([])
    onMounted(()=>{
        get("/clue/clueDetails",{id:clueId}).then((reponse)=>{
            if(reponse.data.code===200){
                clueDetails.value=reponse.data.data
                console.log(clueDetails.value)
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
        get("/dicValue/DicValue",{dicCode:'noteWay'}).then((response)=>{
            if(response.data.code===200){
                noteWayOptions.value=response.data.data
            }else{
                messageTip(response.data.msg,"error")
            }
        })
        clueRemarkPage()
        refreshToken()
    })


    const isShowTranser=computed(()=>{
        if(clueDetails.value.state===null){
            return 1
        }else{
            return clueDetails.value.state
        }
    })

    const onTransfer=()=>{
        get("/product/getProductList").then((reponse)=>{
            if(reponse.data.code===200){
                intentionProductOptions.value=reponse.data.data
                customerdialogVisible.value=true
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
    }


    const toTransfer=()=>{
        transferRuleFormRef.value.validate((valid)=>{
            if(valid){
                customer.value.clueId=clueId
                post("/customer/add",customer.value).then((response)=>{
                    if(response.data.code===200){
                        refreshToken()
                        messageTip("转换成功","success")
                        customerdialogVisible.value=false
                        mitter.emit("relode")
                    }else{
                        messageTip(response.data.msg,"error")
                    }
                })
            }
        })
    }

    const clueRemarkPage=(pageNum)=>{
        get("/clueRemark/clueRemarkPage",{pageNum:pageNum}).then((reponse)=>{
            if(reponse.data.code===200){
                tableData.value=reponse.data.data
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
    }

    let clueRemarkId
    const editOpenDialog=(id)=>{
        clueRemarkId=id
        get("/clueRemark/clueRemarkById",{id:id}).then((reponse)=>{
            if(reponse.data.code===200){
                dialogVisible.value=true
                editDes.value=reponse.data.data
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
    }
    const del=(id)=>{
        deleteA("/clueRemark/delete",{id:id}).then((reponse)=>{
            if(reponse.data.code===200){
                refreshToken()
                messageTip("删除成功",'success')
                deleteItem(tableData,id)
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
    }
    const prev=(pageNum)=>{
        clueRemarkPage(pageNum)
    }
    const next=(pageNum)=>{
        clueRemarkPage(pageNum)
    }
    const current=(pageNum)=>{
        clueRemarkPage(pageNum)
    }
    const onSubmit=()=>{
        ruleFormRef.value.validate((valid)=>{
            if(valid){
                post("/clueRemark/add",{clueId:clueId,noteContent:des.value.noteContent,noteWay:des.value.noteWay}).then((reponse)=>{
                    if(reponse.data.code===200){
                        refreshToken()
                        messageTip("添加成功",'success')
                        ruleFormRef.value.resetFields()
                        mitter.emit("relode")
                    }else{
                        messageTip(reponse.data.msg,"error")
                    }
                })
            }else{
                messageTip("数据验证不通过","warning")
            }
        })
       
    }
    const handleSubmit=()=>{
        editRuleFormRef.value.validate((valid)=>{
            if(valid){
               put("/clueRemark/edit",{id:clueRemarkId,noteContent:editDes.value.noteContent,noteWay:editDes.value.noteWay}).then((reponse)=>{
                    if(reponse.data.code===200){
                        refreshToken()
                        messageTip("修改成功",'success')
                        mitter.emit("relode")
                    }else{
                        messageTip(reponse.data.msg,"error")
                    }
                })
            }else{
                messageTip("数据验证不通过","warning")
            }
        })
    }

</script>

<style scoped>
.el-form{
    margin-top: 10px;
}
</style>