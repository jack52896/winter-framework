package com.framework.scroll.winter.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WinterRequestParam {
    String value() default "";
}
