package com.framework.scroll.winter.v1.webmvc.annotation;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author http://zouyujie.top
 * @date 2021/11/11 14:00
 */
@Data
public class WinterHandleMapping {
    private Object controller;
    private Method method;
    private String url;
}
