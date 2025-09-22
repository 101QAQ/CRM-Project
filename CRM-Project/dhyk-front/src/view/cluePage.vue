<template>
    <el-form :inline="true" :model="formInline.clue" class="demo-form-inline" :rules="rules" ref="formCondition">
        <el-form-item label="负责人">
            <el-select v-model="formInline.clue.ownerId" placeholder="请选择负责人" style="width: 130px">
                <el-option
                    v-for="item in ownerOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="所属活动">
            <el-select v-model="formInline.clue.activityId" placeholder="请选择负所属活动" style="width: 180px">
                <el-option
                    v-for="item in activityOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                />
            </el-select>
        </el-form-item>
        <el-form-item label="线索人姓名" >
            <el-input v-model.trim="formInline.clue.fullName" placeholder="请输入线索人名称" clearable />
        </el-form-item>
        <el-form-item label="手机" prop="phone">
            <el-input v-model.trim="formInline.clue.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
            <el-button type="primary" plain @click="onReset">重置</el-button>
        </el-form-item>
    </el-form>

     <el-button type="primary" @click="addClue" v-if="isPermission(authorityList,'clue:add')">录入线索</el-button>
     <el-button type="success" @click="dialogVisible=true" v-if="isPermission(authorityList,'clue:import')">导入线索(Excel)</el-button>
     <el-button type="danger" @click="deleteBatch" v-if="isPermission(authorityList,'clue:delete')">批量删除</el-button>
    <el-table :data="tableData.list" style="width: 100%" @selection-change="checkedUser">
        <el-table-column type="selection" width="50"/>
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="ownerUser.name" label="负责人"/>
        <el-table-column property="activity.name" label="所属活动"/>
        <el-table-column property="fullName" label="姓名"/>
        <el-table-column property="appellationOD.typeValue" label="称呼"/>
        <el-table-column property="phone" label="手机"/>
        <el-table-column property="weixin" label="微信"/>
        <el-table-column property="needLoanOD.typeValue" label="是否贷款"/>
        <el-table-column property="intentionStateOD.typeValue" label="意向状态"/>
        <el-table-column property="stateOD.typeValue" label="线索状态"/>
        <el-table-column property="sourceOD.typeValue" label="线索来源"/>
        <el-table-column property="nextContactTime" label="下次联系时间"/>
        <el-table-column label="操作" width="230">
            <template #default="scope">
                <el-button type="primary" @click="view(scope.row.id)" v-if="isPermission(authorityList,'clue:view')">详情</el-button>
                <el-button type="success" @click="editOpenDialog(scope.row.id)" v-if="isPermission(authorityList,'clue:edit')">编辑</el-button>
                <el-button type="danger" @click="del(scope.row.id)" v-if="isPermission(authorityList,'clue:delete')" >删除</el-button>
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
    <el-dialog v-model="dialogVisible"  :append-to-body="true" :modal="false" title="导入线索">
        <el-upload
            ref="uploadRef"
            class="upload-demo"
            :http-request="uplodeFile"
            :auto-upload="false">

            <template #trigger>
                <el-button type="primary">选择文件</el-button>
                仅支持后缀名为.xl或.xlsx的文件
            </template>
            <br>
            <el-button type="success" @click="downModel" style="margin-top: 5px;">下载模版</el-button>
            <template #tip>
            <div class="el-upload__tip">
                重要提示
                <ul>
                    <li>上传文件仅支持后缀名为.xl或.xlsx的文件</li>
                    <li>给定Excel文件第一行将视为字段名</li>
                    <li>请确定您的文件大小不超过50MB</li>
                    <li>日起值以文本形式保存，必须符合yyyy-MM-dd格式</li>
                    <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式</li>
                </ul>
            </div>
            </template>
        </el-upload>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">导入</el-button>
            </div>
        </template>
  </el-dialog>
</template>

