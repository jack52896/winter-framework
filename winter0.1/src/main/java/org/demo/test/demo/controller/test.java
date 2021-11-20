package org.demo.test.demo.controller;

import com.framework.scroll.winter.context.WinterClassPathXmlApplicationContext;
import org.demo.test.demo.service.DemoService;
import org.demo.test.demo.service.impl.DemoServiceImpl;

/**
 * @author http://zouyujie.top
 * @date 2021/11/20 9:55
 */
public class test {
    public static void main(String[] args) {
        WinterClassPathXmlApplicationContext context = new WinterClassPathXmlApplicationContext("application.properties");
        DemoServiceImpl s = (DemoServiceImpl) context.getBean("demoService");
        DemoController sq = (DemoController) context.getBean("demoController");
        System.out.println(s.demoController);
        System.out.println(sq.demoService);
    }
}
