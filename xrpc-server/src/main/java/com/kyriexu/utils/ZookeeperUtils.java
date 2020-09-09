package com.kyriexu.utils;

import com.kyriexu.constants.ZookeeperEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KyrieXu
 * @since 2020/9/8 22:26
 **/
@Slf4j
public class ZookeeperUtils {
    public static final String ZK_REGISTER_ROOT_PATH = "/rpc-root";
    private final static String ZOOKEEPER_ADDR = "127.0.0.1:2181";
    private final static int BASE_SLEEP_TIME = 1000;
    private final static int MAX_RETRIES = 3;
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient;

    private ZookeeperUtils() {
    }

    public static CuratorFramework getZkClient(String path) throws IOException {
        Properties properties = PropertiesUtils.getProperties(path);
        // 如果传进来的path没有
        if (properties == null) {
            // 去找默认的path
            properties = PropertiesUtils.getProperties(ZookeeperEnum.RPC_CONFIG_PATH.toString());
            // 如果还没有
            if (properties == null)
                // 就直接指明默认的address
                return getZkClient();
            return getZkClient();
        }
        return getZkClient(properties);
    }

    public static CuratorFramework getZkClient() throws IOException {
        Properties properties = PropertiesUtils.getProperties(ZookeeperEnum.RPC_CONFIG_PATH.toString());
        if (properties != null) {
            return getZkClient(properties);
        }
        return setZkClient(ZOOKEEPER_ADDR);
    }

    private static CuratorFramework getZkClient(Properties properties) {
        String zk_addr = properties.getProperty(ZookeeperEnum.ZK_ADDRESS.toString());
        return setZkClient(zk_addr);
    }

    private static CuratorFramework setZkClient(String addr) {
        System.out.println(addr);
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(addr)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;
    }


    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> result;
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            throw new RuntimeException("");
        }
        return result;
    }

    private static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("");
        }
    }

    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if(zkClient.getState() != CuratorFrameworkState.STARTED)
                zkClient.start();
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                log.info("The node already exists. The node is:[{}]", path);
            } else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("The node was created successfully. The node is:[{}]", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            // throw new RpcException(e.getMessage(), e.getCause());
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void close(CuratorFramework zkClient){
        zkClient.close();
    }
}
