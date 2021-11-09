package com.framework.scroll.winter.annonation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WinterRequestParam {
    String value() default "";
}
