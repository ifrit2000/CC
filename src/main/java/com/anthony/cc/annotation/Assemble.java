package com.anthony.cc.annotation;

import java.lang.annotation.*;

/**
 * Created by chend on 2017/7/28.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Assemble {
    Class<?> clazz() default Object.class;

    String beanName() default "";
}
