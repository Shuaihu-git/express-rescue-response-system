<template>
  <!-- <div class="app-container home">
    
  </div> -->
  <div class="dashboard">
    <div class="top-stats">
      <div class="stat-item">本月发生事件数量（件）
        <div class="content-bar">
          </br><span class="content">100</span>
          <span class="content-right">同比上月 <span style="color: red;">&#x25B2;</span> {{ contentRight }}%</span>
        </div>

      </div>

      <div class="stat-item">
        本月处理事件数量（件）
        <div class="content-bar">
          </br><span class="content">96</span>
          <!-- <div class="bar-chart">
          <div class="bar" :style="{ width: '96%' }"></div>
        </div> -->
          <div ref="barChart" class="barChart"></div>
        </div>

      </div>
      <div class="stat-item">
        本月平均处理时长（分钟）
        <!-- 使用ECharts绘制折线图 -->
        <div class="content-bar">
          </br><span class="content">12.5</span>
          <div ref="lineChart" class="lineChart"></div>
        </div>

      </div>
      <div class="stat-item">
        本月事件处理率

        <!-- 使用ECharts绘制环形进度条 -->
        <div class="content-bar">
          </br><span class="content">{{ this.RateChartValue }}%</span>
          <div ref="rateChart" class="rateChart"></div>
        </div>


      </div>
    </div>
    <div class="mid-container">
      <div class="map-container">
        <span class="map-title">告警地图</span>
        <!-- <div class="map" ref="map"></div> -->
         <img class="map-image" src="../assets/images/u80.png" alt="地图示例">
      </div>
      <div class="mid-right">
        <div class="mid-right-top">
          <span class="alert-title">实时告警（4条待处理）</span>
        <div class="alerts-container">
          
          <ul>
            <li v-for="alert in alerts" :key="alert.id">
              {{ alert.time }} - {{ alert.location }}
            </li>
          </ul>
        </div>
        </div>
      <div class="mid-right-bottom">
          <span class="pie-chart-title">事件发生时间段统计</span>
          <div ref="pieChart" class="chart"></div>
      </div>
        
        
      </div>

    </div>

  </div>
</template>

