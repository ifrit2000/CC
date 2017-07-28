package com.anthony.cc.annotationprocess;

import com.anthony.cc.annotation.Assemble;
import com.anthony.cc.container.BeanEntity;

import java.util.Map;

/**
 * Created by chend on 2017/7/28.
 */
public class ComponentAssembler {

    public void inject(BeanEntity beanEntity) {
        if (beanEntity.getFieldNeedToInject() == null)
            return;


    }
}
