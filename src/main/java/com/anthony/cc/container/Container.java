package com.anthony.cc.container;

import java.util.ArrayList;

/**
 * Created by chend on 2017/7/26.
 */
public interface Container<K,V> {
    void fillContainer(ArrayList<BeanEntity> beanList);
}
