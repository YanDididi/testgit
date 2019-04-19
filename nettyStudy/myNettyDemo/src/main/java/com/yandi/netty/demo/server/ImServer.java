package com.yandi.netty.demo.server;

import com.yandi.netty.demo.kryo.KryoDecoder;
import com.yandi.netty.demo.kryo.KryoEncoder;
import com.yandi.netty.demo.model.MessageDecoder;
import com.yandi.netty.demo.model.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ImServer {
    public void run(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        //实体类传输数据，jdk序列化
                        ch.pipeline().addLast("decoder",new MessageDecoder());
                        ch.pipeline().addLast("encoder",new MessageEncoder());
                        //实体类传输数据，kryo序列化
                        /*ch.pipeline().addLast("decoder",new KryoDecoder());
                        ch.pipeline().addLast("encoder",new KryoEncoder());*/
                        ch.pipeline().addLast(new ServerPoHandler());
                        //字符串数据传输
                        /*ch.pipeline().addLast("decoder", new StringDecoder());
                        ch.pipeline().addLast("encoder", new StringEncoder());
                        ch.pipeline().addLast(new ServerStringHandler());*/
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
        int port = 2222;
        new Thread(() -> {
            new ImServer().run(port);
        }).start();
    }
}
