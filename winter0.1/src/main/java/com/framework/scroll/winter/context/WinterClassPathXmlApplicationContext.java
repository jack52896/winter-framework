package com.framework.scroll.winter.context;

import com.framework.scroll.winter.annonation.WinterAutowired;
import com.framework.scroll.winter.annonation.WinterController;
import com.framework.scroll.winter.annonation.WinterService;
import com.framework.scroll.winter.beans.WinterBeanWrapper;
import com.framework.scroll.winter.beans.config.WinterBeanDefinition;
import com.framework.scroll.winter.context.support.WinterBeanDefinitionReader;
import com.framework.scroll.winter.context.support.WinterDefaultListableBeanFactory;
import com.framework.scroll.winter.core.WinterBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author http://zouyujie.top
 * @date 2021/11/9 10:23
 */
public class WinterClassPathXmlApplicationContext extends WinterDefaultListableBeanFactory implements WinterBeanFactory {
    private String[] configurations;
    private WinterBeanDefinitionReader reader;
    //用于保证注册单例化的容器
    private Map<String, Object> factoryBeanObjectCache = new HashMap<>();
    //用于存储代理所有的代理过的对象
    private Map<String, WinterBeanWrapper> factoryBeanInstanceObjectCache = new ConcurrentHashMap<>();

    public WinterClassPathXmlApplicationContext(String... configurations){
        this.configurations = configurations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Object getBean(String beanName) {
        WinterBeanDefinition winterBeanDefinition = super.beanDefinitionMap.get(beanName);
        Object object = instantiateBean(winterBeanDefinition);
        if(object == null){
            return null;
        }
        //构造代理对象
        WinterBeanWrapper winterBeanWrapper = new WinterBeanWrapper(object);
        factoryBeanInstanceObjectCache.put(beanName, winterBeanWrapper);
        //依赖注入
        doAutoWired(object);
        return factoryBeanInstanceObjectCache.get(beanName).getWrapperInstance();
    }

    private void doAutoWired(Object object) {
        Class<?> clazz = object.getClass();
        if(!clazz.isAnnotationPresent(WinterController.class) || clazz.isAnnotationPresent(WinterService.class)){
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(!field.isAnnotationPresent(WinterAutowired.class)){
                continue;
            }
            field.setAccessible(true);
            String fieldTypeName = field.getAnnotation(WinterAutowired.class).value();
            if("".equals(fieldTypeName)){
                fieldTypeName = field.getType().getSimpleName();
            }
            try {
                field.set(object, this.factoryBeanInstanceObjectCache.get(fieldTypeName).getWrapperInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param winterBeanDefinition
     * @return 容器中className对应的实例化对象
     */
    private Object instantiateBean(WinterBeanDefinition winterBeanDefinition){
        Object instant = null;
        String className = winterBeanDefinition.getBeanClassName();
        try {
            if(this.factoryBeanObjectCache.containsKey(winterBeanDefinition.getBeanClassName())){
                instant = factoryBeanObjectCache.get(winterBeanDefinition.getBeanClassName());
            }
            Class<?> clazz = Class.forName(className);
            instant = clazz.getConstructor().newInstance();
            factoryBeanObjectCache.put(winterBeanDefinition.getFactoryBeanName(), instant);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return instant;
    }
    @Override
    public Object getBean(Class<?> clazz) {
        return null;
    }

    @Override
    protected void refresh() throws Exception {
        //定位配置文件
        reader = new WinterBeanDefinitionReader(configurations);

        List<WinterBeanDefinition> winterBeanDefinitions = reader.loadBeanDefinition();

        doRegisterBeanDefinition(winterBeanDefinitions);

    }
    /**
     * 将配置信息注入容器
     */
    protected void doRegisterBeanDefinition(List<WinterBeanDefinition> winterBeanDefinitions) throws Exception {
        for (WinterBeanDefinition winterBeanDefinition : winterBeanDefinitions) {
            if(super.beanDefinitionMap.containsKey(winterBeanDefinition.getFactoryBeanName())){
                throw new Exception("重复注册");
            }
            super.beanDefinitionMap.put(winterBeanDefinition.getFactoryBeanName(), winterBeanDefinition);
        }
    }
    public Properties getConfig(){
        return this.reader.getConfig();
    }
    /**
     * 获取所有的BeanDefinition
     */
    public List<WinterBeanDefinition> getBeanDefition(){
        return reader.loadBeanDefinition();
    }
}
