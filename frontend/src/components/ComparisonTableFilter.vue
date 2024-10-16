<template>
  <div class="space-y-2">
    <div class="flex flex-row flex-wrap items-center gap-x-8 gap-y-2">
      <NSpace vertical>
        <div class="text-2xl">{{ header }}</div>
        <NSpace>
          <ToolTipComponent direction="bottom" class="min-w-[50%] flex-grow">
            <template #default>
              <SearchBarComponent placeholder="搜索" v-model="searchStringValue" />
            </template>
            <template #tooltip>
              <p class="whitespace-pre text-sm">
                输入名称以仅显示包含此名称的比较
              </p>
              <p class="whitespace-pre text-sm">完全写出的名称将被取消隐藏</p>
            </template>
          </ToolTipComponent>

          <ButtonComponent class="w-24 h-full text-nowrap px-20" @click="changeAnonymousForAll()">
            {{
              store().state.anonymous.size == store().getSubmissionIds.length ? '取消名称隐藏' : '隐藏所有名称'
            }}
          </ButtonComponent>
        </NSpace>
      </NSpace>
    </div>
    <OptionsSelector
      title="排序方式："
      :defaultSelected="getSortingMetric()"
      :labels="tableSortingOptions"
      @selection-changed="(index: number) => changeSortingMetric(index)"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import SearchBarComponent from './SearchBarComponent.vue'
import ToolTipComponent from './ToolTipComponent.vue'
import ButtonComponent from './ButtonComponent.vue'
import OptionsSelector from './optionsSelectors/OptionsSelectorComponent.vue'
import { store } from '@/stores/store'
import { MetricType, metricToolTips } from '@/model/MetricType'
import type { ToolTipLabel } from '@/model/ui/ToolTip'

const props = defineProps({
  searchString: {
    type: String,
    default: ''
  },
  enableClusterSorting: {
    type: Boolean,
    default: true
  },
  header: {
    type: String,
    default: 'Top Comparisons:'
  }
})

const emit = defineEmits<{
  (e: 'update:searchString', v: string): void
}>()

const searchStringValue = computed({
  get: () => props.searchString,
  set: (value) => {
    emit('update:searchString', value)
    // Update the anonymous set

    const searchParts = value
      .trimEnd()
      .toLowerCase()
      .split(/ +/g)
      .map((s) => s.trim().replace(/,/g, ''))
    if (searchParts.length == 0) {
      return
    }

    for (const submissionId of store().getSubmissionIds) {
      const submissionParts = submissionId.toLowerCase().split(/ +/g)
      if (submissionParts.every((part) => searchParts.includes(part))) {
        store().state.anonymous.delete(submissionId)
      }
    }
  }
})

function changeSortingMetric(index: number) {
  store().uiState.comparisonTableSortingMetric =
    index < tableSortingMetricOptions.length ? tableSortingMetricOptions[index] : MetricType.AVERAGE
  store().uiState.comparisonTableClusterSorting = tableSortingOptions.value[index] == '簇'
}

function getSortingMetric() {
  if (store().uiState.comparisonTableClusterSorting && props.enableClusterSorting) {
    return tableSortingOptions.value.indexOf('簇')
  }
  return tableSortingMetricOptions.indexOf(store().uiState.comparisonTableSortingMetric)
}

const tableSortingMetricOptions = [MetricType.AVERAGE, MetricType.MAXIMUM]
const tableSortingOptions = computed(() => {
  const options: (ToolTipLabel | string)[] = tableSortingMetricOptions.map((metric) => {
    return {
      displayValue: metricToolTips[metric].longName,
      tooltip: metricToolTips[metric].tooltip
    }
  })
  if (props.enableClusterSorting) {
    options.push('簇')
  }
  return options
})

/**
 * Sets the anonymous set to empty if it is full or adds all submission ids to it if it is not full
 */
function changeAnonymousForAll() {
  if (store().state.anonymous.size == store().getSubmissionIds.length) {
    store().state.anonymous.clear()
  } else {
    store().state.anonymous = new Set(store().getSubmissionIds)
  }
}
</script>
