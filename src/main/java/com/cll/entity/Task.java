package com.cll.entity;


import java.util.ArrayList;
import java.util.List;

public class Task {

	private int id;
	private double eff;
	private double EFF;
	private List<Integer> skills;
	private double dedication;
	
	public Task(int id){
		this.id=id;
		skills=new ArrayList<>();
	}

	public double getEFF() {
		return EFF;
	}

	public void setEFF(double EFF) {
		this.EFF = EFF;
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
	
	public double getEff() {
		return eff;
	}

	public void setEff(double d) {
		this.eff = d;
	}

	public int getSkillsNum(){
		return skills.size();
	}

	
	public double getDedication() {
		return dedication;
	}

	public void setDedication(double dedication) {
		this.dedication = dedication;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", eff=" + eff + ", skills=" + skills + "]";
	}
	
	

}
