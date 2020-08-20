package com.kyriexu.server;

import com.kyriexu.enity.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KyrieXu
 * @since 2020/8/20 19:02
 **/
public class ServiceHandler implements Runnable {
    public static final Logger log = LoggerFactory.getLogger(ServiceHandler.class);

    private Map<String, Object> registry = new ConcurrentHashMap<>();

    private Socket socket;

    private Object service;

    public ServiceHandler(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
        // registry.put(service.getClass().getName(),service);
    }


    @Override
    public void run() {
        log.info("{}：开始处理", Thread.currentThread().getName());
        ObjectInputStream in;
        ObjectOutputStream out;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            // 读取参数
            Request request = (Request) in.readObject();
            Object obj = invoke(request);
            out = new ObjectOutputStream(socket.getOutputStream());
            // 返回结果
            out.writeObject(obj);

            // 关闭流
            // in.close();
            // out.close();
            // socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            log.error("类名未找到");
        } catch (NoSuchMethodException e) {
            log.error("没有这样的方法");
        } catch (IllegalAccessException e) {
            log.error("非法的访问权限");
        } catch (InvocationTargetException e) {
            log.error("目标执行错误");
        }
    }

    private Object invoke(Request request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object[] args = request.getArgs();
        Method method;
        if (args != null){
            // 获取参数的类型
            Class<?>[] parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
            System.out.println(Arrays.toString(parameterTypes));
            method = service.getClass().getMethod(request.getMethodName(), parameterTypes);
            return method.invoke(service, args);
        }else {
            System.out.println(request.toString());
            method = service.getClass().getMethod(request.getClassName());
            Object invoke = method.invoke(service);
            System.out.println(invoke);
            return invoke;
        }
    }
}
