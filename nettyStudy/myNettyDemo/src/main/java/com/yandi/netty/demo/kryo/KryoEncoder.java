package com.yandi.netty.demo.kryo;

import com.yandi.netty.demo.model.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class KryoEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        KryoSerializer.serialize(msg,out);
        ctx.flush();
    }
}
