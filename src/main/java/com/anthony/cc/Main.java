package com.anthony.cc;

import com.anthony.cc.context.ContainerContext;
import com.anthony.cc.context.Context;

import java.io.IOException;

/**
 * Created by chend on 2017/7/27.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Context context = ContainerContext.getContext();
        context.init("com.anthony");
        Test1 a = (Test1) context.getBean(Test1.class);
        a.print("3");

    }
}
