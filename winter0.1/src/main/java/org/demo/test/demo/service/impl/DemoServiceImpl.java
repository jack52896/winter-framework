package org.demo.test.demo.service.impl;

import com.framework.scroll.winter.annotation.WinterAutowired;
import com.framework.scroll.winter.annotation.WinterService;
import org.demo.test.demo.controller.DemoController;
import org.demo.test.demo.service.DemoService;

/**
 * @author http://zouyujie.top
 * @date 2021/11/16 11:18
 */
@WinterService
public class DemoServiceImpl implements DemoService {
    @WinterAutowired
    DemoController demoController;
    @Override
    public String getData(){
        return "hello ioc&di&mvc";
    }
}
