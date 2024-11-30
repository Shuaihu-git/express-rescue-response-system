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
export function getConfig(configId) {
  return request({
    url: '/system/config/' + configId,
    method: 'get'
  })
}