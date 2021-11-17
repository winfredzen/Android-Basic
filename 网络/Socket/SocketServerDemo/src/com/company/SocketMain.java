package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketMain {
    private static ServerSocket serverSocket;
    private static int port = 20000;

    public static void main(String[] args) {
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

    }
}
