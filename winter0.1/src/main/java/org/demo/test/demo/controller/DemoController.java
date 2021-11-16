package org.demo.test.demo.controller;

import com.framework.scroll.winter.annotation.WinterAutowired;
import com.framework.scroll.winter.annotation.WinterController;
import com.framework.scroll.winter.annotation.WinterRequestMapping;
import com.framework.scroll.winter.annotation.WinterRequestParam;
import com.framework.scroll.winter.v1.webmvc.bean.WinterModelAndView;
import org.demo.test.demo.service.DemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author http://zouyujie.top
 * @date 2021/11/16 11:16
 */
@WinterController
public class DemoController {
    @WinterAutowired
    public DemoService demoService;
    // new DemoServiceImpl

    @WinterRequestMapping("/query.json")
    public WinterModelAndView get(HttpServletRequest request, HttpServletResponse response, @WinterRequestParam("name") String name){
        String data = demoService.getData();
        Map<String, String> map = new HashMap<>();
        map.put("data", data);
        map.put("name", name);
        return new WinterModelAndView("first", map);
    }
}
