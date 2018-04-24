package com.cll.evaluate;


import com.cll.entity.EmployeeManeger;
import com.cll.entity.Graph;
import com.cll.entity.Task;
import com.cll.entity.TaskManeger;
import com.cll.preparework.Person;
import com.cll.preparework.PrepareWork;

import java.io.*;
import java.util.List;

public class Evaluate {

    private PrepareWork pWork = null;
    private EmployeeManeger employeeManeger = null;
    private TaskManeger taskManeger = null;
    private Graph graph = null;
    private int k;
    private ResultHandler resultHandler;

    public Evaluate(PrepareWork pWork, int k) {

        init(pWork, k);

    }

    private void init(PrepareWork pWork, int k) {
        this.k = k;
        this.pWork = pWork;
        this.employeeManeger = pWork.getEmployeeManeger();
        this.taskManeger = pWork.getTaskManeger();
        this.graph = pWork.getGraph();
    }

    // matrix [Employee][Task]
    public boolean getFeasible(double[][] matrix) {
        int reqsk = getReqsk(matrix);
        return (reqsk > 0) ? false : true;
    }

    private int getReqsk(double[][] matrix) {
        int reqsk = 0;
        int sizeOfEmployee = matrix.length;
        int sizeOfTask = matrix[0].length;
        List<Integer> esk_sum = null;
        List<Integer> esk_temp = null;
        List<Integer> tsk = null;
        for (int j = 0; j < sizeOfTask; j++) {
            tsk = taskManeger.findTaskById(j).getSkills();
            for (int i = 0; i < sizeOfEmployee; i++) {
                esk_temp = employeeManeger.findEmployeeById(i).getSkills();
                if (matrix[i][j] > 0) {
                    esk_sum = union(esk_sum, esk_temp);
                }
            }
            if (esk_sum == null) {
                reqsk += 1;
            } else if (!esk_sum.containsAll(tsk)) {
                reqsk += 1;
            }
            esk_sum = null;
        }
        return reqsk;
    }

    private List<Integer> union(List<Integer> l1, List<Integer> l2) {
        if (l1 == null) {
            l1 = l2;
        } else {
            l1.removeAll(l2);
            l1.addAll(l2);
        }
        return l1;
    }

