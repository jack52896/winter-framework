package com.framework.scroll.winter.annonation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WinterController {
    String value() default "";
}
