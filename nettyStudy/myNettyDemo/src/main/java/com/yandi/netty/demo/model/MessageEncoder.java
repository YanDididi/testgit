package com.yandi.netty.demo.model;

import com.yandi.netty.demo.until.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 *消息对象编码器
 * 将对象序列化成字节，通过ByteBuf形式进行传输，ByteBuf是一个byte存放的缓冲区，提供了读写操作。
 * */
public class MessageEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
        byte[] datas=ByteUtils.objectToByte(message);
        out.writeBytes(datas);
        ctx.flush();

    }
}
