package com.anthony.cc.context;

import com.anthony.cc.annotationprocess.CandidateComponentScanner;
import com.anthony.cc.annotationprocess.ComponentAssembler;
import com.anthony.cc.container.BeanEntity;
import com.anthony.cc.container.SimpleContainer;

import java.util.ArrayList;

/**
 * Created by chend on 2017/7/27.
 * Context context = ContainerContext.getContext();
 * context.init("com.anthony");
 */
public class ContainerContext implements Context {

    private static ContainerContext context = new ContainerContext();

    public static ContainerContext getContext() {
        return context;
    }

    private SimpleContainer container;
    private ComponentAssembler assembler;

    private CandidateComponentScanner scanner;

    private ContainerContext() {
        container = new SimpleContainer();
        scanner = new CandidateComponentScanner();
        assembler = new ComponentAssembler();
    }

    @Override
    public void init(String basePackage) {
        init(scanner.findCandidateComponent(basePackage));
    }

    @Override
    public void init(String[] basePackages) {
        init(scanner.findCandidateComponent(basePackages));
    }

    @Override
    public Object getBean(String beanName) {
        return container.getBean(beanName);
    }

    @Override
    public Object getBean(Class<?> clazz) {
        return container.getBean(clazz);
    }

    private void init(ArrayList<BeanEntity> list) {
        container.fillContainer(list);
    }

}
