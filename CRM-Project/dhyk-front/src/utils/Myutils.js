import { get } from '@/http/http'
import { ElMessage, ElMessageBox } from 'element-plus'
export function messageTip(msg,type){
    ElMessage({
        message:msg,
        type: type,
        plain: true,
    })
}

export function removeToken(){
    window.localStorage.removeItem("Jwt")
    window.sessionStorage.removeItem("Jwt")
}

export function isFree(){
    const Jwt=window.localStorage.getItem("Jwt")
    if(Jwt){
        get("/user/isFreeLogin").then((response)=>{
            if(response.data.code===200){
                window.location.href='/workbenchMain'
            }
        })
    }
}
export function messageConfirm(msg){
    return ElMessageBox.confirm(
        msg,
        '系统警告提示',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        })
}

export function refreshToken(){
    get('/user/toknRefresh').then((response)=>{
        if(response.data.code!==601){
            messageTip(response.data.msg,"Warning")
        }
    })
}
export function getCunrrentUser(){
    return get("/user/getInfor")
}

export function getToken(){
    let token=window.localStorage.getItem("Jwt")
    if(!token){
        token=window.sessionStorage.getItem("Jwt")
    }
    if(token){
        return token
    }else{
        messageConfirm(response.data.msg+'.token为空，请返回重新登录').then(() => {
            removeToken()
            window.location.href="/"
        })
        .catch(() => {
          messageTip("登录无效","warning")
        })
    }
}

export function getAuthority(){
    return get("/user/getInfor").then((response)=>{
        if(response.data.code===200){
            return response.data.data.principal.authoritiesStr
        }else{
            return []
        }
    })
}

export function isPermission(authorityList,authority){
    let isPermission=false
    authorityList.forEach(element => {
        if(element==authority){
            isPermission=true
        }
    });
    return isPermission

}



export function getCurrentYearMonth(){
  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth() + 1;
  const formattedMonth = month < 10 ? `0${month}` : month;
  return `${year}-${formattedMonth}`;
};

export function deleteItem(tableData,id){
    tableData.value.list.forEach((e,index)=> {
        if(e.id==id){
            tableData.value.list.splice(index,1)
        }
    });
    tableData.value.total-=1
}

export function deleteBatchUtil(tableData,ids){
    ids.forEach(id=>{
        tableData.value.list.forEach((e,index)=> {
            if(e.id==id){
                tableData.value.list.splice(index,1)
                tableData.value.total-=1
            }
        });
    })
}



