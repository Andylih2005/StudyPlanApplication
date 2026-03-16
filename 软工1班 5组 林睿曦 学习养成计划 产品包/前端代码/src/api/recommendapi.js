import axios from 'axios'

const BASE_URL = 'http://10.255.6.2:9090'

export function getRecommendations(tag, userID) {
  return axios.get(`${BASE_URL}/recommendations`, {
    params: { tag, userID }
  })
}
