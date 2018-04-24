package com.cll.parse;

import com.cll.entity.EmployeeManeger;
import com.cll.entity.Graph;
import com.cll.entity.TaskManeger;
import com.cll.preparework.PrepareWork;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;



public class Parse {

	private Properties properties;
	private List<String> propertyNames;
	private TaskManeger taskManeger;
	private EmployeeManeger employeeManeger;
	private Graph graph;
	private PrepareWork prepareWork;

	public Parse(String file) {
		taskManeger = new TaskManeger();
		employeeManeger = new EmployeeManeger();
		graph = new Graph();
		properties = new Properties();
		propertyNames = new ArrayList();
		try {
			properties.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Enumeration<?> enumeration = properties.propertyNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			propertyNames.add(name);
		}

	}

	public void parse() {
		String value = "";
		String[] str = null;
		for (String name : propertyNames) {
			value = properties.getProperty(name);
			str = StringUtils.split(name, ".");
			switch (str[0]) {
			case "task":
				parseTask(str, value);
				break;
			case "employee":
				parseEmployee(str, value);
				break;
			case "graph":
				parseGraph(str, value);
				break;

			default:
				break;
			}

		}
		graph.setTaskManeger(taskManeger);
		prepareWork=new PrepareWork(graph,employeeManeger);
	}

	
	private void parseTask(String[] str, String value) {
	
		if (str.length == 3) {
			taskManeger.addEffToTask(Integer.parseInt(str[1]), Float.parseFloat(value));
		} else if (str.length == 4 && !str[3].equalsIgnoreCase("number")) {

			taskManeger.addSkillToTask(Integer.parseInt(str[1]), Integer.parseInt(value));
		}
	}

	private void parseEmployee(String[] str, String value) {
//		System.out.println(str[1]);
		//int id = Integer.parseInt(str[1]);
		if (str.length == 3) {
			employeeManeger.addSalaryToTask(Integer.parseInt(str[1]), Double.parseDouble(value));
		} else if (str.length == 4 && !str[3].equalsIgnoreCase("number")) {

			employeeManeger.addSkillToEmployee(Integer.parseInt(str[1]), Integer.parseInt(value));
		}
	}

	private void parseGraph(String[] str, String value) {
		if (!str[2].equals("number")) {
			int id = Integer.parseInt(str[2]);
			String[] s=StringUtils.split(value);
			int frontTask=Integer.parseInt(s[0]); 
			int backTask=Integer.parseInt(s[1]);
			graph.addArc(id, frontTask, backTask);
		}
		
	}
		

	public TaskManeger getTaskManeger() {
		return taskManeger;
	}


	public EmployeeManeger getEmployeeManeger() {
		return employeeManeger;
	}

	public Graph getGraph() {
		return graph;
	}
	
	public PrepareWork getPrepareWork(){
		return prepareWork; 
	}


}
