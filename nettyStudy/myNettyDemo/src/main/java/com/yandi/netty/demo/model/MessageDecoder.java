package com.yandi.netty.demo.model;

import com.yandi.netty.demo.until.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 *消息对象解码器
 *接收ByteBuf数据，将ByteBuf反序列化成对象
 * */
public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        Object object=ByteUtils.byteToObject(ByteUtils.read(in));
        out.add(object);
    }
}
