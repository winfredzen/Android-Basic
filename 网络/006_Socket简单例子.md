# Socket简单例子

内容来自：

+ [3.1 Android的网络编程Socket通信1](https://www.bilibili.com/video/BV1i64y1D7wa?from=search&seid=8462793985702774516&spm_id_from=333.337.0.0)



借用《UNIX网络编程》一书中的一张图，如下，基本TCP套接字编程
![022](https://github.com/winfredzen/Android-Basic/blob/master/网络/images/022.png)



## Java中的Socket

流程上基本类似
### Server
使用`ServerSocket`实现服务端

```java
public ServerSocket(int port) throws IOException
```
+ port - 端口，范围为`0~65535`，其中`0~1023`为系统的保留端口，注意端口不要被占用

创建socket后，代码运行会阻塞，等待别人连接

如下的一个简单的例子：

```java
//创建socket服务器
try {
    //服务器创建后会阻塞（程序暂停执行），直到有客户端的连接
    serverSocket = new ServerSocket(port);
    System.out.println("Socket服务器创建成功，等待连接...");
    //接收客户端的连接
    Socket socket = serverSocket.accept();
    //读或者写数据
    BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    //读取一行数据（注意，如果是多行，要循环下）
    String clientInfo = bReader.readLine();
    System.out.println("来自客户端的消息：" + clientInfo);
    //关闭连接
    socket.close();
} catch (IOException e) {
    e.printStackTrace();
}
```
一些说明：
1.`public Socket accept() throws IOException`方法，如果收到一个客户端的Socket的连接请求，该方法返回一个与连接客户端Socket对应的Socket
2.读取/写入字符
+ 通过`socket.getInputStream()`获取输入流
+ 通过`socket.getOutputStream()`获取输出流

### Client
客户端这边，创建socket，绑定ip和端口号

```java
public Socket(String host, int port)
```
读取/写入字符，与服务端类似

客户端的socket不得阻塞UI线程，所以放在一个线程中，主要代码如下：

```java
public class SocketThread extends Thread{

    Socket mSocket;
    public Handler revHandler;

    @Override
    public void run() {
        try {
            //创建socket
            //创建socket客户端并发起连接
            Socket socket = new Socket("192.168.17.51", 20000);
            //写入数据
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(("Hello, 我是来自客户端的消息" + "\n").getBytes());
            //关闭连接
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

点击某个按钮，启动这个线程

```java
public void onClick(View view) {
    SocketThread socketThread = new SocketThread();
    socketThread.start();
}
```



客户端连接服务端后，在服务端打印输出了客户端发过来的消息

![023](https://github.com/winfredzen/Android-Basic/blob/master/网络/images/023.png)

























