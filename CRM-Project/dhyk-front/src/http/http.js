import { messageConfirm, messageTip, removeToken } from '@/utils/Myutils'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
axios.defaults.baseURL='http://localhost:8080'
export function get(url,params,config = {}){
    return axios.get(url, {
      params: params,
      responseType: config.responseType || 'json'
    })
}
export function post(url,params){
    return axios.post(url,params)
}

export function put(url,params){
    return axios.put(url,params)
}

export function deleteA(url,params){
    return axios.delete(url,{
        params:params
    })
}

axios.interceptors.request.use((config)=>{
    let Jwt=window.sessionStorage.getItem("Jwt")
    if(Jwt){
        config.headers['isRemember']='false'
    }
    if(!Jwt){
        Jwt=window.localStorage.getItem("Jwt")
        if(Jwt){
            config.headers['isRemember']='true'
        }
    }
    config.headers['Authorization'] =  Jwt;
    return config
})
axios.interceptors.response.use((response)=>{
    if(response.data.code>=110002&&response.data.code<=110005){
       messageConfirm(response.data.msg+'.登录状态失效，请返回重新登录').then(() => {
                removeToken()
                window.location.href="/"
            })
            .catch(() => {
              messageTip("登录无效","warning")
            })
    }
    if(response.data.code===601){
        let jwt=window.localStorage.getItem("Jwt")
        if(jwt){
            window.localStorage.setItem("Jwt",response.data.data)
        }else{
            window.sessionStorage.setItem("Jwt",response.data.data)
        }
    }
    return response
})



