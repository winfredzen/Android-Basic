# OkHttp&Retrofit

[okhttp](https://github.com/square/okhttp)的使用流程

+ 创建请求 - `Request.Builder()` -> `Request`对象
+ 通过`Request`对象得到`Call`对象 - `client.newCall(request)` -> `Call`对象
+ 执行`Call` - 同步`call.execute()`，异步`call.enqueue()`
+ 得到`Response`对象



