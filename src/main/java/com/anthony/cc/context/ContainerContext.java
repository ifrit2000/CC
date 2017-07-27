package com.anthony.cc.context;

import com.anthony.cc.container.Container;
import com.anthony.cc.container.NameKeyContainer;
import com.anthony.cc.container.TypeKeyContainer;

/**
 * Created by chend on 2017/7/27.
 */
public class ContainerContext implements Context {

    private static ContainerContext context = new ContainerContext();

    public static ContainerContext getContext() {
        return context;
    }

    private Container nameKeyContainer;
    private Container typeKeyContainer;

    private ContainerContext() {
        nameKeyContainer = new NameKeyContainer();
        typeKeyContainer = new TypeKeyContainer();
    }

    private void findAllCandidateComponent() {

    }

}
