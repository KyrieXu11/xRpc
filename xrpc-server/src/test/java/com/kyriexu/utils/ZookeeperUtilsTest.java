package com.kyriexu.utils;

import org.apache.curator.framework.CuratorFramework;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author KyrieXu
 * @since 2020/9/9 12:53
 **/
class ZookeeperUtilsTest {

    @Test
    void getZkClient() throws IOException {
        CuratorFramework zkClient = ZookeeperUtils.getZkClient();
        // zkClient.start();
        System.out.println(zkClient.getData());
    }

    @Test
    void createPersistentNode() throws IOException {
        String path = "/my-root/fuck_service/127.0.0.1:80";
        CuratorFramework zkClient = ZookeeperUtils.getZkClient();
        ZookeeperUtils.createPersistentNode(zkClient,path);

    }
}