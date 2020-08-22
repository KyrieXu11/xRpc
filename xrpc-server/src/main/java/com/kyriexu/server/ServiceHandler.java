package com.kyriexu.server;

import com.kyriexu.codec.utils.ConvertUtils;
import com.kyriexu.enity.Request;
import io.netty.buffer.ByteBuf;
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

    public ServiceHandler(Object service) {
        super();
        this.service = service;
    }

    private Object service;

    private Object invoke(Request request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object[] args = request.getArgs();
        Method method;
        Class<?>[] parameterTypes = null;
        if (args != null){
            // 获取参数的类型
             parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        method = service.getClass().getMethod(request.getMethodName(), parameterTypes);
        return method.invoke(service,args);
    }
    /**
     * 处理的主要逻辑
     * @param ctx 上下文，负责读写字节流
     * @param msg 传递对象
     */
    @SneakyThrows
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg /* 传递过来的对象 */) {
        Request request = ConvertUtils.BufToByte(msg, Request.class);

        Object res = invoke(request);

        ByteBuf outBuf = ConvertUtils.ObjectToBuf(res);

        ctx.writeAndFlush(outBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 读取完成之后发送读取完成消息
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 捕捉异常的方法
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
