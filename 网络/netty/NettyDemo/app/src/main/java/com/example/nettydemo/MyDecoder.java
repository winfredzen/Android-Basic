package com.example.nettydemo;

import android.util.Log;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    private static final String TAG = "MyDecoder";
    
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Log.d(TAG, "decode :" + byteBuf.toString());

        //1.根据协议分别取出对应的数据
        int tag=byteBuf.readInt();//标识符
        byte code=byteBuf.readByte();//指令
        int len=byteBuf.readInt();//长度
        byte[] bytes=new byte[len];//定义一个字节数据，长度是数据的长度
        byteBuf.readBytes(bytes);//往字节数组读取数据

        //2.通过对象流来转换字节流，转换成User对象
        ByteArrayInputStream is=new ByteArrayInputStream(bytes);
        ObjectInputStream iss=new ObjectInputStream(is);
        User user=(User)iss.readObject();
        is.close();
        iss.close();

        list.add(user);

    }
}
