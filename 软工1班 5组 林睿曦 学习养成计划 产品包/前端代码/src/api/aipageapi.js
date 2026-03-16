import axios from 'axios'

const BASE_URL = 'http://10.255.6.2:9090' // 替换成你后端实际地址
 export const getTaskCompletionReport = (userID, tag = 'all') => {
  return axios.post(`${BASE_URL}/chat/${userID}`, null, {
    params: {
      tag
    }
  }).then(res => res.data)
}