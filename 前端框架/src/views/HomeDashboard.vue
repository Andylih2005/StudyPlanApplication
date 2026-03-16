<template>
    <div class="home-dashboard">
        <!-- ToDo 列表区域 -->
        <div class="todo-area">
            <el-card class="card-box">
                <div class="todo-header">
                    <div class="section-left">
                        <span class="section-title">任务列表</span>
                        <el-button size="small" type="default" plain @click="toggleExpand">
                            {{ expanded ? '收起任务' : '查看更多任务' }}
                        </el-button>
                    </div>
                    <el-dropdown>
                        <el-button type="primary">
                            添加 / 删除 / 编辑
                            <i class="el-icon-arrow-down el-icon--right" />
                        </el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item @click="showAddTask = true">添加任务</el-dropdown-item>
                                <el-dropdown-item @click="showDeleteDialog = true">删除模式</el-dropdown-item>
                                <el-dropdown-item @click="showEditDialog">{{ isEditMode ? '完成编辑' : '编辑模式'
                                    }}</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
                <div class="task-grid-wrapper" :class="{ expanded: expanded }">
                    <div class="task-grid">
                        <TaskCard v-for="task in sortedTasks" :key="task.taskID" :task="task"
                            @taskCompleted="removeTask" :isEditMode="isEditMode" @click-card="handleEditCard" />
                    </div>
                </div>
            </el-card>
        </div>

        <!-- 推荐 + 图表 -->
        <div class="bottom-row">
            <!-- 推荐任务区 -->
            <el-card class="card-box recommend-card">
            <RecommendTask />
            </el-card>

        </div>
    </div>
    <AddTaskDialog v-model="showAddTask" :all-tags="allTags" @submit="handleTaskSubmit" />
    <EditTaskDialog v-if="editdialogVisible" :task="selectedTask" :visible="editdialogVisible" :all-tags="allTags"
        @close="editdialogVisible = false" @save="handleTaskSave" />
    <DeleteDialog v-model:dialogVisible="showDeleteDialog" :task="sortedTasks" v-model:selected="selectedTaskIds"
        @deleted="handleDelete" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import AddTaskDialog from '@/components/AddTaskDialog.vue'
import { useUserStore } from '../stores/user'
import { getTasksByUser } from '@/api/taskapi'
import TaskCard from '@/components/TaskCard.vue'
import EditTaskDialog from '@/components/EditTaskDialog.vue'
import DeleteDialog from '@/components/DeleteDialog.vue'
import { deleteTask } from '@/api/taskapi'
import RecommendTask from '@/components/RecommendTask.vue'

import { computed } from 'vue'
const userStore = useUserStore()

const allTags = computed(() => {
    const tagSet = new Set()
    tasks.value.forEach(task => {
        if (task.userID === currentUser) {
            if (Array.isArray(task.tag)) {
                task.tag.forEach(tag => tagSet.add(tag))
            } else if (typeof task.tag === 'string') {
                tagSet.add(task.tag)
            }
        }
    })
    return [...tagSet]
})



//优先级排序
const sortedTasks = computed(() => {
    return tasks.value.slice().sort((a, b) => b.priority - a.priority)
})


//show task
const tasks = ref([])
const currentUser = userStore.userID // 可从登录信息中读取

const loadTasks = async () => {
    try {
        const res = await getTasksByUser(currentUser)
        tasks.value = res.data
    } catch (err) {
        console.error('获取任务失败', err)
    }
}

const expanded = ref(false)
const toggleExpand = () => {
    expanded.value = !expanded.value
}

//add task
const showAddTask = ref(false)

//edit task
const editdialogVisible = ref(false)
const selectedTask = ref({})
const isEditMode = ref(false)

const showEditDialog = () => {
    isEditMode.value = !isEditMode.value
}

const handleEditCard = (task) => {
    console.log('点击卡片', task.taskID)
    if (!isEditMode.value) return
    selectedTask.value = { ...task } // 复制防止直接修改
    editdialogVisible.value = true
}

const handleTaskSave = (updatedTask) => {
    const index = tasks.value.findIndex(t => t.taskID === updatedTask.taskID)
    if (index !== -1) {
        tasks.value[index] = updatedTask
        tasks.value = [...tasks.value] // ✅ 触发响应式更新，确保 sortedTasks 重新计算

    }
    editdialogVisible.value = false
}

//delete task
const showDeleteDialog = ref(false);
const selectedTaskIds = ref([]); // 用于保存勾选的任务id列表

const handleDelete = async (deletedIds) => {
    console.log('要删除的任务ID:', deletedIds);

    for (const id of deletedIds) {
        console.log('要删除的任务ID:', id);
        await deleteTask(id);
    }
    tasks.value = tasks.value.filter(task => !deletedIds.includes(task.taskID));

    selectedTaskIds.value = [];
    showDeleteDialog.value = false;
}

//finish task
const removeTask = (id) => {
    console.log('要删除的 id 是：', id)
    console.log('所有任务 id 是：', tasks.value.map(t => t.id))
    tasks.value = tasks.value.filter(task => task.taskID !== id)
}

const handleTaskSubmit = (taskData) => {
    console.log('收到新任务：', taskData)
    // 可 push 到任务列表或提交 API
    loadTasks()
}

onMounted(() => {
    loadTasks()
})
</script>

<style scoped>
.home-dashboard {
    padding: 20px;
}

.page-title {
    font-size: 24px;
    font-weight: 600;
    margin-bottom: 24px;
    color: #333;
}

/* 白卡片统一样式 */
.card-box {
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
    overflow: hidden;
    margin-top: -10px;
}

/* todo 区域 */
.todo-area {
    margin-bottom: 24px;
}

.todo-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
}

/* 下半部分左右分布 */
.bottom-row {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
}

/* 推荐任务 */
.recommend-card {
    flex: 1 1 300px;
}

.recommend-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
}

.recommend-list {
    padding-left: 18px;
    color: #555;
}

.recommend-list li {
    margin: 6px 0;
}

/* 响应式布局 */
@media (max-width: 768px) {
    .bottom-row {
        flex-direction: column;
    }
}

/* show task*/
.task-section {
    margin-top: 20px;
    padding: 16px;
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: nowrap;
    gap: 12px;
    margin-bottom: 12px;
}

.section-left {
    display: flex;
    align-items: center;
    gap: 12px;
}

.section-title {
    font-size: 20px;
    font-weight: bold;
    color: #333;
    white-space: nowrap;
}

.section-actions {
    display: flex;
    gap: 10px;
    flex-shrink: 0;
}

/* ✅ Bonus: 响应式适配移动端 */
@media (max-width: 600px) {
    .section-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 6px;
    }

    .section-actions {
        justify-content: flex-start;
        flex-wrap: wrap;
    }
}

.el-button {
    border-radius: 20px;
    font-weight: 500;
}

.el-button--default {
    background-color: #f6f9fc;
    color: #409eff;
    border-color: #d9ecff;
}


.task-grid-wrapper {
    max-height: 350px;
    /* 默认高度限制 */
    overflow-y: auto;
    transition: max-height 0.3s ease;
    padding-right: 8px;
}

/*展开按钮渲染*/
.task-grid-wrapper.expanded {
    max-height: none;
    overflow-y: visible;
}

.task-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 16px;
}

.task-card:hover {
    box-shadow: 0 0 12px rgba(0, 0, 0, 0.08);
}
</style>