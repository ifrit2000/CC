package com.anthony.cc.context;

/**
 * Created by chend on 2017/7/27.
 */
public interface Context {
    void init(String basePackage);
    void init(String[] basePackages);
    Object getBean(String beanName);
    Object getBean(Class<?> clazz);

}
