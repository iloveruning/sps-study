package com.cll.sps.entity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/2/7
 */
public class TaskManager {

    private List<Task> tasks;

    private boolean isSorted=false;

    public TaskManager(int cap){
        this.tasks=new ArrayList<>(cap*3/4);
    }

    public void addTask(Task t){
        this.tasks.add(t);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Task findById(int id) {
        if (!isSorted){
            sort();
        }
        return tasks.get(id);
    }

    private void sort(){
        isSorted=true;
        tasks.sort(Comparator.comparingInt(Task::getId));
    }
}