<script>
import * as echarts from 'echarts';
export default {
  name: "Index",
  data() {
    return {
      barChart: null,
      pieChart: null,
      lineChart: null,
      rateChart: null,
      contentRight: 12,
      RateChartValue: 90,
      alerts: [
        { "id": 1, "time": "2020-12-23 22:31:00", "location": "宁沪高速桩号k100+000", "status": "待处理" },
        { "id": 2, "time": "2020-12-23 22:32:00", "location": "宁沪高速桩号k150+000", "status": "待处理" },
        { "id": 3, "time": "2020-12-23 22:33:00", "location": "宁沪高速桩号k200+000", "status": "待处理" },
        { "id": 4, "time": "2020-12-23 22:34:00", "location": "宁沪高速桩号k250+000", "status": "待处理" },
        { "id": 5, "time": "2020-12-23 22:35:00", "location": "宁沪高速桩号k300+000", "status": "待处理" }
      ],
      eventTimeStats: [
        { "time": "20:00-23:59", "count": 12, "percentage": "30%" },
        { "time": "0:00-4:59", "count": 10, "percentage": "25%" },
        { "time": "5:00-9:59", "count": 6, "percentage": "15%" },
        { "time": "15:00-19:59", "count": 4, "percentage": "10%" },
        { "time": "10:00-14:59", "count": 2, "percentage": "5%" }
      ],
      // 版本号
      version: "3.8.8"
    };
  },
  mounted() {
    this.initBarChart();
    this.initLineChart();
    this.initRateChart();
    this.initMap();
    this.initPieChart();
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank");
    },
    initBarChart() {

      this.barChart = echarts.init(this.$refs.barChart);
      this.barChart.setOption({
        xAxis: {
          show: false,
          type: 'category',
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31']
        },
        yAxis: {
          show: false,
          type: 'value'
        },
        series: [
          {
            data: [140, 232, 101, 264, 90, 340, 250, 200, 167, 157,
              16, 36, 240, 213, 222, 32, 87, 78, 89, 65,
              215, 25, 163, 98, 78, 68, 135, 12, 75, 324,
              321
            ],
            type: 'bar'
          }
        ]
      });
    }
    ,
    initLineChart() {
      this.lineChart = echarts.init(this.$refs.lineChart);
      this.lineChart.setOption({
        color: ['#80FFA5'],
        title: {
          show: false,
          text: 'Gradient Stacked Area Chart'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {
          show: false,
          data: ['Line 1']
        },
        toolbox: {
          feature: {
            saveAsImage: {
              show: false
            }
          }
        },
        grid: {
          // left: '3%',
          // right: '4%',
          // bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            show: false,
            data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31']
          }
        ],
        yAxis: [
          {
            // inverse: true,
            show: false,
            type: 'value'
          }
        ],
        series: [
          {
            name: 'Line 1',
            type: 'line',
            stack: 'Total',
            smooth: true,
            lineStyle: {
              width: 0
            },
            showSymbol: false,
            areaStyle: {
              opacity: 0.8,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(128, 255, 165)'
                },
                {
                  offset: 1,
                  color: 'rgb(1, 191, 236)'
                }
              ])
            },
            emphasis: {
              focus: 'series'
            },
            data: [140, 232, 101, 264, 90, 340, 250, 200, 167, 157,
              16, 36, 240, 213, 222, 32, 87, 78, 89, 65,
              215, 25, 163, 98, 78, 68, 135, 12, 75, 324,
              321
            ]
          }
        ]
      });
    },
    initRateChart() {
      this.rateChart = echarts.init(this.$refs.rateChart);
      this.rateChart.setOption({
        series: [{
          type: 'gauge',
          startAngle: 90,
          endAngle: -270,
          progress: {
            show: true,
            overlap: false,
            roundCap: true,
            clip: false,
            itemStyle: {
              borderWidth: 3,
              // borderColor: 'bule'
            }
          },
          axisLine: { lineStyle: { width: 3 } },
          pointer: { show: false },
          axisTick: { show: false },
          splitLine: { show: false },
          axisLabel: { show: false },
          detail: {
            show: false, // 隐藏中心的详细数值
            valueAnimation: true,
            formatter: '{value}%'
          },
          data: [{ show: false, value: this.RateChartValue, name: '' }]
        }]
      });
    },
    initMap() {
      const mapChart = echarts.init(this.$refs.map);
      // 地图配置，需要引入地图数据
    },
    initPieChart() {
      this.pieChart = echarts.init(this.$refs.pieChart);
      this.pieChart.setOption({
        series: [{
          type: 'pie',
          radius: '50%',
          data: [
            { value: 30, name: '20:00-23:59' },
            { value: 25, name: '0:00-4:59' },
            // ...其他时间段数据
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      });
    }
  }
};
</script>

<style scoped lang="scss">
.dashboard {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
  background-color: #eee;
}

.top-stats {
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-bottom: 20px;
}

.stat-item {
  width: 23%;
  height: 100px;
  padding: 20px;
  border-radius: 5px;
  background-color: white;
  text-align: left;
  font-size: 13px;
  color: #918b8b;
}

.rateChart {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: right;
  margin-left: 30%;
  margin-bottom: 10%;
  // background-color: aqua;
  position: relative;
}

.lineChart {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 5%;
}

.barChart {
  width: 80%;
  height: 80%;
  margin-left: 10%;
  display: flex;
  margin-top: 5%;
  margin-bottom: 10%;
  align-items: center;
  justify-content: center;
}

.content {
  width: 25%;
  height: 75%;
  display: flex;
  // margin-top: 10px;
  align-items: center;
  justify-content: left;
  font-size: 20px;
  color: black;
}

.content-right {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: right;
  // margin-bottom: 5%;
  // background-color: #4caf50;
}

.content-bar {
  width: 100%;
  // margin-top: 10px;
  height: 100%;
  // background-color: #918b8b;
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.chart {
  // float: left;
  width: 75%;
  height: 75%;
  display: flex;
  margin-left: 30px;
  align-items: center;
  justify-content: left;
  position: relative;
}

.bar-chart {
  // display: block;
  width: 75%;
  height: 20px;
  display: flex;
  position: relative;
  align-items: center;
  justify-content: left;
  background-color: #f0f0f0;
  margin-left: 5px;
}

.bar {
  height: 100%;
  width: 100%;
  background-color: #4caf50;
}

.mid-container {
  width: 100%;
  height: 100%;
  display: flex;
  // align-items: center;
  justify-content: left;
  // background-color: #4caf50;
  
}
.map-title{
  display: flex;
  padding: 10px;
  justify-content: left;
  align-items: center;
  text-align: center;
  width: 100%;
  // height: 20%;
  font-size: 20px;
  font-weight: bold;
  border-radius: 5px;
  background-color: #eddfdf;
}


.map-container {
  width: 70%;
  height: 100%;
  margin-bottom: 20px;
  
}
.map-image{
  width: 100%;
  height: 100%;

}
.map{
  background-color: antiquewhite;
  background-image: url("../assets/images/u80.png");
}
.mid-right{
  width: 30%;
  height: 100%;
  margin-left: 2%;
  background-color: #918b8b;
  // display: flex;
  // justify-content: center;
  // align-items: center;

}
.mid-right-top{
  width: 100%;
  height: 50%;
  
}
.alert-title{
  display: flex;
  padding: 10px;
  justify-content: left;
  align-items: center;
  text-align: center;
  width: 100%;
  // height: 20%;
  font-size: 20px;
  font-weight: bold;
  border-radius: 5px;
  background-color: #eddfdf;
}
.alerts-container {
  width: 100%;
  margin-bottom: 20px;

}

.pie-chart-container {
  width: 100%;
  height: 50%;
}
.pie-chart-title{
  display: flex;
  padding: 10px;
  justify-content: left;
  align-items: center;
  text-align: center;
  width: 100%;
  // height: 20%;
  font-size: 20px;
  font-weight: bold;
  border-radius: 5px;
  background-color: #eddfdf;
}
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans",
  "Helvetica Neue",
  Helvetica,
  Arial,
  sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>
