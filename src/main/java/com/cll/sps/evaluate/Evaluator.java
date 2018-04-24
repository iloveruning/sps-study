package com.cll.sps.evaluate;

import com.cll.sps.entity.*;
import org.apache.commons.math3.fraction.Fraction;

import java.util.*;

/**
 * @author chenliangliang
 * @date 2018/2/8
 */
public class Evaluator {


    /**
     * 粒度
     */
    private int k;

    private Graph graph;

    private EmployeeManager employeeManager;

    private TaskManager taskManager;

    private int taskSize;

    private int employeeSize;


    /**
     * employee-task average proficiency table
     */
    private Fraction[][] proficiency;


    public Evaluator(int k, EmployeeManager employeeManager, Graph graph) {
        this.k = k;
        this.graph = graph;
        this.employeeManager = employeeManager;
        this.taskManager = graph.getTaskManager();
        this.taskSize = taskManager.getTasks().size();
        this.employeeSize = employeeManager.getEmployees().size();
        this.proficiency = calculateProficiency(employeeManager, taskManager);
    }

    /**
     * 计算 员工-任务 平均熟练度 表
     *
     * @param employeeManager
     * @param taskManager
     * @return
     */
    private Fraction[][] calculateProficiency(EmployeeManager employeeManager, TaskManager taskManager) {
        Fraction[][] res = new Fraction[employeeSize][taskSize];
        Employee e;
        List<Skill> masterSkills;
        for (int i = 0; i < employeeSize; i++) {
            e = employeeManager.findById(i);
            masterSkills = e.getSkills();
            Task t;
            List<Integer> reqSkills;
            for (int j = 0; j < taskSize; j++) {
                t = taskManager.findById(j);
                reqSkills = t.getSkills();
                Fraction f = Fraction.ZERO;
                int count = 0;
                for (Skill s : masterSkills) {
                    if (reqSkills.contains(s.id)) {
                        count++;
                        if (f.getNumerator() == 0) {
                            f = s.pro;
                        } else {
                            f = f.add(s.pro);
                        }

                    }
                }
                if (count > 1) {
                    res[i][j] = f.divide(count);
                } else {
                    res[i][j] = f;
                }
            }
        }
        return res;

    }

    public Map<String, Double> evaluate(int[][] matrix) {

        Map<String, Double> res = new HashMap<>(6);

        double cost = 0.0;
        double time = 0.0;
        double satisfaction = 0.0;

        convertToTrueMatrix(matrix);

        List<Task> availableTasks = graph.getAvailableTasks();
        while (availableTasks.size() != 0) {
            //预处理
            preHandle(availableTasks, matrix);
            // 找出完成的task以及完成时间
            //单位：天
            double minFinishTaskTime = -1;
            int finishTaskId = 0;
            double temp;
            for (Task t : availableTasks) {

                int tid = t.getId();
                int taskEmployeesTotalWorkTime = 0;
                for (int i = 0; i < employeeSize; i++) {
                    taskEmployeesTotalWorkTime += matrix[i][tid];
                }
                t.setAttribute("employeesTotalWorkTime", taskEmployeesTotalWorkTime);
                temp = t.getRestEff() * k / taskEmployeesTotalWorkTime;
                if (minFinishTaskTime == -1 || minFinishTaskTime > temp) {
                    minFinishTaskTime = temp;
                    finishTaskId = tid;
                }
            }

            //计算剩余工作量
            double restEff;
            for (Task t : availableTasks) {
                //System.out.println(t.getAttribute("employeesTotalWorkTime"));
                //System.out.println(t.getRestEff());
                int tid = t.getId();
                if (tid == finishTaskId) {
                    t.setRestEff(0.0);
                } else {
                    restEff = t.getRestEff() - minFinishTaskTime * (Integer) t.getAttribute("employeesTotalWorkTime") / k;
                    t.setRestEff(restEff);
                }
               // t.removeAttribute("employeesTotalWorkTime");
            }

            //计算time
            time += minFinishTaskTime;

            //计算cost和satisfaction
            double salary;
            int workTime;
            int m;
            Fraction f;
            for (int i = 0; i < employeeSize; i++) {
                //每天的salary
                salary = employeeManager.findById(i).getSalary() / WorkDay.STANDARD_MONTHDAY;
                workTime = 0;
                int count = 0;
                int satisfy_skill = 0;
                int satisfy_workTime = 0;
                for (Task t : availableTasks) {
                    int tid = t.getId();
                    m = matrix[i][tid];
                    f = proficiency[i][tid];
                    if (m != 0) {
                        workTime += m;
                        count++;
                        if (f.compareTo(new Fraction(1, 3)) < 0) {
                            satisfy_skill--;
                        } else if (f.compareTo(new Fraction(2 / 3)) > 0) {
                            satisfy_skill++;
                        }
                    }

                }
                double rate = (double) workTime / k;
                //System.out.println(rate);
                if (rate <= 1) {
                    //没有加班
                    satisfy_workTime++;
                    cost += salary * rate * minFinishTaskTime;
                } else if (rate <= WorkDay.MAX_WORKTIME_RATE) {
                    //加班了
                    satisfy_workTime--;
                    cost += (salary + 2 * salary * (rate - 1)) * minFinishTaskTime;
                }

                if (count==0){
                    satisfaction+=(double)(2-satisfy_workTime)/employeeSize;
                }else {
                    satisfaction += (2-(double)satisfy_skill / count - satisfy_workTime)/employeeSize;
                }

            }


            //更新TPG
            availableTasks = graph.getAvailableTasks();
        }


        //评价完后重置
        graph.reset();

        res.put("cost", cost);
        res.put("time", time);
        res.put("satisfaction", satisfaction);
        return res;

    }

