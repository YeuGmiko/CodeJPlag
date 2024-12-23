import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import { login as fetchLogin } from '@/api/jplag'

interface User {
  username: string
  password: string
}

export const useUserStore = defineStore('user', () => {
  const user = ref<User>({
    username: '',
    password: ''
  })
  const isLogin = computed(() => user.value.username !== '')

  async function login(username: string, password: string) {
    return await fetchLogin({ username, password }).then(() => {
      user.value.username = username
      user.value.password = password
    })
  }
  return {
    user,
    isLogin,
    login
  }
})