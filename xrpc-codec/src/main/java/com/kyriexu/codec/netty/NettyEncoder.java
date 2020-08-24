package com.kyriexu.codec.netty;

import com.kyriexu.codec.Encoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author KyrieXu
 * @since 2020/8/23 21:47
 **/
public class NettyEncoder extends MessageToByteEncoder<Object> {
    public static final Logger logger= LoggerFactory.getLogger(NettyEncoder.class);

    @Setter
    private Encoder encoder;

    public NettyEncoder(Encoder encoder) {
        this.encoder = encoder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (msg != null) {
            byte[] bytes = encoder.encode(msg);
            int dataLength = bytes.length;
            // 一个int 4个字节 + 字节数组的长度 肯定大于或者等于4个字节了
            out.writeInt(dataLength);
            out.writeBytes(bytes);
        }
    }
}
