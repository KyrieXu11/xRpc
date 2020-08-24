package com.kyriexu.codec.netty;

import com.kyriexu.codec.Decoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author KyrieXu
 * @since 2020/8/23 21:40
 **/
@Slf4j
public class NettyDecoder extends ByteToMessageDecoder {
    @Setter
    private Decoder decoder;

    @Setter
    private Class<?> clazz;

    public NettyDecoder(Decoder decoder, Class<?> clazz) {
        this.decoder = decoder;
        this.clazz = clazz;
    }

    private final static int BODY_MIN_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in == Unpooled.EMPTY_BUFFER){
            System.out.println("空buffer");
            return;
        }
        // 字节长度小于4就说明异常了
        if(in.readableBytes() < BODY_MIN_LENGTH){
            return;
        }
        in.markReaderIndex();

        int len = in.readInt();
        if (len < 0){
            log.error("字节长度为：{}",len);
            return;
        }
        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        Object res = decoder.decode(bytes, clazz);
        out.add(res);
        log.info("成功读取");
    }
}