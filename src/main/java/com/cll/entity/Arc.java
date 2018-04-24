package com.cll.entity;

public class Arc {

	private int id;

	private int frontTask;

	private int backTask;
	
	public Arc(){}
	public Arc(int id,int frontTask, int backTask) {
		this.id=id;
		this.frontTask = frontTask;
		this.backTask = backTask;
	}


	public int getFrontTask() {
		return frontTask;
	}
	public void setFrontTask(int frontTask) {
		this.frontTask = frontTask;
	}
	public int getBackTask() {
		return backTask;
	}
	public void setBackTask(int backTask) {
		this.backTask = backTask;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "Arc [id=" + id + ", frontTask=" + frontTask + ", backTask=" + backTask + "]";
	}
	
	
}
