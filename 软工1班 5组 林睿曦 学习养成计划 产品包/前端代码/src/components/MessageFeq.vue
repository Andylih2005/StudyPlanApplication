<template>
    <el-dialog v-model="visible" title="通知频率设定" width="500px" @open="onOpen" @close="handleClose">
      <div class="dialog-body">

        <el-form v-loading="loading" :model="form" label-width="120px" element-loading-text="加载配置中...">
            <el-form-item v-for="config in form.configs" :key="config.priority" :label="`优先级 ${config.priority}`">
                <el-input-number v-model="config.intervalMinutes" :min="1" />
                <span style="margin-left: 10px;">分钟</span>
            </el-form-item>
        </el-form>
      </div>

        <template #footer>
            <div class="dialog-footer">

                <el-button @click="visible = false">取消</el-button>
                <el-button type="primary" @click="submit" :loading="saving">保存</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getReminderConfigList, updateReminderTime } from '@/api/messageFeq'

const props = defineProps({
    modelValue: Boolean,
    userID: {
        type: String,
        required: true
    }
})
const emit = defineEmits(['update:modelValue', 'updated'])

// 弹窗显示控制
const visible = ref(false)
watch(() => props.modelValue, val => {
    visible.value = val
})
watch(visible, val => {
    emit('update:modelValue', val)
})

const loading = ref(false)
const saving = ref(false)

// 表单数据结构
const form = ref({
    configs: []
})

// 弹窗打开时触发，用于加载数据
const onOpen = async () => {
    loading.value = true
    try {
        const res = await getReminderConfigList(props.userID)
        const dbConfigs = res.data || []

        // 创建一个从优先级到配置的映射，方便查找
        const configMap = new Map(dbConfigs.map(item => [item.priority, item]))

        // 生成包含所有1-5优先级的完整列表，如果数据库中不存在则使用默认值
        const newConfigs = Array.from({ length: 5 }, (_, i) => {
            const priority = i + 1
            const existingConfig = configMap.get(priority)
            return {
                priority: priority,
                intervalMinutes: existingConfig?.intervalMinutes ?? 60 // 提供一个安全的默认值
            }
        })

        form.value.configs = newConfigs

    } catch (err) {
        console.error('加载提醒配置失败', err)
        ElMessage.error('加载配置失败，请重试。')
        visible.value = false // 加载失败直接关闭弹窗
    } finally {
        loading.value = false
    }
}

const handleClose = () => {
    // 关闭时可以清空表单，避免下次打开时闪现旧数据
    form.value.configs = []
}

// 提交更新
const submit = async () => {
    saving.value = true
    try {
        // 创建一个包含所有更新请求的Promise数组
        const updatePromises = form.value.configs.map(config =>
            updateReminderTime(props.userID, config.intervalMinutes, config.priority)
        )

        // 使用 Promise.all 并发执行所有请求
        await Promise.all(updatePromises)

        ElMessage.success('提醒频率修改成功')
        emit('updated')
        visible.value = false
    } catch (err) {
        console.error('修改提醒频率失败', err)
        ElMessage.error('修改失败，请稍后重试。')
    } finally {
        saving.value = false
    }
}
</script>

<style scoped>
.dialog-body {
  display: flex;
  flex-direction: column;
  align-items: center; /* 内容居中 */
  gap: 12px;
  margin-top: 10px;
}

.row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.label {
  width: 80px;
  text-align: right;
}

.input {
  width: 120px;
}

.unit {
  width: 40px;
}
  
.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 12px;
  padding-top: 10px;
}

::v-deep(.el-dialog__wrapper) {
  display: flex !important;
  align-items: center;
}

.dialog-footer {

    display: flex;
    justify-content: flex-end;
    gap: 10px;
    /* 两个按钮之间的间距 */
}
</style>