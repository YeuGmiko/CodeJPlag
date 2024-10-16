/**
 * This enum maps the metric type to the index they have in the generated JSON and respectivly in the store.
 */
export enum MetricType {
  AVERAGE = 'AVG',
  MAXIMUM = 'MAX'
}

export type MetricToolTipData = {
  longName: string
  shortName: string
  tooltip: string
}

export const metricToolTips: Record<MetricType, MetricToolTipData> = {
  [MetricType.AVERAGE]: {
    longName: '平均近似值',
    shortName: 'AVG',
    tooltip:
      '两个文件的平均近似度，数值高则表示两程序的代码逻辑相似'
  },
  [MetricType.MAXIMUM]: {
    longName: '最大近似值',
    shortName: 'MAX',
    tooltip:
      '两个文件的最大近似度，如果两程序文件的大小非常不同，则非常有用'
  }
}
