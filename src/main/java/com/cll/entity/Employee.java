package com.cll.entity;

import java.util.ArrayList;
import java.util.List;

public class Employee {
	
	private int id;
	private double salary;
	private List<Integer> skills;
	
	public Employee(int id){
		this.id=id;
		skills=new ArrayList<Integer>();
		//dedication=new double[];

	}
	
	public void addSkill(int skill){
		skills.add(skill);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Integer> getSkills(){
		return skills;
	}
	
	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public int getSkillsNum(){
		return skills.size();
	}


	@Override
	public String toString() {
		return "Employee [id=" + id + ", salary=" + salary + ", skills=" + skills + "]";
	}
	
	

}
