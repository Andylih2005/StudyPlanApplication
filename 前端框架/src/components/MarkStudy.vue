<template>
    <el-dialog v-model="dialogVisible" title="添加新任务" width="500px" @close="handleClose">
        <el-form :model="newTask" label-width="90px">
            <el-form-item label="学习标题">
                <el-input v-model="newTask.title" maxlength="100" show-word-limit />
            </el-form-item>

            <el-form-item label="学习内容">
                <el-input v-model="newTask.content" type="textarea" rows="3" />
            </el-form-item>
            <el-form-item label="标签">
                <el-select v-model="newTask.tag" multiple filterable allow-create default-first-option
                    placeholder="请选择或输入标签">
                    <el-option v-for="tag in allTags" :key="tag" :label="tag" :value="tag" />
                </el-select>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="cancel">取消</el-button>
                <el-button type="primary" :loading="isSubmitting" @click="submit">确认添加</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, watch, computed, onMounted } from 'vue'
import focusSessionApi from '@/api/focusSessions'
import { useUserStore } from '@/stores/user'

const emit = defineEmits(['update:modelValue', 'submit'])
const userStore = useUserStore()

const tasks = ref([])

// props + emits logic for v-model
const props = defineProps({
    modelValue: Boolean,
    duration: Number
})

const dialogVisible = ref(props.modelValue)
watch(() => props.modelValue, (val) => {
    dialogVisible.value = val
    if (val) { // When dialog opens, reset the form.
        resetTask()
    }
})
watch(dialogVisible, (val) => {
    // This makes the component v-model compatible
    emit('update:modelValue', val)
})

onMounted(async () => {
    try {
        // 呼叫 API 獲取該使用者的所有專注紀錄
        const res = await focusSessionApi.getSessionsByUser(userStore.userID);
        // 將獲取到的資料填充到 tasks 陣列中
        tasks.value = res.data || [];
        console.log('已成功獲取歷史專注紀錄用於標籤列表:', tasks.value);
    } catch (err) {
        console.error("獲取歷史標籤失敗:", err);
    }
});

const allTags = computed(() => {
    const tagSet = new Set()
    tasks.value.forEach(task => {
        if (task.userID === userStore.userID) {
            if (Array.isArray(task.tag)) {
                task.tag.forEach(tag => tagSet.add(tag))
            } else if (typeof task.tag === 'string') {
                tagSet.add(task.tag)
            }
        }
    })
    return [...tagSet]
})

const newTask = ref({})

const resetTask = () => {
    console.log('Dialog is opening. Duration prop received:', props.duration); // <--- 新增此行除錯

    newTask.value = {
        userID: userStore.userID,
        title: '',
        content: '',
        // 'completed_at' will be set on submission. 'starttime' can be set here.
        starttime: new Date(),
        tag: [], // Initialize as an empty array for the multi-select
        durationSeconds: props.duration,
    }
}

const isSubmitting = ref(false)

const submit = async () => {
    if (isSubmitting.value) return
    isSubmitting.value = true

    try {
        const taskToSend = {
            ...newTask.value,
            // Convert tags array to comma-separated string for the API
            tag: Array.isArray(newTask.value.tag) ? newTask.value.tag.join(',') : newTask.value.tag,
            // Set completion timestamp at the moment of submission
            completed_at: new Date(),
        }

        // FIX: The payload variable is 'taskToSend', not 'sessionData'
        await focusSessionApi.createSession(taskToSend)

        // Emit the successful submission data to the parent
        emit('submit', taskToSend)
        console.log('API 请求成功，已通知父组件。')

        // Close the dialog
        dialogVisible.value = false

    } catch (err) {
        console.error("Submission failed:", err)
        const status = err.response?.status;
        if (status === 400) {
            alert('请完整填写学习记录');
        } else if (status === 500) {
            alert('服务器异常，请稍后再试');
        } else {
            alert('发生未知错误');
        }
    } finally {
        isSubmitting.value = false
    }
}

const cancel = () => {
    dialogVisible.value = false
}

const handleClose = () => {
    emit('update:modelValue', false)
}
</script>

<style scoped>
/* Styles remain the same */
.dialog-footer {
    display: flex;
    justify-content: center;
    gap: 20px;
    padding-bottom: 10px;
}

:deep(.el-dialog) {
    border-radius: 16px;
}
</style>