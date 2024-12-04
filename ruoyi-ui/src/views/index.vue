<template>
  <!-- <div class="app-container home">

  </div> -->
  <div class="dashboard">
    <audio id="myAudio" preload="auto" src="../assets/audio/sound.mp3" controls style="display: none;"></audio>
    <div class="top-stats">
      <div class="stat-item">本月发生事件数量（件）
        <div class="content-bar">
          </br><span class="content">{{ count }}</span>
          <!-- <span class="content-right">同比上月 <span style="color: red;">&#x25B2;</span> {{ contentRight }}%</span> -->
        </div>

      </div>

      <div class="stat-item">
        最近一周处理事件数量（件）
        <div class="content-bar">
          </br><span class="content">{{ weekDealNum }}</span>

          <div ref="barChart" class="barChart"></div>
        </div>

      </div>
      <!-- <div class="stat-item">
        本月平均处理时长（分钟）
        <div class="content-bar">
          </br><span class="content">12.5</span>
          <div ref="lineChart" class="lineChart"></div>
        </div>
      </div> -->
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
          <span class="alert-title">实时告警（ <span style="color: red; margin-left: 3px;margin-right: 3px;"> {{
            this.alerts.length }} </span> 条待处理）
            <div class="alert-status">
              <router-link :to="'/rrqc'" type="primary"
                style="float: right; font-weight: bold;font-size: 15px; color: blue;">更多 <span
                  style="font-size: large;">></span></router-link>
            </div>
          </span>
          <div class="alerts-container">
            <div class="scroll-container">
              <!-- 告警列表 -->
              <ul class="alerts-list">
                <li v-for="alert in alerts" :key="alert.id" class="alert-item">
                  <div class="alert-info">
                    <p style="font-size: 15px;">{{ alert.address }}处发生扫码事件</p>
                    <span class="time">{{ alert.time }}</span>
                    <span class="status-dot"></span>
                    <span class="status">待处理</span>
                  </div>
                  <div class="alert-status">
                    <router-link :to="'/rrqc'" type="primary"
                      style="float: right; font-weight: bold;font-size: medium; color: blue;">去处理 <span
                        style="font-size: large;">></span></router-link>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <div class="mid-right-bottom">
          <span class="pie-chart-title">事件发生时间段统计</span>
          <div ref="lineChart" class="piechart"></div>

        </div>
      </div>

    </div>

  </div>
</template>

