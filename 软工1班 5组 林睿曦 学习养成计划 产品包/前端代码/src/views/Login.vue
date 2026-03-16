<template>
    <div class="login-container">
        <el-card class="login-card">
            <div class="login-title">Welcome</div>
            <el-form :model="loginForm" label-width="80px">
                <el-form-item label="Username">
                    <el-input v-model="loginForm.username" placeholder="Please input" />
                </el-form-item>
                <el-form-item label="Password">
                    <el-input v-model="loginForm.password" placeholder="Please input" show-password />
                </el-form-item>
            </el-form>
            <el-form-item>
                <el-button style="background-color:#66a9c9;color:#fff;font-size: 16px " @click="handleLogin">Log
                    In</el-button>
            </el-form-item>
            <div class="link-group">
                <el-link type="info" underline="always" @click="handleRegister">register</el-link>
            </div>
        </el-card>

    </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { loginUser } from '../api/userapi'
import { useUserStore } from '../stores/user'
const userStore = useUserStore()

const router = useRouter()

const loginForm = reactive({
    username: '',
    password: ''
})

const handleLogin = async () => {
    try {
        const res = await loginUser({
            userID: loginForm.username,
            password: loginForm.password,
        })

        if (res && res.data) {
            ElMessage.success('登入成功！')
            userStore.setUserID(res.data.userID)
            router.push('/home') // 跳转到主页
        } else {
            ElMessage.error('登入失敗，請確認帳號密碼')
        }
    } catch (err) {
        if (!err.response) {
            // 完全没有 response，说明是连接不上服务器（比如数据库崩溃）
            ElMessage.error('登入失敗：無法連接伺服器，請稍後再試');
        } else if (err.response.status === 401) {
            // 有 response，且狀態碼是 401，說明帳號密碼錯
            ElMessage.error('登入失敗：帳號或密碼錯誤');
        } else if (err.response.status === 500) {
            // 有 response，後端炸了（通常是資料庫錯）
            ElMessage.error('登入失敗：伺服器內部錯誤，請聯絡管理員');
        } else {
            // 其他未知錯誤
            ElMessage.error('登入失敗：' + (err.response.data?.message || '未知錯誤'));
        }
    }

}

const handleForgetPassword = () => {
    router.push('/main') //更改页面
}

const handleRegister = () => {
    router.push('/register') //更改页面

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

.login-title {
    text-align: center;
    font-size: 50px;
    font-weight: bold;
    margin-bottom: 30px;
    color: #333;
    font-family: "DancingScript", cursive;
    letter-spacing: 1.5px;
}

.el-form-item {
    margin-bottom: 20px;
}

.el-button {
    width: 100%;
}

.link-group {
    display: flex;
    justify-content: space-between;
    font-size: 13px;
    margin-top: 15px;
}

.el-link {
    margin-right: 8px;
}
</style>
