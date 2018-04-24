package com.cll.sps.parse;

import com.cll.sps.entity.*;
import com.cll.sps.evaluate.Evaluator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * @author chenliangliang
 * @date 2018/2/7
 */
public class Load {

    private Properties prop;

    private EmployeeManager employeeManager;

    private TaskManager taskManager;

    private Graph graph;

    private int skillLevel;

    public Load() {
        prop = new Properties();
    }

    public void parse(String conf) {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(conf)) {
            prop.load(in);
            this.skillLevel = Integer.parseInt(prop.getProperty("skill.level"));
            this.employeeManager = parseEmployee(prop);
            this.taskManager = parseTask(prop);
            this.graph = parseGraph(prop);

        } catch (IOException e) {
            System.out.println("加载配置文件失败");
            e.printStackTrace();
        }
    }

    private Graph parseGraph(Properties prop) {
        int cap = Integer.parseInt(prop.getProperty("graph.arc.number"));
        Graph g = new Graph(cap);
        Arc arc;
        for (int i = 0; i < cap; i++) {
            String str = prop.getProperty("graph.arc." + i);
            String[] strs = StringUtils.split(str, " ");
            arc = new Arc(i, Integer.parseInt(strs[0]), Integer.parseInt(strs[1]));
            g.addArc(arc);

        }
        return g;
    }

    private TaskManager parseTask(Properties prop) {
        int cap = Integer.parseInt(prop.getProperty("task.number"));
        TaskManager tk = new TaskManager(cap);
        String prefix;
        Task t;
        for (int i = 0; i < cap; i++) {
            prefix = "task." + i + ".";
            int scap = Integer.parseInt(prop.getProperty(prefix + "skill.number"));
            t = new Task(i, Integer.parseInt(prop.getProperty(prefix + "maxHead")), Double.parseDouble(prop.getProperty(prefix + "cost")), scap);
            t.setUrgent(Integer.parseInt(prop.getProperty(prefix+"urgent")));
            for (int j = 0; j < scap; j++) {
                t.addSkillId(Integer.valueOf(prop.getProperty(prefix + "skill." + j)));
            }
            tk.addTask(t);

        }
        return tk;
    }

    private EmployeeManager parseEmployee(Properties prop) {
        int cap = Integer.parseInt(prop.getProperty("employee.number"));
        EmployeeManager em = new EmployeeManager(cap);
        String prefix;
        Employee e;
        for (int i = 0; i < cap; i++) {
            prefix = "employee." + i + ".";
            int scap = Integer.parseInt(prop.getProperty(prefix + "skill.number"));
            e = new com.cll.sps.entity.Employee(i, scap);
            e.setSalary(Double.parseDouble(prop.getProperty(prefix + "salary")));
            e.setMaxTask(Integer.parseInt(prop.getProperty(prefix+"maxTask")));
            Skill s;
            for (int j = 0; j < scap; j++) {
                String str = prop.getProperty(prefix + "skill." + j);
                String[] strs = StringUtils.split(str, ",");
                s = new Skill(Integer.parseInt(strs[0]), skillLevel, Integer.parseInt(strs[1]));
                e.addSkill(s);
            }
            em.addEmployee(e);
        }

        return em;
    }

    public EmployeeManager getEmployeeManager() {
        return employeeManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public Graph getGraph() {
        return graph;
    }

    public static void main(String... args) {
        Load load = new Load();
        load.parse("T20-E15-S5.conf");
        //System.out.println((double) WorkDay.MAX_OVERWORK * 10 / WorkDay.STANDARD_WORDTIME);
        TaskManager tm = load.getTaskManager();
        Graph graph = load.getGraph();
        EmployeeManager em=load.getEmployeeManager();

        //em.getEmployees().forEach(System.out::println);
        //tm.getTasks().forEach(System.out::println);
        //graph.getArcs().forEach(System.out::println);

        System.out.println("================");
        graph.setTaskManager(tm);

        Evaluator evaluate=new Evaluator(10,em,graph);

        int employeeSize=em.getEmployees().size();
        System.out.println("employeeSize: "+employeeSize);
        int taskSize=tm.getTasks().size();
        System.out.println("taskSize: "+taskSize);

        int[][] matrix=new int[employeeSize][taskSize];

        Random random=new Random();
        for (int i = 0; i < employeeSize; i++) {
            for (int j = 0; j < taskSize; j++) {
               matrix[i][j]=random.nextInt(10);
                //matrix[i][j]=8;
            }
        }
        for (int i = 0; i < employeeSize; i++) {
            System.out.println(Arrays.toString(matrix[i]));
        }
        System.out.println("================");
      Map<String,Double> res= evaluate.evaluate(matrix);
        System.out.println(res);
     /* Fraction[][] pros= evaluate.calculateProficiency(em,tm);

        for (int i = 0; i < pros.length; i++) {
            for (int j = 0; j < pros[0].length; j++) {
                System.out.print(pros[i][j].toString()+"|");
            }
            System.out.println();
        }*/

       /* List<Task> availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task0后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(2).setRestEff(0);
        System.out.println("======完成task3后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task1后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task2后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task4后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task5后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task6后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task7后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task8后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task9后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task10后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task11后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task12后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task13后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task14后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task16后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task17后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task15后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task18后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task19后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        //availableTasks.get(0).setRestEff(0);
        System.out.println("======重置==========");

        graph.reset();

        System.out.println("======重置后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(2).setRestEff(0);
        System.out.println("======完成task7后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task3后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(2).setRestEff(0);
        System.out.println("======完成task9后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task4后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(2).setRestEff(0);
        System.out.println("======完成task12后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task5后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task18后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task0后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(2).setRestEff(0);
        System.out.println("======完成task16后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task2后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task6后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(3).setRestEff(0);
        System.out.println("======完成task11后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(3).setRestEff(0);
        System.out.println("======完成task14后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task8后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(2).setRestEff(0);
        System.out.println("======完成task17后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task10后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task13后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(1).setRestEff(0);
        System.out.println("======完成task15后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task1后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);
        availableTasks.get(0).setRestEff(0);
        System.out.println("======完成task19后==========");
        availableTasks = graph.getAvailableTasks();
        availableTasks.forEach(System.out::println);*/


    }
}
