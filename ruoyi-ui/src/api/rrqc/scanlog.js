import request from '@/utils/request'
// 查询登录日志列表
export function list(query) {
    return request({
      url: '/qrlog/list',
      method: 'get',
      params: query
    })
  }
// 查询参数详细
export function getScanLog(id) {
  return request({
    url: '/qrlog/' + id,
    method: 'get'
  })
}

//updateScanLog
// 处理日志状态
export function updateScanLog(data) {
  return request({
    url: '/qrlog/update',
    method: 'put',
    data: data
  })
}

export function getScanLogsOnThisMonth() {
  return request({
    url: '/qrlog/count',
    method: 'get',
  })
}
export function dealRate(){
  return request({
    url: '/qrlog/dealRate',
    method: 'get'
  })
}
export function getCountByType(type){
  return request({
    url: '/qrlog/count/'+type,
    method: 'get',
  })
}
export function getNumByHours(){
  return request({
    url: '/qrlog/hours',
    method: 'get',
  })
}
export function getCountOnTheWeekByType(type){
  return request({
    url: '/qrlog/week/count/'+type,
    method: 'get',
  })
}
export function getNumberOnTheWeek(){
  return request({
    url: '/qrlog/week/list',
    method: 'get',
  })
}