    public void setResultHandler(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public Object evaluate(double[][] matrix) {
        if (resultHandler != null) {
            return evaluate(matrix, resultHandler);
        } else {
            return null;
        }
    }

    public Object evaluate(double[][] matrix, ResultHandler resultHandler) {
        double cost = 0;
        double time = 0;
        int sizeOfEmployee = matrix.length;
        int sizeOfTask = matrix[0].length;
        // Map<String, Double> map = new HashMap<String, Double>();
        int reqsk = getReqsk(matrix);
        if (reqsk > 0) {
            //不可行解
            double salary = 0;
            double eff = 0;
            for (int i = 0; i < sizeOfEmployee; i++) {
                salary = employeeManeger.findEmployeeById(i).getSalary();
                for (int j = 0; j < sizeOfTask; j++) {
                    eff = taskManeger.findTaskById(j).getEff();
                    cost += salary * eff;
                    time += eff;
                }
            }
            cost = 2 * reqsk * cost;
            time = 2 * reqsk * k * time;
            System.out.println("不可行解！！！");

        } else {
            List<Task> availableTasks = graph.getAvailableTasks();
            while (availableTasks.size() > 0) {
                // employee的dedication规范化
                double ede = 0;
                for (int i = 0; i < sizeOfEmployee; i++) {
                    for (Task task : availableTasks) {
                        ede = ede + matrix[i][task.getId()];
                    }
                    if (ede > 1) {
                        for (Task task : availableTasks) {
                            matrix[i][task.getId()] = matrix[i][task.getId()] / ede;
                        }
                    }
                    ede = 0;
                }
                // 计算每个availableTask的total dedication
                double tde = 0;
                for (Task task : availableTasks) {
                    for (int i = 0; i < sizeOfEmployee; i++) {
                        tde = tde + matrix[i][task.getId()];
                    }
                    task.setDedication(tde);
                    tde = 0;
                }
                // 找出完成的task以及完成时间
                int finishTaskId = 0;
                double t = 0;
                double temp = 0;
                for (Task task : availableTasks) {
                    temp = task.getEff() / task.getDedication();

                    // System.out.print(" t:" + temp + "--id:" + task.getId());
                    if (t > temp || t == 0) {
                        t = temp;
                        finishTaskId = task.getId();
                    }
                }

                // 计算cost和time
                double salary = 0;
                double d;
                for (int i = 0; i < sizeOfEmployee; i++) {
                    salary = employeeManeger.findEmployeeById(i).getSalary();
                    d = 0;
                    for (Task task : availableTasks) {
                        d += matrix[i][task.getId()];
                    }
                    cost += t * salary * d;
                }

                time = time + t;
                // 更新TPG
                // System.out.println(" finishTaskId:" + finishTaskId);
                //double eff = taskManeger.findTaskById(finishTaskId).getEff();

                for (Task task : availableTasks) {
                    task.setEff((task.getEff() - t * task.getDedication()));
                    task.setDedication(0);
                }

                graph.update(finishTaskId);

                availableTasks = graph.getAvailableTasks();

            }
            graph.reset();
        }
        // map.put("cost", cost);
        // map.put("time", time);
        System.out.println("cost:" + cost + "   time:" + time);
        // return map;
        return resultHandler.handle(cost, time);

    }


    public void dispatch(Person person, String file) {
        double cost = 0;
        double time = 0;
        double[][] matrix = person.getMatrix();
        int sizeOfEmployee = matrix.length;
        int sizeOfTask = matrix[0].length;
        OutputStream out = null;
        OutputStreamWriter output = null;
        BufferedWriter writer = null;

        try {
            out = new FileOutputStream(file);
            output = new OutputStreamWriter(out, "UTF-8");
            writer = new BufferedWriter(output);

            List<Task> availableTasks = graph.getAvailableTasks();
            while (availableTasks.size() > 0) {

                writer.write("当前可做Tasks:");
                writer.newLine();

                // employee的dedication规范化
                double ede = 0;
                for (int i = 0; i < sizeOfEmployee; i++) {
                    for (Task task : availableTasks) {
                        ede = ede + matrix[i][task.getId()];
                    }
                    if (ede > 1) {
                        for (Task task : availableTasks) {
                            matrix[i][task.getId()] = matrix[i][task.getId()] / ede;
                        }
                    }
                    ede = 0;
                }
                // 计算每个availableTask的total dedication
                double tde = 0;
                for (Task task : availableTasks) {
                    writer.write("Task" + task.getId() + " : ");
                    for (int i = 0; i < sizeOfEmployee; i++) {
                        if (matrix[i][task.getId()] > 0) {
                            writer.write("  employee" + i + " 参与工作量" + matrix[i][task.getId()]);
                        }

                        tde = tde + matrix[i][task.getId()];
                    }
                    task.setDedication(tde);
                    tde = 0;
                    writer.newLine();
                }
                // 找出完成的task以及完成时间
                int finishTaskId = 0;
                double t = 0;
                double temp = 0;
                for (Task task : availableTasks) {
                    temp = task.getEff() / task.getDedication();
                    // System.out.print(" t:" + temp + "--id:" + task.getId());
                    if (t > temp || t == 0) {
                        t = temp;
                        finishTaskId = task.getId();
                    }
                }

                writer.write("经过时间 t=" + t + " 后，Task" + finishTaskId + " 完成！");
                writer.newLine();

                // 计算cost和time
                double salary = 0;
                double d;
                for (int i = 0; i < sizeOfEmployee; i++) {
                    salary = employeeManeger.findEmployeeById(i).getSalary();
                    d = 0;
                    for (Task task : availableTasks) {
                        d += matrix[i][task.getId()];
                    }
                    cost += t * salary * d;
                }

                time = time + t;
                writer.write("此时 cost=" + cost + " , time=" + time);
                writer.newLine();
                writer.write("更新TPG和剩余Tasks的工作量");
                writer.newLine();

                // 更新TPG
                // System.out.println(" finishTaskId:" + finishTaskId);
                //double eff = taskManeger.findTaskById(finishTaskId).getEff();
                for (Task task : availableTasks) {
                    task.setEff((task.getEff() - t * task.getDedication()));
                    task.setDedication(0);
                }

                graph.update(finishTaskId);

                availableTasks = graph.getAvailableTasks();
                writer.write("---------迭代--------------");
                writer.newLine();
            }
            graph.reset();


        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }
            }

        }


    }

}
