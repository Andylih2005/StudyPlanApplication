<template>
    <div class="login-container">
        <el-card class="login-card">
            <el-form :model="registerForm" label-width="80px">
                <el-form-item label="Username">
                    <el-input v-model="registerForm.username" placeholder="Please input" />
                </el-form-item>
                <el-form-item label="Password">
                    <el-input v-model="registerForm.password" placeholder="Please input" show-password />
                </el-form-item>
            </el-form>
            <el-form-item>
                <el-button style="background-color:#66a9c9;color:#fff;font-size: 16px " @click="handleLogin">Sign
                    In</el-button>
            </el-form-item>
            <div class="link-group">
                <el-link type="info" underline="always" @click="handleLoginImm">back</el-link>
            </div>
        </el-card>
    </div>
</template>

<script lang="ts" setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { registerUser } from '../api/userapi'
const router = useRouter()

import { ref } from 'vue'
const input = ref('')

const registerForm = reactive({
    username: '',
    password: ''
})
const handleLoginImm = () => {
    router.push('/login')
}

const handleLogin = async () => {
 const flag = Boolean(registerForm.username) && Boolean(registerForm.password)
 if (flag) {
  try {
   console.log(registerForm.username + '\n' + registerForm.password)
   const res = await registerUser({
    userID: registerForm.username,
    password: registerForm.password,
   })
   ElMessage.success('创建成功')
   router.push('/login')
  } catch (err) {
   ElMessage.error('ID重复')
  }
 } else {
  alert('注册资料有空，请检查')
 }
}
</script>

<style scoped>
.login-container {
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    background: linear-gradient(to right, #66a9c9, #d8e3e7);
}

.login-card {
    width: 400px;
    padding: 30px 30px;
    border-radius: 20px;
    box-shadow: 0 6px 18px rgba(0, 0, 0, 0.15);
}

.el-link {
    justify-content: center;
    text-align: center;
}
.link-group {
  display: flex;
  justify-content: center;  /* 水平居中 */
  margin-top: 20px;
}

</style>