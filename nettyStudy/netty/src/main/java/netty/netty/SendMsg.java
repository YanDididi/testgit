package netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class SendMsg {

	
	public static void send(String id) {
		System.out.println("===发送msg=====chennl长度=== "+ChannelMapClass.channelMaps.size());
		Channel ctx = ChannelMapClass.get(id);
		if(ctx == null) {
			System.out.println("chennl 为 null, 终止发送任务");
			return;
		}
		ByteBuf buff = ctx.alloc().buffer();
		buff.writeBytes("发送成功".getBytes());
		ChannelFuture rs = ctx.writeAndFlush(buff);
		System.out.println(rs.isSuccess());
	}
}
