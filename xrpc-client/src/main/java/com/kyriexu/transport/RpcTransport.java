package com.kyriexu.transport;

import com.kyriexu.enity.Request;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author KyrieXu
 * @since 2020/8/20 20:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcTransport {
    public static final Logger logger= LoggerFactory.getLogger(RpcTransport.class);

    private String host;
    private int port;

    private class ClientHandler extends ChannelInboundHandlerAdapter{
        public Object data;
        // TODO: 写业务逻辑

    }

    public Object sendRequest(Request request) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();                //1
            ClientHandler handler = new ClientHandler();
            b.group(group)                                //2
                    .channel(NioSocketChannel.class)            //3
                    .remoteAddress(new InetSocketAddress(host, port))    //4
                    .handler(new ChannelInitializer<SocketChannel>() {    //5
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    handler);
                        }
                    });

            ChannelFuture f = b.connect().sync();        //6

            f.channel().closeFuture().sync();            //7

            return handler.data;
        } finally {
            group.shutdownGracefully().sync();            //8
        }
    }

}
