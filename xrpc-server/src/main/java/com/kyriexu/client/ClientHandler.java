package com.kyriexu.client;

import com.kyriexu.enity.Response;
import com.kyriexu.future.ResponseMap;
import com.kyriexu.singleton.SingletonFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private ResponseMap map;

    public ClientHandler() {
        map = SingletonFactory.getInstance(ResponseMap.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response resp = (Response) msg;
        // 全局单例的map，执行异步编程
        map.complete(resp);
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