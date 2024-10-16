<template>
  <NMessageProvider>
    <div :class="{ dark: store().uiState.useDarkMode }">
      <div
        class="max-w-screen max-h-fit min-h-screen bg-backgorund-light text-black dark:bg-backgorund-dark dark:text-amber-50"
      >
        <RouterView class="max-h-screen overflow-hidden print:max-h-none print:overflow-visible" />
      </div>
    </div>
  </NMessageProvider>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faMoon, faSun } from '@fortawesome/free-solid-svg-icons'
import { store } from './stores/store'

library.add(faMoon)
library.add(faSun)
</script>
