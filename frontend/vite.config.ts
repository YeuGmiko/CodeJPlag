import { fileURLToPath, URL } from 'node:url'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { NaiveUiResolver} from 'unplugin-vue-components/resolvers'
import { defineConfig } from 'vite'
import type { UserConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig((userConfig: UserConfig) => {
  let base = '/'
  switch (userConfig.mode) {
    case 'dev':
      base = '/JPlag-Dev/'
      break
    case 'prod':
      base = '/JPlag/'
      break
    case 'demo':
      base = '/Demo/'
      break
  }
  return {
    plugins: [
      vue(),
      AutoImport({
        imports: [
          'vue'
        ]
      }),
      Components({
        resolvers: [NaiveUiResolver()]
      })
    ],
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url))
      }
    },
    base: base,
    server: {
      port: 3322,
      open: true,
      proxy: {
        '/api-jplag': {
          target: 'http://localhost:8080',
          changeOrigin: true
        }
      },
      cors: true,
    }
  }
})
