import type { AxiosResponse } from 'axios'
import Axios from 'axios'

export const axios = Axios.create({
    baseURL: '/api-jplag',
    timeout: 8 * 60 * 1000
})

axios.interceptors.response.use((res: AxiosResponse) => {
    const { data } = res
    const code = data.code as number
    switch (Math.floor(code / 100)) {
        case 4:
            return Promise.reject(res)
        case 5:
            res.data.msg = '服务器错误'
            return Promise.reject(res)
        default:
          return Promise.resolve(res)
    }
})