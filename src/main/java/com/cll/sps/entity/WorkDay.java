package com.cll.sps.entity;

import java.io.Serializable;

/**
 * @author chenliangliang
 * @date 2018/2/7
 */
public class WorkDay implements Serializable {

    private static final long serialVersionUID = -8222766132604882372L;

    /**
     * 标准工作日工作时间
     * 单位：小时
     */
    public static final int STANDARD_WORDTIME = 8;


    /**
     * 标准每月工作天数
     */
    public static final int STANDARD_MONTHDAY = 30;


    /**
     * 标准工作日最长工作时间
     */
    public static final int MAX_OVERWORK = 15;

    /**
     * 加班工资
     * 单位：元/小时
     * 18000/30/8
     */
    public static final double SALARY_OVERWORK=75;

    /**
     * 最大工作时间率
     */
    public static final double MAX_WORKTIME_RATE=(double) MAX_OVERWORK/STANDARD_WORDTIME;

}
