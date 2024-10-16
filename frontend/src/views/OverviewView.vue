<!--
  A view displaying the overview file of a JPlag report.
-->
<template>
  <div class="absolute bottom-0 left-0 right-0 top-0 flex flex-col">
    <div class="relative left-0 right-0 top-0 flex space-x-5 p-5 pb-0">
      <Container class="flex-grow">
        <h2>检测信息</h2>
        <div
          class="flex flex-row items-center space-x-5 print:flex-col print:items-start print:space-x-0"
        >
          <TextInformation label="提交总数" class="flex-auto">{{
            store().getSubmissionIds.length
          }}</TextInformation>

          <TextInformation label="已展示/全部比较总数" class="flex-auto">
            <template #default
              >{{ overview.shownComparisons }} / {{ overview.totalComparisons }}</template
            >
            <template #tooltip>
              <div class="whitespace-pre text-sm">
                <TextInformation label="Shown Comparisons">{{
                  overview.shownComparisons
                }}</TextInformation>
                <TextInformation label="Total Comparisons">{{
                  overview.totalComparisons
                }}</TextInformation>
                <div v-if="overview.missingComparisons > 0">
                  <TextInformation label="Missing Comparisons">{{
                    overview.missingComparisons
                  }}</TextInformation>
                  <p>
                    To include more comparisons in the report modify the number of shown comparisons
                    in the CLI.
                  </p>
                </div>
              </div>
            </template>
          </TextInformation>

          <TextInformation label="最小Token匹配值" class="flex-auto">
            <template #default>
              {{ overview.matchSensitivity }}
            </template>
            <template #tooltip>
              <div class="whitespace-pre text-sm">
                <p>
                  Tunes the comparison sensitivity by adjusting the minimum token required to be
                  counted as a matching section.
                </p>
                <p>It can be adjusted in the CLI.</p>
              </div>
            </template>
          </TextInformation>

          <ToolTipComponent direction="left" class="flex-grow-0 print:hidden">
            <template #default>
              <Button @click="router.push({ name: 'InfoView' })"> 更多 </Button>
            </template>
            <template #tooltip>
              <p class="whitespace-pre text-sm">More information about the CLI run of JPlag</p>
            </template>
          </ToolTipComponent>
        </div>
      </Container>
    </div>

    <div
      class="relative bottom-0 left-0 right-0 flex flex-grow space-x-5 px-5 pb-7 pt-5 print:flex-col print:space-x-0 print:space-y-5"
    >
      <Container
        class="flex max-h-0 min-h-full flex-1 flex-col print:max-h-none print:min-h-fit print:flex-none"
      >
        <NTabs size="large" type="segment" tab-class="bg-gray-200" pane-class="flex-1 flex" class="flex-1">
          <NTabPane name="123" tab="对比结果图例">
            <NFlex vertical class="w-full flex-1">
              <DistributionDiagram
                :distribution="overview.distribution[store().uiState.distributionChartConfig.metric]"
                :x-scale="store().uiState.distributionChartConfig.xScale"
                class="h-2/3 w-full print:h-fit print:w-fit"
              />
              <div class="flex flex-grow flex-col space-y-1 print:grow-0 flex-1">
                <h3 class="text-lg underline">选项</h3>
                <ScrollableComponent class="h-fit flex-grow">
                  <MetricSelector
                    class="mt-2"
                    title="比较:"
                    :defaultSelected="store().uiState.distributionChartConfig.metric"
                    @selection-changed="
                (metric: MetricType) => (store().uiState.distributionChartConfig.metric = metric)
              "
                  />
                  <OptionsSelector
                    class="mt-2"
                    title="X轴缩放:"
                    :labels="['线性', '对数']"
                    :defaultSelected="store().uiState.distributionChartConfig.xScale == 'linear' ? 0 : 1"
                    @selection-changed="
                (i: number) =>
                  (store().uiState.distributionChartConfig.xScale =
                    i == 0 ? 'linear' : 'logarithmic')
              "
                  />
                </ScrollableComponent>
              </div>
            </NFlex>
          </NTabPane>
          <NTabPane name="234" tab="集体相似列表">
            <NFlex vertical class="w-full flex-1">
              <NCollapse arrow-placement="left">
                <NCollapseItem v-for="(cluster, index) in clusters" :key="cluster.averageSimilarity" v-show="cluster.members.length > 2" :name="index">
                  <template #header>
                    <NSpace align="center">
                      <NText>{{`平均近似度：${(cluster.averageSimilarity * 100).toFixed(2)}%`}}</NText>
                      <NText>{{`匹配数量：${cluster.members.length}`}}</NText>
                      <RouterLink :to="{
                        name: 'ClusterView',
                        params: {
                          clusterIndex: index
                        }
                      }"><NText type="info">查看详情</NText></RouterLink>
                    </NSpace>
                  </template>
                  <NList bordered hoverable show-divider>
                    <NListItem v-for="name in cluster.members" :key="name">
                      {{store().getDisplayName(name)}}
                    </NListItem>
                  </NList>
                </NCollapseItem>
              </NCollapse>
            </NFlex>
          </NTabPane>
        </NTabs>
      </Container>

      <Container class="flex max-h-0 min-h-full flex-1 flex-col print:hidden">
        <ComparisonsTable
          :clusters="overview.clusters"
          :top-comparisons="overview.topComparisons"
          class="min-h-0 flex-1 print:min-h-full print:flex-grow"
        >
          <template #footer v-if="overview.topComparisons.length < overview.totalComparisons">
            <p class="w-full pt-1 text-center font-bold">
              没有可展示的数据，若有疑惑，请点击“更多”查看检测结果详情
            </p>
          </template>
        </ComparisonsTable>
      </Container>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, type PropType, onErrorCaptured, type ComputedRef } from 'vue'
import { redirectOnError, router } from '@/router'
import DistributionDiagram from '@/components/DistributionDiagram.vue'
import ComparisonsTable from '@/components/ComparisonsTable.vue'
import { store } from '@/stores/store'
import Container from '@/components/ContainerComponent.vue'
import Button from '@/components/ButtonComponent.vue'
import ScrollableComponent from '@/components/ScrollableComponent.vue'
import { MetricType } from '@/model/MetricType'
import TextInformation from '@/components/TextInformation.vue'
import MetricSelector from '@/components/optionsSelectors/MetricSelector.vue'
import ToolTipComponent from '@/components/ToolTipComponent.vue'
import OptionsSelector from '@/components/optionsSelectors/OptionsSelectorComponent.vue'
import { Overview } from '@/model/Overview'
import type { Cluster } from '@/model/Cluster'

const props = defineProps({
  overview: {
    type: Object as PropType<Overview>,
    required: true
  }
})

const clusters: ComputedRef<Cluster[]> = computed(() => {
  return props.overview?.clusters
})

document.title = `${store().state.uploadedFileName} - JPlag Report Viewer`

const hasMoreSubmissionPaths = computed(() => props.overview?.submissionFolderPath.length > 1)
const submissionPathValue = computed(() =>
  hasMoreSubmissionPaths.value
    ? 'Click More to see all paths'
    : props.overview.submissionFolderPath[0]
)
onErrorCaptured((error) => {
  redirectOnError(error, 'Error displaying overview:\n')
  return false
})
</script>
