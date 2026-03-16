import axios from 'axios'
const BASE_URL = 'http://10.255.6.2:9090/tasks'
const api = axios.create({
  baseURL: BASE_URL,
  timeout: 5000,
})

// 获取用户所有任务
export function getTasksByUser(userID) {
  return api.get(`/user/${userID}`)
}

// 根据任务ID获取任务详情
export function getTaskById(taskID) {
  return api.get(`/${taskID}`)
}

// 添加任务
export function addTask(task) {
  return api.post('', task)
}

// 更新任务
export function updateTask(taskID, task) {
  return api.put(`/${taskID}`, task)
}

// 删除任务
export function deleteTask(taskID) {
  return api.delete(`/${taskID}`)
}

export function getTaskByTag(userID,tag){
 axios.get(`/tag/${tag}/${userID}`)
}

export function fetchReminders(userID) {
  axios.get(`${BASE_URL}/remind/${userID}`)
    .then(res => {
      try {
        const tasks = res.data;
        if (Array.isArray(tasks)) {
          tasks.forEach(task => {
            if (Notification.permission === 'granted') {
              new Notification('任务提醒', {
                body: `任务「${task.title}」即将到期！`
              });
            } else {
              alert(`提醒：${task.title} 快到了！`);
            }
          });
        } else {
          console.warn('提醒返回不是数组：', tasks);
        }
      } catch (e) {
        console.error('解析提醒出错：', e);
      }
    })
    .catch(err => {
      console.error('获取提醒失败', err);
    });
}
