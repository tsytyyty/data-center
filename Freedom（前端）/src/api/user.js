import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/vue-element-admin/user/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-element-admin/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}

export function dataSourceSave() {
  return request({
    url: '/dataSourceSave',
    method: 'post',
    data
  })
}

export function dataSourceConnectionTest() {
  return request({
    url: '/dataSourceTest',
    method: 'post',
    data
  })
}


export function dataAcquisition() {
  return request({
    url: '/dataAcquisition',
    method: 'post',
  })
}
