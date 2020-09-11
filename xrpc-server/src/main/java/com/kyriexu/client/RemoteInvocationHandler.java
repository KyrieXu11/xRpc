package com.kyriexu.client;

import com.kyriexu.enity.Request;
import com.kyriexu.enity.Response;
import com.kyriexu.singleton.SingletonFactory;
import com.kyriexu.transport.RpcTransport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteInvocationHandler implements InvocationHandler {
    public static final Logger logger= LoggerFactory.getLogger(RemoteInvocationHandler.class);

    private String host;
    private int port;
    private Class<?> aClazz;
    private RpcTransport transport;

    public RemoteInvocationHandler(String host, int port, Class<?> aClazz) {
        this.host = host;
        this.port = port;
        this.aClazz = aClazz;
        transport = SingletonFactory.getInstance(RpcTransport.class);
        transport.setPort(port);
        transport.setHost(host);
        transport.setAClazz(aClazz);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = new Request();
        request.setMethodName(method.getName());
        request.setArgs(args);
        request.setClassName(method.getDeclaringClass().getName());
        Class<?>[] parameterTypes = null;
        if (args != null) {
            // 获取参数的类型
            parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
        }
        request.setParameterTypes(parameterTypes);
        request.setId(UUID.randomUUID().toString());
        Response response = (Response) transport.sendRequest(request);
        // 关闭连接
        transport.close();
        return response.getResult();
    }
}
