import request from '@/utils/request'
import requestLocal from '@/utils/requestLocal'


export function getInformation(params) {
  return request({
    url: '/api/v1/topics',
    method: 'get',
    params
  })
}

export function collectPost(data) {
  return request({
    url: '/api/v1/topic_collect/collect',
    method: 'post',
    data: data,
  })
}

export function collectPostLocal() {
  return requestLocal({
    url: '/',
    method: 'post',
    data: {},
  })
}