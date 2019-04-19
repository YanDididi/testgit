  package netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class TcpClient {

	public static String HOST = "127.0.0.1";
	public static int PORT = 8001;
	public static Bootstrap bootstrap = getBootstrap();
	public static Channel channel = getChannel(HOST, PORT);
	public static ChannelHandlerContext ctx;
	public static final Bootstrap getBootstrap() { 
		 EventLoopGroup group = new NioEventLoopGroup(); 
		 Bootstrap b = new Bootstrap(); 
		 b.group(group).channel(NioSocketChannel.class); 
		 b.handler(new ChannelInitializer<Channel>() {
			 @Override
			 protected void initChannel(Channel ch) throws Exception {
				 ChannelPipeline pipeline = ch.pipeline(); 
				 pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
				 pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
				 pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
				 pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
//				 pipeline.addLast("handler", new TcpHendler());
			 }
				
		}); 
			 b.option(ChannelOption.SO_KEEPALIVE, true); 
			  			return b; 
			}
		
	public static final Channel getChannel(String host, int port) { 
		 Channel channel = null; 
			 try {
		channel = bootstrap.connect(host, port).sync().channel(); 
			 } catch (Exception e) {
				 e.printStackTrace();
		 return null; 
			 }
		 return channel; 
	 }

	public static void sendMsg(String msg) throws Exception { 
		  if (channel != null) {
			  channel.writeAndFlush(msg).sync();
			  //ctx.writeAndFlush(msg).sync();
		  }else {
			  System.out.println("发送失败");
		  }
	}

	public static void main(String[] args) throws Exception {
	 try {
			 TcpClient.sendMsg("你好1");
		} catch (Exception e) { }
	}

}
