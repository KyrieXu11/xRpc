package com.kyriexu.server;

import com.kyriexu.annotation.RpcService;
import com.kyriexu.codec.json.JSONDecoder;
import com.kyriexu.codec.json.JSONEncoder;
import com.kyriexu.codec.netty.NettyDecoder;
import com.kyriexu.codec.netty.NettyEncoder;
import com.kyriexu.enity.Request;
import com.kyriexu.registry.ServiceRegistry;
import com.kyriexu.singleton.SingletonFactory;
import com.kyriexu.utils.AnnotationUtils;
import com.kyriexu.utils.SpiUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author KyrieXu
 * @since 2020/8/20 18:57
 **/
@Slf4j
public class ServerRpcProxy {
    @Setter
    private int port = 8080;

    @Setter
    private String addr = "127.0.0.1";

    private NioEventLoopGroup boss;
    private NioEventLoopGroup worker;
    private ServerBootstrap server;
    private ServiceHandler handler;

    public ServerRpcProxy() {
        handler = SingletonFactory.getInstance(ServiceHandler.class);
        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(boss, worker)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .channel(NioServerSocketChannel.class)
                // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁
                // 服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new ChannelInitializer<SocketChannel>() { //7
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new NettyEncoder(new JSONEncoder()))
                                .addLast(new NettyDecoder(new JSONDecoder(), Request.class))
                                .addLast(handler);
                    }
                });
    }

    public ServerRpcProxy(int port) {
        this();
        this.port = port;
    }

    @SneakyThrows
    public void run(Class<?> clazz) {
        Map<String, Object> instance = AnnotationUtils.getInstance(clazz, RpcService.class);
        // 测试通过
        // publishService(instance.get("com.kyriexu.service.HelloServiceImpl"), port);
        ServiceRegistry serviceRegistry = SpiUtils.getServiceRegistry(clazz);
        for (String serviceName : instance.keySet()) {
            serviceRegistry.registerService(new InetSocketAddress(addr,8080),serviceName);
        }
        // TODO:注册到Zookeeper中去
        // ServiceRegistry serviceRegistry = new ZkServiceRegistry();
        // for (String serviceName : instance.keySet()) {
        //     serviceRegistry.registerService(new InetSocketAddress(port), serviceName);
        // }
    }

    private void publishService(Object service, int port) throws Exception {
        try {
            handler.setService(service);
            server.localAddress(new InetSocketAddress(port));
            ChannelFuture f = server.bind().sync();
            System.out.println(ServerRpcProxy.class.getName() + " 开始监听 " + f.channel().localAddress());
            f.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully().sync();
            worker.shutdownGracefully().sync();
        }
    }
}
