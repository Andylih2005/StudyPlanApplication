<template>
  <div class="ai-page">
    <div class="title">
      <h2>AI 智能反饋</h2>
    </div>
    <div class="tag-section">
      <div class="tag-bar">
        <el-tag v-for="tag in topTags" :key="tag" size="large" type="success" effect="plain" @click="selectTag(tag)"
          :class="{ active: selectedTag === tag }">
          {{ tag }}
        </el-tag>
      </div>
    </div>

    <el-button type="primary" @click="handleSubmit" class="submit-btn" :loading="isLoading">
      {{ isLoading ? '正在生成建議...請不要切換頁面...' : '發送給AI生成建議' }}
    </el-button>

    <el-divider />

    <div class="response-box" v-loading="isLoading" element-loading-text="AI 正在思考中，請耐心等待..."
      element-loading-background="rgba(255, 255, 255, 0.8)">
      <div v-if="!isLoading && response" v-html="renderedHtml"></div>

      <div v-if="!isLoading && !response" class="placeholder">
        請選擇標籤後，點擊上方按鈕產生回饋建議。
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserCompletions } from '@/api/taskcompletionapi'
import { getTasksByUser } from '@/api/taskapi'
import focusSessionApi from '@/api/focusSessions'
import { useUserStore } from '../stores/user'
import { getTaskCompletionReport } from '@/api/aipageapi'
import { marked } from 'marked' // <-- 新增：引入 marked 库

const response = ref('') // AI 返回内容

const isLoading = ref(false) // <-- 新增：用于控制加载状态

const userStore = useUserStore()
const currentUser = userStore.userID

const tasks = ref([])

const renderedHtml = computed(() => {
  if (response.value) {
    // 使用 marked 将 markdown 字符串转换为 HTML 字符串
    return marked(response.value)
  }
  return '' // 如果没有内容，则返回空字符串
})

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
    console.error('取得任務失敗', err)
  }
}

onMounted(() => {
  loadTasks()
})

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

let finalTag = ''

const selectTag = (tag) => {
  selectedTag.value = selectedTag.value === tag ? '' : tag
  if (selectedTag.value == '') {
    finalTag = 'all'
    console.log(finalTag)
  } else {
    finalTag = selectedTag.value
    console.log(finalTag)
  }
}

// 提交给 AI
async function handleSubmit() {
  if (isLoading.value) return // 如果正在加载，则阻止重复提交

  isLoading.value = true // <-- 开始加载
  response.value = '' // (可选) 清空上一次的结果
  try {
    // 1. 使用 await 等待 Promise 完成，并将结果赋值给 res
    const res = await getTaskCompletionReport(currentUser, finalTag)
    console.log('成功從後端接收的回應:', res)
    // 2. 直接使用 res.result，因为 .data 已在 aipageapi.js 中处理
    response.value = res || 'AI 沒有返回內容'
    ElMessage.success('已成功發送請求')
    alert("建議生成成功，已保存到對應文件夾中，請及時查看！")
  } catch (err) {
    // 建议在开发中打印出完整错误，方便调试
    console.error('請求 AI 建議失敗:', err);

    const status = err.response?.status;

    if (status === 400) {
      alert('請完整填寫任務');
    } else if (status === 500) {
      alert('伺服器異常，可能是資料庫連線失敗，請稍後再試');
    } else {
      // 可以提供更具体的错误信息
      alert(`發生未知錯誤: ${err.message}`);
    }
  } finally {
    isLoading.value = false // <-- 结束加载
  }
}
</script>

<style scoped>
.ai-page {
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.tag-section {
  margin-bottom: 20px;
}

.tag-item {
  margin: 5px;
  cursor: pointer;
}

.submit-btn {
  margin-top: 10px;
}

.response-box {
  background: #f4f6fa;
  padding: 15px;
  border-radius: 8px;
  margin-top: 20px;
}


.tag-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.tag-bar .active {
  background-color: #67c23a;
  /* 使用主題色 */
  color: white;
  border-color: #67c23a;
}

.title {
  margin-top: -20px;
}

.response-box {
  background: #f4f6fa;
  padding: 15px 25px;
  /* 增加左右内边距 */
  border-radius: 8px;
  margin-top: 20px;
  line-height: 1.7;
  /* 增加行高，提升可读性 */
}

/* 让 v-html 里的内容样式更好看
  您可能需要根据实际效果调整
*/
.response-box :deep(h1),
.response-box :deep(h2),
.response-box :deep(h3) {
  margin-top: 1.2em;
  margin-bottom: 0.6em;
  font-weight: 600;
}

.response-box :deep(p) {
  margin-bottom: 1em;
}

.response-box :deep(ul),
.response-box :deep(ol) {
  padding-left: 2em;
}

.response-box :deep(li) {
  margin-bottom: 0.5em;
}

.response-box :deep(code) {
  background-color: #e2e2e2;
  padding: 2px 4px;
  border-radius: 4px;
  font-family: 'Courier New', Courier, monospace;
}

.response-box {
  background: #f4f6fa;
  padding: 25px;
  border-radius: 8px;
  margin-top: 20px;
  line-height: 1.7;
  min-height: 200px;
  /* 给一个最小高度，防止加载时容器塌陷 */
  transition: background-color 0.3s;
}

.placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 160px;
  /* 调整高度以适应 min-height */
  color: #909399;
  font-size: 15px;
}
</style>