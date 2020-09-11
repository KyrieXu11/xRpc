package com.kyriexu.server;

import com.kyriexu.enity.Request;
import com.kyriexu.enity.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Setter;
import lombok.SneakyThrows;

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
    @Setter
    private Object service;

    private Map<String,Object> registry;

    public ServiceHandler() {
        registry = new ConcurrentHashMap<>();
    }

    private Response invoke(Request request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object[] args = request.getArgs();
        Method method;
        Class<?>[] parameterTypes = request.getParameterTypes();
        method = service.getClass().getMethod(request.getMethodName(), parameterTypes);
        Object data = method.invoke(service, args);
        return new Response(request.getId(),method.getReturnType(), data);
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

    // @Override
    // public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    //     // 读取完成之后发送读取完成消息
    //     ctx.addListener(ChannelFutureListener.CLOSE);
    // }

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
