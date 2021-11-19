package com.example.nettydemo;

import android.net.Uri;
import android.util.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class MyEncoder extends MessageToByteEncoder<User> {
    private static final String TAG = "MyEncoder";

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, User user, ByteBuf byteBuf) throws Exception {
        Log.d(TAG, "encode :" + user.toString());

        //1.创建一个内存输出流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //2.创建一个对象输出流
        ObjectOutputStream oos = new ObjectOutputStream(os);
        //3.把user对象写到内存流里面
        oos.writeObject(user);
        //4.通过内存流获取user对象转换后的字节数字
        byte[] bytes=os.toByteArray();
        //5.关闭流
        oos.close();
        os.close();

        //6.根据协议组装数据
        byteBuf.writeInt(1);//标识
        byteBuf.writeByte(1);//指令
        byteBuf.writeInt(bytes.length);//长度
        byteBuf.writeBytes(bytes);//数据内容
    }
}
