<template>
    <el-form :inline="true" :model="dic" class="demo-form-inline" :rules="rules" ref="productConditionRef">
        <el-form-item label="字典类型代码">
            <el-input v-model.trim="dic.typeCode" placeholder="请输入字典类型代码" clearable />
        </el-form-item>
        <el-form-item label="字典值" prop="guidePriceS">
            <el-input v-model.trim="dic.typeValue" placeholder="请输入字典值" clearable />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
            <el-button type="primary" plain @click="onReset">重置</el-button>
        </el-form-item>
    </el-form>

    <div class="activityButton">
        <el-button type="primary" @click="dicRecode={};dialogVisible=true" >添加字典值</el-button>
        <el-button type="danger"  @click="deleteBatch">批量删除</el-button>
    </div>
  <el-table :data="tableData.list" style="width: 100%" @selection-change="checkedUser" >
        <el-table-column type="selection" width="50"/>
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="typeCode" label="字典类型代码"/>
        <el-table-column property="typeValue" label="字典值"/>
        <el-table-column label="操作" width="230">
        <template #default="scope">
            <el-button type="success" @click="editOpenDialog(scope.row.id)" >编辑</el-button>
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


    <el-dialog v-model="dialogVisible"  :append-to-body="true" :modal="false" :title="dicRecode.isEdit?'修改字典值':'新增字典值'">
        <el-form
                ref="recodeDicRef"
                style="max-width: 100%"
                :model="dicRecode"
                status-icon
                :rules="rules"
                label-width="auto"
                class="demo-ruleForm"
                >
                <el-form-item label="字典类型" prop="typeCode">
                    <el-select v-model="dicRecode.typeCode" placeholder="请选择字典类型" style="width: 100%">
                        <el-option v-for="item in dicTypeCodeOption" :key="item.id" :label="item.typeName":value="item.typeCode"/>
                    </el-select>
                </el-form-item>
                <el-form-item label="字典值" prop="typeValue">
                    <el-input v-model="dicRecode.typeValue" placeholder="输入字典值"  type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="字典值描述" prop="remark">
                    <el-input
                        v-model.trim="dicRecode.remark"
                        style="width: 100%"
                        :rows="3"
                        type="textarea"
                        placeholder="请输入字典值描述"
                    />
                </el-form-item>
            </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">
            {{ dicRecode.isEdit ? '更新' : '提交' }}
        </el-button>
      </div>
    </template>
  </el-dialog>

</template>

<script setup>
import { deleteA, get, post, put } from '@/http/http';
import { deleteBatchUtil, messageConfirm, messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
import { mitter } from '@/mitt/mitt';
import { deleteItem } from '../utils/Myutils';

    let dic=ref({})

    let dicRecode=ref({})

    let tableData=ref({})

    let recodeDicRef=ref(null)

    let dialogVisible=ref(false)

    let dicTypeCodeOption=ref([])

    let dicValueIdList=ref([])


    const getDicPage=(params)=>{
        get("/dicValue/dicValuePage",params).then((res)=>{
            if(res.data.code===200){
                tableData.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }



    const rules={
        typeCode:{required:true,message:"不能为空",trigger:'blur'},
        typeValue:[
            {required:true,message:"不能为空",trigger:'blur'},
            {pattern:/^[\u4e00-\u9fa5]+$/,message:'请输入中文',trigger:'blur'}
        ]
    }



    const onReset=()=>{
        dic.value={}
        getDicPage({condition:JSON.stringify(dic.value)})
    }

    const onSubmit=()=>{
        refreshToken()
        getDicPage({condition:JSON.stringify(dic.value)})
    }

    onMounted(()=>{
        refreshToken()
        getDicPage({condition:JSON.stringify(dic.value)})
        get("/dicType/dicTypeList").then((res)=>{
            if(res.data.code===200){
                dicTypeCodeOption.value=res.data.data
            }else{
                messageTip(res.data.msg,'error')
            }
        })

    })


    const prev=(pageNum)=>{
        refreshToken()
        getDicPage({pageNum,condition:JSON.stringify(dic.value)})
    }

    const next=(pageNum)=>{
        refreshToken()
        getDicPage({pageNum,condition:JSON.stringify(dic.value)})
    }

    const current=(pageNum)=>{
        refreshToken()
        getDicPage({pageNum,condition:JSON.stringify(dic.value)})
    }


    let dicValueId
    const editOpenDialog=(id)=>{
        dicValueId=id
        get("/dicValue/dicValue/"+id).then((res)=>{
            if(res.data.code===200){
                dicRecode.value=res.data.data
                dicRecode.value.isEdit=true
                dialogVisible.value=true
            }else{
                messageTip(res.data.msg,'error')
            }
        })
    }

    const del=(id)=>{
        messageConfirm("确定要删除吗?").then(()=>{
            deleteA("/dicValue/delete",{id}).then((res)=>{
                if(res.data.code===200){
                    refreshToken()
                    messageTip("删除成功",'success')
                    deleteItem(tableData,id)
                }else{
                    messageTip(res.data.msg,'error')
                }
            })
        })
    }




    const checkedUser=(dicValue)=>{
        dicValueIdList.value=[]

        dicValue.forEach(element => {
            dicValueIdList.value.push(element.id)
        });
    }

    const deleteBatch=()=>{

        if(dicValueIdList.value.length<=0){
            messageTip("请选择要删除的数据",'warning')
            return
        }

        messageConfirm("确定要删除吗").then(()=>{
            let idStr=''
            dicValueIdList.value.forEach((id,index)=>{
                if(index!=dicValueIdList.value.length-1){
                    idStr+=(id+',')
                }else{
                    idStr+=id
                }
            })
            deleteA("/dicValue/deleteBatch",{ids:idStr}).then((res)=>{
                if(res.data.code===200){
                    refreshToken()
                    messageTip("删除成功,成功"+res.data.data+'条数据','success')
                   deleteBatchUtil(tableData,dicValueIdList.value)
                }else{
                    messageTip(res.data.msg,'error')
                }
            })
        })


    }


    const handleSubmit=()=>{
        recodeDicRef.value.validate((valid)=>{
            if(valid){
                (dicRecode.value.isEdit?put("/dicValue/edit",dicRecode.value):post("/dicValue/add",dicRecode.value)).then((res)=>{
                    if(res.data.code===200){
                        refreshToken()
                        messageTip(dicRecode.value.isEdit?"修改成功":"添加成功",'success')
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
.el-pagination{
    margin-top: 12px;
}
</style>