package com.framework.scroll.winter.v1.webmvc.annotation;

import com.framework.scroll.winter.annotation.WinterRequestParam;
import com.framework.scroll.winter.v1.webmvc.bean.WinterModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author http://zouyujie.top
 * @date 2021/11/11 14:37
 */
public class WinterHandleAdapters {
    /**
     * 查看传入的控制器是否符合标准
     * @param handle
     * @return
     */
    public boolean support(Object handle){
        return handle instanceof WinterHandleMapping;
    }

    /**
     * 对控制器进行处理
     * @param req
     * @param rep
     * @param handle
     */
    public WinterModelAndView handle(HttpServletRequest req, HttpServletResponse rep, Object handle) throws Exception {
        //当前方法对应的形参
        Map<String, Integer> paramMapping = new HashMap<>();
        if(!this.support(handle)){
            throw new Exception("该请求控制器参数不合法");
        }
        WinterHandleMapping handleMapping = (WinterHandleMapping) handle;
        Annotation[][] parameterAnnotations = handleMapping.getMethod().getParameterAnnotations();
        for(int i = 0; i< parameterAnnotations.length;i++){
            for(int j = 0; j< parameterAnnotations[i].length; j++){
                String paraname = ((WinterRequestParam)parameterAnnotations[i][j]).value();
                if(!"".equals(paraname)){
                    //一个形参可以由多个注解
                    paramMapping.put(paraname, i);
                }
            }
        }
        Class<?>[] parameterTypes = handleMapping.getMethod().getParameterTypes();
        for(int m = 0; m < parameterTypes.length; m++){
            Class<?> parameterType = parameterTypes[m];
            if(parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class){
                paramMapping.put(parameterType.getName(), m);
            }
        }
        //当前方法对应的实参
        Object[] paramValues = new Object[parameterTypes.length];
        //控制器传入的参数
        Map<String, String[]> parameterMap = req.getParameterMap();
        for (Map.Entry<String, String[]> param : parameterMap.entrySet()) {
            if(!paramMapping.containsKey(param.getKey())){
                continue;
            }
            int index = paramMapping.get(param.getKey());
            paramValues[index] = Case(parameterTypes[index],
                    Arrays.toString(param.getValue())
                            .replaceAll("\\[|\\]","")
                            .replaceAll("\\s",""));
        }

        if(paramMapping.containsKey(HttpServletRequest.class.getName())){
            paramValues[paramMapping.get(HttpServletRequest.class.getName())] = req;
        }
        if(paramMapping.containsKey(HttpServletResponse.class.getName())){
            paramValues[paramMapping.get(HttpServletResponse.class.getName())] = rep;
        }

        Object invoke = handleMapping.getMethod().invoke(handleMapping.getController(), paramValues);
        if(invoke == null) {
            throw new RuntimeException("当前对象方法不合法");
        }
        return (WinterModelAndView) invoke;

    }
    public Object Case(Class clazz, String value){
        if(clazz == String.class){
            return value;
        }else if(clazz == int.class){
            return Integer.valueOf(value).intValue();
        }else if(clazz == Integer.class){
            return Integer.valueOf(value);
        }else{
            return null;
        }
    }
}
