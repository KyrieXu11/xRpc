package com.kyriexu.registry;

import com.kyriexu.annotation.service.SPI;

import java.net.InetSocketAddress;

/**
 * @author KyrieXu
 * @since 2020/9/10 12:40
 **/
@SPI
public interface ServiceDiscovery {
    InetSocketAddress discoverService(String serviceName);
}
