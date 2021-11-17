package com.example.myclient;

import android.os.Handler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * create by wangzhen 2021/11/17
 */
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
