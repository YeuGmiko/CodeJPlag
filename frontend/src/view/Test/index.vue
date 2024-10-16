<script setup lang="ts">
import type {UploadCustomRequestOptions, UploadFileInfo} from 'naive-ui'
import {computed, type ComputedRef, type CSSProperties, ref, type Ref} from "vue";
import type { CodeFileSuffix, CodeLang} from '@/typing/CodeFile'
import JSZip from "jszip";

const codeFileType: Record<CodeLang, CodeFileSuffix> = {
  Java: '.java',
  C: '.c',
  JavaScript: ".js",
  TypeScript: '.ts'
}

// States
const codeLang: Ref<CodeLang> = ref('C')
const codeFileSuffix: ComputedRef<CodeFileSuffix> = computed(() => codeFileType[codeLang.value])
const isDirectory: Ref<boolean> = ref(true)
const filesMap: Ref<Map<string, UploadFileInfo>> = ref(new Map())
const zipFilesMap: Ref<Map<string, JSZip>> = ref(new Map())
const dirBasePath: Ref<string> = ref('')

// Component Props
const fileList: ComputedRef<UploadFileInfo[]> = computed(() => Array.from(filesMap.value.values()))
const zipFileList: ComputedRef<UploadFileInfo[]> = computed(() => {
  const list = Array.from(zipFilesMap.value.keys())
  return list.map(fileName => ({
    id: fileName,
    name: fileName,
    status: 'pending'
  }))
})
const codeChooseOptions: Array<{label: CodeLang, value: CodeLang}> = [
  {
    label: 'C',
    value: 'C'
  },
  {
    label: 'Java',
    value: 'Java'
  }
]

function fileChooseStyle(info: { focused: boolean, checked: boolean }): CSSProperties {
  const style: CSSProperties = {}
  style.background = info.checked ? '#4be2b7' : '#2080f0'
  return style
}


function updateFiles(options: { file: UploadFileInfo, fileList: Array<UploadFileInfo>, event?: Event }) {
  const fileInfo = options.file
  const { status: op, name: fileName } = options.file
  // Remove
  if (op === 'removed') {
    filesMap.value.delete(fileName)
    return
  }
  // Pushed
  if (filesMap.value.get(fileName))
    return;
  filesMap.value.set(fileName, fileInfo)
}

function updateDirs(options: { file: UploadFileInfo, fileList: Array<UploadFileInfo>, event?: Event }) {
  const fileInfo = options.file
  const { status: op, name: fileName } = fileInfo
  // Remove
  if (op === 'removed') {
    zipFilesMap.value.delete(fileName)
    return
  }

  const fullName: string = fileInfo.fullPath ?? ''
  const splitsPath = fullName.split('/').slice(1)
  const filePath = splitsPath.slice(1).join('/')
  const key = splitsPath[0]
  const content: Blob = fileInfo.file as Blob
  if (!dirBasePath.value)
    dirBasePath.value = splitsPath[0]
  // Pushed
  if (!zipFilesMap.value.get(key))
    zipFilesMap.value.set(key, new JSZip())
  const zip = zipFilesMap.value.get(key)
  zip?.file(filePath, content)
}

function handler() {
  console.log(filesMap.value)
  console.log(zipFilesMap.value)
}
</script>

<template>
  <NSpace vertical>
    <NSpace class="items-center p1">
      <NSwitch
          size="large"
          :rail-style="fileChooseStyle"
          :default-value="true"
          :value="!isDirectory"
          @update-value="isDirectory = !isDirectory"
      >
        <template #checked>
          选择文件
        </template>
        <template #unchecked>
          选择目录
        </template>
      </NSwitch>
      <NSelect
          class="min-w-[200px]"
          :options="codeChooseOptions"
          v-model:value="codeLang"
      />
      <NButtonGroup>
        <NButton @click="">Clear</NButton>
        <NButton @click="handler">Submit</NButton>
        <NButton @click="console.log(zipFilesMap)">Check</NButton>
      </NButtonGroup>
    </NSpace>
    <n-upload
        v-if="!isDirectory"
        multiple
        directory-dnd
        :file-list="fileList"
        :accept="codeFileSuffix"
        @change="updateFiles"
        :disabled="isDirectory"
    >
      <n-upload-dragger>
        <n-text style="font-size: 16px">
          点击或者拖动文件到该区域来上传
        </n-text>
        <n-p depth="3" style="margin: 8px 0 0 0">
          请不要上传敏感数据，比如你的银行卡号和密码，信用卡号有效期和安全码
        </n-p>
      </n-upload-dragger>
    </n-upload>
    <n-upload
        v-else
        multiple
        directory
        directory-dnd
        :file-list="zipFileList"
        @change="updateDirs"
        :disabled="!isDirectory"
    >
      <n-upload-dragger>
        <n-text style="font-size: 16px">
          点击或者拖动文件到该区域来上传
        </n-text>
        <n-p depth="3" style="margin: 8px 0 0 0">
          请直接上传Submission根目录，不支持零碎式添加
        </n-p>
      </n-upload-dragger>
    </n-upload>


  </NSpace>
</template>

<style scoped></style>