import axios from 'axios'


// axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// axios.defaults.withCredentials = true;

// 创建axios实例
const service = axios.create({
 
  baseURL:'https://cnodejs.org',
  // 超时
  timeout: 50000
})

// // request拦截器
// service.interceptors.request.use(config => {
//   return config
// }, error => {
//     Promise.reject(error)
// })

// // 响应拦截器
// service.interceptors.response.use(res => {
//       return res.data
//   },
//   error => {
//     console.log('err' + error)
//     let { message } = error;
//     if (message == "Network Error") {
//       message = "后端接口连接异常";
//     }
//     else if (message.includes("timeout")) {
//       message = "系统接口请求超时";
//     }
//     else if (message.includes("Request failed with status code")) {
//       message = "系统接口" + message.substr(message.length - 3) + "异常";
//     }
//     console.log('message', message)
//     return Promise.reject(error)
//   }
// )

export default service
