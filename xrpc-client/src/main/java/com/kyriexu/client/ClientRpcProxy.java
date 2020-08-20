package com.kyriexu.client;

import java.lang.reflect.Proxy;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:55
 **/
public class ClientRpcProxy {
    public Object getService(Class<?> service, String host, int port) {
        return Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, new RemoteInvocationHandler(host, port));
    }
}
