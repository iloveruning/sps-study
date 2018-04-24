package com.cll.sps.entity;

import org.apache.commons.math3.fraction.Fraction;

import java.io.Serializable;

/**
 * @author chenliangliang
 * @date 2018/2/7
 */
public class Skill implements Serializable {


    private static final long serialVersionUID = -7249113559432203373L;

    /**
     * 技能id
     */
    public int id;

    /**
     * 熟练度
     */
    public Fraction pro;

    public Skill(int id,int level,int score) {
        this.id=id;
        this.pro=new Fraction(score,level);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPro(Fraction pro) {
        this.pro = pro;
    }

    public void setPro(int level,int score) {
        this.pro = new Fraction(score,level);
    }

    @Override
    public String toString() {
        return String.format("Skill{id=%d, pro=%s}", id, pro);
    }
}
