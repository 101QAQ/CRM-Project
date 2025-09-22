<template>
     <el-button type="primary" plain @click="addOpenDialog">添加用户</el-button>
     <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    <el-table :data="tableData.list" style="width: 100%" @selection-change="checkedUser">
        <el-table-column type="selection" width="50"/>
        <el-table-column type="index" label="序号" width="65"/>
        <el-table-column property="loginAct" label="账号"/>
        <el-table-column property="name" label="姓名"/>
        <el-table-column property="phone" label="手机"/>
        <el-table-column property="email" label="邮箱"/>
        <el-table-column property="createTime" label="创建时间"/>
        <el-table-column label="操作" width="230">
        <template #default="scope">
            <el-button type="primary" @click="view(scope.row.id)">详情</el-button>
            <el-button type="success" @click="editOpenDialog(scope.row.id)">编辑</el-button>
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
    <el-dialog v-model="dialogVisible"  :append-to-body="true" :modal="false" :title="userForm.user.isEdit?'修改用户':'新增用户'">
        <el-form
                ref="ruleFormRef"
                style="max-width: 100%"
                :model="userForm.user"
                status-icon
                :rules="rules"
                label-width="auto"
                class="demo-ruleForm"
                >
                <el-form-item label="账号" prop="loginAct">
                    <el-input v-model="userForm.user.loginAct" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="密码"  prop="loginPwd">
                    <el-input v-model.trim="userForm.user.loginPwd" type="password" autocomplete="off" />
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                    <el-input v-model="userForm.user.name" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="电话" prop="phone">
                    <el-input v-model="userForm.user.phone" type="number" autocomplete="off" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <el-input v-model="userForm.user.email" type="text" autocomplete="off" />
                </el-form-item>
                <el-form-item label="账号是否未过期" prop="accountNoExpired">
                    <el-select v-model="userForm.user.accountNoExpired" placeholder="Select" style="width: 100%">
                        <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="凭证是否未过期" prop="credentialsNoExpired">
                    <el-select v-model="userForm.user.credentialsNoExpired" placeholder="Select" style="width: 100%">
                        <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="账户是否未锁定" prop="accountNoLocked">
                    <el-select v-model="userForm.user.accountNoLocked" placeholder="Select" style="width: 100%">
                        <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="账户是否启用" prop="accountEnabled">
                    <el-select v-model="userForm.user.accountEnabled" placeholder="Select" style="width: 100%">
                        <el-option
                            v-for="item in options"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value"
                        />
                    </el-select>
                </el-form-item>
            </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">
            {{ userForm.user.isEdit ? '更新' : '提交' }}
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { deleteA, get, post, put } from '@/http/http';
import { deleteItem, messageConfirm, messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, reactive,ref,nextTick} from 'vue';
import router from '../router/router'
import { mitter } from '@/mitt/mitt';
    const dialogVisible = ref(false)
    const ruleFormRef = ref(null) 
    let userCheckedList=[]

    const checkedUser=(user)=>{
        userCheckedList=[]
        console.log(user)
        user.forEach(element => {
           userCheckedList.push(element.id)
        });
    }

   
    let tableData=ref({})
    
    const initUser={
            loginAct:"",
            loginPwd:"",
            name:"",
            phone:'',
            email:"",
            accountNoExpired:0,
            credentialsNoExpired:0,
            accountNoLocked:0,
            accountEnabled:0
        }
    let userForm=reactive({
        user:initUser
    })
    let options=[
        {label:'是',value:1},
        {label:'否',value:0}
    ]

    
    const rules={
        loginAct:[
            {required:true,message:"请输入账户",trigger:'blur'},
        ],
        loginPwd:[
            {min:6,max:16,message:"密码长度为6~16为",trigger:'blur'},
            {required:true,message:"请输入密码",trigger:'blur'}
        ],
        name:[
            {required:true,message:"请输入姓名",trigger:'blur'},
            {pattern:/^[\u4e00-\u9fa5]+$/,message:'请输入中文',trigger:'blur'}
        ],
        phone:[
            {required:true,message:"请输入手机号",trigger:'blur'},
            {pattern:/^(?:(?:\+|00)86)?1[3-9]\d{9}$/,message:'号码不正确',trigger:'blur'},
        ],
        email:[
            {required:true,message:"请输入邮箱",trigger:'blur'},
            {pattern:/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,message:'号码不正确',trigger:'blur'},
        ],
        accountNoExpired:{required:true,message:"账户过期状态不能为空",trigger:'blur'},
        credentialsNoExpired:{required:true,message:"账户凭证过期状态不能为空",trigger:'blur'},
        accountNoLocked:{required:true,message:"账户锁定状态不能为空",trigger:'blur'},
        accountEnabled:{required:true,message:"账户启用状态不能为空",trigger:'blur'},
    }

    const getUserList=(pageNum)=>{
        get('/user/userPage',pageNum).then((reponse)=>{
            if(reponse.data.code==200){
                tableData.value=reponse.data.data
                refreshToken()
                console.log(tableData)
            }else{
                messageTip(reponse.data.msg,'Warning')
            }
        })
    }
    onMounted(()=>{
        getUserList({pageNum:1})
    })

    


    const prev=(current)=>{
        getUserList({pageNum:current})
    }
    const next=(current)=>{
        getUserList({pageNum:current})
    }   
    const current=(current)=>{
        getUserList({pageNum:current})
    }


    const view=(id)=>{
        router.push({name:'userDetails',params:{id:id}})
    }


    const isEditMode = ref(false);
    const currentEditId = ref(null);

    const editOpenDialog=(id)=>{
        dialogVisible.value=true
        get('/user/details',{id:id}).then((reponse)=>{
            if(reponse.data.code===200){
                userForm.user=reponse.data.data
                userForm.user.isEdit=true
                userForm.user.loginPwd=''
                isEditMode.value=true
                currentEditId.value=id
            }else{
                messageTip(reponse.data.msg,"error")
            }
        })
    }

    const addOpenDialog=() => {
        userForm.user = initUser;
        dialogVisible.value = true;
        isEditMode.value=false
    }


    const handleSubmit = () => {
        ruleFormRef.value.validate((valid) => {
            if(valid) {
                const api = isEditMode.value 
                    ? put("/user/edit", {id: currentEditId.value, ...userForm.user})
                    : post("/user/add", userForm.user);
                    
                api.then(response => {
                    if(response.data.code === 200) {
                        mitter.emit("relode");
                        refreshToken();
                        messageTip(isEditMode.value ? "修改成功" : "新增成功", "success");
                        dialogVisible.value = false;
                    }
                });
            }
        });
    }

    const del=(id)=>{
        messageConfirm("确定要删除吗").then(()=>{
            deleteA("/user/delete",{id:id}).then((reponse)=>{
                if(reponse.data.code===200){
                    deleteItem(tableData,id)
                    refreshToken();
                    messageTip("删除成功", "success");
                }else{
                    messageTip(reponse.data.msg,"error")
                }
            })
        }) 
        .catch(()=>{
            messageTip("取消操作")
        })
        
    }

    const delBatch=()=>{

        if(userCheckedList.length<=0){
            messageTip("请选择要删除的数据","warning")
            return
        }

        messageConfirm("确定要删除吗").then(()=>{
            let ids=''
            userCheckedList.forEach((id,index)=>{
                if(index!=userCheckedList.length-1){
                    ids+=(id+',')
                }else{
                    ids+=id
                }
            })
            deleteA("/user/deleteBatch",{ids:ids}).then((reponse)=>{
                if(reponse.data.code===200){
                    mitter.emit("relode");
                    refreshToken();
                    messageTip("删除成功 "+":成功"+reponse.data.data+"条数据", "success");
                }else{
                    messageTip(reponse.data.msg,"error")
                }
            })
        })
        .catch(()=>{
            messageTip("取消操作","warning")
        })
    }
</script>

<style scoped>
.el-pagination{
    margin-top: 12px;
}
</style>