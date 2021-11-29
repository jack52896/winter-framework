package com.framework.scroll.winter.aop;

/**
 * @author http://zouyujie.top
 * @date 2021/11/23 22:28
 */
public interface WinterAopProxy {
     Object getProxy(ClassLoader loader);
     Object getProxy();
}
