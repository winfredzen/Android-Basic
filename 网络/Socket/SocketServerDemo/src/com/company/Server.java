package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * create by wangzhen 2021/11/18
 * socket服务器
 */
public class Server {
    private static final int PORT = 12345;
    private List<Socket> mSocketList = new ArrayList<>();
    private ServerSocket mServerSocket = null;
    private ExecutorService mExecutorService = null;

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
        try {
            mServerSocket = new ServerSocket(PORT);
            //创建线程池

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
