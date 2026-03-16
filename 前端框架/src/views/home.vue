<template>
  <div class="common-layout">
    <el-container class="homepage-container">
      <el-header style="
          display: flex;
          align-items: center;
          justify-content: flex-end;
          font-size: 12px;
          height: 60px;
        ">
        <el-text class="mx-1" style="margin-right: 8px">欢迎你 {{ userStore.userID }}</el-text>
        <el-dropdown>
          <el-icon style="margin-right: 8px">
            <setting />
          </el-icon>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="handleLogout">Logout</el-dropdown-item>
              <el-dropdown-item @click="MessageMethods">通知方式设定</el-dropdown-item>
              <el-dropdown-item @click="showDialog = true">通知频率设定</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-container>
        <el-aside width="200px" style="background-color: #e0f2ff">
          <el-menu default-active="1" class="el-menu-vertical-demo" background-color="#f5f7fa"
            @select="(i) => (activeIndex = i)">
            <el-menu-item index="1">首頁</el-menu-item>
            <el-menu-item index="2">分析圖表</el-menu-item>
            <el-menu-item index="3">學習資料</el-menu-item>
            <el-menu-item index="4">番茄鐘</el-menu-item>
            <el-menu-item index="5">學習歷史回顧</el-menu-item>
            <el-menu-item index="6">AI智能反饋</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main>
          <div class="page-content">
            <component :is="componentsMap[activeIndex]" />
          </div>
        </el-main>
      </el-container>
    </el-container>
    <MessageFeq v-model="showDialog" :userID="userStore.userID" @updated="onUpdated" />

  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { onMounted, onUnmounted } from 'vue';
import axios from 'axios';
import { Menu as IconMenu, Message, Setting } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { useRouter } from 'vue-router'
import { fetchReminders } from '@/api/taskapi'
import HomeDashboard from './HomeDashboard.vue'
import AnalysisChart from './AnalysisChart.vue'
import StudyMaterial from './StudyMaterial.vue'
import TaskTracker from './TaskTracker.vue'
import StudyTracker from './StudyTracker.vue';
import AiPage from './AiPage.vue';
import MessageFeq from '@/components/MessageFeq.vue';

const activeIndex = ref('1')
const componentsMap = {
  '1': HomeDashboard,
  '2': AnalysisChart,
  '3': StudyMaterial,
  '4': TaskTracker,
  '5': StudyTracker,
  '6': AiPage
}

const router = useRouter()
const userStore = useUserStore()
const dialogVisible = ref(false)

function handleLogout() {
  userStore.setUserID(null)
  router.push('/login')
}

function MessageMethods() {
  if ('Notification' in window) {
    Notification.requestPermission().then((permission) => {
      if (permission === "granted") {
        console.log("✅ 通知权限已允许");
      } else if (permission === "denied") {
        console.log("❌ 用户拒绝了通知权限");
        alert('通知权限已被拒绝。请点击浏览器地址栏左侧的🔒图标，选择「站点设置」，手动开启通知权限。')
      } else {
        console.log("⏳ 用户关闭了提示窗口");
      }
    });
  } else {
    alert('当前浏览器不支持通知功能')
  }
}

const showDialog = ref(false)

const onUpdated = () => {
  console.log('提醒时间修改成功')
}

let reminderTimer = null;

onMounted(() => {
  //请求通知
  if ('Notification' in window && Notification.permission !== 'granted') {
    Notification.requestPermission().then(permission => {
      if (permission === 'granted') {
        console.log('🎉 用户已授权通知权限');
      } else if (permission === 'denied') {
        console.warn('❌ 用户拒绝了通知权限');
      } else {
        console.log('🤔 用户暂未选择是否授权');
      }
    });
  }


  if (userStore.userID) {
    // 首次立即检测
    fetchReminders(userStore.userID);

    // 每分钟检测一次
    reminderTimer = setInterval(() => {
      fetchReminders(userStore.userID);
    }, 60 * 1000);
  }


});

onUnmounted(() => {
  if (reminderTimer) {
    clearInterval(reminderTimer);
  }
});
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.content {
  flex: 1;
  padding: 20px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}


.homepage-container .el-header {
  position: relative;
  background-color: var(--el-color-primary-light-7);
  color: var(--el-text-color-primary);
}

.homepage-container {
  min-height: 100vh;
  /* 视口高度 */
}

.form-card {
  max-width: 600px;
  margin: 20px auto;
  padding: 24px;
  background-color: #ffffff;
  border: 1px solid #dcdfe6;
  /* 淡灰边框 */
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  /* light shadow */
}
</style>