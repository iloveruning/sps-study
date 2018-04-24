package com.cll.preparework;

import com.cll.entity.*;

import java.util.Collections;

public class PrepareWork {
	
	private Graph graph=null;
	private EmployeeManeger employeeManeger=null;
	private TaskManeger taskManeger=null;
	
	

	



	public PrepareWork(Graph graph, EmployeeManeger employeeManeger) {
		this.graph = graph;
		this.employeeManeger = employeeManeger;
		this.taskManeger=graph.getTaskManeger();
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public void setEmployeeManeger(EmployeeManeger employeeManeger) {
		this.employeeManeger = employeeManeger;
	}
	
	
	
	public TaskManeger getTaskManeger() {
		return taskManeger;
	}

	public void setTaskManeger(TaskManeger taskManeger) {
		this.taskManeger = taskManeger;
	}

	public Graph getGraph() {
		return graph;
	}

	public EmployeeManeger getEmployeeManeger() {
		return employeeManeger;
	}

	public double[][] getPrepareMatrix(){
		int e=employeeManeger.getSize();
		int t=taskManeger.getSize();
		double[][] prepareMatrix=new double[e][t];
		Employee employee=null;
		Task task=null;
		for (int i = 0; i < e; i++) {
			for (int j = 0; j < t; j++) {
				employee=employeeManeger.findEmployeeById(i);
				task=taskManeger.findTaskById(j);
				if (!canDo(employee, task)) {
					prepareMatrix[i][j]=-1;
				}else {
					prepareMatrix[i][j]=0;
				}
			}
		}
		return prepareMatrix;
	}
	
	public static boolean canDo(Employee e,Task t){
		return !Collections.disjoint(e.getSkills(), t.getSkills());
	}

}
