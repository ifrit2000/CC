package com.anthony.cc.container;

/**
 * Created by chend on 2017/7/26.
 */
public interface Container<K> {
    Object getBean(K key);
}
