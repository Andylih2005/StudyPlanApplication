<template>
  <div class="p-4 space-y-6">
    <h1 class="text-xl font-bold mb-4">學習資料清單</h1>

    <div class="button-flex-wrapper">
      <el-button type="warning" @click="folderDialogVisible = true">新增資料夾</el-button>
      <el-button type="primary" @click="openFileDialog">新增本地文件</el-button>
      <el-button type="success" @click="openUrlDialog">新增網址資料</el-button>
      <el-button type="danger" @click="opendeleteDialog">管理文件夹</el-button>
    </div>
    <el-input v-model="searchTitle" placeholder="輸入標題關鍵字" class="w-60 mt-2" clearable>
      <template #append>
        <el-button type="primary" @click="handleSearch">搜尋</el-button>
      </template>
    </el-input>
    <p></p>
    <el-select v-model="selectedFolderIDForView" placeholder="選擇資料夾以查看資料" @change="handleFolderChange"
      class="block mt-4 w-60" clearable>
      <el-option :value="null" label="📁 未分類" />
      <el-option v-for="folder in folderList" :key="folder.folderID" :label="`📁 ${folder.folderName}`"
        :value="folder.folderID" />
    </el-select>

    <!-- 無限滾動資料清單 -->
    <div
      class="flex-1 p-5 bg-white rounded-xl shadow-md hover:shadow-xl transform hover:-translate-y-0.5 transition-all duration-300"
      v-infinite-scroll="loadMaterials" infinite-scroll-disabled="loading || !hasMore" infinite-scroll-distance="50"
      style="max-height: 400px; overflow: auto" v-loading="previewloading">
      <ul class="space-y-4">
        <MaterialCard v-for="material in materials" :key="material.materialID" :material="material"
          :progress="progressMap[material.materialID] || 0" @preview="previewMaterial" @delete="deleteMaterial" />
      </ul>
      <p v-if="loading" class="text-center text-gray-400 mt-2">載入中...</p>
      <p v-if="!hasMore && materials.length > 0" class="text-center text-gray-400 mt-2">
        已載入全部資料
      </p>
    </div>

    <!-- 新增資料夾 -->
    <el-dialog title="新增資料夾" v-model="folderDialogVisible" width="450px">
      <el-form :model="folderForm" label-width="100px">
        <el-form-item label="資料夾名稱" required>
          <el-input v-model="folderForm.folderName" placeholder="請輸入名稱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="button-flex-wrapper1">
          <el-button @click="folderDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitFolder">確定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 上傳文件 -->
    <el-dialog title="新增本地文件" v-model="fileDialogVisible" width="400px">
      <el-form label-width="80px">
        <el-form-item label="資料夾">
          <el-select v-model="selectedFolderID" placeholder="請選擇資料夾">
            <el-option :value="null" label="📁 未分類" />
            <el-option v-for="folder in folderList" :key="folder.folderID" :label="folder.folderName"
              :value="folder.folderID" />
          </el-select>
        </el-form-item>
      </el-form>
      <el-upload v-loading="uploadloading" class="upload-demo" drag action="" :auto-upload="false" :file-list="fileList"
        :on-change="handleFileChange" :disabled="fileList.length > 0">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">將文件拖到此處，或<em>點擊上傳</em></div>
      </el-upload>
      <template #footer>
        <div class="button-flex-wrapper1">
          <el-button type="danger" @click="clearAllFiles">删除已选文件</el-button>
          <el-button @click="fileDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitFile">確定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 上傳網址 -->
    <el-dialog title="新增網址資料" v-model="urlDialogVisible" width="400px">
      <el-form :model="urlForm" label-width="80px">
        <el-form-item label="標題" required>
          <el-input v-model="urlForm.title" placeholder="請輸入標題" />
        </el-form-item>
        <el-form-item label="網址" required>
          <el-input v-model="urlForm.url" placeholder="請輸入網址" />
        </el-form-item>
        <el-form-item label="標籤">
          <el-input v-model="urlForm.tags" placeholder="可選" />
        </el-form-item>
        <el-form-item label="資料夾">
          <el-select v-model="urlForm.folderID" placeholder="請選擇資料夾">
            <el-option :value="null" label="📁 未分類" />
            <el-option v-for="folder in folderList" :key="folder.folderID" :label="folder.folderName"
              :value="folder.folderID" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="button-flex-wrapper1">
          <el-button @click="urlDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitUrl">確定</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog title="选择要删除的文件夹" v-model="deletedialogVisible" width="500px" :before-close="handleClose">
      <div v-if="loading">加载中...</div>
      <div v-else>
        <el-checkbox-group v-model="selectedFolderIDs">
          <el-checkbox v-for="folder in folderList" :key="folder.folderID" :value="folder.folderID">
            {{ folder.folderName }}
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <template #footer>
        <div class="button-flex-wrapper1">
          <el-button @click="deletedialogVisible = false">取消</el-button>
          <el-button type="danger" :disabled="selectedFolderIDs.length === 0" @click="deleteSelectedFolders">
            删除
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="videoDialogVisible" title="預覽影片" width="60%">
      <video ref="videoPlayer" :src="videoUrl" controls style="width: 100%" @timeupdate="onVideoProgress"
        @ended="onVideoEnded"></video>
    </el-dialog>
  </div>
