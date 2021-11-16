package com.framework.scroll.winter.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WinterAutowired {
    String value() default "";
}
