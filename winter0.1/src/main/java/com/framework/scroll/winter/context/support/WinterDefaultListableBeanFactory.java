package com.framework.scroll.winter.context.support;

/**
 * @author http://zouyujie.top
 * @date 2021/11/8 20:44
 */

import com.framework.scroll.winter.beans.config.WinterBeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义winter顶层中的ioc缓存
 */
public class WinterDefaultListableBeanFactory extends WinterAbstractApplicationConetxt{

    protected final Map<String, WinterBeanDefinition> beanDefinitionMap = new HashMap<String, WinterBeanDefinition>();
    public Map<String, WinterBeanDefinition> getMap(){
        return beanDefinitionMap;
    }
}
