package com.example.nettydemo;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * create by wangzhen 2021/11/18
 */
public class NettyClient {

    private static final String TAG = "NettyClient_TAG";

    private static final String IP = "192.168.17.51";
    private static final int PORT = 20000;

    public void connect() {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                // 1.指定线程模型
                .group(workerGroup)
                // 2.指定 IO 类型为 NIO
                .channel(NioSocketChannel.class)
                // 3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        //自定义业务 Handler
                        //ch.pipeline().addLast(new NettyClientHandler());

                        //1.拆包器
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,5,4));
                        //2.自定义编码器
                        ch.pipeline().addLast(new MyDecoder());
                        ch.pipeline().addLast(new MyEncoder());
                        //3.业务处理Handler
                        ch.pipeline().addLast(new ClientTestHandler());

                    }
                });

        // 4.建立连接

        connect(bootstrap, IP, PORT);

//        ChannelFuture future = bootstrap.connect(IP, PORT);
//        future.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                if (future.isSuccess()) {
//                    Log.d(TAG, "连接成功!");
//                } else {
//                    Log.d(TAG, "连接失败!");
//                }
//            }
//        });

//        try {
//            ChannelFuture future=bootstrap.connect("192.168.17.51", 20000).sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    //处理失败重连

    private static void connect(Bootstrap bootstrap, String host, int port) {
        bootstrap.connect(host, port).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    Log.d(TAG, "连接成功!");
                } else {
                    Log.d(TAG, "连接失败!");

                    //第一种方式递归调用
                    //connect(bootstrap, host, port);

                    // 避免短时间内频繁的请求连接，可以使用定时线程池来每隔 n 秒重连一次。
                    EventLoopGroup eventLoopGroup = bootstrap.config().group();
                    eventLoopGroup.schedule(new Runnable() {
                        @Override
                        public void run() {
                            connect(bootstrap, host, port);
                        }
                    }, 5, TimeUnit.SECONDS);

                }
            }
        });
    }

//    class NettyClientHandler extends ChannelInboundHandlerAdapter {
//        //客户端连接成功之后触发该事件，只会触发一次
//        @Override
//        public void channelActive(ChannelHandlerContext ctx) throws Exception {
//            Log.d("NettyClientHandler", "channelActive");
//
//            ctx.channel().writeAndFlush(Unpooled.copiedBuffer("Hello World".getBytes()));
//        }
//
//        @Override
//        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//            super.channelInactive(ctx);
//
//            Log.d("NettyClientHandler", "channelInactive");
//        }
//
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//            super.exceptionCaught(ctx, cause);
//
//            Log.d("NettyClientHandler", "exceptionCaught");
//        }
//
//        //接受服务端响应时触发该事件
//        @Override
//        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//            Log.d("NettyClientHandler", "channelRead");
//
//
//
//            ByteBuf buffer=(ByteBuf)msg;
//            byte[] bytes=new byte[buffer.readableBytes()];
//            buffer.readBytes(bytes);
//            String res=new String(bytes,"UTF-8");
//            System.out.println("服务端响应："+res);
//        }
//    }


    public class ClientTestHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            Log.d("ClientTestHandler", "channelActive");

            for (int i = 0; i < 1000; i++) {
                User user = new User();
                user.setName(i + "->zwy");
                user.setAge(18);

                //注意，这里直接写user对象，无需再手工转换字节流了，编码器会自动帮忙处理。
                ctx.channel().writeAndFlush(user);
            }
        }
    }


}
