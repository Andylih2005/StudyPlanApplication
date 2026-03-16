// src/api/statisticsapi.js
import axios from 'axios'

const BASE_URL = 'http://10.255.6.2:9090/statistics'

/**
 * 根據使用者與時間區間，取得每日任務統計資料
 * @param {string} userID - 使用者 ID
 * @param {string} startDate - 起始時間，例如 "2025-06-01 00:00:00"
 * @param {string} endDate - 結束時間，例如 "2025-08-30 23:59:59"
 * @returns Axios Promise
 */
export function getTaskStatistics(userID, startDate, endDate) {
  return axios.get(`${BASE_URL}/daily`, {
    params: {
      userID,
      startDate,
      endDate
    }
  })
}
