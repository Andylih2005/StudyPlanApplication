<template>
  <div class="task-card">
    <div class="card-header">
      <span class="card-title">{{ task.title }}</span>
      <div class="tags-container">
        <el-tag v-for="tag in parseTags(task.tag)" :key="tag" type="info" size="small">{{ tag }}</el-tag>
      </div>
    </div>

    <p class="card-content">{{ task.content }}</p>

    <div class="card-footer">
      <span class="card-deadline">
        <template v-if="task.deadline">
          ⏰ {{ formatDeadline(task.deadline) }}
        </template>
        <template v-else-if="task.completed_at">
          ✅ {{ formatDeadline(task.completed_at) }}
        </template>
      </span>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue';

// 接收從父元件傳來的單一任務物件
defineProps({
  task: {
    type: Object,
    required: true
  }
});

// 解析標籤字串為陣列
const parseTags = (tag) => {
  if (!tag) return [];
  if (Array.isArray(tag)) return tag;
  return tag.split(',').map(t => t.trim());
};

// 格式化日期，只顯示年月日
const formatDeadline = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString(); // 使用本地化格式，例如 "2025/7/2"
}
</script>

<style scoped>
/* 這裡是單張卡片的詳細樣式 */
.task-card {
  background-color: #ffffff;
  border: 1px solid #e9eef3;
  border-radius: 12px;
  padding: 16px 20px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  transition: transform 0.2s, box-shadow 0.2s;
}

.task-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
}

.card-title {
  font-size: 1.15rem; /* 標題字體 */
  font-weight: 600;
  color: #333;
  word-break: break-all;
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  justify-content: flex-end;
  margin-left: 10px;
  flex-shrink: 0; /* 防止標籤被壓縮 */
}

.card-content {
  font-size: 0.9rem; /* 內容字體 */
  color: #606266;
  line-height: 1.5;
  margin: 8px 0 12px 0;
  flex-grow: 1; /* 讓內容區域自動撐開，確保 footer 在底部 */
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.card-deadline {
  font-size: 0.85rem; /* 日期字體 */
  color: #909399;
}
</style>