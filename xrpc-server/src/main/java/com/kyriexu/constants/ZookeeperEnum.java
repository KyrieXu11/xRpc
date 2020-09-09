package com.kyriexu.constants;

/**
 * @author KyrieXu
 * @since 2020/9/8 22:33
 **/
public enum ZookeeperEnum {

    RPC_CONFIG_PATH("rpc.properties"),
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;


    ZookeeperEnum(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        return propertyValue;
    }
}
