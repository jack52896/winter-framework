package com.framework.scroll.winter.v1.mvc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @author http://zouyujie.top
 * @date 2021/11/15 16:08
 */
@Data
@AllArgsConstructor
public class WinterModelAndView {
    private String viewName;
    private Map<String, ?> model;
    public WinterModelAndView(String viewName){
        this(viewName, null);
    }
}
