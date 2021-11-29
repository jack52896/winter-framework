package com.framework.scroll.winter.aop.support;

import com.framework.scroll.winter.aop.config.WinterAopConfig;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author http://zouyujie.top
 * @date 2021/11/23 22:56
 */
@Data
public class WinterAdvisedSupport {
    private Object instant;
    private Class<?> clazz;
    private WinterAopConfig config;
    private transient Map<Method, List<Object>> mapCache;
    private Pattern pattern;

    public WinterAdvisedSupport(WinterAopConfig config) {
        this.config = config;
    }
}
