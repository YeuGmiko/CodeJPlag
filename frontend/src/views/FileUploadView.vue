<script setup lang="ts">
import type { MessageReactive, UploadFileInfo } from 'naive-ui'
import { useMessage } from 'naive-ui'
import { computed, type ComputedRef, onBeforeUnmount, ref, type Ref } from 'vue'
import { store } from '@/stores/store'
import { ZipFileHandler } from '@/model/fileHandling/ZipFileHandler'
import { router } from '@/router'
import { fetchJplagByFiles } from '@/api/jplag'

export type CodeLang = 'Java' | 'C' | 'JavaScript' | 'TypeScript' | 'Python' | 'Kotlin'
export type CodeFileSuffix = '.java' | '.c' | '.js' | '.ts' | '.py' | '.kt'

const codeFileType: Record<CodeLang, CodeFileSuffix> = {
  Java: '.java',
  C: '.c',
  JavaScript: ".js",
  TypeScript: '.ts',
  Python: '.py',
  Kotlin: '.kt'
}


const filesMap: Ref<Map<string, UploadFileInfo>> = ref(new Map())
const codeFileLang: Ref<CodeLang> = ref('C')
const codeFileSuffix: ComputedRef<CodeFileSuffix> = computed(() => {
  clearList()
  return codeFileType[codeFileLang.value]
})

const message = useMessage()
let loadingMsg: MessageReactive | null = null

// Component Props
const fileList: ComputedRef<UploadFileInfo[]> = computed(() => Array.from(filesMap.value.values()))
const langOptions: Array<{label: CodeLang, value: CodeLang}> = [
  {
    label: 'Java',
    value: 'Java'
  },
  {
    label: 'C',
    value: 'C'
  },
  {
    label: 'JavaScript',
    value: 'JavaScript'
  },
  {
    label: 'TypeScript',
    value: 'TypeScript'
  },
  {
    label: 'Python',
    value: 'Python'
  },
  {
    label: 'Kotlin',
    value: 'Kotlin'
  }
]


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
function delFile(file: UploadFileInfo) {
  file.status = 'removed'
  updateFiles({
    file,
    fileList: [file]
  })
}

function reset() {
  filesMap.value = new Map<string, UploadFileInfo>()
}
function clearList() {
  filesMap.value = new Map<string, UploadFileInfo>()
}

function handler() {
  getJPlagResultByFilesList()
}

function getJPlagResultByFilesList() {
  const files: File[] = []
  fileList.value.forEach(fileInfo => {
    if (fileInfo.file)
      files.push(fileInfo.file)
  })
  if (files.length < 2) {
    message.error("请至少选择两个文件！")
    return
  }
  keepLoading()
 fetchJplagByFiles({
    baseCode: undefined,
    language: codeFileLang.value,
    files,
  }).then(async res => {
   const { data: base64Str } = res.data
   const decodedStr = atob(base64Str)

   const buffer = new Uint8Array(decodedStr.length)
   for (let i = 0, len = decodedStr.length; i < len; i++)
     buffer[i] = decodedStr.charCodeAt(i)

   const blob = new Blob([buffer], {type: 'application/zip'})
   await handleFile(blob)
   removeLoading()
   message.success("成功")
 }).catch(err => {
   const { msg } = err.data
   removeLoading()
   message.error(msg)
 })
}

function keepLoading() {
  if (!loadingMsg) {
    loadingMsg = message.loading('处理中，请稍后', {
      duration: 0
    })
  }
}
function removeLoading() {
  if (loadingMsg) {
    loadingMsg.destroy()
    loadingMsg = null
  }
}

async function handleFile(file: Blob) {
  switch (file.type) {
    case 'application/zip':
    case 'application/zip-compressed':
    case 'application/x-zip-compressed':
    case 'application/x-zip':
      store().setLoadingType('zip')
      await new ZipFileHandler().handleFile(file)
      return navigateToOverview()
    default:
      throw new Error(`Unknown MIME type '${file.type}'`)
  }
}
function navigateToOverview() {
  router.push({
    name: 'OverviewView'
  })
}

onBeforeUnmount(removeLoading)
</script>

<template>
  <NFlex vertical class="h-[100vh]">
    <div class="header p-[20px]">
      <NFlex>
        <div>
          <span class="text-3xl font-bold text-black">JY锦屿</span>
        </div>
        <div class="flex items-end">
          <span class="text-xl">JPlag代码重复度检测</span>
        </div>
      </NFlex>
    </div>
    <div class="options flex">
      <NCard>
        <template #header>
          <NText class="text-xl font-bold">配置项</NText>
        </template>
        <NFlex>
          <div>
            <NText>检测语言:</NText>
            <NSelect class="min-w-[200px]" :options="langOptions" v-model:value="codeFileLang"></NSelect>
          </div>
        </NFlex>
      </NCard>
      <NCard>
        <template #header>
          <NText class="text-xl font-bold">操作项</NText>
        </template>
        <NButtonGroup>
          <NButton @click="reset">清空</NButton>
          <NButton @click="handler">提交</NButton>
        </NButtonGroup>
      </NCard>
    </div>
    <div class="fileUpload flex-1 flex">
      <NCard class="flex-1">
        <template #header>
          <NText class="text-xl font-bold">上传文件</NText>
        </template>
        <div>
          <NUpload
            multiple
            directory-dnd
            :show-file-list="false"
            :file-list="fileList"
            :accept="codeFileSuffix"
            @change="updateFiles"
          >
            <NUploadDragger>
              <n-text style="font-size: 16px">
                点击或者拖动文件到该区域来上传
              </n-text>
              <n-p depth="3" style="margin: 8px 0 0 0">
                请不要上传敏感数据，比如你的银行卡号和密码，信用卡号有效期和安全码
              </n-p>
            </NUploadDragger>
          </NUpload>
          <div class="desc mt-2">
            <NText>
              上传多个代码文件，以检测这些文件的重复代码，通常用于算法平台的代码提交的检测。
            </NText>
          </div>
        </div>
      </NCard>
      <NCard class="flex-1">
        <template #header>
          <NText class="text-xl font-bold">文件上传列表</NText>
        </template>
        <NList bordered clickable hoverable show-divider v-if="fileList.length >= 1">
          <NInfiniteScroll class="max-h-[400px]">
            <NListItem v-for="file in fileList" :key="file.id" :id="file.id">
              <template #prefix>
                <VIcon name="io-document-outline"/>
              </template>
              <template #default>
                {{file.name}}
              </template>
              <template #suffix>
                <div @click.prevent="delFile(file)">
                  <VIcon name="io-close-circle-outline"/>
                </div>
              </template>
            </NListItem>
          </NInfiniteScroll>
        </NList>
        <div class="no-data w-full rounded-xl overflow-hidden border-2 border-dashed border-black" v-else>
          <div class="content bg-gray-200 flex items-center justify-center text-center h-[50px]">
            <NText>
              无数据
            </NText>
          </div>
        </div>
      </NCard>
    </div>
  </NFlex>
</template>

<style scoped></style>