<script setup>
import { deleteA, get, post } from '@/http/http';
import {  deleteItem, getAuthority, isPermission, messageConfirm, messageTip, refreshToken,deleteBatchUtil} from '@/utils/Myutils';
import { onMounted, reactive, ref } from 'vue';
import router from '@/router/router';
import { mitter } from '@/mitt/mitt';
        let tableData=ref({})
        const uploadRef=ref(null)
        let dialogVisible=ref(false)
        let authorityList=ref([])
        let ownerOptions=ref([])
        let activityOptions=ref([])
        let formInline=reactive({
            clue:{}
        })
        const formCondition=ref(null)
        const rules={
            phone:{pattern:/^(?:(?:\+|00)86)?1[3-9]\d{9}$/,message:'号码格式有误',trigger:'blur'}
        }

        const cluePage=(params)=>{
            get("/clue/cluePage",params).then((reponse)=>{
                if(reponse.data.code===200){
                    tableData.value=reponse.data.data
                    refreshToken()
                }else{
                    messageTip(reponse.data.msg,"error")
                }
            })
        }


        onMounted(async ()=>{
            refreshToken()
            let condition=JSON.stringify(formInline.clue)
            cluePage({condition:condition})
            authorityList.value=await getAuthority()
            get("/user/usersList").then((res)=>{
                if(res.data.code===200){
                    ownerOptions.value=res.data.data
                }else{
                    messageTip(res.data.msg,'error')
                }
            })
            get("/activity/activityList").then((res)=>{
                if(res.data.code===200){
                    activityOptions.value=res.data.data
                }else{
                    messageTip(res.data.msg,'error')
                }
            })
        })


        const onSubmit=()=>{
            formCondition.value.validate((valid)=>{
                if(valid){
                    cluePage({condition:JSON.stringify(formInline.clue)})
                }else{
                    messageTip("数据验证不通过",'error')
                }
            })
        }

        const onReset=()=>{
            formInline.clue={}
            cluePage({condition:JSON.stringify(formInline.clue)})
        }

        const del=(id)=>{
            messageConfirm("确定要删除这个线索吗").then(()=>{
                deleteA("/clue/delete",{id:id}).then((reponse)=>{
                    if(reponse.data.code===200){
                        refreshToken()
                        messageTip("删除成功","success")
                        deleteItem(tableData,id)
                    }else{
                        messageTip(reponse.data.msg,"error")
                    }
                })
            }).catch(()=>{
                messageTip('取消操作')
            })

            
        }

        const addClue=()=>{
            router.push({
                name:'addClue'
            })
        }

        const handleSubmit=()=>{
            uploadRef.value.submit()
        }

        const uplodeFile=(fileO)=>{
            let formDate=new FormData()
            formDate.append("file",fileO.file)
            post("/clue/importClue",formDate).then((reponse)=>{
                if(reponse.data.code===200){
                    messageTip("录入成功","success")
                    refreshToken()
                    dialogVisible.value=false
                    mitter.emit("relode")
                }else{
                    messageTip(reponse.data.msg,"error")
                }
            })
        }

        const downModel=()=>{
            get("/clue/downModel",{},{ responseType: 'blob' }).then((response)=>{
                const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
                const downloadUrl = window.URL.createObjectURL(blob);
                const link = document.createElement('a');
                link.href = downloadUrl;
                const contentDisposition = response.headers['content-disposition'];
                let fileName = 'download.xlsx';
                if (contentDisposition) {
                    const fileNameMatch = contentDisposition.match(/filename=(.+)/);
                    if (fileNameMatch && fileNameMatch.length > 1) {
                        fileName = fileNameMatch[1];
                    }
                }
                link.setAttribute('download', fileName);
                document.body.appendChild(link);
                link.click();
                link.remove();
                window.URL.revokeObjectURL(downloadUrl);
            }).catch(error => {
                messageTip(error,"error")
            })
        }

       

        const view=(id)=>{
            router.push({
                name:'clueDetails',
                params:{
                    id:id
                }
            })
        }

        const editOpenDialog=(id)=>{
            router.push({
                name:'editClue',
                params:{
                    id:id
                }
            })
        }

        let clueIdList=[]

        const checkedUser=(clueList)=>{
            clueIdList=[]
            clueList.forEach(element => {
                clueIdList.push(element.id)
            });
            console.log(clueIdList)
        }

        const deleteBatch=()=>{
            if(clueIdList.length<=0){
                messageTip("请选择要删除的数据","warning")
                return
            }
            messageConfirm("确定要删除这些线索吗").then(()=>{
                let idStr=''
                clueIdList.forEach((e,index)=>{
                    if(index!=clueIdList.length-1){
                        idStr+=(e+',')
                    }else{
                        idStr+=e
                    }
                })
                console.log(idStr)
                deleteA("/clue/deleteBatch",{ids:idStr}).then((response)=>{
                    if(response.data.code===200){
                        deleteBatchUtil(tableData,clueIdList)
                        refreshToken()
                        messageTip("删除成功 成功："+response.data.data+"条数据","success")
                    }else{
                        messageTip(response.data.msg,"error")
                    }
                })
            }).catch(()=>{
                messageTip("取消删除")
            })

            
        }

        const prev=(pageNum)=>{
            cluePage({pageNum:pageNum,condition:JSON.stringify(formInline.clue)})
        }
        const next=(pageNum)=>{
            cluePage({pageNum:pageNum,condition:JSON.stringify(formInline.clue)})
        }
        const current=(pageNum)=>{
            cluePage({pageNum:pageNum,condition:JSON.stringify(formInline.clue)})
        }

</script>

<style scoped>
.el-table {
  font-size: 12px;
}
.el-upload__tip{
    margin-top: 5px;
}
/* 调整表头样式 */
.el-table__header th {
  padding: 4px 0;
  background-color: #f5f7fa;
  font-weight: 500;
  height: 28px;
}

/* 调整单元格样式 */
.el-table__body td {
  padding: 2px 0;
  height: 24px;
}

/* 调整行高 */
.el-table__row {
  height: 28px;
}

/* 调整单元格内文本样式 */
.el-table__cell {
  line-height: 24px;
}

/* 调整选择框样式 */
.el-checkbox__inner {
  width: 14px;
  height: 14px;
}

/* 调整分页样式 */
.el-pagination {
  margin-top: 6px;
  font-size: 12px;
}
</style>