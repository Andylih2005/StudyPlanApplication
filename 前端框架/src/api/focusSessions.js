import axios from 'axios'

const BASE_URL = 'http://10.255.6.2:9090/focus-sessions'

const focusSessionApi = {
  createSession: (session) => {
    return axios.post(`${BASE_URL}`, session)
  },

  getSessionsByUser: (userID) => {
    return axios.get(`${BASE_URL}/user/${userID}`)
  },

  getSessionById: (id) => {
    return axios.get(`${BASE_URL}/${id}`)
  },

  deleteSession: (id) => {
    return axios.delete(`${BASE_URL}/${id}`)
  },
}

export default focusSessionApi