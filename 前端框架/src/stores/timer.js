// timer.js (修改后的版本)

import { defineStore } from 'pinia'

export const useTimerStore = defineStore('timer', {
    state: () => ({
        duration: 25 * 60,
        timeLeft: 25 * 60,
        isRunning: false,
        timerId: null,
        completedCount: 0
    }),
    actions: {
        start() {
            if (this.isRunning || this.timeLeft <= 0) return
            this.isRunning = true
            this.timerId = setInterval(() => {
                if (this.timeLeft > 0) {
                    this.timeLeft--
                } else {
                      this.pause()
                }
            }, 1000)
        },
        pause() {
            this.isRunning = false
            clearInterval(this.timerId)
            this.timerId = null
        },
        reset() {
            this.pause()
            this.timeLeft = this.duration
        },
        setDuration(min) {
            this.duration = min * 60
            if (!this.isRunning) {
                this.timeLeft = this.duration
            }
        },
        // 原来的 complete 函数被拆分了
        // 新增一个只负责计数的 action，给组件调用
        incrementCompletedCount() {
            this.completedCount++
        }
    }
})