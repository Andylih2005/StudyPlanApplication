<template>
  <el-dialog :model-value="visible" title="编辑任务" width="500px" @close="$emit('close')">
    <el-form :model="localTask" label-width="80px" :rules="rules" ref="formRef">
      <el-form-item label="标题" prop="title">
        <el-input v-model="localTask.title" maxlength="100" show-word-limit />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <el-input v-model="localTask.content" type="textarea" rows="3" />
      </el-form-item>
      <el-form-item label="标签" prop="tag">
        <el-select v-model="localTask.tag" multiple filterable allow-create default-first-option placeholder="请选择或输入标签">
          <el-option v-for="tag in allTags" :key="tag" :label="tag" :value="tag" />
        </el-select>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-rate v-model="localTask.priority" :max="5" show-score />
      </el-form-item>
      <el-form-item label="截止时间" prop="deadline">
        <el-date-picker v-model="localTask.deadline" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"
          placeholder="选择截止时间" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="$emit('close')">取消</el-button>
        <el-button type="primary" @click="saveTask">保存修改</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch, defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'
import { updateTask } from '@/api/taskapi'
import { useUserStore } from '../stores/user'

const userStore = useUserStore()

const props = defineProps({
  task: Object,
  visible: Boolean,
  allTags: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['close', 'save'])

// 创建 localTask 的副本，以免直接修改 props
const localTask = ref({})

// 如果父组件传入的 task 发生变化（比如点击另一个任务），更新 localTask
watch(() => props.task, (newTask) => {
  if (newTask) {
    localTask.value = {
      ...newTask, tag: typeof newTask.tag === 'string'
        ? newTask.tag.split(',').map(t => t.trim())
        : (newTask.tag || [])
    }
  } else {
    localTask.value = {}; // 当没有任务传入时，确保 localTask.value 是一个空对象
  }
}, { immediate: true })

const saveTask = async () => {
  try {
    // 确保 formRef 存在且 validate 方法可用
    if (formRef.value) {
      await formRef.value.validate();
    }
    const taskToUpdate = {
      ...localTask.value, // 使用 localTask 的所有当前值
      deadline: new Date(localTask.value.deadline).getTime(), // 转成时间戳
      userID: userStore.userID, // 添加 userID
      tag: Array.isArray(localTask.value.tag)
        ? localTask.value.tag.join(',')
        : localTask.value.tag
    };
    console.log(props.task.taskID)
    console.log(taskToUpdate)

    await updateTask(props.task.taskID, taskToUpdate)
    emit('save', { ...localTask.value })
  } catch (err) {
    if (err === false) { // 验证失败
      ElMessage.error('请填写完整的任务信息！');
    } else { // API 调用失败
      console.error('保存任务失败:', err);
      const status = err.response?.status;
      if (status === 400) {
        ElMessage.error('请完整填写任务');
      } else if (status === 500) {
        ElMessage.error('服务器异常，可能是数据库连接失败，请稍后再试');
      } else {
        ElMessage.error('保存任务时发生未知错误');
      }
    }
  }
}

const formRef = ref(null); // 添加 formRef 的定义，以便在 saveTask 中使用

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  priority: [{ required: true, message: '请输入优先级', trigger: 'blur' }],
  deadline: [{ required: true, message: '请选择截止时间', trigger: 'change' }]
}

</script>

<style scoped>
.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 16px;
  padding-top: 10px;
}

:deep(.el-dialog) {
  border-radius: 16px;
}
</style>