    /**
     * 根据熟练度表将matrix做一下调整
     *
     * @param matrix
     */
    private void convertToTrueMatrix(int[][] matrix) {
        for (int i = 0; i < employeeSize; i++) {
            for (int j = 0; j < taskSize; j++) {
                if (proficiency[i][j].compareTo(Fraction.ZERO) == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
    }


    /**
     * 预处理：如果员工同时做的工作数>maxTask,则将该员工最不擅长的工作移除，直到满足要求
     * 如果员工一天工作时间>MAX_OVERWORK，则将该员工最不擅长的工作移除，直到满足要求
     * 如果一个task同时在做的人数>maxHead,则将最不擅长做该task的员工移除，直到满足要求
     *
     * @param availableTasks 当前可做任务
     * @param matrix 分配矩阵
     */
    private void preHandle(List<Task> availableTasks, int[][] matrix) {


        int[] employeeWorkTime = new int[employeeSize];
        int[] employeeTaskSize = new int[employeeSize];
        int[] taskEmployeeSize = new int[taskSize];
        for (Task t : availableTasks) {
            int tid = t.getId();
            for (int i = 0; i < employeeSize; i++) {
                if (matrix[i][tid] > 0) {
                    employeeWorkTime[i] += matrix[i][tid];
                    employeeTaskSize[i]++;
                    taskEmployeeSize[tid]++;
                }

            }
        }


        Employee e;
        for (int i = 0; i < employeeSize; i++) {
            e = employeeManager.findById(i);
            //员工同时做的工作数>maxTask
            if (employeeTaskSize[i] > e.getMaxTask()) {
                //将该员工有效工作时间最低的工作移除，直到满足要求
                //有效工作时间=平均熟练度*工作时间
                int removeTaskId;
                Fraction minRealWorkTime ;
                Fraction temp;
                int count = 0;
                do {
                    count++;
                    //找出该员工有效工作时间最低的task id
                    removeTaskId = -1;
                    minRealWorkTime = Fraction.ZERO;
                    for (Task t : availableTasks) {
                        int tid = t.getId();
                        if (matrix[i][tid]>0){
                            temp = proficiency[i][tid].multiply(matrix[i][tid]);
                            if (removeTaskId == -1 || minRealWorkTime.compareTo(temp) > 0) {
                                removeTaskId = tid;
                                minRealWorkTime = temp;
                            }
                        }

                    }
                    //移除该工作
                    if (removeTaskId != -1) {
                        employeeTaskSize[i]--;
                        taskEmployeeSize[removeTaskId]--;
                        employeeWorkTime[i] -= matrix[i][removeTaskId];
                        matrix[i][removeTaskId] = 0;
                        //System.out.println("员工同时做的工作数 employee "+i+" removeTaskId: "+removeTaskId);
                    }

                } while (employeeWorkTime[i] < e.getMaxTask() && count < taskSize);


            }

            //超时员工：工作时间>最大加班时间
            if (employeeWorkTime[i] > (double)WorkDay.MAX_OVERWORK * k / WorkDay.STANDARD_WORDTIME) {
                //将该员工有效工作时间最低的工作移除，直到满足要求
                //有效工作时间=平均熟练度*工作时间
                int removeTaskId;
                Fraction minRealWorkTime;
                Fraction temp;
                int count = 0;
                do {
                    count++;
                    //找出该员工有效工作时间最低的task id
                    removeTaskId=-1;
                    minRealWorkTime = Fraction.ZERO;
                    for (Task t : availableTasks) {
                        int tid = t.getId();
                        if (matrix[i][tid]>0){
                            //System.out.println("matrix["+i+"]["+tid+"]: "+matrix[i][tid]);
                            temp = proficiency[i][tid].multiply(matrix[i][tid]);
                            if (removeTaskId == -1 || minRealWorkTime.compareTo(temp) > 0) {
                                removeTaskId = tid;
                                minRealWorkTime = temp;
                            }
                        }

                    }
                    //移除该工作
                    if (removeTaskId != -1) {
                        taskEmployeeSize[removeTaskId]--;
                        employeeTaskSize[i]--;
                        employeeWorkTime[i] -= matrix[i][removeTaskId];
                        matrix[i][removeTaskId] = 0;
                        //System.out.println("employee "+i+" removeTaskId: "+removeTaskId);
                    }

                }
                while (employeeWorkTime[i] > (double)WorkDay.MAX_OVERWORK * k / WorkDay.STANDARD_WORDTIME && count < taskSize);

            }
        }

        for (Task t : availableTasks) {
            int tid = t.getId();
            //该task同时在做的人数>maxHead
            if (taskEmployeeSize[tid] > t.getMaxHead()) {
                //将有效工作时间最低的员工移除，直到满足要求
                //有效工作时间=平均熟练度*工作时间
                int removeEmployeeId;
                Fraction minRealWorkTime ;
                Fraction temp;
                int count = 0;
                do {
                    count++;
                    //找出有效工作时间最低的员工id
                    minRealWorkTime = Fraction.ZERO;
                    removeEmployeeId = -1;
                    for (int i = 0; i < employeeSize; i++) {
                        if (matrix[i][tid]!=0){
                            temp = proficiency[i][tid].multiply(matrix[i][tid]);
                            if (removeEmployeeId == -1 || minRealWorkTime.compareTo(temp) > 0) {
                                removeEmployeeId = i;
                                minRealWorkTime = temp;
                            }
                        }

                    }

                    //移除该工作
                    if (removeEmployeeId != -1) {
                        taskEmployeeSize[removeEmployeeId]--;
                        employeeTaskSize[removeEmployeeId]--;
                        employeeWorkTime[removeEmployeeId] -= matrix[removeEmployeeId][tid];
                        matrix[removeEmployeeId][tid] = 0;
                    }
                } while (taskEmployeeSize[removeEmployeeId] > t.getMaxHead() && count < employeeSize);

            }
        }


        /**
         *  “再调度”：将经过预处理后有空闲时间的员工再分配一次，分为两种情况：
         *  availableTasks中是否有紧急任务，如果有：并且有空闲时间的员工也参加了这个任务，
         *  则增加该员工的工作时间，使之时间利用率最高；
         *  如果空闲员工没有参加该紧急任务并且在满足最大人数的约束下，将空闲员工分配到紧急任务上；
         *  如果没有紧急任务：则增加空闲员工效率最高的工作的工作时间。
         */

        List<Employee> availableEmployees = new ArrayList<>();

        //1.获取空闲员工：工作时间<标准工作时间
        for (int i = 0; i < employeeSize; i++) {
            if (employeeWorkTime[i] < k) {
                availableEmployees.add(employeeManager.findById(i));
            }
        }

        for (Task t : availableTasks) {
            int tid = t.getId();
            //如果是紧急任务
            if (t.isUrgent()) {
                //有空闲时间的员工也参加了这个task，则增加该员工的工作时间
                Iterator<Employee> it = availableEmployees.iterator();
                Employee ee;
                int eid;
                while (it.hasNext()) {
                    ee = it.next();
                    eid = ee.getId();
                    if (matrix[eid][tid] > 0) {
                        matrix[eid][tid] += (k - employeeWorkTime[eid]);
                        employeeWorkTime[eid] = k;
                        //System.out.println("紧急任务 "+tid+" 分配参加了该任务的空闲员工 "+eid);
                        //分配完之后，从空闲员工列表中移除
                        it.remove();
                    }
                }

                //再检查该紧急任务的工作人数
                if (taskEmployeeSize[tid] < t.getMaxHead()) {
                    //如果空闲员工没有参加该紧急任务并且在满足最大人数的约束下，将空闲员工分配到紧急任务上
                    int addNewTaskEmployeeId;
                    Fraction maxRealWorkTime;
                    Fraction temp;
                    int count = 0;
                    do {
                        count++;
                        addNewTaskEmployeeId = -1;
                        maxRealWorkTime = Fraction.ZERO;
                        //找出没有参加该task的空闲员工中有效工作时间最高的员工id
                        it = availableEmployees.iterator();
                        while (it.hasNext()) {
                            ee = it.next();
                            eid = ee.getId();
                            if (matrix[eid][tid] == 0 && employeeTaskSize[eid] < ee.getMaxTask() && proficiency[eid][tid].compareTo(Fraction.ZERO) > 0) {
                                temp = proficiency[eid][tid].multiply(matrix[eid][tid]);
                                if (addNewTaskEmployeeId == -1 || maxRealWorkTime.compareTo(temp) < 0) {
                                    addNewTaskEmployeeId = eid;
                                    maxRealWorkTime = temp;
                                }
                            }
                        }

                        if (addNewTaskEmployeeId != -1) {
                            int diff = k - employeeWorkTime[addNewTaskEmployeeId];
                            matrix[addNewTaskEmployeeId][tid] += diff;
                            employeeTaskSize[addNewTaskEmployeeId]++;
                            employeeWorkTime[addNewTaskEmployeeId] = k;
                            taskEmployeeSize[tid]++;
                            availableEmployees.remove(employeeManager.findById(addNewTaskEmployeeId));
                            //System.out.println("紧急任务 "+tid+" 分配没有参加了该任务的有效工作时间最高的空闲员工 "+addNewTaskEmployeeId);

                        }


                    } while (taskEmployeeSize[tid] < t.getMaxHead() && count < employeeSize);


                }

            }
        }

        for (Employee e1 : availableEmployees) {
            int eid = e1.getId();
            int diff = k - employeeWorkTime[eid];
            if (diff > 0) {

                //找出该员工有效工作时间最高的task id
                //有效工作时间=平均熟练度*工作时间
                int addWorkTimeTaskId = -1;
                Fraction maxRealWorkTime = Fraction.ZERO;
                Fraction temp;
                //找出该员工有效工作时间最高的task id
                for (Task t : availableTasks) {
                    int tid = t.getId();
                    if (matrix[eid][tid] > 0) {
                        temp = proficiency[eid][tid].multiply(matrix[eid][tid]);
                        if (addWorkTimeTaskId == -1 || maxRealWorkTime.compareTo(temp) < 0) {
                            addWorkTimeTaskId = tid;
                            maxRealWorkTime = temp;
                        }
                    }

                }
                //增加做该工作的时间
                if (addWorkTimeTaskId != -1) {
                    employeeWorkTime[eid] = k;
                    matrix[eid][addWorkTimeTaskId] += diff;
                    //System.out.println("员工 "+eid+" 分配有效工作时间最高的任务 "+addWorkTimeTaskId);
                }

            }
        }

        /*for (int i = 0; i < employeeSize; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }*/
        System.out.println("================");
    }

    public int getK() {
        return k;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public int getEmployeeSize() {
        return employeeSize;
    }
}
