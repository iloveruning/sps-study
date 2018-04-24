package com.cll.entity;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManeger {
	private List<Employee> employees;

	public EmployeeManeger() {
		employees = new ArrayList<Employee>();
	}

	public void addSkillToEmployee(int id, int skill) {
		Employee employee = findEmployeeById(id);
		if (employee != null) {
			employee.addSkill(skill);
		} else {
			employee = new Employee(id);
			employee.addSkill(skill);
			employees.add(employee);
		}

	}
	
	public void addSalaryToTask(int id, double salary) {
		Employee employee = findEmployeeById(id);
		if (employee != null) {
			employee.setSalary(salary);
		} else {
			employee = new Employee(id);
			employee.setSalary(salary);
			employees.add(employee);
		}

	}

	public Employee findEmployeeById(int id) {
		for (Employee employee : employees) {
			if (employee.getId() == id) {
				return employee;
			}
		}
		return null;
	}
	
	public int getSize(){
		return employees.size();
	}
	
	public Employee[] sort(){
		int l=getSize();
		Employee[] e=null;
		if (l>0) {
			e=new Employee[l];
			for (int i = 0; i < l; i++) {
				e[i]=findEmployeeById(i);
			}
		}
		return e;
	}
}
