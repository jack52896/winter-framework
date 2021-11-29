package com.framework.scroll.winter.aop;

import com.framework.scroll.winter.aop.support.WinterAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author http://zouyujie.top
 * @date 2021/11/23 22:30
 */
public class WinterJDKAopProxy implements InvocationHandler, WinterAopProxy {
    private WinterAdvisedSupport support;
    public WinterJDKAopProxy(WinterAdvisedSupport support){
        this.support = support;
    }
    @Override
    public Object getProxy(ClassLoader loader) {
        return Proxy.newProxyInstance(loader, support.getClazz().getInterfaces(), this);
    }

    @Override
    public Object getProxy() {
       return this.getProxy(support.getClazz().getClassLoader());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        method.invoke(support, args);
        return null;
    }
}
