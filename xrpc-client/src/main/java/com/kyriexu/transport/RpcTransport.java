package com.kyriexu.transport;

import com.kyriexu.codec.Decoder;
import com.kyriexu.codec.Encoder;
import com.kyriexu.codec.impl.JSONDecoder;
import com.kyriexu.codec.impl.JSONEncoder;
import com.kyriexu.codec.utils.ConvertUtils;
import com.kyriexu.enity.Request;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
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
import java.util.Arrays;

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

    @ChannelHandler.Sharable
    private class ClientHandler extends ChannelInboundHandlerAdapter{
        public Object data;

        private Request request;

        public ClientHandler(Request request) {
            this.request = request;
        }

        // TODO: 写业务逻辑
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ByteBuf byteBuf = ConvertUtils.ObjectToBuf(request);
            ctx.writeAndFlush(byteBuf);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            this.data = ConvertUtils.BufToByte(msg, Object.class);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {                    //4
            cause.printStackTrace();
            ctx.close();
        }
    }

    public Object sendRequest(Request request) throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();                //1
            ClientHandler handler = new ClientHandler(request);
            b.group(group)                                //2
                    .channel(NioSocketChannel.class)            //3
                    .remoteAddress(new InetSocketAddress(host, port))    //4
                    .handler(new ChannelInitializer<SocketChannel>() {    //5
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(handler);
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

}
