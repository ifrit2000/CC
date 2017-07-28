package com.anthony.cc;

import com.anthony.cc.annotation.Assemble;
import com.anthony.cc.annotation.Component;

/**
 * Created by chend on 2017/7/28.
 */
@Component(singleton=false)
public class Test1 {
    @Assemble
    private Test2 t;

    public void print() {
        System.out.println(t.a);
    }
}
