<template>
  <el-row>
    <el-col :span="6">
      <el-statistic :value="summaryStatistic.heldActivityAndSum">
        <template #title>
          <div style="display: inline-flex; align-items: center">
            市场活动
          </div>
        </template>
      </el-statistic>
    </el-col>
    <el-col :span="6">
        <el-statistic title="线索总数" :value="summaryStatistic.clueSum" />
    </el-col>
    <el-col :span="6">
      <el-statistic title="客户总数" :value="summaryStatistic.customerSum" />
    </el-col>
    <el-col :span="6">
        <el-statistic :value="summaryStatistic.transactionSum">
        <template #title>
            <div style="display: inline-flex; align-items: center">
                交易总额
            </div>
        </template>
      </el-statistic>
    </el-col>
  </el-row>
    <div class="chart-container">
        <div id="summaryFunnel" style="width: 50%;height:400px;"></div>
        <div id="summaryPie" style="width: 50%;height:400px;"></div>
    </div>

    <div class="block">
        <span class="demonstration">年份</span>
            <el-date-picker
                v-model="year"
                type="year"
                placeholder="请选择年份"
                value-format="YYYY"
                @blur="activityMonthCategory(year)"
            />
        <div id="activityMonthCategory" style="width:100%;height:400px;"></div>
    </div>


    <div class="block">
        <span class="demonstration">月份</span>
        <el-date-picker
          v-model="clueYearMonth"
          type="month"
          placeholder="请选择月份"
          value-format="YYYY-MM"
          @blur="clueDayCategory(clueYearMonth)"
        />
        <div id="clueDayCategory" style="width:100%;height:400px;"></div>
    </div>
    <div class="block">
        <span class="demonstration">月份</span>
        <el-date-picker
          v-model="customerYearMonth"
          type="month"
          placeholder="请选择月份"
          value-format="YYYY-MM"
          @blur="customerDayCategory(customerYearMonth)"
        />
        <div id="customerDayCategory" style="width:100%;height:400px;"></div>
    </div>

    <div class="block">
        <span class="demonstration">月份</span>
        <el-date-picker
          v-model="tranYearMonth"
          type="month"
          placeholder="请选择月份"
          value-format="YYYY-MM"
          @blur="tranDayCategory(tranYearMonth)"
        />
        <div id="tranDayCategory" style="width:100%;height:400px;"></div>
    </div>

</template>

