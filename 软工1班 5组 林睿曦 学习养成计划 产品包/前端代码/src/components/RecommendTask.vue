<template>
  <div class="recommendation-panel">
    <h3 class="section-title">📚 推荐任务（根据标签）</h3>

    <div class="input-group">
      <el-input
        v-model="tag"
        placeholder="请输入关键词（默认自动识别）"
        class="tag-input"
        clearable
      />
      <el-button type="primary" @click="loadRecommendations">🔄 刷新推荐</el-button>
    </div>

    <el-empty v-if="!recommendations.length" description="暂无推荐结果" />

    <div class="card-list" v-else>
      <div
        class="recommend-card"
        v-for="(item, index) in recommendations.slice(0, 6)"
        :key="index"
      >
        <div class="recommend-title">
          <a :href="item.url" target="_blank" class="recommend-link">
            {{ item.title }}
          </a>
          <span class="recommend-source">（{{ item.source }}）</span>
        </div>
        <el-button
          size="small"
          type="success"
          @click="handleAddTask(item)"
        >
          + 添加
        </el-button>
      </div>
    </div>

    <AddTaskDialog
      v-model="dialogVisible"
      :initial-content="prefillContent"
      :all-tags="tagList"
      @submit="handleSubmit"
    />
  </div>
</template>


<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'   
import { ElMessage } from 'element-plus'

import { getRecommendations } from '@/api/recommendapi'
import AddTaskDialog from './AddTaskDialog.vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const userID = userStore.userID

const tag = ref('')
const tagList = ref([])
const recommendations = ref([])

const dialogVisible = ref(false)
const prefillContent = ref('')

function handleAddTask(item) {
  prefillContent.value = `${item.title} ${item.url}`
  dialogVisible.value = true
}

// ⭐ 提交任务后通知用户
function handleSubmit(task) {
  ElMessage.success(`成功添加任务：${task.title}`)
  // 可以补充刷新列表逻辑等
}

// 🔍 获取历史 tag 列表
const loadAllTags = async () => {
  try {
    const res = await axios.get(`http://10.255.6.2:9090/tasks/user/${userID}`)
    const tasks = res.data
    const tags = new Set()
    tasks.forEach(task => {
      if (task.tag) {
        task.tag.split(',').forEach(t => tags.add(t.trim()))
      }
    })
    tagList.value = Array.from(tags)
  } catch (err) {
    console.error('❌ 获取 tag 失败', err)
  }
}

// 🔍 从所有任务中自动识别最常用 tag
const detectTopTag = async () => {
  const res = await axios.get(`http://10.255.6.2:9090/tasks/user/${userID}`)
  const tasks = res.data
  const tagCount = {}
  tasks.forEach(task => {
    if (task.tag) {
      const t = task.tag.trim()
      if (t) tagCount[t] = (tagCount[t] || 0) + 1
    }
  })
  const sorted = Object.entries(tagCount).sort((a, b) => b[1] - a[1])
  tag.value = sorted[0]?.[0] || ''
}

// 🎯 调用推荐接口
const loadRecommendations = async () => {
  if (!tag.value) await detectTopTag()
  if (!tag.value) return

  try {
    const res = await getRecommendations(tag.value, userID)
    recommendations.value = res.data
  } catch (error) {
    console.error('❌ 推荐数据获取失败：', error)
  }
}

// 初始化：加载推荐 + 标签
onMounted(() => {
  loadRecommendations()
  loadAllTags()
})
</script>

<style scoped>
.recommendation-panel {
  padding: 20px;
  border-radius: 12px;
  background-color: #f9f9fb;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}

.input-group {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.tag-input {
  width: 300px;
}

.card-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recommend-card {
  background-color: #ffffff;
  border-radius: 8px;
  padding: 12px 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.2s;
}

.recommend-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.recommend-title {
  display: flex;
  flex-direction: column;
}

.recommend-link {
  font-weight: 500;
  color: #409eff;
  text-decoration: none;
}

.recommend-link:hover {
  text-decoration: underline;
}

.recommend-source {
  color: #999;
  font-size: 12px;
}
</style>
