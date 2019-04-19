package netty.netty;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
/**
 * ChannelInboundHandlerAdapter 该类处理客户端发来的数据
 * @author lenovo
 *
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf buff = ctx.alloc().buffer();
		try {
			ByteBuf in = (ByteBuf) msg;
			byte[] bytes = new byte[in.readableBytes()];
			in.readBytes(bytes);
			System.out.println("收到消息:" + new String(bytes, Charset.forName("UTF-8")));
			String str = "response " + new Date();
			buff.writeBytes(str.getBytes());
			ChannelFuture rs = ctx.channel().writeAndFlush(buff);
			System.out.println(rs.isSuccess());
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ByteBuf buff = ctx.alloc().buffer();
		try {
			String str = "发生异常!  " + new Date();
			buff.writeBytes(str.getBytes());
			ChannelFuture rs = ctx.channel().writeAndFlush(buff);
			System.out.println(rs);
		} finally {
			System.out.println("发生异常! ");
			ctx.close();
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("已连接:" + ctx.channel().remoteAddress());
		System.out.println("添加channel " + ctx.channel().hashCode());
		ChannelMapClass.add(ctx.channel().hashCode()+"", ctx.channel());
		System.out.println("=====当前channel长度-==== " + ChannelMapClass.channelMaps.size());
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("已断开:" + ctx.channel().remoteAddress());
		ctx.fireChannelUnregistered();
	}


}
