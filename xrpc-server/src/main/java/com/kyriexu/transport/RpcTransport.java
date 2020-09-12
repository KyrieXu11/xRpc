package com.kyriexu.transport;

import com.kyriexu.client.ClientHandler;
import com.kyriexu.codec.json.JSONDecoder;
import com.kyriexu.codec.json.JSONEncoder;
import com.kyriexu.codec.netty.NettyDecoder;
import com.kyriexu.codec.netty.NettyEncoder;
import com.kyriexu.enity.Request;
import com.kyriexu.enity.Response;
import com.kyriexu.future.ResponseMap;
import com.kyriexu.registry.ServiceDiscovery;
import com.kyriexu.singleton.SingletonFactory;
import com.kyriexu.utils.SpiUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @author KyrieXu
 * @since 2020/8/20 20:04
 **/
@Slf4j
public class RpcTransport {
    public static final Logger logger = LoggerFactory.getLogger(RpcTransport.class);
    @Setter
    @Getter
    private String host;

    @Setter
    @Getter
    private Class<?> aClazz;

    @Setter
    @Getter
    private int port;
    private ResponseMap map;
    private EventLoopGroup boss;
    private Bootstrap b;

    public RpcTransport() {
        map = SingletonFactory.getInstance(ResponseMap.class);
        boss = new NioEventLoopGroup();
        b = new Bootstrap();
        ClientHandler handler = new ClientHandler();
        b.group(boss)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new NettyEncoder(new JSONEncoder()))
                                .addLast(new NettyDecoder(new JSONDecoder(), Response.class))
                                .addLast(handler);
                    }
                });
    }

    public RpcTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object sendRequest(Request request) throws Exception {
        ServiceDiscovery discovery = SpiUtils.getServiceDiscovery(aClazz);
        InetSocketAddress socketAddress = discovery.discoverService(request.getClassName());
        b.remoteAddress(socketAddress);

        ChannelFuture f = b.connect().sync();

        CompletableFuture<Response> completableFuture = new CompletableFuture<>();

        // 异步编程的关键
        map.put(request.getId(), completableFuture);
        f.channel().writeAndFlush(request).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("client send message: [{}]", request);
            } else {
                future.channel().close();
                completableFuture.completeExceptionally(future.cause());
                log.error("Send failed:", future.cause());
            }
        });
        return completableFuture.get();
    }

    public void close() {
        boss.shutdownGracefully();
    }
}
