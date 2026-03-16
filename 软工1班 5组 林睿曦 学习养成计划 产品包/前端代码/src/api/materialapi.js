import axios from 'axios'

const BASE_URL = 'http://10.255.6.2:9090/material'

export function getMaterialsByUser(userID) {
  return axios.get(`${BASE_URL}/user/${userID}`)
}

export const getUncategorizedMaterials = (userID) => {
  return axios.get(`${BASE_URL}/uncategorized`, {
    params: { userID },
  })
}
export const findByUserAndTitle = (userID, title) => {
  return axios.get(`${BASE_URL}/search`, {
    params: { userID, title },
  })
}
export function previewMaterialById(id) {
  return axios.get(`${BASE_URL}/${id}/content`,{
    responseType: 'blob'
  })
}

export function deleteMaterialById(id) {
  return axios.delete(`${BASE_URL}/delete/${id}`)
}

export function uploadMaterial(file, userID, folderID) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('userID', userID)
  formData.append('title', file.name)
  formData.append('tags', '')
  if (folderID != null) {
    formData.append('folderID', folderID)
  }
  return axios.post(`${BASE_URL}/upload`, formData)
}

export const saveUrlMaterial = (formData) => {
  return axios.post('http://10.255.6.2:9090/material/save-url', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
}
export function getMaterialsByFolderPaged(userID, folderID, page, pageSize) {
  return axios.get(`${BASE_URL}/folderPaged`, {
    params: {
      userID,
      folderID,
      page,
      pageSize,
    },
  })
}
