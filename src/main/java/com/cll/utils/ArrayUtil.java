package com.cll.utils;

import java.util.Random;

public class ArrayUtil {
	
	private static Random random;
	
	
	public static void shuffle(int[] arr){
		int size=arr.length;
		if (random==null) {
			random=new Random();
		}
		for (int i=size; i>1; i--) {
			arr[i-1]=arr[random.nextInt(i)];
		}
	}
	
	public static void shuffleRow(double[][] arr){
		int size=arr.length;
		if (random==null) {
			random=new Random();
		}
		for (int i=size; i>1; i--) {
			arr[i-1]=arr[random.nextInt(i)];
		}
	}
	
	public static void shuffleCol(double[][] arr){
		int size=arr[0].length;
		if (random==null) {
			random=new Random();
		}
		for (int i=size; i>1; i--) {
			arr[i-1]=arr[random.nextInt(i)];
		}
	}

}
