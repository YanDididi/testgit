package com.yandi.netty.demo.client;


import com.yandi.netty.demo.kryo.KryoDecoder;
import com.yandi.netty.demo.kryo.KryoEncoder;
import com.yandi.netty.demo.model.Message;
import com.yandi.netty.demo.model.MessageDecoder;
import com.yandi.netty.demo.model.MessageEncoder;
import com.yandi.netty.demo.server.ServerPoHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.UUID;

public class ImClient  {

    private Channel channel;

    public Channel connect(String host, int port) {
        doConnect(host, port);
        return this.channel;
    }
    private void doConnect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ////实体类传输数据，jdk序列化
                    ch.pipeline().addLast("decoder",new MessageDecoder());
                    ch.pipeline().addLast("encoder",new MessageEncoder());
                    //实体类传输数据，kryo序列化
                    /*ch.pipeline().addLast("decoder",new KryoDecoder());
                    ch.pipeline().addLast("encoder",new KryoEncoder());*/
                    ch.pipeline().addLast(new ClientPoHandler());
                    //字符串传输数据
                    /*ch.pipeline().addLast("decoder", new StringDecoder());
                    ch.pipeline().addLast("encoder", new StringEncoder());
                    ch.pipeline().addLast(new ClientStringHandler());*/
                }
            });
            ChannelFuture f = b.connect(host, port).sync();
            channel = f.channel();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 2222;
        Channel channel = new ImClient().connect(host, port);
        //字符串传输
        /*channel.writeAndFlush("yandidi");*/
        //对象传输数据
        Message message = new Message();
        message.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        message.setContent("hello yandidi");
        channel.writeAndFlush(message);

    }
}

