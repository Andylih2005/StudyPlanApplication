<template>
    <div class="pomodoro-timer">
        <svg width="200" height="200" class="circle">
            <circle r="90" cx="100" cy="100" stroke="#8fb2c9" stroke-width="10" fill="none"
                :stroke-dasharray="circumference" :stroke-dashoffset="dashOffset" />
        </svg>
        <div class="time">{{ formattedTime }}</div>

        <div class="controls">
            <input type="number" v-model.number="durationInput" min="1" max="60" /> 分钟
            <button @click="handleStart">开始</button>
            <button @click="timer.pause()">暂停</button>
            <button @click="timer.reset()">重置</button>
        </div>
    </div>
    <MarkStudy v-model="showMarkDialog" :duration="completedDuration" @submit="handleStudySubmit" />
</template>

<script setup>
import { ref, computed, watch, onUnmounted } from 'vue'
import { useTimerStore } from '@/stores/timer'
// focusSessionApi is not needed here anymore, it's handled in MarkStudy
import { useUserStore } from '../stores/user'
import MarkStudy from './MarkStudy.vue'

const showMarkDialog = ref(false)
const completedDuration = ref(0)

const userStore = useUserStore()
const timer = useTimerStore()

const durationInput = ref(25)

const handleStart = () => {
    if (timer.timeLeft <= 0) {
        timer.setDuration(durationInput.value)
    }
    timer.start()
}

const handleComplete = () => {
    console.log('组件侦测到计时结束, 准备打开弹窗...')
    completedDuration.value = timer.duration
    completedDuration.value = timer.duration * 60
    showMarkDialog.value = true
}

// FIX 2: Add logic to handle the successful submission from the dialog
function handleStudySubmit(data) {
    console.log("Study data submission successful:", data);
    // This logic now correctly runs in the parent component
    timer.incrementCompletedCount()
    timer.reset()
}

const stopWatch = watch(
    () => timer.timeLeft,
    (newTime, oldTime) => {
        if (newTime === 0 && oldTime > 0) { // Condition is more robust
            handleComplete()
        }
    }
)

onUnmounted(() => {
    stopWatch()
    console.log('Component unmounted, watcher stopped.')
})

watch(durationInput, (newVal) => {
    timer.setDuration(newVal)
})

const radius = 90
const circumference = 2 * Math.PI * radius
const dashOffset = computed(() => {
    // Prevent division by zero if duration is 0
    if (timer.duration === 0) return circumference;
    return circumference * (1 - timer.timeLeft / timer.duration)
})

const formattedTime = computed(() => {
    const m = Math.floor(timer.timeLeft / 60)
    const s = timer.timeLeft % 60
    return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`
})
</script>

<style scoped>
/* Styles remain the same */
.pomodoro-timer {
    text-align: center;
}

.circle {
    transform: rotate(-90deg);
}

.time {
    font-size: 2.5em;
    color: #3a7ca5;
    margin-top: -130px;
    margin-bottom: 20px;
    position: relative;
    z-index: 1;
    pointer-events: none;
    /* 新增：让时间显示不接受鼠标事件 */
    margin-bottom: 100px;
}

.controls {
    display: flex;
    justify-content: center;
    gap: 10px;
    position: relative;
    /* 新增：为 z-index 生效创造条件 */
    z-index: 2;
    /* 新增：确保层级高于 .time */
}
</style>