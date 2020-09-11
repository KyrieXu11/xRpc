package com.kyriexu.registry.discoveryImpl;

import com.kyriexu.registry.ServiceDiscovery;
import com.kyriexu.utils.ZookeeperUtils;
import lombok.SneakyThrows;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author KyrieXu
 * @since 2020/9/10 12:52
 **/
public class ZkServiceDiscovery implements ServiceDiscovery {
    @SneakyThrows
    @Override
    public InetSocketAddress discoverService(String serviceName) {
        CuratorFramework zkClient = ZookeeperUtils.getZkClient();
        List<String> nodes = ZookeeperUtils.getChildrenNodes(zkClient, serviceName);
        if(nodes.size() == 0){
            throw new RuntimeException("没有找到对应的服务");
        }
        String[] addrPort = nodes.get(0).split(":");
        int port = Integer.parseInt(addrPort[1]);
        return new InetSocketAddress(addrPort[0],port);
    }
}
