package com.framework.scroll.winter.beans.config;

import lombok.Data;

/**
 * @author http://zouyujie.top
 * @date 2021/11/8 19:56
 */
@Data
public class WinterBeanDefinition {
    //原生类的全类名
    public String beanClassName;
    //标记是否延迟加载
    public boolean lazyInit;
    //保存在beanfatcory中的bean的key
    public String factoryBeanName;

}
