package com.framework.scroll.winter.v1.mvc.annonation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public void handle(HttpServletRequest req, HttpServletResponse rep, Object handle) throws Exception {
        if(!this.support(handle)){
            throw new Exception("该请求不存在");
        }

    }
}
