//userapi.js

import axios from 'axios'
const BASE_URL = 'http://10.255.6.2:9090/user'
export function loginUser(userData) {
 return axios.post(`${BASE_URL}/login`, userData)
}
// 注册请求
export function registerUser(userData) {
 return axios.post(`${BASE_URL}/register`, userData)
}