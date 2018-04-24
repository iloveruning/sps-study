package com.cll.sps.test;


import org.apache.commons.math3.fraction.Fraction;

/**
 * @author chenliangliang
 * @date 2018/4/7
 */
public class Test {


    public static void main(String[] args) {

        Fraction f=new Fraction(0.144);
        System.out.println(f);

        System.out.println(f.multiply(7).intValue());
    }
}
