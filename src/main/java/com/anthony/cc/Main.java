package com.anthony.cc;

import com.anthony.cc.scanner.CandidateComponentScanner;

import java.io.IOException;

/**
 * Created by chend on 2017/7/27.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        CandidateComponentScanner c = new CandidateComponentScanner();
        c.findCandidateComponent("com.anthony.cc").forEach(System.out::println);

    }
}
