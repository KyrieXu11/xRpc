package com.kyriexu.registry.registryImpl;


import com.kyriexu.registry.ServiceRegistry;
import com.kyriexu.utils.ZookeeperUtils;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @author KyrieXu
 * @since 2020/9/8 22:10
 **/
public class ZkServiceRegistry implements ServiceRegistry {

    @SneakyThrows
    @Override
    public void registerService(InetSocketAddress address, String serviceName) {
        String servicePath = ZookeeperUtils.ZK_REGISTER_ROOT_PATH + "/" + serviceName + address.getAddress() + ":" + address.getPort();
        CuratorFramework zkClient = ZookeeperUtils.getZkClient();
        ZookeeperUtils.createPersistentNode(zkClient,servicePath);
    }
}
