<template>
  <div class="card-container">
    <div class="card">
      <PomodoroTimer />
    </div>
    <div class="card">
      <MotivationView />
    </div>
  </div>
  <div class="card-vedio">
    <div class="card">
      <VideoCard />
    </div>
  </div>
</template>

<script setup>
import PomodoroTimer from '@/components/PomodoroTimer.vue'
import { useTimerStore } from '@/stores/timer'
import { computed, onMounted } from 'vue'
import VideoCard from '@/components/VideoCard.vue'
import MotivationView from '@/components/MotivationView.vue'
const timer = useTimerStore()

</script>

<style scoped>
.card-container {
  display: flex;
  justify-content: space-between;
  margin-top: -20px;
  gap: 20px;
  padding: 30px;
}

.card {
  flex: 1;
  background-color: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  padding: 15px;
  text-align: center;
}

.card-vedio {
  display: flex;
  justify-content: space-between;
  gap: 20px;
  padding: 0px;
}
</style>
