package com.cll.sps.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/2/7
 */
public class EmployeeManager {

    private List<Employee> employees;

    private boolean isSorted=false;

    public EmployeeManager(int cap) {
        employees = new ArrayList<>(cap * 3 / 4 + 1);
    }

    public void addEmployee(Employee e) {
        this.employees.add(e);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee findById(int id) {
        if (!isSorted){
            sort();
        }
        return employees.get(id);
    }

    private void sort(){
        isSorted=true;
        employees.sort(Comparator.comparingInt(Employee::getId));
    }
}
