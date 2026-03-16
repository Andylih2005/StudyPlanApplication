import axios from 'axios'
const BASE_URL = 'http://10.255.6.2:9090/api'

// 1. 更新进度
export function updateProgress(progress) {
  return axios.post(`${BASE_URL}/progress/update`, progress)
}

// 2. 获取单个资料的进度
export function getProgress(userID, materialID) {
  return axios.get(`${BASE_URL}/progress`, {
    params: { userID, materialID },
  })
}

// 3. 获取某个用户的所有进度
export function getAllProgressByUser(userID) {
  return axios.get(`${BASE_URL}/progress/user/${userID}`)
}
