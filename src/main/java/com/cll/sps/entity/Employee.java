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
public class Employee implements Serializable {

    private static final long serialVersionUID = 4505977617430814615L;


    /**
     * 员工id
     */
    private int id;

    /**
     * 掌握的技能
     */
    private List<Skill> skills;


    /**
     * 薪水
     */
    private double salary;

    /**
     * 能同时做的工作数
     */
    private int maxTask;

    private Map<String,Object> attribute;


    public Employee(int id,int cap) {
        this.id=id;
        attribute=new HashMap<>();
        this.skills=new ArrayList<>(cap*3/4+1);
    }

    public void addSkill(Skill s){
        this.skills.add(s);
    }

    public void addSkill(int id,int level,int score){
        this.skills.add(new Skill(id, level, score));
    }

    public Object getAttribute(String key) {
        return attribute.get(key);
    }

    public void setAttribute(String key,Object value) {
        this.attribute .put(key, value);
    }

    public void removeAttribute(String key){
        this.attribute.remove(key);
    }

    public int getMaxTask() {
        return maxTask;
    }

    public void setMaxTask(int maxTask) {
        this.maxTask = maxTask;
    }

    public Skill getSkillById(int id){
        for(Skill s:this.skills){
            if (s.id==id){
                return s;
            }
        }
        return null;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", skills=" + skills +
                ", salary=" + salary +
                ", maxTask=" + maxTask +
                '}';
    }
}
