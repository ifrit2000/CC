package com.anthony.cc.container;

import com.anthony.cc.annotation.Assemble;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chend on 2017/7/27.
 */
public class BeanEntity {
    private String name;
    private Object bean;
    private Class<?> clazz;
    private boolean singleton;
    private Map<String, Assemble> fieldNeedToInject;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Map<String, Assemble> getFieldNeedToInject() {
        return fieldNeedToInject;
    }

    public void setFieldNeedToInject(Map<String, Assemble> fieldNeedToInject) {
        this.fieldNeedToInject = fieldNeedToInject;
    }

    public static BeanEntity buildBeanEntity(String name, Class<?> clazz, boolean singleton) throws IllegalAccessException, InstantiationException {
        BeanEntity beanEntity = new BeanEntity();
        beanEntity.setName(name.equals("") ? clazz.getName() : name);
        beanEntity.setClazz(clazz);
        beanEntity.setSingleton(singleton);
        beanEntity.setBean(beanEntity.isSingleton() ? clazz.newInstance() : null);

        //查找需要注入的属性
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Assemble> assembleMap = new HashMap<>();
        for (int i = 0; i != fields.length; ++i) {
            Assemble assemble = fields[i].getAnnotation(Assemble.class);
            if (null == assemble)
                continue;

            assembleMap.put(fields[i].getName(), assemble);
        }
        beanEntity.setFieldNeedToInject(assembleMap.isEmpty() ? null : assembleMap);
        return beanEntity;
    }

    @Override
    public BeanEntity clone() {
        BeanEntity entity = new BeanEntity();
        entity.setName(this.getName());
        entity.setBean(this.getBean());
        entity.setClazz(this.getClazz());
        entity.setSingleton(this.isSingleton());
        entity.setFieldNeedToInject(this.getFieldNeedToInject());
        return entity;
    }


}