<script>
import * as echarts from 'echarts';
import { Notification } from 'element-ui';
import { parseTime } from "../utils/ruoyi";
import { getScanLogsOnThisMonth, dealRate, getCountByType, getNumByHours, getCountOnTheWeekByType, getNumberOnTheWeek } from "../api/rrqc/scanlog";
export default {
  name: "Index",
  data() {
    return {
      count: 0,
      beepSound: new Audio('../assets/audio/sound.mp3'),
      barChart: null,
      barChartData: null,
      dealNum: null,
      pieChart: null,
      lineChart: null,
      lineChartData: null,
      rateChart: null,
      weekDealNum: null,
      contentRight: 12,
      // newLogCount: 5,
      RateChartValue: 0,
      alerts: [

      ],
      eventTimeStats: [
        { "time": "20:00-23:59", "count": 12, "percentage": "30%" },
        { "time": "0:00-4:59", "count": 10, "percentage": "25%" },
        { "time": "5:00-9:59", "count": 6, "percentage": "15%" },
        { "time": "15:00-19:59", "count": 4, "percentage": "10%" },
        { "time": "10:00-14:59", "count": 2, "percentage": "5%" }
      ],
      socket: null,
      newLogCount: 0,
      // 版本号
      version: "3.8.8"
    };
  },
  beforeUnmount() {
    // 在组件销毁时关闭 WebSocket
    if (this.socket) {
      this.socket.close();
    }
  },
  beforeMount() {
    // 在这里执行你想要在mounted之前进行的操作
    console.log('在组件即将挂载到DOM之前执行此操作');
    this.getNumByHours();
    // 添加点击事件监听器来初始化音频
document.body.addEventListener('click', this.initializeAudio);

// 添加页面滚动事件监听器来初始化音频
document.body.addEventListener('scroll', this.initializeAudio);

// 添加鼠标移动事件监听器来初始化音频
document.body.addEventListener('mousemove', this.initializeAudio);

  },
  mounted() {
    console.log("欢迎进入系统");
    // 初始化音频，避免浏览器权限限制
    // document.body.addEventListener('click', () => {
    //   this.beepSound.play().then(() => {
    //     this.beepSound.pause();
    //     this.beepSound.currentTime = 0; // 重置到开始位置
    //   }).catch(error => console.log('Audio permission initialized:', error));
    // });


    this.getNumByHours();
    this.getScanLogsOnThisMonth();
    this.dealRate();
    this.getCountByType();
    this.getCountOnTheWeekByType();
    this.getNumberOnTheWeek();


    // this.initBarChart();
    // this.initLineChart();
    // this.initRateChart();
    // // this.initMap();
    // this.initPieChart();
    this.connectWebSocket();
    // 自动清除悬浮通知
    setInterval(() => {
      this.clearNewLogCount();
      this.clearNewLog();
      window.location.reload();
    }, 3600000); // 每 3600秒清空一次
  },
  methods: {
    initializeAudio() {
    this.beepSound.play().then(() => {
        this.beepSound.pause();
        this.beepSound.currentTime = 0; // 重置到开始位置
    }).catch(error => console.log('Audio permission initialized:', error));
},
    getScanLogsOnThisMonth() {
      getScanLogsOnThisMonth().then(response => {
        this.count = response.data;
      })
    },
    dealRate() {
      dealRate().then(response => {
        console.log('获取当月事件处理率', response.msg);
        this.RateChartValue = response.msg;
        this.initRateChart();
      })
    },
    getCountByType() {
      getCountByType('1').then(response => {

        this.dealNum = response.data;
      })
    },
    getNumByHours() {
      getNumByHours().then(response => {

        this.lineChartData = response.data;
        this.initLineChart();
      })
    },
    getCountOnTheWeekByType() {
      getCountOnTheWeekByType('1').then(response => {
        this.weekDealNum = response.data;
      })
    },
    getNumberOnTheWeek() {
      getNumberOnTheWeek().then(response => {
        this.barChartData = response.data;
        this.initBarChart();
      })
    },
    playAudio() {
      const audio = document.getElementById('myAudio');
      if (audio) {
        audio.play();
      }
    },

    connectWebSocket() {
      console.log("开始连接 WebSocket");
      // 创建 WebSocket 连接
      // this.socket = new WebSocket("ws://192.168.10.136:9000/websocket/admin" + Date.now());
      this.socket = new WebSocket("ws://221.226.32.250:9023/websocket/admin" + Date.now());

      // WebSocket 连接成功
      this.socket.onopen = () => {
        console.log("WebSocket 已连接");
      };

      // 接收消息
      this.socket.onmessage = (event) => {
        const newLog = event.data;
        // this.alerts.push(JSON.parse(event.data)) ;
        const log = JSON.parse(newLog);
        log.time = parseTime(log.time);
        console.log('新日志添加', log);
        this.playAudio();
        this.alerts.unshift(log); // 新日志添加到列表顶部
        this.newLogCount++; // 增加新日志计数
        // 接收到消息时弹出消息弹窗
        Notification.success({ title: "您收到一条新的实时告警消息" })
      };

      // WebSocket 关闭
      this.socket.onclose = () => {
        console.log("WebSocket 已关闭");
      };

      // WebSocket 错误
      this.socket.onerror = (error) => {
        console.error("WebSocket 错误：", error);
      };
    },
    clearNewLogCount() {
      // 清空新日志计数
      this.newLogCount = 0;
    },
    clearNewLog() {
      // 清空新日志计数
      this.alerts = null;
    },
    /**
     * 重置新日志计数（当悬浮组件点击后）。
     */
    resetNewLogsCount() {
      this.newLogsCount = 0;
    },
    goTarget(href) {
      window.open(href, "_blank");
    },
    initBarChart() {
      this.barChart = echarts.init(this.$refs.barChart);
      this.barChart.setOption({
        xAxis: {
          show: false,
          type: 'category',
          data: [1, 2, 3, 4, 5, 6, 7]
        },
        yAxis: {
          show: true,
          type: 'value'
        },
        series: [
          {
            data: this.barChartData,
            type: 'bar'
          }
        ]
      });
    },
    initLineChart() {
      this.lineChart = echarts.init(this.$refs.lineChart);
      this.lineChart.setOption({
        color: ['#80FFA5'],
        title: {
          show: false,
          text: '事件发生时间段统计'
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
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            show: true,
            data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24']
          }
        ],
        yAxis: [
          {
            // inverse: true,
            show: true,
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
            showSymbol: true,
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
            data: this.lineChartData
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
    // initMap() {
    //   const mapChart = echarts.init(this.$refs.map);
    //   // 地图配置，需要引入地图数据
    // },
    initPieChart() {
      this.pieChart = echarts.init(this.$refs.pieChart);
      this.pieChart.setOption({
        xAxis: {
          show: false,
          type: 'category',
          data: ['1', '2', '3', '4', '5']
        },
        yAxis: {
          show: false,
          type: 'value'
        },
        series: [
          {
            data: [140, 232, 101, 264, 90
            ],
            type: 'bar'
          }
        ]
        // title: {
        //   text: '事件发生时间段统计',
        //   subtext: '事件总数: 40',
        //   left: 'center'
        // },
        // tooltip: {
        //   trigger: 'item'
        // },
        // legend: {
        //   orient: 'vertical',
        //   left: 'left'
        // },
        // series: [
        //   {
        //     name: '事件',
        //     type: 'pie',
        //     radius: ['50%', '70%'],
        //     avoidLabelOverlap: false,
        //     label: {
        //       show: false,
        //       position: 'center'
        //     },
        //     emphasis: {
        //       label: {
        //         show: true,
        //         fontSize: '20',
        //         fontWeight: 'bold'
        //       }
        //     },
        //     labelLine: {
        //       show: false
        //     },
        //     data: [
        //       { value: 12, name: '20:00 - 23:59' },
        //       { value: 10, name: '0:00 - 4:59' },
        //       { value: 6, name: '5:00 - 9:59' },
        //       { value: 4, name: '15:00 - 19:59' },
        //       { value: 2, name: '10:00 - 14:59' }
        //     ]
        //   }
        // ]
      });
    }
  }
};
</script>

<style scoped lang="scss">
.alerts-container {
  width: 100%;
  height: 100%;
  border-radius: 5px;
  background-color: white;
  text-align: left;
  // background-color: aqua;
  margin-bottom: 20px;
  padding: 3px 3px;
  // background-color: #4caf50;
}

.scroll-container {
  height: 300px;
  /* 固定高度 */
  overflow-y: auto;
  /* 启用垂直滚动 */
  width: 100%;
  /* 使宽度适应alerts-list */
}

.alerts-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.alert-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 5px;
  background-color: white;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.alert-info {
  max-width: 70%;
}

.time {
  color: #888;
  font-size: 12px;
}

.status {

  color: red;
  margin-left: 5px;
  font-size: 12px;

}

.status-dot {
  margin-left: 20px;
  width: 8px;
  height: 8px;
  background: red;
  border-radius: 50%;
  display: inline-block;
}

.alert-status {
  margin-left: 5px;
  display: flex;
  align-items: center;
  justify-content: left;
}

.dashboard {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  background-color: #eee;
}

.top-stats {
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-bottom: 20px;
}

.stat-item {
  width: 30%;
  height: 100px;
  padding: 20px;
  border-radius: 5px;
  background-color: white;
  text-align: left;
  font-size: 13px;
  color: #918b8b;
}

.piechart {
  width: 100%;
  height: 80%;
  display: flex;
  align-items: center;
  justify-content: right;
  background-color: white;
  position: relative;
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

}

.map-title {
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

.map-image {
  width: 100%;
  height: 100%;

}

.map {
  background-color: antiquewhite;
  background-image: url("../assets/images/u80.png");
}

.mid-right {
  width: 30%;
  height: 350px;
  margin-left: 2%;
}

.mid-right-top {
  width: 100%;
  height: 100%;
}

.alert-title {
  display: flex;
  padding: 10px;
  justify-content: left;
  align-items: center;
  text-align: left;
  width: 100%;
  // height: 20%;
  font-size: 17px;
  font-weight: bold;
  border-radius: 5px;
  background-color: #eddfdf;
}

.pie-chart-container {
  width: 100%;
  height: 100%;
  background-color: #4caf50;
}

.mid-right-bottom {
  width: 100%;
  height: 400px;
}

.pie-chart-title {
  display: flex;
  padding: 10px;
  margin-top: 10px;
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

.chart {
  margin-right: 10px;
  // float: right;
  width: 100%;
  height: 100%;
  display: flex;
  background-color: white;
  align-items: center;
  justify-content: center;
  position: absolute;
  z-index: 100;
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
