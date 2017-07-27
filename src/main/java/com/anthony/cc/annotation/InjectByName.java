package com.anthony.cc.annotation;

import java.lang.annotation.*;

/**
 * Created by chend on 2017/7/27.
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectByName {

}
