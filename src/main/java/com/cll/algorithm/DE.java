/*
package com.cll.algorithm;

import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

import entity.Fraction;

public class DE implements Algorithm {
	
	private int granularity;
	private Fraction[] determineds;
	private Random random;
	
	public DE(int k) {
		this.granularity = k+1;
		this.determineds = new Fraction[granularity];
		//random=new Random();
		Fraction f=null;
		for (int i = 0; i < granularity; i++) {
			f=new Fraction(i, k);
			determineds[i]=f;
		}
	}
	
	@Override
	public double[][] getMatrix(double[][] prepareMatrix){
		for (int i = 0; i < prepareMatrix.length; i++) {
			for (int j = 0; j < prepareMatrix[0].length; j++) {
				if (prepareMatrix[i][j]==-1) {
					prepareMatrix[i][j]=determineds[0].getValue();
					System.out.print(determineds[0]+"  ");
				}else {
					//int index=random.nextInt(granularity);
					int index=RandomUtils.nextInt(0, granularity);
					prepareMatrix[i][j]=determineds[index].getValue();
					System.out.print(determineds[index]+"  ");
				}
				
			}
			System.out.println();
		}
		return prepareMatrix;
	}
	

}
*/
