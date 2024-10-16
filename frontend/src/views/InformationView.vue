<template>
  <div
    class="absolute bottom-0 left-0 right-0 top-0 flex flex-row space-x-5 p-5 pb-7 print:flex-col print:space-x-0 print:space-y-2 print:p-0"
  >
    <Container class="infoContainer print:!border-none">
      <h2>运行结果：</h2>

      <ScrollableComponent class="flex-grow px-4 pt-2">
        <div v-if="options == undefined">
          <TextInformation label="语言" class="pb-1">{{ overview.language }}</TextInformation>
          <TextInformation label="最小Token匹配值" class="pb-1">{{
            overview.matchSensitivity
          }}</TextInformation>
        </div>

        <div v-else class="space-y-2">
          <TextInformation label="语言">{{ options.language }}</TextInformation>
          <TextInformation label="最小Token匹配值">{{ options.minTokenMatch }}</TextInformation>
          <TextInformation label="Base Directory">{{ options.baseDirectory }}</TextInformation>
          <TextInformation label="近似度度量">{{
            metricToolTips[options.similarityMetric].longName
          }}</TextInformation>
          <TextInformation label="相似度阈值">{{
            options.similarityThreshold
          }}</TextInformation>
          <TextInformation label="最大比较数">{{
            options.maxNumberComparisons
          }}</TextInformation>

          <div class="!mt-5 space-y-2" v-if="options.clusterOptions.enabled">
            <h3 class="font-bold">簇:</h3>
            <TextInformation label="近似度度量">{{
              metricToolTips[options.clusterOptions.similarityMetric].longName
            }}</TextInformation>
            <TextInformation label="算法">{{
              options.clusterOptions.algorithm
            }}</TextInformation>

<!--            <div-->
<!--              v-if="options.clusterOptions.algorithm.toLowerCase() == 'spectral'"-->
<!--              class="space-y-2"-->
<!--            >-->
<!--              <TextInformation label="Spectral Bandwidth">{{-->
<!--                options.clusterOptions.spectralBandwidth-->
<!--              }}</TextInformation>-->
<!--              <TextInformation label="Spectral Gausssian Process Variance">{{-->
<!--                options.clusterOptions.spectralGaussianProcessVariance-->
<!--              }}</TextInformation>-->
<!--              <TextInformation label="Spectral Min Runs">{{-->
<!--                options.clusterOptions.spectralMinRuns-->
<!--              }}</TextInformation>-->
<!--              <TextInformation label="Spectral Max Runs">{{-->
<!--                options.clusterOptions.spectralMaxRuns-->
<!--              }}</TextInformation>-->
<!--              <TextInformation label="K-Means Iterations">{{-->
<!--                options.clusterOptions.spectralMaxKMeansIterations-->
<!--              }}</TextInformation>-->
<!--            </div>-->

<!--            <TextInformation v-else label="Agglomerative Treshold">{{-->
<!--              options.clusterOptions.agglomerativeThreshold-->
<!--            }}</TextInformation>-->

<!--            <TextInformation label="Preprocessor">{{-->
<!--              options.clusterOptions.preprocessor-->
<!--            }}</TextInformation>-->
<!--            <TextInformation label="Preprocessor Threshold">{{-->
<!--              options.clusterOptions.preprocessorThreshold-->
<!--            }}</TextInformation>-->
<!--            <TextInformation label="Preprocessor Percentile">{{-->
<!--              options.clusterOptions.preprocessorPercentile-->
<!--            }}</TextInformation>-->
<!--            <TextInformation label="Inter Cluster Similarity">{{-->
<!--              options.clusterOptions.interClusterSimilarity-->
<!--            }}</TextInformation>-->
          </div>

          <div class="mt-5 space-y-2" v-if="options.mergingOptions.enabled">
            <h3 class="font-bold">Match Merging:</h3>
            <TextInformation label="Min Neighbor Length">{{
              options.mergingOptions.minNeighborLength
            }}</TextInformation>
            <TextInformation label="Max Gap Size"
              >{{ options.mergingOptions.maxGapSize }} }}</TextInformation
            >
          </div>
        </div>

        <TextInformation label="生成时间" class="pb-1">{{
            overview.dateOfExecution
          }}</TextInformation>
        <TextInformation label="数据分析耗时" class="pb-1"
        >{{ overview.durationOfExecution }} ms</TextInformation
        >
        <TextInformation label="总提交数" class="pb-1">{{
            store().getSubmissionIds.length
          }}</TextInformation>
        <TextInformation label="总对比数" class="pb-1">{{
            overview.totalComparisons
          }}</TextInformation>
        <TextInformation label="已展示对比数" class="pb-1">{{
            overview.shownComparisons
          }}</TextInformation>
        <TextInformation label="不合格提交数" class="pb-1">{{
            overview.missingComparisons
          }}</TextInformation>
      </ScrollableComponent>
    </Container>

<!--    <Container class="infoContainer print:!border-none">-->
<!--      <h2>运行数据:</h2>-->

<!--      <ScrollableComponent class="flex-grow px-4 pt-2">-->
<!--      </ScrollableComponent>-->
<!--    </Container>-->
  </div>
</template>

<script setup lang="ts">
import Container from '@/components/ContainerComponent.vue'
import TextInformation from '@/components/TextInformation.vue'
import ScrollableComponent from '@/components/ScrollableComponent.vue'
import { store } from '@/stores/store'
import { Overview } from '@/model/Overview'
import { onErrorCaptured, type PropType } from 'vue'
import { redirectOnError } from '@/router'
import type { CliOptions } from '@/model/CliOptions'
import { metricToolTips } from '@/model/MetricType'

defineProps({
  overview: {
    type: Object as PropType<Overview>,
    required: true
  },
  options: {
    type: Object as PropType<CliOptions>,
    // @deprecated since 5.0.0. When pre 5.0.0 format is no longer supported this can be made required
    required: false
  }
})

onErrorCaptured((error) => {
  redirectOnError(error, 'Error displaying information:\n', 'OverviewView', 'Back to overview')
  return false
})
</script>

<style scoped lang="postcss">
.infoContainer {
  @apply flex max-h-0 min-h-full flex-1 flex-col overflow-hidden print:max-h-none print:min-h-0 print:flex-none;
}
</style>
