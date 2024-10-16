import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedState from 'pinia-plugin-persistedstate'
import { OhVueIcon, addIcons } from 'oh-vue-icons'

import VueVirtualScroller from 'vue-virtual-scroller'
import 'highlight.js/lib/common'

import App from './App.vue'
import { router } from './router'
import * as IonIcons from 'oh-vue-icons/icons/io'

import 'vue-virtual-scroller/dist/vue-virtual-scroller.css'

import './style.css'

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedState)

addIcons(...Object.values(IonIcons))

app.use(pinia)
app.use(router)
app.use(VueVirtualScroller)
app.component("VIcon", OhVueIcon)

app.config.errorHandler = (err, vm, info) => {
  console.error(err)
  console.error(info)
  alert('An unhandeled error occured. Please check the console for more details.')
}

app.mount('#app')
