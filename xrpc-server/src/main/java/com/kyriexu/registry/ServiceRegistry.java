package com.kyriexu.registry;

import java.net.InetSocketAddress;

/**
 * @author KyrieXu
 * @since 2020/9/9 9:07
 **/
public interface ServiceRegistry {
    void registerService(InetSocketAddress address,String serviceName);
}