</template>

<script setup>
import MaterialCard from '@/components/MaterialCard.vue'
import { ref, onMounted, onBeforeUnmount, reactive, watch } from 'vue'
import { useUserStore } from '@/stores/user'
import {
  getMaterialsByFolderPaged,
  previewMaterialById,
  deleteMaterialById,
  uploadMaterial,
  saveUrlMaterial,
  getUncategorizedMaterials,
  findByUserAndTitle,
} from '@/api/materialapi'
import { createFolder, getFoldersByUser, deleteFolder } from '@/api/folderapi'
import { ElMessage } from 'element-plus'
import { updateProgress, getProgress } from '@/api/progressapi'
// 狀態
const materials = ref([])
const videoDialogVisible = ref(false)
const videoPlayer = ref(null)
const videoUrl = ref('')
const lastReportedProgress = ref(0)
const currentMaterial = ref(null)
const folderList = ref([])
const fileList = ref([])
const currentFolderID = ref(null)
const page = ref(1)
const pageSize = 10
const hasMore = ref(true)
const loading = ref(false)
const selectedFolderIDForView = ref(null)
const userStore = useUserStore()
const selectedFolderIDs = ref([])
const userID = userStore.userID
const searchTitle = ref('')
const previewloading = ref(false)
const uploadloading = ref(false)
// 對話框
const fileDialogVisible = ref(false)
const urlDialogVisible = ref(false)
const folderDialogVisible = ref(false)
const deletedialogVisible = ref(false)
const selectedFolderID = ref('')
const urlForm = ref({
  title: '',
  url: '',
  tags: '',
  folderID: '',
})
const folderForm = ref({
  folderName: '',
})
const progressMap = reactive({})
let progressInterval = null
function startProgressPolling() {
  progressInterval = setInterval(async () => {
    const list = materials.value
    await Promise.all(
      list.map(async (material) => {
        try {
          const res = await getProgress(userID, material.materialID)
          progressMap[material.materialID] = res.data?.progress ?? 0
        } catch {
          progressMap[material.materialID] = 0
        }
      })
    )
  }, 3000)
}
const loadUncategorizedMaterials = async () => {
  loading.value = true
  try {
    const res = await getUncategorizedMaterials(userID)
    const list = res.data || []
    materials.value = list

    await Promise.all(
      list.map(async (material) => {
        try {
          const res = await getProgress(userID, material.materialID)
          progressMap[material.materialID] = res.data?.progress ?? 0
        } catch {
          progressMap[material.materialID] = 0 // 出错时默认0
        }
      })
    )
    hasMore.value = false // 因為不是分頁載入
    currentFolderID.value = null
  } catch (e) {
    ElMessage.error('載入未分類資料失敗')
    console.error(e)
  } finally {
    loading.value = false
  }
}
// 初始化
onMounted(async () => {
  await fetchFolders()
  await loadUncategorizedMaterials()
  startProgressPolling()
})
onBeforeUnmount(() => {
  clearInterval(progressInterval)
})
// 資料夾
const fetchFolders = async () => {
  try {
    const res = await getFoldersByUser(userID)
    folderList.value = res.data || []
  } catch (e) {
    console.error('獲取資料夾失敗', e)
  }
}

const handleFolderChange = async (folderID) => {
  if (folderID === null) {
    await loadUncategorizedMaterials()
  } else {
    await resetAndLoadMaterials(folderID)
  }
}

