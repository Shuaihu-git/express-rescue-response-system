import request from '@/utils/request'
// 查询登录日志列表
export function list(query) {
    return request({
      url: '/tel/get',
      method: 'get',
      params: query
    })
  }
export function getTels(){
    return request({
        url: '/tel/query',
        method: 'get'
    })
}
export function updateTels(data){
    return request({
        url: '/tel/update',
        method: 'put',
        data: data
    })
}