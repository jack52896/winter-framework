package com.framework.scroll.winter.core;

/**
 * @author http://zouyujie.top
 * @date 2021/11/8 19:53
 */
public interface WinterBeanFactory {
    /**
     * 通过beanName从ioc容器中获取实例bean
     * @param beanName
     * @return
     */
    Object getBean(String beanName);
    Object getBean(Class<?> clazz);
}
