package com.framework.scroll.winter.aop.aspect;

import java.lang.reflect.Method;

/**
 * @author http://zouyujie.top
 * @date 2021/11/23 15:54
 */
public interface WinterJoinPoint {
    Method getMethod();
    Object[] getArguments();
    Object getThis();

}
