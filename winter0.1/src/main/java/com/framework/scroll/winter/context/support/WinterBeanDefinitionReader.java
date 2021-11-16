package com.framework.scroll.winter.context.support;

import com.framework.scroll.winter.beans.config.WinterBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author http://zouyujie.top
 * @date 2021/11/9 11:18
 */
public class WinterBeanDefinitionReader {
    private List<String> registerClasses = new ArrayList<String>();
    private final Properties properties = new Properties();
    private final String SCAN_PACKAGE = "scanPackage";
    public WinterBeanDefinitionReader(String... configuration){
        InputStream is = this.getClass().getClassLoader()
                .getResourceAsStream(configuration[0].replace("ClassPath:", ""));
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScan(properties.getProperty(SCAN_PACKAGE));
    }
    public Properties getConfig(){
        return this.properties;
    }
    public void doScan(String property) {
        URL url = this.getClass().getClassLoader().getResource(property.replaceAll("\\.", "/"));
        File classPath = new File(url.getFile());
        for(File file : classPath.listFiles()){
            if(file.isDirectory()){
                doScan(property+"."+file.getName());
            }else{
                if(!file.getName().endsWith(".class")){
                    continue;
                }
                //全类名
                String className = property+"."+file.getName().replace(".class","");
                registerClasses.add(className);
            }
        }
    }

//    public static void main(String[] args) {
//        InputStream is = WinterBeanDefinitionReader.class.getClassLoader().getResourceAsStream("application.properties");
//        URL resource = WinterBeanDefinitionReader.class.getClassLoader().getResource("application.properties");
//        System.out.println(resource);
//        System.out.println(is);
//    }
    /**
     * 1）解析所有进入List集合中的全类名
     * 2）将类转化为BeanDefinition对象
     * 3）放入result
     */
    public List<WinterBeanDefinition> loadBeanDefinition(){
        List<WinterBeanDefinition> result = new ArrayList<>();
        for (String registerClass : registerClasses) {
            try {
                Class<?> clazz = Class.forName(registerClass);
                if(clazz.isInterface()){
                    continue;
                }
                WinterBeanDefinition winterBeanDefinition = doCreateBeanDefinition(charFirst(clazz.getSimpleName()), clazz.getName());
                result.add(winterBeanDefinition);

                Class<?>[] interfaces = clazz.getInterfaces();
                for (Class<?> anInterface : interfaces) {
                    WinterBeanDefinition interfaceDefinition = doCreateBeanDefinition(charFirst(anInterface.getSimpleName()), clazz.getName());
                    result.add(interfaceDefinition);
                 }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return result;
    }
    public WinterBeanDefinition doCreateBeanDefinition(String beanClassName, String factoryBeanName){
        WinterBeanDefinition winterBeanDefinition  = new WinterBeanDefinition();
        winterBeanDefinition.setFactoryBeanName(factoryBeanName);
        winterBeanDefinition.setBeanClassName(beanClassName);
        return winterBeanDefinition;
    }
    /**
     * 首字母小写
     */
    public String charFirst(String factoryName){
        char[] xss = factoryName.toCharArray();
        xss[0] += 32;
        return String.valueOf(xss);
    }
}