<script setup>
import { get } from '@/http/http';
import { getCurrentYearMonth, messageTip, refreshToken } from '@/utils/Myutils';
import { onMounted, ref } from 'vue';
import * as echarts from 'echarts';

    let summaryStatistic=ref({})
    let year=ref('')
    let clueYearMonth=ref(getCurrentYearMonth())
    let customerYearMonth=ref(getCurrentYearMonth())
    let tranYearMonth=ref(getCurrentYearMonth())
    onMounted(()=>{
        refreshToken()
        get("/summary/summaryData").then((res)=>{
            if(res.data.code===200){
                summaryStatistic.value=res.data.data
            }else{
                messageTip(res.data.msg,"error")
            }
        })

        loadFunnelData()
        lodePieData()
        activityMonthCategory()
        clueDayCategory()
        customerDayCategory()
        tranDayCategory()
    })


    const loadFunnelData=()=>{
        get("/summary/FunnelData").then((res)=>{
            if(res.data.code===200){
                var chartDom = document.getElementById('summaryFunnel');
                var myChart = echarts.init(chartDom);
                var option = {
                    title: {
                        text: '销售漏斗图'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c}'
                    },
                    toolbox: {
                        feature: {
                            dataView: { readOnly: false },
                            restore: {},
                            saveAsImage: {}
                        }
                    },
                    legend: {
                        data: ['线索', '客户', '交易', '成交']
                    },
                    series: [
                        {
                            name: '销售数据',
                            type: 'funnel',
                            left: '10%',
                            top: 60,
                            bottom: 20,
                            width: '80%',
                            min: 0,
                            max: 100,
                            minSize: '0%',
                            maxSize: '100%',
                            sort: 'descending',
                            gap: 2,
                            label: {
                                show: true,
                                position: 'inside'
                            },
                            labelLine: {
                                length: 10,
                                lineStyle: {
                                width: 1,
                                type: 'solid'
                                }
                            },
                            itemStyle: {
                                borderColor: '#fff',
                                borderWidth: 1
                            },
                            emphasis: {
                                label: {
                                fontSize: 20
                                }
                            },
                            data: res.data.data
                        }
                    ]
                };
                option && myChart.setOption(option);
            }else{
                messageTip(res.data.msg,"error")
            }
        })
    }


    const lodePieData=()=>{
        get("/summary/PieData").then((res)=>{
            if(res.data.code===200){
                var chartDom = document.getElementById('summaryPie');
                var myChart = echarts.init(chartDom);
                var option;

                option = {
                    title: {
                        text: '线索来源品饼状图',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left'
                    },
                    series: [
                        {
                            name: '线索来源数据',
                            type: 'pie',
                            radius: '50%',
                            data:res.data.data,
                            emphasis: {
                                itemStyle: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };
                option && myChart.setOption(option);
            }else{
                messageTip(res.data.msg,"error")
            }
        })   
    }


    const activityMonthCategory=(year)=>{

        get("/summary/activityMonthCategory",{year:year}).then((res)=>{
            if(res.data.code===200){
                const backendData =res.data.data
                const months = backendData.map(item => item.name+'月');
                const values = backendData.map(item => item.value);
                var chartDom = document.getElementById('activityMonthCategory');
                var myChart = echarts.init(chartDom);
                var option;

                option = {
                    title: {
                        text: '市场活动每月统计柱状图',
                        left: 'center'       
                    },
                    xAxis: {
                        type: 'category',
                        data: months
                    },
                    yAxis: {
                        type: 'value'
                    },
                    tooltip: {
                        trigger: 'item'
                    },
                    series: [
                        {   
                            name: '活动月数据',
                            data: values,
                            type: 'bar'

                        }
                    ]
                };
                option && myChart.setOption(option);
            }else{
                messageTip(res.data.mes,"error")
            }
        })
        
    }


    const clueDayCategory=(clueYearMonth)=>{
            get("/summary/clueDayCategory",{yearMonth:clueYearMonth}).then((res)=>{
                if(res.data.code===200){
                    const backendData =res.data.data
                    const day = backendData.map(item => item.name+'号');
                    const values = backendData.map(item => item.value);
                    var chartDom = document.getElementById('clueDayCategory');
                    var myChart = echarts.init(chartDom);
                    var option = {
                        title: {
                                text: '线索数据每天统计柱状图',
                                left: 'center'       
                            },
                        xAxis: {
                            type: 'category',
                            data: day
                        },
                        yAxis: {
                            type: 'value'
                        },
                        tooltip: {
                                trigger: 'item'
                            },
                        series: [
                            {
                                name:'线索日数据',
                                data: values,
                                type: 'bar'
                            }
                        ]
                    };
                    option && myChart.setOption(option);
                }
                else{
                    messageTip(res.data.msg,"error")
                }
            })  
        }


    const customerDayCategory=(yearMonth)=>{
        get("/summary/customerDayCategory",{yearMonth:yearMonth}).then((res)=>{
                if(res.data.code===200){
                    const backendData =res.data.data
                    const day = backendData.map(item => item.name+'号');
                    const values = backendData.map(item => item.value);
                    var chartDom = document.getElementById('customerDayCategory');
                    var myChart = echarts.init(chartDom);
                    var option = {
                        title: {
                                text: '客户数据每天统计柱状图',
                                left: 'center'       
                            },
                        xAxis: {
                            type: 'category',
                            data: day
                        },
                        yAxis: {
                            type: 'value'
                        },
                        tooltip: {
                                trigger: 'item'
                            },
                        series: [
                            {
                                name:'客户日数据',
                                data: values,
                                type: 'bar'
                            }
                        ]
                    };
                    option && myChart.setOption(option);
                }
                else{
                    messageTip(res.data.msg,"error")
                }
            }) 
        }





        const tranDayCategory=(tranYearMonth)=>{
            get("/summary/tranDayCategory",{yearMonth:tranYearMonth}).then((res)=>{
                if(res.data.code===200){
                    const backendData =res.data.data
                    const day = backendData.map(item => item.name+'号');
                    const values = backendData.map(item => item.value);
                    var chartDom = document.getElementById('tranDayCategory');
                    var myChart = echarts.init(chartDom);
                    var option = {
                        title: {
                                text: '交易数据每天统计柱状图',
                                left: 'center'       
                            },
                        xAxis: {
                            type: 'category',
                            data: day
                        },
                        yAxis: {
                            type: 'value'
                        },
                        tooltip: {
                                trigger: 'item'
                            },
                        series: [
                            {
                                name:'交易日数据',
                                data: values,
                                type: 'bar'
                            }
                        ]
                    };
                    option && myChart.setOption(option);
                }
                else{
                    messageTip(res.data.msg,"error")
                }
            })  
        }

</script>

<style scoped>
.el-row{
    text-align: center;
}
#summaryFunnel{
    margin-top: 40px;
}
#summaryPie{
    margin-top: 40px;
    margin-left: 100px;
}
.chart-container {
    width: 100%;
  display: flex; /* 弹性布局 */
  gap: 20px; /* 图表间距，可选 */
  padding: 20px; /* 容器内边距，可选 */
}
</style>