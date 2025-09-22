<template>
    
    <div></div>
     <el-form
                ref="clueFormRef"
                style="max-width: 100%"
                :model="clue"
                status-icon
                :rules="rules"
                label-width="auto"
                class="demo-ruleForm"
                >
                <el-form-item label="负责人" prop="ownerId" v-if="clueId">
                    <el-select v-model="clue.ownerId" placeholder="请选择负责人" style="width: 100%">
                        <el-option
                            v-for="item in ownerOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="负责人" prop="ownerId" v-else>
                    <el-select v-model="clue.ownerId" placeholder="请选负责人" style="width: 100%" :disabled="true">
                        <el-option
                            v-for="item in ownerOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="所属活动" prop="activityId">
                    <el-select v-model="clue.activityId" placeholder="请选择所属活动" style="width: 100%">
                        <el-option
                            v-for="item in activityOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="姓名">
                    <el-input v-model="clue.fullName" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="称呼">
                    <el-select v-model="clue.appellation" placeholder="请选择称呼" style="width: 100%">
                        <el-option
                            v-for="item in appellationOptions"
                            :key="item.id"
                            :label="item.typeValue"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="clue.phone" type="number" autocomplete="off" />
                </el-form-item>
                <el-form-item label="微信" prop="weixin">
                    <el-input v-model="clue.weixin" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="clue.email" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="年龄" >
                    <el-input v-model="clue.age" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="职业" >
                    <el-input v-model="clue.job" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="年收入" >
                    <el-input v-model="clue.yearIncome" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="地址" >
                    <el-input v-model="clue.address" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="是否贷款">
                    <el-select v-model="clue.needLoan" placeholder="请选择是否贷款" style="width: 100%">
                        <el-option
                            v-for="item in needLoanOptions"
                            :key="item.id"
                            :label="item.typeValue"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="意向状态">
                    <el-select v-model="clue.intentionState" placeholder="请选择意向状态" style="width: 100%">
                        <el-option
                            v-for="item in intentionStateOptions"
                            :key="item.id"
                            :label="item.typeValue"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="意向产品" >
                    <el-select v-model="clue.intentionProduct" placeholder="请选择意向产品" style="width: 100%">
                        <el-option
                            v-for="item in intentionProductOptions"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="线索状态" >
                    <el-select v-model="clue.state" placeholder="请选择线索状态" style="width: 100%">
                        <el-option
                            v-for="item in stateOptions"
                            :key="item.id"
                            :label="item.typeValue"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="线索来源">
                    <el-select v-model="clue.source" placeholder="请选择线索来源" style="width: 100%">
                        <el-option
                            v-for="item in sourceOptions"
                            :key="item.id"
                            :label="item.typeValue"
                            :value="item.id"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="下次联系时间">
                    <el-date-picker
                        v-model="clue.nextContactTime"
                        type="date"
                        placeholder="选择时间"
                        value-format="YYYY-MM-DD HH:mm:ss"
                        style="width: 100%"
                    />
                </el-form-item>
                <el-form-item label="线索描述" prop="description">
                    <el-input
                        v-model="clue.description"
                        style="width: 100%"
                        :rows="10"
                        type="textarea"
                        placeholder="请输入线索描述"
                    />
                </el-form-item>
        </el-form>
        <el-button type="primary" @click="submit">{{ clueId?'修改':'录入' }}</el-button>
        <el-button type="primary" plain @click="back">返回</el-button>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import router from '../router/router'
import { get, post, put } from '@/http/http';
import { getCunrrentUser, messageTip, refreshToken } from '@/utils/Myutils';
import { useRoute } from 'vue-router';
import { mitter } from '@/mitt/mitt';
    const clueFormRef=ref(null)
    const route=useRoute()
    const clueId=route.params.id
    let ownerOptions=ref([])
    let activityOptions=ref([])
    let appellationOptions=ref([])
    let needLoanOptions=ref([])
    let intentionStateOptions=ref([])
    let intentionProductOptions=ref([])
    let stateOptions=ref([])
    let sourceOptions=ref([])
    let clue=ref({})
    let curremtUser=ref(null);
    const rules={
        ownerId:{required:true,message:"负责人不能为空",trigger:'blur'},
        activityId:{required:true,message:"所属活动不能为空",trigger:'blur'},
        phone:[
            {required:true,message:"电话不能为空",trigger:'blur'},
            {pattern:/^(?:(?:\+|00)86)?1[3-9]\d{9}$/,message:'号码格式有误',trigger:'blur'},
            {validator:checkPhone,trigger:'blur'}
        ],
        weixin:{pattern:/^[a-zA-Z][a-zA-Z0-9_-]{5,19}$/,message:'微信号格式有误',trigger:'blur'},
        email:{pattern:/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,message:'邮箱格式有误',trigger:'blur'},
        description:{pattern:/^[\u4e00-\u9fa5]+$/,message:'请输入中文',trigger:'blur'}
    }
    onMounted(async()=>{
        refreshToken()

        if(!clueId){
            await getCunrrentUser().then((result)=>{
                const data=result.data.data
                    if(result.data.code===200){
                        curremtUser=data.principal
                    }else{
                        messageTip(result.data.msg,"error")
                    }
            })
            console.log(curremtUser)
            clue.value.ownerId=curremtUser.id
            console.log(clue.value)
        }

        get("/dicValue/clueDicValue").then((reponse)=>{
            let dicValues=reponse.data.data
            if(reponse.data.code===200){
                appellationOptions.value=dicValues.appellation
                needLoanOptions.value=dicValues.needLoan
                intentionStateOptions.value=dicValues.intentionState
                stateOptions.value=dicValues.state
                sourceOptions.value=dicValues.source
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
        get("/product/getProductList").then((reponse)=>{
            if(reponse.data.code===200){
                intentionProductOptions.value=reponse.data.data
            }else{
                messageTip("数据加载失败","warning")
            }
        })
        get("/activity/activityList").then((reponse)=>{
            if(reponse.data.code===200){
                activityOptions.value=reponse.data.data
            }else{
                messageTip("数据加载失败","warning")
            }
        })
        get("/user/usersList").then((response)=>{
            if(response.data.code===200){
                ownerOptions.value=response.data.data
            }else{
                messageTip("数据加载失败","warning")
            }
        })
        console.log(clueId)
        if(clueId){
            get("/clue/getClue",{id:clueId}).then((reponse)=>{
                if(reponse.data.code===200){
                    clue.value=reponse.data.data
                }else{
                    messageTip("数据加载失败","warning")
                }
            })
        }

    })
    
    function checkPhone(rule,value,callback){
        if(clueId){
            return callback()
        }
        if(value){
            get("/clue/checkPhone",{phone:value}).then((reponse)=>{
                if(reponse.data.code==801){
                    return callback(new Error('改号码已经存在'))
                }else{
                    return callback()
                }
            })
        }else{
            return callback(new Error('请输入号码'))
        }
    }

    
    const submit=()=>{
        clueFormRef.value.validate((valid)=>{
            if(valid){
                (clueId?put("/clue/edit",clue.value):post("/clue/add",clue.value))
                .then((reponse)=>{
                    if(reponse.data.code===200){
                        refreshToken()
                        messageTip(clueId?"修改成功":"录入成功","success")
                        if(clueId){
                            router.back()
                        }else{
                            mitter.emit("relode")
                        }
                    }else{
                        messageTip(reponse.data.msg,"error")
                    }
                })
            }
        })
    }

    const back=()=>{
        router.back()
    }
   
</script>

<style>

</style>