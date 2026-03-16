<template>
  <!-- AddTaskDialog -->
  <el-dialog v-model="dialogVisible" title="添加新任务" width="500px" @close="handleClose">
    <el-form :model="newTask" label-width="90px">
      <el-form-item label="任务标题">
        <el-input v-model="newTask.title" maxlength="100" show-word-limit />
      </el-form-item>

      <el-form-item label="任务内容">
        <el-input v-model="newTask.content" type="textarea" rows="3" />
      </el-form-item>

      <el-form-item label="标签">
        <el-select
          v-model="newTask.tag"
          multiple
          filterable
          allow-create
          default-first-option
          placeholder="请选择或输入标签"
        >
          <el-option v-for="tag in allTags" :key="tag" :label="tag" :value="tag" />
        </el-select>
      </el-form-item>

      <el-form-item label="优先级">
        <el-rate v-model="newTask.priority" :colors="colors" :max="5" show-score />
      </el-form-item>

      <el-form-item label="截止时间">
        <el-date-picker
          v-model="newTask.deadline"
          type="datetime"
          placeholder="选择截止时间"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
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
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { addTask } from '@/api/taskapi.js'
import { useUserStore } from '@/stores/user'

// props + emits
const props = defineProps({
  modelValue: Boolean,
  initialContent: String,
  allTags: Array
})
const emit = defineEmits(['update:modelValue', 'submit'])

// 控制对话框显示
const dialogVisible = ref(props.modelValue)
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
})
watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

// 用户信息
const userStore = useUserStore()

// 颜色设定
const colors = ref(['#99A9BF', '#F7BA2A', '#FF9900'])

// 任务对象
const newTask = ref({
  userID: userStore.userID,
  title: '',
  content: '',
  priority: 0,
  deadline: '',
  uploaddate: new Date(),
  tag: []
})

// 自动填入 initialContent
watch(() => props.initialContent, (val) => {
  newTask.value.content = val || ''
}, { immediate: true })

// 重置任务
const resetTask = () => {
  newTask.value = {
    userID: userStore.userID,
    title: '',
    content: '',
    priority: 0,
    deadline: '',
    uploaddate: new Date(),
    tag: []
  }
}

const isSubmitting = ref(false)

// 提交表单
const submit = async () => {
  if (isSubmitting.value) return
  isSubmitting.value = true

  try {
    const taskToSend = {
      ...newTask.value,
      tag: Array.isArray(newTask.value.tag)
        ? newTask.value.tag.join(',')
        : newTask.value.tag
    }

    await addTask(taskToSend)
    emit('submit', taskToSend)
    ElMessage.success('任务已保存到数据库！')

    dialogVisible.value = false
    resetTask()
  } catch (err) {
    const status = err.response?.status
    if (status === 400) {
      alert('请完整填写任务')
    } else if (status === 500) {
      alert('服务器异常，请稍后再试')
    } else {
      alert('发生未知错误')
    }
  } finally {
    isSubmitting.value = false
  }
}

const cancel = () => {
  dialogVisible.value = false
  resetTask()
}

const handleClose = () => {
  resetTask()
  emit('update:modelValue', false)
}
</script>

<style scoped>
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
