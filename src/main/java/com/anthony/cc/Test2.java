package com.anthony.cc;

import com.anthony.cc.annotation.Component;

/**
 * Created by chend on 2017/7/28.
 */
@Component
public class Test2 extends Test1 {
    @Override
    public void print(String a) {
        System.out.println("test2: "+a);
    }
}
