<template>
  <div class="pdf-container" ref="containerRef" @scroll="onScroll"
    style="height: 600px; overflow-y: auto; width: 800px; margin: 20px auto">
    <VuePdfEmbed :source="pdfUrl" @rendered="onPdfRendered" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProgress, updateProgress } from '@/api/progressapi'
import VuePdfEmbed from 'vue-pdf-embed'

const route = useRoute()
const materialID = route.query.materialID
const url = decodeURIComponent(route.query.url)
const userID = route.query.userID
const pdfUrl = ref(url)

const containerRef = ref(null)
let lastProgressPercent = 0

let savedProgressPercent = 0 // 保存进度等 PDF 渲染完后再滚动用

function onScroll() {
  if (!containerRef.value) return
  const el = containerRef.value
  const scrollTop = el.scrollTop
  const scrollHeight = el.scrollHeight - el.clientHeight
  const percent = Math.floor((scrollTop / scrollHeight) * 100)
  if (percent !== lastProgressPercent) {
    lastProgressPercent = percent
    updateProgress({
      userID,
      materialID,
      progress: percent,
      progressType: 'pdf',
    })
  }
}

// ✅ 等待 PDF 渲染完成后执行滚动操作
async function onPdfRendered() {
  if (!containerRef.value || !savedProgressPercent) return
  await new Promise((resolve) => setTimeout(resolve, 50)) // 等 DOM 更新更稳妥
  const el = containerRef.value
  const totalScrollHeight = el.scrollHeight - el.clientHeight
  el.scrollTop = (savedProgressPercent / 100) * totalScrollHeight
}

onMounted(async () => {
  const res = await getProgress(userID, materialID)
  if (res?.data?.progress) {
    savedProgressPercent = res.data.progress
  }
})
</script>
