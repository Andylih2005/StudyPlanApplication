import axios from 'axios'

const BASE_URL = 'http://10.255.6.2:9090/reminder'  // 根据后端部署情况调整

// 新增提醒配置
export function addReminderConfig(config) {
  // config 应包含 priority, intervalMinutes, userID
  return axios.post(`${BASE_URL}/add`, config)
}

// 修改提醒时间
export function updateReminderTime(userID, minus, priority) {
  return axios.put(`${BASE_URL}/${userID}`, null, {
    params: {
      minus,
      priority
    }
  })
}

//get
// 获取用户全部提醒设置（优先级1~5）
export function getReminderConfigList(userID) {
  return axios.get(`${BASE_URL}/${userID}`)
}

