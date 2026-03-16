<template>
  <div class="motivation-view">
    <h2>🌱</h2>
    <p>你一共專注了 {{ totalCount }} 次</p>
    <p>累計專注時長：{{ formattedDuration }} </p>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { useTimerStore } from '@/stores/timer'
import { useUserStore } from '@/stores/user'
import focusSessionApi from '@/api/focusSessions'
const userStore = useUserStore()
const currentUser = userStore.userID

const sessions = ref([])
const totalCount = ref(0)
const totalDuration = ref(0) // 單位：分鐘

//讀取資料
const fetchSessions = async () => {
  try {
    const res = await focusSessionApi.getSessionsByUser(currentUser)
    sessions.value = res.data || []
    totalCount.value = sessions.value.length
    totalDuration.value = sessions.value.reduce((sum, s) => sum + (s.durationSeconds || 0), 0)
    console.log(res.durationSeconds)
    console.log(totalDuration.value)
  } catch (err) {
    console.error('讀取專注記錄失敗:', err)
  }
}

onMounted(() => {
  fetchSessions()
})

const formattedDuration = computed(() => {
  const totalSeconds = totalDuration.value / 60;
  const hours = Math.floor(totalSeconds / 3600); // 1 小時 = 3600 秒
  const minutes = Math.floor((totalSeconds % 3600) / 60); // 剩餘秒數再換算成分鐘
  return `${hours} 小時 ${minutes} 分鐘`;
})

</script>

<style scoped>
.motivation-view {
  font-size: 1.2em;
  color: #2e7d7d;
  text-align: center;
  padding: 10px;
}
</style>