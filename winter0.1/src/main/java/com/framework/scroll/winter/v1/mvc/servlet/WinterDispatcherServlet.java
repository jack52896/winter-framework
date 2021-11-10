package com.framework.scroll.winter.v1.mvc.servlet;

import com.framework.scroll.winter.context.WinterClassPathXmlApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author http://zouyujie.top
 * @date 2021/11/8 19:46
 */
public class WinterDispatcherServlet extends HttpServlet {
    private WinterClassPathXmlApplicationContext context;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
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

    }
}