// 分頁載入資料

const loadMaterials = async () => {
  if (loading.value || !hasMore.value) return
  loading.value = true
  try {
    const res = await getMaterialsByFolderPaged(userID, currentFolderID.value, page.value, pageSize)
    const list = res.data || []
    materials.value.push(...list)
    // 异步批量加载每个 material 的进度
    await Promise.all(
      list.map(async (material) => {
        try {
          const res = await getProgress(userID, material.materialID)
          progressMap[material.materialID] = res.data?.progress ?? 0
        } catch {
          progressMap[material.materialID] = 0 // 出错时默认0
        }
      })
    )

    if (list.length < pageSize) hasMore.value = false
    page.value++
  } catch (e) {
    ElMessage.error('載入資料失敗')
    console.error(e)
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  if (!searchTitle.value.trim()) {
    ElMessage.warning('請輸入關鍵字')
    return
  }
  try {
    loading.value = true
    const res = await findByUserAndTitle(userID, searchTitle.value.trim())
    materials.value = res.data || []
    hasMore.value = false // 搜尋結果不分頁
    currentFolderID.value = null
    selectedFolderIDForView.value = null
  } catch (e) {
    ElMessage.error('搜尋失敗')
    console.error(e)
  } finally {
    loading.value = false
  }
}
const resetAndLoadMaterials = async (folderID) => {
  currentFolderID.value = folderID
  materials.value = []
  page.value = 1
  hasMore.value = true
  await loadMaterials()
}

// 文件操作
const openFileDialog = () => {
  fileDialogVisible.value = true
}
const opendeleteDialog = async () => {
  deletedialogVisible.value = true
  loading.value = true
  try {
    await fetchFolders() // 重新加载最新文件夹列表
  } catch (e) {
    ElMessage.error('获取文件夹失败')
  } finally {
    loading.value = false
    selectedFolderIDs.value = [] // 打开时清空已选
  }
}
const deleteSelectedFolders = async () => {
  if (selectedFolderIDs.value.length === 0) {
    ElMessage.warning('请先选择要删除的文件夹')
    return
  }
  loading.value = true
  try {
    await Promise.all(selectedFolderIDs.value.map((id) => deleteFolder(id)))
    ElMessage.success('删除成功')
    deletedialogVisible.value = false
    selectedFolderIDs.value = []
    await fetchFolders()
    currentFolderID.value = null
    selectedFolderIDForView.value = null
    loadUncategorizedMaterials()
  } catch (e) {
    ElMessage.error('删除失败，请稍后再试')
  } finally {
    loading.value = false
  }
}
const handleFileChange = (file, rawList) => {
  if (fileList.value.length > 0) {
    ElMessage.warning('只能上傳一個文件，請先移除原有文件')
    return
  }
  fileList.value = rawList.map((f) => f.raw)
}
const submitFile = async () => {
  if (!fileList.value.length) {
    ElMessage.warning('請先選擇文件')
    return
  }
  try {
    uploadloading.value = true
    const res = await uploadMaterial(fileList.value[0], userID, selectedFolderID.value || null)
    const materialID = res?.data?.materialID
    await updateProgress({
      userID,
      materialID,
      progress: 0,
      progressType: fileList.value[0].type,
    })
    ElMessage.success('文件上传成功')
    uploadloading.value = false
    fileDialogVisible.value = false
    fileList.value = []
    if (currentFolderID.value === null) {
      await loadUncategorizedMaterials()
    } else {
      await resetAndLoadMaterials(currentFolderID.value)
    }
  } catch (err) {
    ElMessage.error('文件上传失败')
    console.error(err)
    uploadloading.value = false
  }
}

// 網址操作
const openUrlDialog = () => {
  urlDialogVisible.value = true
}
const submitUrl = async () => {
  if (!urlForm.value.title || !urlForm.value.url) {
    ElMessage.warning('標題和網址為必填項')
    return
  }
  try {
    const formData = new FormData()
    formData.append('userID', userID)
    formData.append('title', urlForm.value.title)
    formData.append('url', urlForm.value.url)
    formData.append('tags', urlForm.value.tags || '')
    if (urlForm.value.folderID != null) {
      formData.append('folderID', urlForm.value.folderID)
    }
    const res = await saveUrlMaterial(formData)
    const materialID = res?.data?.materialID
    await updateProgress({ userID, materialID, progress: 0, progressType: 'text/html' })
    ElMessage.success('網址資料儲存成功')
    urlDialogVisible.value = false
    urlForm.value = { title: '', url: '', tags: '', folderID: '' }
    if (currentFolderID.value === null) {
      await loadUncategorizedMaterials()
    } else {
      await resetAndLoadMaterials(currentFolderID.value)
    }
  } catch (err) {
    ElMessage.error('儲存網址失敗')
    console.error(err)
  }
}

// 預覽與刪除
const previewMaterial = async (material) => {
  try {
    previewloading.value = true
    if (material.fileName) {
      // 檔案型資料（PDF、影片、PPT 等）
      const res = await previewMaterialById(material.materialID)
      const res1 = await getProgress(userID, material.materialID)
      const progressType = res1.data.progressType
      const url = URL.createObjectURL(res.data)
      console.log(progressType)
      if (progressType === 'pdf' || progressType === 'word' || progressType === 'ppt') {
        window.open(
          `/pdf-viewer?materialID=${material.materialID}&url=${encodeURIComponent(
            url
          )}&userID=${userID}`,
          '_blank'
        )
      } else {
        if (progressType === 'video') {
          videoUrl.value = url
          lastReportedProgress.value = 0
          currentMaterial.value = material
          videoDialogVisible.value = true
          previewloading.value = false
        } else {
          window.open(url, '_blank')
          await updateProgress({
            userID,
            materialID: material.materialID,
            progress: 100,
            progressType: progressType,
          })
        }
      }
    } else if (material.url) {
      window.open(material.url, '_blank')
      await updateProgress({
        userID,
        materialID: material.materialID,
        progress: 100,
        progressType: 'url',
      })
    }
  } catch (e) {
    console.error('預覽失敗:', e)
  } finally {
    previewloading.value = false
  }
}
watch(videoDialogVisible, (visible) => {
  if (!visible && videoPlayer.value) {
    const video = videoPlayer.value
    video.pause()
    video.currentTime = 0
    if (video.src.startsWith('blob:')) {
      URL.revokeObjectURL(video.src)
    }
    videoUrl.value = ''
    lastReportedProgress.value = 0
  }
})
const onVideoProgress = () => {
  const video = videoPlayer.value
  if (!video || video.duration === 0) return

  const percent = Math.floor((video.currentTime / video.duration) * 100)
  if (percent % 2 === 0 && percent !== lastReportedProgress.value) {
    lastReportedProgress.value = percent
    updateProgress({
      userID,
      materialID: currentMaterial.value.materialID,
      progress: percent,
      progressType: 'video',
    }).catch(console.error)
  }
}
const onVideoEnded = () => {
  updateProgress({
    userID,
    materialID: currentMaterial.value.materialID,
    progress: 100,
    progressType: 'video',
  }).catch(console.error)
}
const deleteMaterial = async (id) => {
  await deleteMaterialById(id)
  if (currentFolderID.value === null) {
    await loadUncategorizedMaterials()
  } else {
    await resetAndLoadMaterials(currentFolderID.value)
  }
}
// 建立資料夾
const submitFolder = async () => {
  if (!folderForm.value.folderName.trim()) {
    ElMessage.warning('請輸入資料夾名稱')
    return
  }
  try {
    await createFolder({ userID, folderName: folderForm.value.folderName })
    ElMessage.success('資料夾建立成功')
    folderDialogVisible.value = false
    folderForm.value.folderName = ''
    await fetchFolders()
  } catch (err) {
    ElMessage.error('建立資料夾失敗')
    console.error(err)
  }
}
const clearAllFiles = () => {
  fileList.value = []
}
</script>

<style scoped>
@media (max-width: 480px) {
  .action-button {
    flex: 1 1 100%;
  }
}

.button-flex-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 16px;
  margin-bottom: 20px;
}
.button-flex-wrapper1 {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  padding: 10px 0;
}

.button-flex-wrapper .el-button {
  flex: 1 1 200px;
  /* 最小宽度200px，自适应缩放 */
  max-width: 250px;
  padding: 12px 0;
  border-radius: 10px;
  font-weight: bold;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  text-align: center;
}

.button-flex-wrapper .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}
</style>
