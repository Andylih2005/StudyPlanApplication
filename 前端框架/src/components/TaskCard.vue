<template>
  <el-card class="task-card"
    :style="{ backgroundColor: isEditMode ? '#f5f5f5' : 'white', cursor: isEditMode ? 'pointer' : 'default' }"
    @click="handleClick">
    <div class="task-title"><div class="title">
      <strong>{{ task.title }}</strong></div><div class="star">
      <span>⭐ {{ task.priority }}</span></div>
    </div>
    <div class="task-tags" v-if="task.tag">
  <el-tag
    v-for="(tag, index) in tagList"
    :key="index"
    type="success"
    size="small"
    style="margin-right: 6px;"
  >
    {{ tag }}
  </el-tag>
</div>
    <div class="task-content">{{ task.content }}</div>
    <div class="check-box">
      <div class="task-deadline">⏰ {{ task.deadline }}</div>
      <input type="checkbox" v-model="task.completed" class="checkbox" @change="toggleComplete" />

    </div>
  </el-card>
</template>

<script setup>
import { defineProps } from 'vue'
import taskcompletionapi from '@/api/taskcompletionapi'
import { deleteTask } from '@/api/taskapi'
import { useUserStore } from '../stores/user'
const userStore = useUserStore()

const props = defineProps({
  task: Object,
  isEditMode: Boolean
})
const emit = defineEmits(['taskCompleted', 'click-card'])

const toggleComplete = async () => {
  console.log("复选框被点击了！")  // <-- 加这句确认事件触发
  console.log({
    taskID: props.task.taskID,
    userID: userStore.userID,
    completedAt: new Date().toISOString(),
    title: props.task.title,
  })
  try {
    await taskcompletionapi.addCompletion({
      taskID: props.task.taskID,
      userID: userStore.userID,
      completedAt: new Date(),
      title: props.task.title,
      deadline:props.task.deadline
    })

    await deleteTask(props.task.taskID)
    emit('taskCompleted', props.task.taskID)
    alert('任务完成，已移至完成记录并删除')

  } catch (err) {
    const status = err.response?.status;

    if (status === 400) {
      alert('请完整填写任务');
    } else if (status === 500) {
      alert('服务器异常，可能是数据库连接失败，请稍后再试');
    } else {
      alert('发生未知错误');
    }
  }
}

//edit
const handleClick = () => {
  if (props.isEditMode) {
    emit('click-card', props.task)
  }
}

//tag
import { computed } from 'vue'

const tagList = computed(() => {
  if (!props.task.tag) return []
  if (Array.isArray(props.task.tag)) return props.task.tag
  return props.task.tag.split(',').map(t => t.trim())
})


</script>

<style scoped>
/*滾動條 */
.task-grid-wrapper {
  max-height: 400px;
  overflow-y: auto;
  transition: max-height 0.3s ease;
}

.task-grid-wrapper.expanded {
  max-height: 100%;
}

/*自適應*/
.task-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
  padding: 10px;
}

.task-card {
  border-radius: 12px;
  padding: 12px 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease;
}

.task-card:hover {
  transform: translateY(-3px);
}

.task-title {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
  margin-bottom: 6px;
}

.title{
  font-size: 25px;
}

.star{
  font-size: 20px;
}

.task-content {
  margin-bottom: 8px;
  color: #666;
    font-size: 18px;

}

.task-deadline {
  font-size: 15px;
  color: #999;
}

.task-card.edit-mode {
  background-color: #f5f5f5;
  cursor: pointer;
}

.check-box {
  display: flex;
  justify-content: space-between;
  font-weight: bold;
  margin-bottom: 1px;
}

.task-card {
  margin: 10px;
  transition: background-color 0.3s ease;
}

.task-tags ::v-deep(.el-tag) {
  font-size: 13px;
  padding: 2px 8px;
  margin-bottom: 8px;
}

</style>
