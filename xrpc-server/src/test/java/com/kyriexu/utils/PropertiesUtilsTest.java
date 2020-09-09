package com.kyriexu.utils;

import com.kyriexu.constants.ZookeeperEnum;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

/**
 * @author KyrieXu
 * @since 2020/9/9 9:02
 **/
class PropertiesUtilsTest {

    @Test
    void getProperties() throws IOException {
        Properties properties = PropertiesUtils.getProperties(ZookeeperEnum.RPC_CONFIG_PATH.toString());
        assert properties != null;
        System.out.println(properties.getProperty(ZookeeperEnum.ZK_ADDRESS.toString()));
    }
}