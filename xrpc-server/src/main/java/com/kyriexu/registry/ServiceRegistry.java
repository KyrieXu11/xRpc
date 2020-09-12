package com.kyriexu.registry;

import com.kyriexu.spi.SPI;

import java.net.InetSocketAddress;

/**
 * @author KyrieXu
 * @since 2020/9/9 9:07
 **/
@SPI
public interface ServiceRegistry {
    void registerService(InetSocketAddress address,String serviceName);

    void close();
}
