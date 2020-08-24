package com.kyriexu.transport;

import com.kyriexu.codec.impl.JSONDecoder;
import com.kyriexu.codec.impl.JSONEncoder;
import com.kyriexu.codec.netty.NettyDecoder;
import com.kyriexu.codec.netty.NettyEncoder;
import com.kyriexu.enity.Request;
import com.kyriexu.enity.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author KyrieXu
 * @since 2020/8/20 20:04
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcTransport {
    public static final Logger logger = LoggerFactory.getLogger(RpcTransport.class);

    private String host;
    private int port;

    public Object sendRequest(Request request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();                //1
            ClientHandler handler = new ClientHandler(request);
            b.group(group)                                //2
                    .channel(NioSocketChannel.class)            //3
                    .remoteAddress(new InetSocketAddress(host, port))    //4
                    .handler(new ChannelInitializer<SocketChannel>() {    //5
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new NettyEncoder(new JSONEncoder()))
                                    .addLast(new NettyDecoder(new JSONDecoder(), Response.class))
                                    .addLast(handler);
                        }
                    });
            ChannelFuture f = b.connect().sync();        //6
            f.channel().closeFuture().sync();            //7
            // f.channel().attr()
            return handler.data;
        } finally {
            group.shutdownGracefully().sync();            //8
        }
    }

    @ChannelHandler.Sharable
    private class ClientHandler extends ChannelInboundHandlerAdapter {
        public Object data;

        private Request request;

        public ClientHandler(Request request) {
            this.request = request;
        }

        // TODO: 写业务逻辑
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.writeAndFlush(request);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Response resp = (Response) msg;
            System.out.println(resp);
            this.data = resp.getResult();
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.channel().close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {                    //4
            cause.printStackTrace();
            ctx.close();
        }
    }

}
