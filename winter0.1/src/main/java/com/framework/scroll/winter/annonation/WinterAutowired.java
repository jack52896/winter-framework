package com.framework.scroll.winter.annonation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WinterAutowired {
    String value() default "";
}
