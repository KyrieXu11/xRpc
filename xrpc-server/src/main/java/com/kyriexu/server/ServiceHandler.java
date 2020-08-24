package com.kyriexu.server;

import com.kyriexu.enity.Request;
import com.kyriexu.enity.Response;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:02
 **/
@ChannelHandler.Sharable
public class ServiceHandler extends ChannelInboundHandlerAdapter {
    public static final Logger log = LoggerFactory.getLogger(ServiceHandler.class);

    private Map<String, Object> registry = new ConcurrentHashMap<>();
    private Object service;

    public ServiceHandler(Object service) {
        super();
        this.service = service;
    }

    private Response invoke(Request request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object[] args = request.getArgs();
        Method method;
        Class<?>[] parameterTypes = null;
        if (args != null) {
            // 获取参数的类型
            parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        method = service.getClass().getMethod(request.getMethodName(), parameterTypes);
        Object data = method.invoke(service, args);
        return new Response(method.getReturnType(), data);
    }

    /**
     * 处理的主要逻辑
     *
     * @param ctx 上下文，负责读写字节流
     * @param msg 传递对象
     */
    @SneakyThrows
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg /* 传递过来的对象 */) {
        // 有了解码编码器直接强转
        Request request = ((Request) msg);

        Response res = invoke(request);
        if (!"void".equals(res.getReturnType().toString()))
            // 编码器直接发送
            ctx.writeAndFlush(res);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 读取完成之后发送读取完成消息
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 捕捉异常的方法
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
