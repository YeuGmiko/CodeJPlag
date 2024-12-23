import { fileURLToPath, URL } from 'node:url'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { NaiveUiResolver} from 'unplugin-vue-components/resolvers'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig( {
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
    server: {
      port: 3322,
      open: true,
      proxy: {
        '/api-jplag': {
          target: 'http://8.138.14.75:8081',
          changeOrigin: true
        }
      },
      cors: true,
    }
  })
