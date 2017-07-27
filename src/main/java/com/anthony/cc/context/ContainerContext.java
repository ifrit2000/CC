package com.anthony.cc.context;

import com.anthony.cc.scanner.CandidateComponentScanner;

import java.io.IOException;

/**
 * Created by chend on 2017/7/27.
 */
public class ContainerContext implements Context {

    private static ContainerContext context = new ContainerContext();

    public static ContainerContext getContext() {
        return context;
    }

    private ContainerContext() {

    }

    private void findAllCandidateComponent() {

    }

}
