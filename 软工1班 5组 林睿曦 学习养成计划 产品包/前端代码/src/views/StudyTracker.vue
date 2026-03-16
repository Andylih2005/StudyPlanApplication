<template>
  <div class="study-progress">
    <el-input v-model="searchQuery" placeholder="輸入任務標題或標籤進行搜索" prefix-icon="el-icon-search" clearable
      style="width: 400px; margin-bottom: 16px" />

    <div class="tag-bar">
      <el-tag v-for="tag in topTags" :key="tag" size="large" type="success" effect="plain" @click="selectTag(tag)"
        :class="{ active: selectedTag === tag }">
        {{ tag }}
      </el-tag>
    </div>

    <el-card class="task-panel">
      <h3 class="panel-title">
        {{ selectedTag ? `標籤：${selectedTag}` : '全部任務 / 學習記錄' }}
      </h3>
      
      <div class="task-grid-container">
        <TaskCardItem 
          v-for="task in filteredTasks" 
          :key="task.taskID || task.title" 
          :task="task" 
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getUserCompletions } from '@/api/taskcompletionapi'
import { getTasksByUser } from '@/api/taskapi'
import focusSessionApi from '@/api/focusSessions'
import { useUserStore } from '../stores/user'
// 引入我們新建的子元件
import TaskCardItem from '@/components/StudyTrackerCard.vue' 

const userStore = useUserStore()
const currentUser = userStore.userID

const tasks = ref([])

const loadTasks = async () => {
  try {
    const resTasks = await getTasksByUser(currentUser)
    const resSessions = await focusSessionApi.getSessionsByUser(currentUser)
    const resCompletions = await getUserCompletions(currentUser)

    // 資料合併邏輯保持不變
    tasks.value = [
      ...(resTasks.data || []),
      ...(resSessions.data || []),
      ...(resCompletions.data || [])
    ]
  } catch (err) {
    console.error('获取任务失败', err)
  }
}

onMounted(() => {
  loadTasks()
})

// 其他的 script 邏輯（搜索、篩選等）完全保持不變
const searchQuery = ref('')
const selectedTag = ref('')

const parseTags = (tag) => {
  if (!tag) return []
  if (Array.isArray(tag)) return tag
  return tag.split(',').map(t => t.trim())
}

const topTags = computed(() => {
  const countMap = {}
  tasks.value.forEach(task => {
    if (task && task.tag) {
      parseTags(task.tag).forEach(tag => {
        countMap[tag] = (countMap[tag] || 0) + 1
      })
    }
  })
  return Object.entries(countMap)
    .sort((a, b) => b[1] - a[1])
    .map(([tag]) => tag)
    .slice(0, 7)
})

const filteredTasks = computed(() => {
  return tasks.value.filter(task => {
    if (!task) return false;
    const query = searchQuery.value.toLowerCase()
    const titleMatch = (task.title || '').toLowerCase().includes(query)
    const contentMatch = (task.content || '').toLowerCase().includes(query)
    const tagsArray = parseTags(task.tag)
    const tagContent = tagsArray.join(',')
    const tagMatchText = tagContent.toLowerCase().includes(query)
    const tagMatch = selectedTag.value ? tagsArray.includes(selectedTag.value) : true
    return tagMatch && (titleMatch || contentMatch || tagMatchText)
  })
})

const selectTag = (tag) => {
  selectedTag.value = selectedTag.value === tag ? '' : tag
}
</script>

<style scoped>
/* 這裡是主頁面的佈局和樣式 */
.study-progress {
  padding: 18px;
}

.tag-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.tag-bar .active {
  background-color: #67c23a; /* 使用主題色 */
  color: white;
  border-color: #67c23a;
}

.task-panel {
  padding: 20px;
  border-radius: 12px;
  background-color: #f7f9fc; /* 給面板一個淺色背景 */
}

.panel-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
  margin-top:-20px;
}

/* 關鍵的網格佈局樣式：
  這會讓 TaskCardItem 自動換行並填滿空間。
*/
.task-grid-container {
  display: grid;
  gap: 20px; /* 卡片之間的間距 */
  /* 每張卡片最小 320px，如果空間足夠，則平均分配寬度 */
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
}
</style>