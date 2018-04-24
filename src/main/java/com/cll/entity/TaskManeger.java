package com.cll.entity;

import java.util.ArrayList;
import java.util.List;

public class TaskManeger {

	private List<Task> tasks;

	public TaskManeger() {
		tasks = new ArrayList<Task>();
	}

	public void addSkillToTask(int id_task, int skill) {
		Task task = findTaskById(id_task);
		if (task != null) {
			task.addSkill(skill);
		} else {
			task = new Task(id_task);
			task.addSkill(skill);
			tasks.add(task);
		}

	}
	
	public void addEffToTask(int id_task, float eff) {
		Task task = findTaskById(id_task);
		if (task != null) {
			task.setEff(eff);
			task.setEFF(eff);
		} else {
			task = new Task(id_task);
			task.setEff(eff);
			task.setEFF(eff);
			tasks.add(task);
		}

	}

	public Task findTaskById(int id) {
		for (Task task : tasks) {
			if (task.getId() == id) {
				return task;
			}
		}
		return null;
	}
	
	public int getSize(){
		return tasks.size();
	}
	public double[] getEffs(){
		double[] effs=new double[getSize()];
		for (Task task : tasks) {
			effs[task.getId()]=task.getEff();
		}
		return effs;
	}
	public boolean removeTaskById(int id){
		return removeTask(findTaskById(id));
	}
	
	public boolean removeTask(Task task){
		return tasks.remove(task);
	}

	public void setEffs() {
		/*for (int i = 0; i < effs.length; i++) {
			findTaskById(i).setEff(effs[i]);
		}*/

		for (Task task : tasks) {
			task.setEff(task.getEFF());
		}
	}

	public void resetDedication() {
		for (Task task : tasks) {
			task.setDedication(0);
		}
	}

	
	public Task[] sort(){
		int l=getSize();
		Task[] t=null;
		if (l>0) {
			t=new Task[l];
			for (int i = 0; i < l; i++) {
				t[i]=findTaskById(i);
			}
		}
		return t;
	}

}
