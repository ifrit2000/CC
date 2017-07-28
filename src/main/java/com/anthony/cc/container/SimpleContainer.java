package com.anthony.cc.container;

import com.anthony.cc.annotation.Assemble;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by chend on 2017/7/27.
 */
public class SimpleContainer implements Container<String, BeanEntity> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected Map<String, BeanEntity> beanNameMap = new HashMap<>();
    protected Map<String, BeanEntity> beanTypeMap = new HashMap<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    protected ReentrantReadWriteLock getLock() {
        return lock;
    }

    public Object getBean(Class<?> clazz) {
        String key = clazz.getName();
        Object bean = getBean(key, beanTypeMap);
        if (null == bean) {
            String subKey = null;
            for (String typeName : beanTypeMap.keySet()) {
                if (!typeName.equals(key))
                    try {
                        Class<?> subClazz = getClass().getClassLoader().loadClass(typeName);
                        if (clazz.isAssignableFrom(subClazz) && null == subKey)
                            subKey = subClazz.getName();
                        else if (null != subKey) {
                            logger.error("按类型注入,找到多个可注入的bean");
                            return null;
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
            }
            bean = getBean(subKey, beanTypeMap);
        }
        return bean;
    }

    public Object getBean(String beanName) {
        return getBean(beanName, beanNameMap);
    }

    private Object getBean(String key, Map<String, BeanEntity> map) {
        getLock().readLock().lock();
        BeanEntity beanEntity = map.get(key);
        if (beanEntity == null)
            return null;
        Object bean;
        if (beanEntity.isSingleton()) {
            bean = beanEntity.getBean();
            getLock().readLock().unlock();
        } else {
            try {
                //非单例的bean在构造时就要注入
                bean = beanEntity.getClazz().newInstance();
                BeanEntity newBeanEntity = beanEntity.clone();
                newBeanEntity.setBean(bean);
                newBeanEntity.setSingleton(true);
                inject(newBeanEntity);
                bean = newBeanEntity.getBean();
            } catch (InstantiationException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                System.out.println(1);
                e.printStackTrace();
                return null;
            } finally {
                getLock().readLock().unlock();
            }
        }
        return bean;
    }


    @Override
    public void fillContainer(ArrayList<BeanEntity> beanList) {
        getLock().writeLock().lock();
        beanList.forEach((v) -> {
            if (beanNameMap.containsKey(v.getName()))
                logger.warn("two beans have same name: " + v.getName());
            beanNameMap.put(v.getName(), v);
            beanTypeMap.put(v.getClazz().getName(), v);
        });
        for (String name : beanTypeMap.keySet())
            inject(beanTypeMap.get(name));
        getLock().writeLock().unlock();
    }

    private void inject(BeanEntity beanEntity) {
        if (beanEntity.getBean() == null)
            return;
        Map<String, Assemble> assembleMap = beanEntity.getFieldNeedToInject();
        if (assembleMap == null)
            return;
        for (String fieldName : assembleMap.keySet()) {
            Assemble assemble = assembleMap.get(fieldName);
            Object bean = null;
            //按照类型注入
            if (!assemble.type().equals(Object.class))
                bean = getBean(assemble.type());
            else {
                try {
                    bean = getBean(beanEntity.getBean().getClass().getDeclaredField(fieldName).getType());
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            if (null == bean && !assemble.beanName().equals("")) {
                bean = getBean(assemble.beanName());
            }

            if (bean != null) {
                //注入
                try {
                    Field field = beanEntity.getBean().getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    field.set(beanEntity.getBean(), bean);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.out.println(1);
                    e.printStackTrace();
                }
            }

        }
    }
}
