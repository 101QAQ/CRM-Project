<template>

<el-form :inline="true" :model="product" class="demo-form-inline" :rules="rules" ref="productConditionRef">
        <el-form-item label="产品名">
            <el-input v-model.trim="product.name" placeholder="请输入产品名称" clearable />
        </el-form-item>
        <el-form-item label="官方指导起始价" prop="guidePriceS">
            <el-input v-model.trim="product.guidePriceS" placeholder="请输入官方指导价" clearable />
        </el-form-item>
        <el-form-item label="官方指导最高价" prop="guidePriceE">
            <el-input v-model.trim="product.guidePriceE" placeholder="请输入官方指导最高价" clearable />
        </el-form-item>
        <el-form-item label="经销商报价" prop="quotation">
            <el-input v-model.trim="product.quotation" placeholder="请输入经销商报价" clearable />
        </el-form-item>
        <el-form-item label="销售状态">
            <el-select v-model="product.state" placeholder="请选择状态" style="width: 130px">
                <el-option label="在售":value="0"/>
                <el-option label="售罄":value="1"/>
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
            <el-button type="primary" plain @click="onReset">重置</el-button>
        </el-form-item>
    </el-form>
    <div class="activityButton">
        <el-button type="primary" @click="dialogVisible=true" v-if="isPermission(authorityList,'product:add')">录入产品</el-button>
        <el-button type="danger"  @click="deleteBatch" v-if="isPermission(authorityList,'product:delete')">批量删除</el-button>
    </div>

  <el-table :data="tableData.list" style="width: 100%" @selection-change="checkedUser" >
        <el-table-column type="selection" width="50"/>
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="name" label="产品名称"/>
        <el-table-column property="guidePriceS" label="官方指导价"/>
        <el-table-column property="guidePriceE" label="官方指导最高价"/>
        <el-table-column property="quotation" label="经销商报价"/>
        <el-table-column property="state" label="销售状态">
            <template #default="scope">
                {{ scope.row.state==0?'在售':'售罄' }}
            </template>
        </el-table-column>
        <el-table-column property="createTime" label="创建时间"/>
        <el-table-column label="操作" width="230">
        <template #default="scope">
            <el-button type="primary" @click="view(scope.row.id)" v-if="isPermission(authorityList,'product:view')">详情</el-button>
            <el-button type="success" @click="editOpenDialog(scope.row.id)" v-if="isPermission(authorityList,'product:edit')">编辑</el-button>
            <el-button type="danger" @click="del(scope.row.id)" v-if="isPermission(authorityList,'product:delete')">删除</el-button>
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


    <el-dialog v-model="dialogVisible"  :append-to-body="true" :modal="false" :title="productRecode.isEdit?'修改产品':'新增产品'">
        <el-form
                ref="recodeProductRef"
                style="max-width: 100%"
                :model="productRecode"
                status-icon
                :rules="recodeRules"
                label-width="auto"
                class="demo-ruleForm"
                >
                <el-form-item label="产品名" prop="name">
                    <el-input v-model="productRecode.name" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="官方指导价" prop="guidePriceS">
                    <el-input v-model="productRecode.guidePriceS" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="官方指导最高价" prop="guidePriceE">
                    <el-input v-model="productRecode.guidePriceE" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="经销商报价" prop="quotation">
                    <el-input v-model="productRecode.quotation" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="销售状态" prop="state">
                    <el-select v-model="productRecode.state" placeholder="请选择状态" style="width: 100%">
                        <el-option label="在售":value="0"/>
                        <el-option label="售罄":value="1"/>
                    </el-select>
                </el-form-item>
            </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">
            {{ productRecode.isEdit ? '更新' : '提交' }}
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup>
import { deleteA, get, post, put } from '@/http/http';
import { mitter } from '@/mitt/mitt';
import router from '@/router/router';
import { deleteItem, getAuthority, isPermission, messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
    const productConditionRef=ref(null)

    const recodeProductRef=ref(null)

    let dialogVisible=ref(false)

    let tableData=ref({})

    let product=ref({})

    let authorityList=ref([])

    let productRecode=ref({})

    const rules={
        guidePriceS:{pattern:/^(0|[1-9]\d*)(\.\d{1,2})?$/,message:'必须为数字(保留两位小数)',trigger:'blur'},
        guidePriceE:{pattern:/^(0|[1-9]\d*)(\.\d{1,2})?$/,message:'必须为数字(保留两位小数)',trigger:'blur'},
        quotation:{pattern:/^(0|[1-9]\d*)(\.\d{1,2})?$/,message:'必须为数字(保留两位小数)',trigger:'blur'},
    }


    const recodeRules={
        name:{required:true,message:"请输入产品名",trigger:'blur'},
        guidePriceS:{pattern:/^(0|[1-9]\d*)(\.\d{1,2})?$/,message:'必须为数字(保留两位小数)',trigger:'blur'},
        guidePriceE:{pattern:/^(0|[1-9]\d*)(\.\d{1,2})?$/,message:'必须为数字(保留两位小数)',trigger:'blur'},
        quotation:{pattern:/^(0|[1-9]\d*)(\.\d{1,2})?$/,message:'必须为数字(保留两位小数)',trigger:'blur'},
        state:{required:true,message:"销售状态不能为空",trigger:'blur'}
    }

    const getProductPage=(params)=>{
        get("/product/productPage",params).then((res)=>{
            if(res.data.code===200){
                tableData.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    const checkedUser=()=>{
        
    }


    onMounted(async ()=>{
        refreshToken()
        getProductPage({condition:JSON.stringify(product.value)})
        authorityList.value=await getAuthority()
    })

    const onSubmit=()=>{
        productConditionRef.value.validate((valid)=>{
            if(valid){
                refreshToken()
                getProductPage({condition:JSON.stringify(product.value)})
            }
        })
    }

    const onReset=()=>{
        product.value={}
        getProductPage({condition:JSON.stringify(product.value)})
    }


    const view=(id)=>{
        router.push({
            name:'productDetails',
            params:{
                id
            }
        })
    }

    const del=(id)=>{
        deleteA("/product/delete",{id}).then((res)=>{
            if(res.data.code===200){
                refreshToken()
                messageTip("删除成功",'success')
                deleteItem(tableData,id)
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    const handleSubmit=()=>{
        recodeProductRef.value.validate((valid)=>{
            if(valid){
                (productRecode.value.isEdit?put("/product/edit",productRecode.value):post("/product/add",productRecode.value)).then((res)=>{
                    if(res.data.code===200){
                        refreshToken()
                        messageTip(productRecode.value.isEdit?"修改成功":"录入成功",'success')
                        mitter.emit("relode")
                    }else{
                        messageTip(res.data.msg,'error')
                    }
                })
            }
        })
       
    }

    const deleteBatch=()=>{

    }

    const editOpenDialog=(id)=>{
        get("/product/getProduct",{id}).then((res)=>{
            if(res.data.code===200){
                productRecode.value=res.data.data
                productRecode.value.isEdit=true
                dialogVisible.value=true
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    const prev=(pageNum)=>{
        getProductPage({pageNum,condition:JSON.stringify(product.value)})
    }

    const next=(pageNum)=>{
        getProductPage({pageNum,condition:JSON.stringify(product.value)})
    }

    const current=(pageNum)=>{
        getProductPage({pageNum,condition:JSON.stringify(product.value)})
    }
</script>

<style scoped>
    .el-pagination{
        margin-top: 12px;
    }
</style>