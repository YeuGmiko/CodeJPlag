import { defineConfig } from 'vite'
import { fileURLToPath, URL } from 'node:url'
import envCompatible from "vite-plugin-env-compatible";
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import UnoCSS from 'unocss/vite'
import vue from '@vitejs/plugin-vue'
import {NaiveUiResolver} from "unplugin-vue-components/resolvers";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    UnoCSS(),
    envCompatible({
      prefix: ''
    }),
      AutoImport({
        imports: [
            'vue',
            'pinia',
        ]
      }),
      Components({
        resolvers: [
            NaiveUiResolver(),
        ]
      })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      '@view': fileURLToPath(new URL('./src/view', import.meta.url))
    }
  },
  server: {
    port: 80
  }
})
