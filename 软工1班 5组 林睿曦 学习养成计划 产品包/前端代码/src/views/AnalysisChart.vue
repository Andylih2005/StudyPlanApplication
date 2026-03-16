<template>
  <div class="analysis-page">
    <h2 class="page-title">📊 Data Analysis</h2>

    <!-- 日期選擇器 -->
    <el-date-picker
      v-model="dateRange"
      type="daterange"
      start-placeholder="開始日期"
      end-placeholder="結束日期"
      format="YYYY-MM-DD"
      value-format="YYYY-MM-DD"
      style="margin-bottom: 20px"
      @change="fetchStats"
    />

    <div class="chart-grid">
      <div ref="chart1" class="chart-box"></div>
      <div ref="chart2" class="chart-box"></div>
      <div ref="chart3" class="chart-box"></div>
      <div ref="chart4" class="chart-box"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getTaskStatistics } from '@/api/statisticsapi'
import { useUserStore } from '@/stores/user'
import dayjs from 'dayjs'

// 拿使用者 ID
const userStore = useUserStore()
const userID = userStore.userID

// 日期區間（預設當月）
const dateRange = ref([
  dayjs().startOf('month').format('YYYY-MM-DD'),
  dayjs().endOf('month').format('YYYY-MM-DD')
])

// 綁定四張圖表
const chart1 = ref(null)
const chart2 = ref(null)
const chart3 = ref(null)
const chart4 = ref(null)

const fetchStats = async () => {
  const [startDateRaw, endDateRaw] = dateRange.value
  const startDate = `${startDateRaw} 00:00:00`
  const endDate = `${endDateRaw} 23:59:59`

  try {
    const res = await getTaskStatistics(userID, startDate, endDate)
    const stats = res.data

    const dates = stats.map(item => item.date)
    const totalTasks = stats.map(item => item.totalTasks)
    const completedTasks = stats.map(item => item.completedTasks)
    const rates = stats.map(item => item.completionRate)
    const times = stats.map(item => item.averageCompletionTime)

    // 圖表 1：任務總數
    const instance1 = echarts.init(chart1.value)
    instance1.setOption({
      title: { text: '每日任務總數' },
      xAxis: {
        type: 'category',
        data: dates,
        axisLabel: { formatter: val => val.substring(5), rotate: 45 }
      },
      yAxis: { type: 'value' },
      series: [{ type: 'line', data: totalTasks }]
    })

    // 圖表 2：完成任務數
    const instance2 = echarts.init(chart2.value)
    instance2.setOption({
      title: { text: '每日完成任務數' },
      xAxis: {
        type: 'category',
        data: dates,
        axisLabel: { formatter: val => val.substring(5), rotate: 45 }
      },
      yAxis: { type: 'value' },
      series: [{ type: 'line', data: completedTasks }]
    })

    // 圖表 3：完成率
    const instance3 = echarts.init(chart3.value)
    instance3.setOption({
      title: { text: '每日任務完成率' },
      xAxis: {
        type: 'category',
        data: dates,
        axisLabel: { formatter: val => val.substring(5), rotate: 45 }
      },
      yAxis: { type: 'value' },
      series: [{ type: 'bar', data: rates }]
    })

    // 圖表 4：平均耗時（分鐘）
    const instance4 = echarts.init(chart4.value)
    instance4.setOption({
      title: { text: '每日完成時間差距（分鐘）' },
      xAxis: {
        type: 'category',
        data: dates,
        axisLabel: { formatter: val => val.substring(5), rotate: 45 }
      },
      yAxis: {
        type: 'value',
        name: '分鐘',
        axisLine: { onZero: true },
        min: value => Math.min(-30, value.min - 10),
        max: value => Math.max(30, value.max + 10)
      },
      series: [
        {
          type: 'line',
          data: times,
          label: { show: true, formatter: '{c} min' },
          itemStyle: {
            color: params => (params.value > 0 ? 'red' : 'green')
          }
        }
      ]
    })
  } catch (err) {
    console.error('📉 統計資料獲取失敗：', err)
  }
}

// 頁面載入時呼叫一次
onMounted(fetchStats)
</script>

<style scoped>
.analysis-page {
  padding: 30px;
  font-family: Arial, sans-serif;
}

.page-title {
  text-align: center;
  font-size: 28px;
  margin-bottom: 30px;
  margin-top: -20px;
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  justify-items: center;
}

.chart-box {
  width: 600px;
  height: 300px;
  background-color: #f7f7f7;
  border-radius: 10px;
}
</style>
