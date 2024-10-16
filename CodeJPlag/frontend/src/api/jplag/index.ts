import {axios} from "@/api";
import type {AxiosResponse} from "axios";


export function fetchJplagByFiles(option: JPlagByFilesOption): Promise<AxiosResponse<Blob>> {
    const formData = new FormData();
    formData.append('language', option.language);

    option.files.forEach((file, index) => {
        formData.append(`files`, file);
    });

    if (option.baseCode) {
        formData.append('baseCode', option.baseCode);
    }

    return axios.post('/jplag/files', formData, {
        responseType: 'blob'
    });
}