package com.anthony.cc.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    protected Object getBean(String key, Map<String, BeanEntity> map) {
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
                bean = beanEntity.getClazz().newInstance();
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
        getLock().writeLock().unlock();
    }
}
