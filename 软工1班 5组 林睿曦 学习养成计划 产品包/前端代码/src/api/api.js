import axios from 'axios';

const apiClient = axios.create({
 baseURL: 'http://10.255.6.2:9090',
 timeout: 5000,
 headers: {
  'Content-Type': 'application/json',
 },
});

export default apiClient;