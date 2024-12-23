<script setup lang="ts">
import { useRouter } from 'vue-router'
import { reactive } from 'vue'
import { useUserStore } from '@/stores/user'
import { useMessage } from 'naive-ui'

const { login } = useUserStore()

const model = reactive({
  username: '',
  password: ''
})

const router = useRouter()
const message = useMessage()

function reset() {
  model.username = ''
  model.password = ''
}

async function submit() {
  try {
    await login(model.username, model.password)
    message.success('登陆成功')
    await router.push('/home')
  } catch (e) {
    message.error('用户名或密码错误')
  }
}
</script>

<template>
  <div class="auth flex h-[100vh] justify-center items-center">
    <div class="main flex flex-col gap-[20px]">
      <div>
        <h1 class="font-bold text-4xl">JY锦屿</h1>
        <p class="text-[ghostwhite] text-xl">JPlag代码重复度检测工具</p>
      </div>
      <NForm label-placement="top" :show-label="false"   :model="model">
        <NFormItem>
          <NInput v-model:value="model.username" placeholder="用户名" size="large" clearable></NInput>
        </NFormItem>
        <NFormItem>
          <NInput v-model:value="model.password" type="password" size="large" placeholder="密码" show-password-on="click" clearable></NInput>
        </NFormItem>
        <NFormItem>
          <NFlex class="w-full" justify="space-between">
            <NButton class="flex-1" type="primary" size="large" @click="submit">登录</NButton>
            <NButton type="warning" size="large" @click="reset">重置</NButton>
          </NFlex>
        </NFormItem>
      </NForm>
    </div>
  </div>
</template>

<style scoped>
.main {
  min-width: 400px;
  max-width: 500px;
}

.auth {
  background-color: #ff74bc;
  background-image:
    radial-gradient(closest-side, rgba(235, 105, 78, 1), rgba(235, 105, 78, 0)),
    radial-gradient(closest-side, rgba(243, 11, 164, 1), rgba(235, 105, 78, 0)),
    radial-gradient(closest-side, rgba(254, 234, 131, 1), rgba(235, 105, 78, 0)),
    radial-gradient(closest-side, rgba(170, 142, 245, 1), rgba(235, 105, 78, 0)),
    radial-gradient(closest-side, rgba(248, 192, 147, 1), rgba(235, 105, 78, 0));
  animation: 10s backgroundMove linear infinite;
}

.auth::after {
  content: "";
  display: block;
  width: inherit;
  height: inherit;
  backdrop-filter: blur(50px);
}

@keyframes backgroundMove {
  0%,
  100% {
    background-size:
      130vmax 130vmax,
      80vmax 80vmax,
      90vmax 90vmax,
      110vmax 110vmax,
      90vmax 90vmax;
    background-position:
      -80vmax -80vmax,
      60vmax -30vmax,
      10vmax 10vmax,
      -30vmax -10vmax,
      50vmax 50vmax;
  }

  25% {
    background-size:
      100vmax 100vmax,
      90vmax 90vmax,
      100vmax 100vmax,
      90vmax 90vmax,
      60vmax 60vmax;
    background-position:
      -60vmax -90vmax,
      50vmax -40vmax,
      0vmax -20vmax,
      -40vmax -20vmax,
      40vmax 60vmax;
  }

  50% {
    background-size:
      80vmax 80vmax,
      110vmax 110vmax,
      80vmax 80vmax,
      60vmax 60vmax,
      80vmax 80vmax;
    background-position:
      -50vmax -70vmax,
      40vmax -30vmax,
      10vmax 0vmax,
      20vmax 10vmax,
      30vmax 70vmax;
  }

  75% {
    background-size:
      90vmax 90vmax,
      90vmax 90vmax,
      100vmax 100vmax,
      90vmax 90vmax,
      70vmax 70vmax;
    background-position:
      -50vmax -40vmax,
      50vmax -30vmax,
      20vmax 0vmax,
      -10vmax 10vmax,
      40vmax 60vmax;
  }
}
</style>