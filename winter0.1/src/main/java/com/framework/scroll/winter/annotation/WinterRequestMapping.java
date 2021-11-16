package com.framework.scroll.winter.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WinterRequestMapping {
    String value() default "";
}
