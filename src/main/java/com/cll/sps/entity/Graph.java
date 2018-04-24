package com.cll.sps.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/2/7
 */
public class Graph implements Serializable {


    private static final long serialVersionUID = -5525945208775286068L;


    private List<Arc> arcs;

    private TaskManager tm;

    private int[] availableTasks;

    private int taskSize;

    public Graph(int cap) {
        this.arcs = new ArrayList<>(cap * 3 / 4 + 1);
    }

    public TaskManager getTaskManager() {

        return tm;
    }

    public void setTaskManager(TaskManager tm) {
        taskSize = tm.getTasks().size();
        availableTasks = new int[taskSize];
        for (int i = 0; i < taskSize; i++) {
            availableTasks[i] = 1;
        }
        this.tm = tm;
    }

    public void addArc(Arc arc) {
        this.arcs.add(arc);
    }

    public List<Arc> getArcs() {
        return arcs;
    }

    public void setArcs(List<Arc> arcs) {
        this.arcs = arcs;
    }

    public List<Task> getAvailableTasks() {
        List<Task> list = new ArrayList<>();
        for (Arc arc : arcs) {
            if (arc.isCancel(tm)) {
                availableTasks[arc.getPreTask()] = -1;
            } else {
                availableTasks[arc.getNextTask()] = 0;
            }
        }
        Task t;
        for (int i = 0; i < taskSize; i++) {

            if (availableTasks[i] == 1) {
                t=tm.findById(i);
                if (t.isCompleted()){
                    availableTasks[i]=-1;
                }else {
                    list.add(t);
                }
            } else if (availableTasks[i] == 0) {
                availableTasks[i] = 1;
            }
        }
        return list;
    }

    public void reset(){
        for (int i = 0; i < taskSize; i++) {
            availableTasks[i] = 1;
        }

        tm.getTasks().forEach(t->t.reset());
    }

}
