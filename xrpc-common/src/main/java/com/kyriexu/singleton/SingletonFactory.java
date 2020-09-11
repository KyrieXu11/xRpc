package com.kyriexu.singleton;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KyrieXu
 * @since 2020/9/3 13:47
 **/
public class SingletonFactory {
    private static final Map<String,Object> map = new HashMap<>();
    private SingletonFactory(){
    }

    public synchronized static  <T> T getInstance(Class<T> clazz){
        Object instance = map.get(clazz.toGenericString());
        try {
            if(instance == null){
                instance = clazz.getConstructor().newInstance();
                map.put(clazz.toGenericString(),instance);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return clazz.cast(instance);
    }
}
