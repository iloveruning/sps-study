package com.cll.sps.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenliangliang
 * @date 2018/2/7
 */
public class Task implements Serializable {

    private static final long serialVersionUID = -7360220439160614557L;


    /**
     * 任务id
     */
    private int id;

    /**
     * 总工作量
     */
    private final double eff;

    /**
     * 剩余工作量
     */
    private double restEff;

    /**
     * 完成该任务需要的最多人数
     */
    private final int maxHead;


    /**
     * 完成该工作需要的技能
     */
    private List<Integer> skills;

    /**
     * 是否是紧急任务
     */
    private boolean urgent;

    private Map<String,Object> attribute;

    public Task(int id, int maxHead, double eff, int cap) {
        this.id = id;
        this.maxHead = maxHead;
        this.eff = eff;
        this.restEff = eff;
        this.skills = new ArrayList<>(cap * 3 / 4+1);
        attribute=new HashMap<>();
    }

    public void addSkillId(Integer id) {
        this.skills.add(id);
    }

    public int getId() {
        return id;
    }

    public Object getAttribute(String key) {
        return attribute.get(key);
    }
    public void removeAttribute(String key){
        this.attribute.remove(key);
    }

    public void setAttribute(String key,Object value) {
        this.attribute .put(key, value);
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(int urgent) {
        this.urgent = urgent == 1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getEff() {
        return eff;
    }

    public int getMaxHead() {
        return maxHead;
    }

    public List<Integer> getSkills() {
        return skills;
    }

    public void setSkills(List<Integer> skills) {
        this.skills = skills;
    }

    public double getRestEff() {
        return restEff;
    }

    public void setRestEff(double restEff) {
        this.restEff = restEff;
    }

    public boolean isCompleted() {
        return restEff == 0.0;
    }

    public void reset() {
        this.restEff = this.eff;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", eff=" + eff +
                ", maxHead=" + maxHead +
                ", skills=" + skills +
                ", urgent=" + urgent +
                '}';
    }
}
