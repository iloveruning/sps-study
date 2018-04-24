package com.cll.sps.entity;




import java.io.Serializable;

/**
 * @author chenliangliang
 * @date 2018/2/7
 */
public class Arc implements Serializable {

    private int id;

    private int preTask;

    private int nextTask;

    public Arc(int id, int preTask, int nextTask) {
        this.id = id;
        this.preTask = preTask;
        this.nextTask = nextTask;
    }

    public boolean isCancel(TaskManager tm){
        return tm.findById(this.preTask).isCompleted();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPreTask() {
        return preTask;
    }

    public void setPreTask(int preTask) {
        this.preTask = preTask;
    }

    public int getNextTask() {
        return nextTask;
    }

    public void setNextTask(int nextTask) {
        this.nextTask = nextTask;
    }

    @Override
    public String toString() {
        return "Arc{" +
                "id=" + id +
                ", preTask=" + preTask +
                ", nextTask=" + nextTask +
                '}';
    }
}
