<template>
  <li class="rounded-2xl border shadow-md p-4 bg-white">
    <div v-if="material.folderName" class="text-sm text-gray-500 mb-2">
      📂 {{ material.folderName }}
    </div>

    <!-- 同行內容 -->
    <div class="flex items-center justify-between gap-4 overflow-hidden">
      <!-- 資訊欄 -->
      <div v-if="material.fileName" class="text-sm text-gray-700 truncate max-w-[70%]">
        {{ fileIcon(material.fileName) }} 文件：{{ material.fileName }}
      </div>
      <div v-else-if="material.url" class="text-sm text-blue-600 underline cursor-pointer truncate max-w-[70%]"
        @click="openUrl(material.url)">
        🔗 網址：{{ material.title }}
      </div>

      <!-- 操作按鈕 -->
      <div class="flex flex-row gap-2 flex-shrink-0">
        <button class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-1 rounded-md text-sm"
          @click="$emit('preview', material)">
          預覽
        </button>
        <button class="bg-red-500 hover:bg-red-600 text-white px-4 py-1 rounded-md text-sm"
          @click="$emit('delete', material.materialID)">
          刪除
        </button>
      </div>
    </div>

    <!-- 進度條 -->
    <div v-if="progress != null" class="mt-2">
      <el-progress :percentage="progress" />
    </div>
  </li>
</template>


<script setup>
defineProps({
  material: Object,
  progress: {
    type: Number,
    default: 0,
  },
})
defineEmits(['preview', 'delete'])

function openUrl(url) {
  window.open(url, '_blank')
}

function fileIcon(fileName) {
  if (fileName.endsWith('.pdf')) return '📄'
  if (fileName.endsWith('.png') || fileName.endsWith('.jpg') || fileName.endsWith('.jpeg'))
    return '🖼'
  if (fileName.endsWith('.mp4') || fileName.endsWith('.mov')) return '🎬'
  if (fileName.endsWith('.mp3')) return '🎵'
  return '📎' // 通用文件
}
</script>
