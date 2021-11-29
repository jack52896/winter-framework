package com.framework.scroll.winter.aop.config;

import lombok.Data;

/**
 * @author http://zouyujie.top
 * @date 2021/11/23 23:26
 */
@Data
public class WinterAopConfig {
    //切面表达式
    private String pointCut;
    private String aspectBefore;
    private String aspectAfter;
    //切面类
    private String aspectClass;
    private String aspectAfterThrow;
    private String aspectAfterThrowingName;
}
