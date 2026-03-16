import axios from 'axios'

const BASE_URL = 'http://10.255.6.2:9090/completion'

export default {
  // 添加任务完成记录
  addCompletion(data) {
    return axios.post(BASE_URL, data)
  },

  // 删除任务完成记录
  deleteCompletion(taskID, userID) {
    return axios.delete(BASE_URL, {
      params: { taskID, userID },
    })
  },
}

// 根据 userID 获取该用户的任务完成记录
export function getUserCompletions(userID) {
  return axios.get(`${BASE_URL}/${userID}`)
}

export function getTaskByTag(userID, tag) {
  axios.get(`/tag/${tag}/${userID}`)
}