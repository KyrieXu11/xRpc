package com.kyriexu.server;

import com.kyriexu.codec.impl.JSONDecoder;
import com.kyriexu.codec.impl.JSONEncoder;
import com.kyriexu.codec.netty.NettyDecoder;
import com.kyriexu.codec.netty.NettyEncoder;
import com.kyriexu.enity.Request;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author KyrieXu
 * @since 2020/8/20 18:57
 **/
public class ServerRpcProxy {
    public static final Logger logger = LoggerFactory.getLogger(ServerRpcProxy.class);

    private ExecutorService threadPool = Executors.newFixedThreadPool(3);

    public void publishService(Object service, int port) throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(); //3
        // NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            ServiceHandler handler = new ServiceHandler(service);
            server.group(bossGroup)                                //4
                    .channel(NioServerSocketChannel.class)        //5
                    .localAddress(new InetSocketAddress(port))    //6
                    .childHandler(new ChannelInitializer<SocketChannel>() { //7
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline()
                                    .addLast(new NettyEncoder(new JSONEncoder()))
                                    .addLast(new NettyDecoder(new JSONDecoder(), Request.class))
                                    .addLast(handler);
                        }
                    });
            ChannelFuture f = server.bind().sync();            //8
            System.out.println(ServerRpcProxy.class.getName() + " 开始监听 " + f.channel().localAddress());
            f.channel().closeFuture().sync();            //9
        } finally {
            bossGroup.shutdownGracefully().sync();            //10
        }
    }
}
