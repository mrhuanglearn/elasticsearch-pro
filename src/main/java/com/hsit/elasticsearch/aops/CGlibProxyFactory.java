package com.hsit.elasticsearch.aops;



import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Objects;

public class CGlibProxyFactory implements MethodInterceptor {
    private static CGlibProxyFactory instance = new CGlibProxyFactory();

    private CGlibProxyFactory() {
    }


    public static CGlibProxyFactory getInstance() {
        return instance;
    }


    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer.create(cls, this);
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        if (StringUtils.equals(method.getName(), "setConnectTimeOutConfig") || StringUtils.equals(method.getName(), "setMultipleConnectConfig"))
            before(o,objects);
        return methodProxy.invokeSuper(o, objects);
    }

    private void before(Object o,Object[] objects) {
        if (Objects.isNull(objects) || Objects.isNull(objects[0])) {
            throw new IllegalArgumentException("params must not be null nor empty");
        }
    }
}
