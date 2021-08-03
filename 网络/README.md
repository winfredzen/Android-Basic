# Android原生网络操作

网络权限

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

网络状态权限

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

**HTTP请求限制**

从Android P开始，默认不再允许直接访问HTTP请求，**需要通过设置Network Security Configuration支持**

在res目录下，创建一个xml目录，在xml目录下，创建一个`network_security_config.xml`文件，内容如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true">
        <trust-anchors>
            <certificates src="system" />
            <certificates src="user" />
        </trust-anchors>
    </base-config>
</network-security-config>
```

然后在`AndroidManifest.xml`中引用这个文件

```xml
    <application
        ...
        android:networkSecurityConfig="@xml/network_security_config"
        ...>
```



**GET请求vsPOST请求**

如下是简单的GET请求和POST请求，注意这里只是演示，其中的输入流和输出流，并没有被关闭，在实际应用中需要关闭

```java
    public void myClick(View view) {
        switch (view.getId()) {
            case R.id.get_button:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        get();
                    }
                }.start();
            case R.id.post_button:
                String account = mAccountEdt.getText().toString();
                String password = mPwdEdt.getText().toString();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        post(account, password);
                    }
                }.start();
                break;
            default:
                break;
        }
    }

    private void post(String account, String password) {
        //实例化URL对象
        try {
            URL url = new URL("https://www.imooc.com/api/okhttp/postmethod");
            //获取HttpURLConnection实例
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            //设置和请求相关的属性
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(6000);
            //设置允许输出
            connection.setDoOutput(true);
            //设置提交数据的类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //获取输出流
            OutputStream outputStream = connection.getOutputStream();
            //写数据
            outputStream.write(("account="+account+"&pwd="+password).getBytes());
            //获取响应码
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //判断响应码并获取响应数据
                //获取输入流
                InputStream is = connection.getInputStream();
                //循环中读取输入流
                byte[] b = new byte[1024];
                int len = 0;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = is.read(b)) > -1) {
                    //将字节数组里面的内容存入缓存流
                    baos.write(b, 0, len);
                }

                String msg = new String(baos.toByteArray());
                Log.d(TAG, "msg  = " + msg);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void get() {
        //实例化URL对象
        try {
            URL url = new URL("https://www.imooc.com/api/teacher?type=3&cid=1");
            //获取HttpURLConnection实例
            HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
            //设置和请求相关的属性
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);
            //获取响应码
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //判断响应码并获取响应数据
                //获取输入流
                InputStream is = connection.getInputStream();
                //循环中读取输入流
                byte[] b = new byte[1024];
                int len = 0;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = is.read(b)) > -1) {
                    //将字节数组里面的内容存入缓存流
                    baos.write(b, 0, len);
                }

                String msg = new String(baos.toByteArray());
                Log.d(TAG, "msg  = " + msg);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
```





























