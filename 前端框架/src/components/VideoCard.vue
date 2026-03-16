<template>
  <div class="video-wrapper">
    <iframe
      class="bili-player"
      :src="currentEmbedUrl"
      frameborder="0"
      allowfullscreen
      scrolling="no"
    ></iframe>
    <el-button type="primary" @click="refreshVideo" style="margin-top: 16px">
      换一个视频
    </el-button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

// B站视频 BV 号列表
const bvList = [
  'BV14LL8zUEbf',
  'BV1oC411G7FB',
  'BV1hWK5zREpw',
  'BV1zg411C7an',
  'BV1CwNHzFEjM',
  'BV13U4y1r7ig',
  'BV1Pw4m1v7PW',
  'BV1Vr1RYYEuY',
  'BV1Q5411U7SN',
  'BV1Q3411e7SZ',
  'BV1Ss411V7hQ',
  'BV1VY411V7AG',
  'BV1p2NyztEur',
  'BV1AL4y1E7pi',
  'BV1UJtmekErb',
  'BV11M4y1F7u2',
  'BV1UF411g71A',
  'BV1Dr421M7jY',
  'BV1zVVnz3EkC'
]

// 当前嵌入链接
const currentEmbedUrl = ref('')

function refreshVideo() {
  const index = Math.floor(Math.random() * bvList.length)
  const bv = bvList[index]
  currentEmbedUrl.value = `https://player.bilibili.com/player.html?bvid=${bv}&autoplay=1`
}

// 初次加载
onMounted(() => {
  refreshVideo()
})
</script>

<style scoped>
.video-wrapper {
  max-width: 700px;
  margin: auto;
  text-align: center;
}
.bili-player {
  width: 100%;
  height: 520px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}
</style>
