package com.anthony.cc.annotation;

import java.lang.annotation.*;

/**
 * Created by chend on 2017/7/26.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
    String value() default "";
}
