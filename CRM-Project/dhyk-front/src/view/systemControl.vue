<template>
    <h1>{{ systemInfo.title }},{{ systemInfo.version }}</h1>
    <el-collapse v-model="activeNames" @change="handleCollapseChange">
      <el-collapse-item title="系统设置" name="1" @click="systemSettingDialogVisible=true">
      </el-collapse-item>
      <el-collapse-item title="权限管理" name="2" @click="router.push({name:'systemPermission'})">
      </el-collapse-item>
      <el-collapse-item title="关于系统" name="2" @click="systemAboutDialogVisible=true">
    </el-collapse-item>
    </el-collapse>


    <el-dialog v-model="systemAboutDialogVisible"  :append-to-body="true" :modal="false" title="系统信息" center>
        
        <div>
            系统名称：{{systemInfo.title }}
        </div>
        <div>
            {{systemInfo.description}}
        </div>
        <div>
            地址：{{systemInfo.address}}
        </div>
        <div>
            {{ systemInfo.version}}
        </div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="systemAboutDialogVisible = false">确定</el-button>
            </div>
        </template>
  </el-dialog>

  <el-dialog v-model="systemSettingDialogVisible"  :append-to-body="true" :modal="false" title="系统设置" center>
    <el-switch v-model="openSystem" active-text="开启系统"/>
    <el-form
                ref="ruleFormRef"
                style="max-width: 100%"
                :model="systemInfo"
                status-icon
                :rules="rules"
                label-width="auto"
                class="demo-ruleForm"
                >
                <el-form-item label="系统名称" prop="title">
                    <el-input v-model="systemInfo.title" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="联系邮箱" prop="email">
                    <el-input v-model="systemInfo.email" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="公司地址" prop="address">
                    <el-input v-model="systemInfo.address" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="系统描述" prop="description">
                    <el-input
                    v-model.trim="systemInfo.description"
                    style="width: 100%"
                    :rows="3"
                    type="textarea"
                    placeholder="请输入系统描述"
                />
                </el-form-item>
            </el-form>
    <template #footer>
        <div class="dialog-footer">
                <el-button @click="systemSettingDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleSubmit">确定</el-button>
            </div>
    </template>
</el-dialog>

  

</template>

<script setup>
import { get, put } from '@/http/http'
import router from '@/router/router'
import { messageTip, refreshToken } from '@/utils/Myutils'
import { onMounted, ref } from 'vue'
const activeNames = ref([])
let systemAboutDialogVisible=ref(false)
let systemSettingDialogVisible=ref(false)
let systemInfo=ref({})
const ruleFormRef=ref(null)
let openSystem=ref(false)
const handleCollapseChange = (newActiveNames) => {
  activeNames.value = []
}

const rules={
    title:{required:true,message:"不能为空",trigger:'blur'},
    email:{required:true,message:"不能为空",trigger:'blur'},
    address:{required:true,message:"不能为空",trigger:'blur'},
    description:{required:true,message:"不能为空",trigger:'blur'}
}
onMounted(()=>{
    refreshToken()
    get("/system/infor").then((res)=>{
        if(res.data.code===200){
            systemInfo.value=res.data.data
            openSystem.value=(systemInfo.value.isopen==='true')
        }else{
            messageTip(res.data.msg,'error')
        }
    })
})

const handleSubmit=()=>{
    ruleFormRef.value.validate((valid)=>{
        if(valid){
            systemInfo.value.isopen=openSystem.value+''
            put("/system/reset",systemInfo.value).then((res)=>{
                if(res.data.code===200){
                    refreshToken()
                    messageTip("修改成功",'success')
                    systemSettingDialogVisible.value=false
                }else{
                    messageTip(res.data.msg,'error')
                }
            })
        }
    })
}

</script>

<style scoped>
.el-dialog{
    text-align: center;
}

</style>