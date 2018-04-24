package com.cll.algorithm;

import com.cll.entity.Fraction;
import com.cll.preparework.PrepareWork;
import org.apache.commons.lang3.RandomUtils;



public class SimpleEA {

	private int granularity;
	private Fraction[] determineds;
	private double[][] prepareMatrix;
	// private Random random;
	private PrepareWork pWork = null;
	private double Pm;
	private int n;
	private int m;

	public SimpleEA(PrepareWork pWork, int k) {
		this.granularity = k + 1;
		this.pWork = pWork;
		this.determineds = new Fraction[granularity];
		this.prepareMatrix = pWork.getPrepareMatrix();
		n = prepareMatrix.length;
		m = prepareMatrix[0].length;
		// Pm=100/(double)(m*n);
		Pm = 0.5;
		// System.out.println(Pm);
		// System.out.println(Math.random());
		// random=new Random();
		Fraction f = null;
		for (int i = 0; i < granularity; i++) {
			f = new Fraction(i, k);
			determineds[i] = f;
		}
	}

	public double[][] initMatrix() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (prepareMatrix[i][j] == -1) {
					prepareMatrix[i][j] = determineds[0].getValue();
					System.out.print(determineds[0] + "  ");
				} else {
					// int index=random.nextInt(granularity);
					int index = RandomUtils.nextInt(0, granularity);
					prepareMatrix[i][j] = determineds[index].getValue();
					System.out.print(determineds[index] + "  ");
				}

			}
			System.out.println();
		}
		return prepareMatrix;
	}

	public double[][] mutate(double[][] matrix) {
		double random = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				random = Math.random();
				if (random < Pm) {
					int index = RandomUtils.nextInt(0, granularity);
					matrix[i][j] = determineds[index].getValue();
				}
			}
		}
		// int index = RandomUtils.nextInt(0, granularity);
		// int row=RandomUtils.nextInt(0, n);
		// int col=RandomUtils.nextInt(0, m);
		// matrix[row][col] = determineds[index].getValue();
		return matrix;
	}

	public double[][] mutate2(double[][] matrix) {
		// double random = Math.random();
		// if (random < 0.5) {
		// int row = RandomUtils.nextInt(0, n);
		//
		// for (int j = 0; j < m; j++) {
		// int index = RandomUtils.nextInt(0, granularity);
		// matrix[row][j] = determineds[index].getValue();
		// }
		// } else {
		// int col = RandomUtils.nextInt(0, m);
		// for (int i = 0; i < n; i++) {
		// int index = RandomUtils.nextInt(0, granularity);
		// matrix[i][col] = determineds[index].getValue();
		// }
		// }
		int index = RandomUtils.nextInt(0, granularity);
		int row = RandomUtils.nextInt(0, n);
		int col = RandomUtils.nextInt(0, m);
		matrix[row][col] = determineds[index].getValue();

		return matrix;
	}

//	public static void main(String[] args) {
//		Parse p = new Parse("input3");
//		p.parse();
//		PrepareWork pWork = p.getPrepareWork();
//		SimpleEA simpleEA = new SimpleEA(pWork, 10);
//		Evaluate evaluate = new Evaluate(pWork, 10);
//		evaluate.setResultHandler(new ResultHandler() {
//
//			@Override
//			public Object handle(double cost, double time) {
//
//				return time;
//			}
//
//		});
//		double[][] matrix = simpleEA.initMatrix();
//		double[][] mutate;
//		double time = (double) evaluate.evaluate(matrix);
//		;
//
//		// double time=map.get("time");
//		double t = 0;
//
//		for (int i = 0; i < 1000; i++) {
//			if (i < 1000) {
//
//				mutate = simpleEA.mutate(matrix);
//			} else {
//				mutate = simpleEA.mutate2(matrix);
//			}
//
//			t = (double) evaluate.evaluate(mutate);
//			// t=map.get("time");
//			if (t < time) {
//				time = t;
//				matrix = mutate;
//
//			}
//
//		}
//
//		System.out.println(time);
//
//	}

}
