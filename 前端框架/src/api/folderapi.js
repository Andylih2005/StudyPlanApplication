import axios from 'axios'

const BASE_URL = 'http://10.255.6.2:9090/folders'

export function getFoldersByUser(userID) {
  return axios.get(`${BASE_URL}/${userID}`)
}
export const createFolder = (data) => {
  return axios.post(`${BASE_URL}`, data)
}
export function deleteFolder(folderID) {
  return axios.delete(`${BASE_URL}/${folderID}`)
}
