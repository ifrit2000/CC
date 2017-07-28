package com.anthony.cc.scanner;

import com.anthony.cc.annotation.Component;
import com.anthony.cc.container.BeanEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by chend on 2017/7/27.
 */
public class CandidateComponentScanner {


    public ArrayList<BeanEntity> findCandidateComponent(String basePackage) {
        basePackage = "/" + basePackage.replaceAll("\\.", "/");
        return filterComponent(findAllFullyQualifiedClassName(basePackage));
    }

    public ArrayList<BeanEntity> findCandidateComponent(String[] basePackages) {
        String basePackage;
        Set<String> nameSet = new HashSet<>();
        for (int i = 0; i != basePackages.length; ++i) {
            basePackage = "/" + basePackages[i].replaceAll("\\.", "/");
            nameSet.addAll(findAllFullyQualifiedClassName(basePackage));
        }
        return filterComponent(nameSet);
    }

    //根据名字创建对象
    private ArrayList<BeanEntity> filterComponent(Set<String> nameSet) {
        ArrayList<BeanEntity> list = new ArrayList<>();
        for (String name : nameSet) {
            try {
                Class<?> clazz = getClass().getClassLoader().loadClass(name);
                if (!clazz.isAnnotation()) {
                    Component component = clazz.getAnnotation(Component.class);
                    if (null != component) {
                        BeanEntity beanEntity = new BeanEntity();
                        beanEntity.setName(component.beanName().equals("")?clazz.getName():component.beanName());
                        beanEntity.setClazz(clazz);
                        beanEntity.setSingleton(component.singleton());
                        beanEntity.setBean(beanEntity.isSingleton() ? clazz.newInstance() : null);
                        list.add(beanEntity);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.out.println("构造器");
                e.printStackTrace();
            } catch (InstantiationException e) {
                System.out.println("构造器2");
                e.printStackTrace();
            }

        }
        return list;
    }

    //找到所有的完全限定类名
    private Set<String> findAllFullyQualifiedClassName(String path) {
        Set<String> tmp = findAllClassName(path);
        Set<String> nameSet = null;
        if (null != tmp && !tmp.isEmpty()) {
            nameSet = new HashSet<>();
            for (String name : tmp) {
                nameSet.add(name.substring(1).replace(".class", "").replace("/", "."));
            }
        }
        return nameSet;
    }


    private Set<String> findAllClassName(String path) {
        URL url = getClass().getResource(path);
        if (null == url)
            return null;
        else {
            List<String> nameList;
            Set<String> nameSet = null;
            if ("file".equals(url.getProtocol())) {
                nameList = readFromDir(url.getPath());
                if (null != nameList && !nameList.isEmpty()) {
                    nameSet = new HashSet<>();
                    for (String name : nameList) {
                        String fullyQualifiedName = path + "/" + name;
                        if (fullyQualifiedName.endsWith(".class"))
                            nameSet.add(fullyQualifiedName);
                        else {
                            Set<String> tmp = findAllClassName(fullyQualifiedName);
                            if (null != tmp)
                                nameSet.addAll(tmp);
                        }
                    }
                }
            } else if ("jar".equals(url.getProtocol())) {
                nameList = readFromJar(url.getPath());
                if (null != nameList) {
                    nameSet = new HashSet<>();
                    nameSet.addAll(nameList);
                }
            }
            return nameSet;
        }
    }

    private List<String> readFromDir(String absolutePath) {
        File file = new File(absolutePath);
        String[] names = file.list();
        if (null == names) {
            return null;
        }
        return Arrays.asList(names);
    }

    private List<String> readFromJar(final String jarPath) {
        String[] paths = jarPath.split("!");
        String jarFilePath = paths[0];
        String prefixPath = paths[1].substring(1);
        List<String> nameList = new ArrayList<>();
        try {
            JarInputStream jarIn = new JarInputStream(new FileInputStream(jarFilePath.replace("file:/", "")));
            JarEntry entry = jarIn.getNextJarEntry();
            while (null != entry) {
                String name = entry.getName();
                if (name.endsWith(".class") && name.startsWith(prefixPath)) {
                    nameList.add("/" + name);
                }
                entry = jarIn.getNextJarEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nameList.isEmpty() ? null : nameList;
    }

}
