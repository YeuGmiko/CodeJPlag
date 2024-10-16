import {axios} from "@/api";
import type {AxiosResponse} from "axios";
import type { CodeLang } from '@/views/FileUploadView.vue'

interface Response <T = unknown> {
    code: number;
    msg: string;
    data: T;
}

export function fetchJplagByFiles(option: {
    baseCode: undefined;
    files: File[];
    language: CodeLang;
}): Promise<AxiosResponse<Response<string>>> {
    const formData = new FormData();
    formData.append('language', option.language);

    option.files.forEach((file, index) => {
        formData.append(`files`, file);
    });

    if (option.baseCode) {
        formData.append('baseCode', option.baseCode);
    }

    return axios.post('/jplag/files', formData);
}

export function fetchJplagByZipFiles(option: {
    baseCode: undefined;
    files: File[];
    language: CodeLang;
}) {
    const formData = new FormData()
    formData.append('language', option.language)
    option.files.forEach(file => {
        formData.append('files', file, file.name)
    })
    return axios.post('/jplag/dirs', formData)
}