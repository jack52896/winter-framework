package com.framework.scroll.winter.v1.webmvc.servlet;

import com.framework.scroll.winter.annotation.WinterAutowired;
import com.framework.scroll.winter.annotation.WinterController;
import com.framework.scroll.winter.annotation.WinterRequestMapping;
import com.framework.scroll.winter.beans.config.WinterBeanDefinition;
import com.framework.scroll.winter.context.WinterClassPathXmlApplicationContext;
import com.framework.scroll.winter.v1.webmvc.annotation.WinterHandleAdapters;
import com.framework.scroll.winter.v1.webmvc.annotation.WinterHandleMapping;
import com.framework.scroll.winter.v1.webmvc.bean.WinterModelAndView;
import com.framework.scroll.winter.v1.webmvc.bean.WinterView;
import com.framework.scroll.winter.v1.webmvc.bean.WinterViewResolver;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author http://zouyujie.top
 * @date 2021/11/8 19:46
 */
public class WinterDispatcherServlet extends HttpServlet {
    private WinterClassPathXmlApplicationContext context;
    private List<WinterHandleMapping> winterHandleMappings = new ArrayList<>();
    private Map<WinterHandleMapping, WinterHandleAdapters> adaptersMap = new HashMap<>();
    private List<WinterViewResolver> winterViewResolvers = new ArrayList<>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDisPatcher(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDisPatcher(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        WinterHandleMapping mapping = this.getHandle(req);
        if(mapping == null){
            try {
                throw new Exception("没有找到该请求对应的控制器");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WinterHandleAdapters handleAdapters = getHandleAdapters(mapping);

        //寻找该控制器对应的适配器
        //适配器处理对应控制器的请求返回WinterModelAndView对象
        WinterModelAndView modelAndView = handleAdapters.handle(req, resp, mapping);

        if(modelAndView == null){
            return;
        }

        if(winterViewResolvers.isEmpty()){
            return;
        }

        for (WinterViewResolver winterViewResolver : winterViewResolvers) {
            WinterView winterView = winterViewResolver.resolverViewName(modelAndView.getViewName());
            if(winterView != null){
                winterView.render(modelAndView.getModel(), req, resp);
                return;
            }
        }
    }

    private WinterHandleAdapters getHandleAdapters(WinterHandleMapping mapping) {
        if(this.adaptersMap.isEmpty()){
            return null;
        }
        WinterHandleAdapters adapters = adaptersMap.get(mapping);
        if(adapters.support(mapping)){
            return adapters;
        }
        return null;
    }

    private WinterHandleMapping getHandle(HttpServletRequest req) {
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        String replace = url.replace(contextPath, "");
        System.out.println("当前的路径"+replace);
        for (WinterHandleMapping winterHandleMapping : winterHandleMappings) {
            if(winterHandleMapping.getUrl().equals(replace)){
                return winterHandleMapping;
            }
        }
        return null;
    }
    public String charFirst(String factoryName){
        char[] xss = factoryName.toCharArray();
        xss[0] += 32;
        return String.valueOf(xss);
    }
    @Override
    public void init(ServletConfig config) throws ServletException {
        context = new WinterClassPathXmlApplicationContext(config.getInitParameter("contextConfigLocation"));
        initCompoents(context);
    }

    /**
     *
     * @param context
     */
    public void initCompoents(WinterClassPathXmlApplicationContext context){
        //开始初始化mvc的9大组件
        //1、初始化handleMapping组件
        initHandleMapping(context);
        //2、初始化handleadapters组件
        initHandleAdapters(context);
        //3、初始化视图解析器
        initViewResolvers(context);
        //初始化完成mvc的核心组件
    }

    /**
     * 初始化视图解析器
     * @param context
     */
    private void initViewResolvers(WinterClassPathXmlApplicationContext context) {
        Properties config = context.getConfig();
        URL url = this.getClass().getClassLoader().getResource(config.getProperty("templateRoot"));
        File file = new File(url.getFile());
        for (File listFile : file.listFiles()) {
            winterViewResolvers.add(new WinterViewResolver(
                    this.getClass().getClassLoader()
                            .getResource(config.getProperty("templateRoot")
                                    .replaceAll("\\.", "/")).getFile()));
        }
    }

    /**
     * 初始化hanlemapping
     * @param context
     */
    private void initHandleMapping(WinterClassPathXmlApplicationContext context) {
        List<WinterBeanDefinition> beanDefitions = context.getBeanDefition();
        for (WinterBeanDefinition beanDefition : beanDefitions) {
            try {
                Class<?> clazz = Class.forName(beanDefition.getBeanClassName());
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if(field.isAnnotationPresent(WinterAutowired.class)){
                        context.getBean(charFirst(field.getType().getSimpleName()));
                    }
                }
                Object obj = context.getBean(beanDefition.getFactoryBeanName());
                if(clazz.isAnnotationPresent(WinterController.class)){
                    StringBuilder baseUrl = new StringBuilder(clazz.getAnnotation(WinterController.class).value());
                    if(clazz.isAnnotationPresent(WinterRequestMapping.class)){
                        baseUrl.append(clazz.getAnnotation(WinterRequestMapping.class).value());
                    }
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        if(method.isAnnotationPresent(WinterRequestMapping.class)){
                            baseUrl.append(method.getAnnotation(WinterRequestMapping.class).value());
                            WinterHandleMapping mapping = new WinterHandleMapping();
                            mapping.setMethod(method);
                            mapping.setUrl(baseUrl.toString());
                            mapping.setController(obj);
                            //收集好所有构造出来的handlemapping闯入handleMappingAdapters
                            winterHandleMappings.add(mapping);
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void initHandleAdapters(WinterClassPathXmlApplicationContext context) {
        for (WinterHandleMapping winterHandleMapping : this.winterHandleMappings) {
            this.adaptersMap.put(winterHandleMapping, new WinterHandleAdapters());
        }
    }
}
