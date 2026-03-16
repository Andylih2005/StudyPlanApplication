<template>
    <div v-if="dialogVisible" class="dialog-bg">
        <div class="dialog">
            <h3>批量删除任务</h3>

            <div v-for="task in props.task" :key="task.taskID">
                <label>
                    <input type="checkbox" :value="task.taskID" v-model="selected"/>
                    {{ task.title }}
                </label>
            </div>

            <div class="actions">
                <el-button type="primary" @click="confirmDelete">确认</el-button>
                <el-button @click="emit('update:dialogVisible', false)">取消</el-button>

            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { deleteTask } from '@/api/taskapi'
import { computed } from 'vue'

const props = defineProps({
    dialogVisible: Boolean,
    task: Array,
    selected: Array
});
const emit = defineEmits(['update:dialogVisible', 'update:selected', 'deleted'])

const selected = computed({
  get: () => props.selected,
  set: (val) => emit('update:selected', val)
})

// 删除按钮逻辑
function confirmDelete() {
    if (!props.selected.length) {
        alert('请选择任务再删除');
        return;
    }
    emit('deleted', props.selected);
    emit('update:dialogVisible', false);
}
</script>

<style scoped>
.dialog-bg {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
}

.dialog {
    background: white;
    padding: 20px;
    border-radius: 8px;
    min-width: 300px;
}

.actions {
    margin-top: 16px;
    display: flex;
    justify-content: space-between;
}
</style>
