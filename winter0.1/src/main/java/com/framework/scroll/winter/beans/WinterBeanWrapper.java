package com.framework.scroll.winter.beans;

/**
 * @author http://zouyujie.top
 * @date 2021/11/8 20:39
 */

import lombok.Data;

/**
 * 封装创建好的Bean实例、包括代理对象和原生对象
 */
@Data
public class WinterBeanWrapper {
    private Object WrapperInstance;
    private Class<?> wrapperClass;

    public WinterBeanWrapper(Object wrapperInstance) {
        WrapperInstance = wrapperInstance;
    }
}
