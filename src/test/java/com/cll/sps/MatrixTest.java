package com.cll.sps;

import com.cll.sps.parse.Load;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.testng.annotations.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author chenliangliang
 * @date 2018/2/7
 *
 *  RealMatrix接口的三个主要的实现是 Array2DRowRealMatrix和 BlockRealMatrix为密集矩阵（
 * 第二个是在50以上或100更适合的尺寸）和 SparseRealMatrix为稀疏矩阵。
 */
public class MatrixTest {

    @Test
    public void test(){

        double[][] x1=new double[][]{{3.0,2.0},{1.0,4.0},{5.0,3.0}};
        double[][] x2=new double[][]{{1.0,3.0,6.0},{5.0,2.0,1.0}};

        RealMatrix m1=new Array2DRowRealMatrix(x1);
        RealMatrix m2=new Array2DRowRealMatrix(x2);


        //矩阵相乘
        RealMatrix res= m1.multiply(m2);

        //反转p，使用LU分解
        RealMatrix pInverse = new LUDecomposition(res).getSolver().getInverse();
        System.out.println(pInverse);

    }

    @Test
    public void testFraction(){
        Fraction x=new Fraction(1,2);  //  1/2
        Fraction y=new Fraction(1,3);  //  1/3

        Fraction res=x.add(y);  //加法  5/6
        System.out.println(res);
        res=x.multiply(y);      //乘法  1/6
        System.out.println(res);
        res=x.divide(y);        //除法  3/2
        res=res.divide(3);        //除法  1/2
        System.out.println(res);
        res=x.subtract(y);      //减法  1/6
        System.out.println(res);
        res=y.reciprocal();     //倒数  3
        System.out.println(res);
        res=y.abs();            //绝对值  1/3
        System.out.println(res);
        System.out.println(y.doubleValue());
        System.out.println(new Fraction(new Fraction(999,88).doubleValue()));
    }

    @Test
    public void testRandom(){

        Random r1=new Random(10);
        System.out.println(r1.nextInt());
        System.out.println(r1.nextInt());
        System.out.println(r1.nextInt());
        System.out.println("================");
        Random r2=new Random(10);
        System.out.println(r2.nextInt());
        System.out.println(r2.nextInt());
        System.out.println(r2.nextInt());
        System.out.println("================");

        SecureRandom r3=new SecureRandom("10".getBytes());

        System.out.println(r3.nextInt());

        SecureRandom r4=new SecureRandom("10".getBytes());

        System.out.println(r4.nextInt());
        System.out.println("================");

        //包装java Random
        System.out.println(RandomUtils.nextInt(0, 10));




    }

    @Test
    public void testResources(){
        Load load=new Load();

        System.out.println(load.getClass().getResource(""));
    }


    @Test
    public void testCollections(){
        List<Integer> s1= Arrays.asList(1,2,3,4,5);
        List<Integer> s2=Arrays.asList(3,4,5,6,7);

        //并集
       List res= (List) CollectionUtils.union(s1,s2);
        System.out.println(res);

        //交集
       res= (List) CollectionUtils.retainAll(s1,s2);
        System.out.println(res);

        //交集
        res= (List) CollectionUtils.subtract(s1,s2);
        System.out.println(res);

    }
}
