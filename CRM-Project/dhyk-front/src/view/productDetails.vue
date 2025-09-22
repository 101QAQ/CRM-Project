<template>
  <el-button
      type="primary"
      text
      @click="router.back()"
    >
    <el-icon><Back /></el-icon>返回
    </el-button>
  <el-descriptions v-if="productDetails"
    :title="productDetails.name+'的详情'"
    direction="vertical"
    border
    style="margin-top: 20px"
  >
        <el-descriptions-item label="产品名">{{ productDetails.name}}</el-descriptions-item>
        <el-descriptions-item label="官方指导起始价">{{productDetails.guidePriceS}}</el-descriptions-item>
        <el-descriptions-item label="官方指导最高价">{{ productDetails.guidePriceE}}</el-descriptions-item>
        <el-descriptions-item label="经销商报价">{{ productDetails.quotation}}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ productDetails.state==0?'在售':'售罄' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ productDetails.createTime}}</el-descriptions-item>
        <el-descriptions-item label="创建人">{{ productDetails.createUserName||''}}</el-descriptions-item>
        <el-descriptions-item label="编辑时间">{{ productDetails.editTime}}</el-descriptions-item>
        <el-descriptions-item label="编辑人">{{ productDetails.editUserName||''}}</el-descriptions-item>
  </el-descriptions>



</template>

<script setup>
import { get } from '@/http/http';
import { messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import router from '@/router/router';

const productId=useRoute().params.id

let productDetails=ref(null)

onMounted(()=>{
    refreshToken()
    get("/product/getProduct",{id:productId}).then((res)=>{
        if(res.data.code===200){
            productDetails.value=res.data.data
        }else{
            messageTip(res.data.msg,'error')
        }
    })
})




</script>

<style>

</style>