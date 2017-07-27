package com.anthony.cc.container;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by chend on 2017/7/27.
 */
public abstract class AbstractContainer<K> implements Container<K> {
    private Map<K, BeanEntity> beanMap=new HashMap<>();
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public Object getBean(K key) {
        lock.readLock().lock();
        Object bean = beanMap.get(key).getBean();
        lock.readLock().unlock();
        return bean;
    }



}
