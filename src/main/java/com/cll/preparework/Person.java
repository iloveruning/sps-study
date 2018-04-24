package com.cll.preparework;

public class Person {
	private double[][] matrix;
	private double fitness;

	public Person(double[][] matrix, double fitness) {
		this.matrix = matrix;
		this.fitness = fitness;
	}

	public Person() {

	}
	
	public Person getNewPerson(Person p) {
		return new Person(p.getMatrix(), p.getFitness());
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}


	public Person copy(){
		int n=matrix.length;
		int m=matrix[0].length;
		double [][] mt=new double[n][m];
		for(int i=0;i<n;i++){
			for(int j=0;j<m;j++){
				mt[i][j]=matrix[i][j];
			}
		}
		return new Person(mt,fitness);
	}

	@Override
	public String toString() {
		return "Person [fitness=" + fitness + "]";
	}

}
