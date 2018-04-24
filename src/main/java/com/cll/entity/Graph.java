package com.cll.entity;

import java.util.ArrayList;
import java.util.List;

public class Graph {

	private List<Arc> arcs;
	private int taskSize;
	private int arcSize;
	private TaskManeger taskManeger;
	private int[] availableTask;
	private int[] availableArc;

	public Graph() {
		arcs = new ArrayList<>();
	}

	public TaskManeger getTaskManeger() {
		return taskManeger;
	}

	public void setTaskManeger(TaskManeger taskManeger) {
		this.taskManeger = taskManeger;
		this.taskSize = taskManeger.getSize();
		this.arcSize = arcs.size();
		availableTask = new int[taskSize];
		availableArc = new int[arcSize];
		for (int i = 0; i < taskSize; i++) {
			availableTask[i] = 1;
		}
		for (int i = 0; i < taskSize; i++) {
			availableArc[i] = 1;
		}
	}

	public void addArc(int id, int frontTask, int backTask) {
		Arc arc = new Arc(id, frontTask, backTask);
		arcs.add(arc);
	}

	public List<Task> getAvailableTasks() {
		List<Task> list = new ArrayList<Task>();

		for (Arc arc : arcs) {
			if (availableArc[arc.getId()] != -1) {
				availableTask[arc.getBackTask()] = 0;
			}
		}

		for (int i = 0; i < taskSize; i++) {
			if (availableTask[i] == 1) {
				list.add(taskManeger.findTaskById(i));
			}
		}
		return list;

	}

	public void update(int id_task_finish) {
		// taskManeger.removeTaskById(id_task_finish);
		availableTask[id_task_finish] = -1;
		for (Arc arc : arcs) {
			if (arc.getFrontTask() == id_task_finish) {
				availableArc[arc.getId()] = -1;
			}
		}
		// Iterator<Arc> iterator = arcs.iterator();
		// while (iterator.hasNext()) {
		// Arc arc = iterator.next();
		// if (arc.getFrontTask() == id_task_finish) {
		// iterator.remove();
		// }
		//
		// }

		for (int i = 0; i < taskSize; i++) {
			if (availableTask[i] != -1) {
				availableTask[i] = 1;
			}
		}
		for (int i = 0; i < arcSize; i++) {
			if (availableArc[i] != -1) {
				availableArc[i] = 1;
			}
		}
	}

	public void reset() {
		for (int i = 0; i < taskSize; i++) {
			availableTask[i] = 1;
		}
		for (int i = 0; i < taskSize; i++) {
			availableArc[i] = 1;
		}
		taskManeger.setEffs();
		//taskManeger.resetDedication();
	}

}
