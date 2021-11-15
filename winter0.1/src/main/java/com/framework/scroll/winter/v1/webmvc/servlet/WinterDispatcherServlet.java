package com.framework.scroll.winter.v1.webmvc.servlet;

import com.framework.scroll.winter.annonation.WinterController;
import com.framework.scroll.winter.annonation.WinterRequestMapping;
import com.framework.scroll.winter.beans.config.WinterBeanDefinition;
import com.framework.scroll.winter.context.WinterClassPathXmlApplicationContext;
import com.framework.scroll.winter.v1.webmvc.annonation.WinterHandleAdapters;
import com.framework.scroll.winter.v1.webmvc.annonation.WinterHandleMapping;
import com.framework.scroll.winter.v1.webmvc.bean.WinterModelAndView;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author http://zouyujie.top
 * @date 2021/11/8 19:46
 */
public class WinterDispatcherServlet extends HttpServlet {
    private WinterClassPathXmlApplicationContext context;
    private List<WinterHandleMapping> winterHandleMappings = new ArrayList<>();
    private Map<WinterHandleMapping, WinterHandleAdapters> adaptersMap = new HashMap<>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
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
        for (WinterHandleMapping winterHandleMapping : winterHandleMappings) {
            if(winterHandleMapping.getPattern().equals(Pattern.compile(replace))){
                return winterHandleMapping;
            }
        }
        return null;
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
                if(clazz.isAnnotationPresent(WinterController.class)){
                    StringBuilder baseUrl = new StringBuilder(clazz.getAnnotation(WinterController.class).value());
                    if(clazz.isAnnotationPresent(WinterRequestMapping.class)){
                        baseUrl.append(clazz.getAnnotation(WinterRequestMapping.class).value());
                    }
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        if(method.isAnnotationPresent(WinterRequestMapping.class)){
                            baseUrl.append(method.getAnnotation(WinterRequestMapping.class).value());
                            Pattern pattern = Pattern.compile(String.valueOf(baseUrl));
                            WinterHandleMapping mapping = new WinterHandleMapping();
                            mapping.setMethod(method);
                            mapping.setPattern(pattern);
                            try {
                                mapping.setController(clazz.getConstructor().newInstance());
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
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
