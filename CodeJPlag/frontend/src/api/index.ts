import Axios from "axios";

export const axios = Axios.create({
    baseURL: 'http://localhost:8080/api',
    withCredentials: true
})