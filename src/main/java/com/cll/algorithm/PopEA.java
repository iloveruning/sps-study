package com.cll.algorithm;

import com.cll.entity.Fraction;
import com.cll.evaluate.Evaluate;
import com.cll.preparework.Person;
import com.cll.preparework.PrepareWork;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

;


public class PopEA {
	private int size;
	private List<Person> population;
	private Evaluate eva;
	private int granularity;
	private Fraction[] determineds;
	private double[][] prepareMatrix;
	private PrepareWork pWork = null;
	private double Pm;
	private double Pc;
	private int n;
	private int m;
	private int[] row;
	private int[] col;
	private MyComparator comparator;

	public PopEA(PrepareWork pWork, int size, int k, double Pc) {

		this.pWork = pWork;
		this.size = size;
		this.granularity = k+1;
		this.eva = new Evaluate(pWork, k);
		population = new ArrayList<Person>();
		this.determineds = new Fraction[granularity];
		this.prepareMatrix = pWork.getPrepareMatrix();
		n = prepareMatrix.length;
		m = prepareMatrix[0].length;
		//Pm=100/(double)(m*n);
		Pm = 0.5;
		this.Pc = Pc;
		row = new int[n];
		col = new int[m];
		for (int i = 0; i < n; i++) {
			row[i] = i;
		}
		for (int i = 0; i < m; i++) {
			col[i] = i;
		}
		Fraction f = null;
		for (int i = 0; i < granularity; i++) {
			f = new Fraction(i, k);
			determineds[i] = f;
		}

		eva.setResultHandler((cost,time)-> 0.000001*cost+0.1*time);

		comparator=new MyComparator();
	}

	public List<Person> initPop() {
		double[][] matrix;
		double fitness;
		Person p = null;
		for (int i = 0; i < size; i++) {
			matrix = initMatrix();
			fitness = (double) eva.evaluate(matrix);
			p = new Person(matrix, fitness);
			population.add(p);
		}
		return population;
	}

	private double[][] initMatrix() {
		double[][] matrix=new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (prepareMatrix[i][j] == -1) {
					matrix[i][j] = determineds[0].getValue();
					// System.out.print(determineds[0] + " ");
				} else {
					// int index=random.nextInt(granularity);
					int index = RandomUtils.nextInt(0, granularity);
					matrix[i][j] = determineds[index].getValue();
					// System.out.print(determineds[index] + " ");
				}

			}
			// System.out.println();
		}
		return matrix;
	}

	public List<Person> binTournament(List<Person> pop, int times) {
		int s;
		//@SuppressWarnings("unchecked")
		//List<Person> temp = (List<Person>) ((ArrayList<Person>)pop).clone();
		List<Person> temp =new ArrayList<>(pop.size());
		pop.forEach(p->{
			temp.add(p.copy());
		});
		List<Person> res = new ArrayList<>();
		Person one = null;
		Person two = null;
		for (int i = 0; i < times; i++) {
			s = temp.size();
			System.out.println("s--->"+s);
			Collections.shuffle(temp);
			if (s % 2 == 0) {
				for (int j = 0; j < s; j += 2) {
					one = temp.get(j);
					two = temp.get(j + 1);
					if (one.getFitness() < two.getFitness()) {
						res.add(one);
					} else {
						res.add(two);
					}
				}

			} else {
				for (int j = 0; j < s - 1; j += 2) {
					one = temp.get(j);
					two = temp.get(j + 1);
					if (one.getFitness() < two.getFitness()) {
						res.add(one);
					} else {
						res.add(two);
					}
				}
				res.add(temp.get(s - 1));
			}
			temp.clear();
			temp .addAll(res);
			System.out.println("temp--size->"+temp.size());
			res.clear();
		}
		System.out.println("temp--size->"+temp.size());
		return temp;
	}

	public List<Person> crossover(List<Person> parents) {
		if (Math.random() > Pc) {
			return parents;
		} else {
			System.out.println("-----crossover---");
			List<Person> child = new ArrayList<Person>();
			double[][] tempMatrix;
			double[][] matrix;
			double fitness;
			for (Person parent : parents) {
				tempMatrix = parent.getMatrix();
				matrix = new double[n][m];
				if (Math.random() < 0.5) { // exchange rows
					System.out.println("exchange rows");
					ArrayUtils.shuffle(row);
					for (int i = 0; i < n; i++) {

						matrix[i] = tempMatrix[row[i]];
					}
//					int row1=RandomUtils.nextInt(0, granularity);
//					int row2=RandomUtils.nextInt(0, granularity);
//					matrix[row1]=matrix[row2];
					
				} else { // exchange cols
					System.out.println("exchange cols");
					ArrayUtils.shuffle(col);
					for (int j = 0; j < m; j++) {
						for (int i = 0; i < n; i++) {
							matrix[i][j] = tempMatrix[i][col[j]];
						}
						
					}
				}
				fitness=(double) eva.evaluate(matrix);
				child.add(new Person(matrix, fitness));
				System.out.println("+++++++++++++++++");
			}
			for (Person ds : child) {
				System.out.println("child: "+ds.getFitness());
			}
			return child;
		}

	}
	
	public void mutation(List<Person> child){
		System.out.println("-------mutation------");
		double[][] matrix;
		double fitness;
		for (Person p : child) {
			matrix=p.getMatrix();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					if (Math.random() < Pm) {
						int index = RandomUtils.nextInt(0, granularity);
						matrix[i][j] = determineds[index].getValue();
					}
				}
			}
			fitness=(double) eva.evaluate(matrix);
			p.setMatrix(matrix);
			p.setFitness(fitness);
		}
	}
	
	
	public List<Person> select(List<Person> pop,List<Person> child){
		System.out.println("pop--ǰ-="+pop.size());
		System.out.println("child---="+child.size());
		//pop=ListUtils.union(pop, child);
		pop.addAll(child);
		System.out.println("pop--��-="+pop.size());
		int d=pop.size()-size;
		System.out.println("d---="+d);
		Collections.sort(pop, comparator);
		for (Person person : pop) {
			System.out.println(person.getFitness());
		}
		System.out.println("--------select--��------------");
		for (int i = 0; i < d; i++) {
			pop.remove(pop.size()-1);
		}
		for (Person person : pop) {
			System.out.println(person.getFitness());
		}
		return pop;
	}
	
	class MyComparator implements Comparator<Person>{

		@Override
		public int compare(Person p1, Person p2) {
			double f1=p1.getFitness();
			double f2=p2.getFitness();
			if (f1>f2) {
				return 1;
			}else if (f1<f2) {
				return -1;
			}else {
				return 0;
			}
		}
		
	}


	public void dispatch(Person person, String file){
		eva.dispatch(person,file);
	}

//	public List<Person> getPopulation() {
//		return population;
//	}
//	public Person getBestPerson() {
//		return population.get(0);
//	}

}
