package com.framework.scroll.winter.context;

import com.framework.scroll.winter.annotation.WinterAutowired;
import com.framework.scroll.winter.annotation.WinterController;
import com.framework.scroll.winter.annotation.WinterService;
import com.framework.scroll.winter.aop.WinterJDKAopProxy;
import com.framework.scroll.winter.aop.config.WinterAopConfig;
import com.framework.scroll.winter.aop.support.WinterAdvisedSupport;
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
    public Map<String, WinterBeanDefinition> getMap(){
        return super.getMap();
    }
    @Override
    public Object getBean(String beanName) {
        /**
         * 依赖注入
         * 1.从beanDefinitionMap中寻找用户传入的beanName
         * 2.初始化beanDefinition
         * 3.构造返回的对象为WinterBeanWrapper，并且存入factoryBeanInstanceObjectCache
         * 4.为对象带有AutoWired注解的属性依赖注入属性
         */
        //demoService
        WinterBeanDefinition winterBeanDefinition = super.beanDefinitionMap.get(beanName);
        Object object = instantiateBean(winterBeanDefinition);
        if(object == null){
            return null;
        }
        //构造代理对象
        //beanName = factoryName;
        WinterBeanWrapper winterBeanWrapper = new WinterBeanWrapper(object);
        factoryBeanInstanceObjectCache.put(beanName, winterBeanWrapper);
        fieldWired(object);
        return factoryBeanInstanceObjectCache.get(beanName).getWrapperInstance();
    }

    private void fieldWired(Object object) {
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
                fieldTypeName = this.reader.charFirst(field.getType().getSimpleName());
            }
            try {
                Object wrapperInstance = this.factoryBeanInstanceObjectCache.get(fieldTypeName).getWrapperInstance();
                field.set(object, wrapperInstance);
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
        //当前类的全类名
        String className = winterBeanDefinition.getBeanClassName();
        try {
            if(this.factoryBeanObjectCache.containsKey(winterBeanDefinition.getFactoryBeanName())){
                instant = factoryBeanObjectCache.get(winterBeanDefinition.getFactoryBeanName());
            }
            Class<?> clazz = Class.forName(className);
            instant = clazz.getConstructor().newInstance();

            WinterAdvisedSupport support = doCreateSupport(winterBeanDefinition);
            support.setInstant(instant);
            support.setClazz(clazz);
            instant = new WinterJDKAopProxy(support).getProxy();

            //key=demoService value=instant
            factoryBeanObjectCache.put(winterBeanDefinition.getFactoryBeanName(), instant);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return instant;
    }
    public WinterAdvisedSupport doCreateSupport(WinterBeanDefinition beanDefinition){
        WinterAopConfig config = new WinterAopConfig();
        config.setPointCut(this.reader.getConfig().getProperty("pointCut"));
        config.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
        config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new WinterAdvisedSupport(config);
    }
    @Override
    public Object getBean(Class<?> clazz) {
        return getBean(clazz.getSimpleName());
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
//            if(super.beanDefinitionMap.containsKey(winterBeanDefinition.getFactoryBeanName())){
//                throw new Exception("重复注册");
//            }

            //key=demoService value=WinterBeanDefinition
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
