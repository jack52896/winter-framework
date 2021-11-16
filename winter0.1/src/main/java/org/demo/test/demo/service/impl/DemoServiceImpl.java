package org.demo.test.demo.service.impl;

import com.framework.scroll.winter.annonation.WinterService;
import org.demo.test.demo.service.DemoService;

/**
 * @author http://zouyujie.top
 * @date 2021/11/16 11:18
 */
@WinterService
public class DemoServiceImpl implements DemoService {
    @Override
    public String getData(){
        return "hello ioc&di&mvc";
    }
}
