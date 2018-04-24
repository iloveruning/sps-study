package com.cll.test;


import com.cll.algorithm.PopEA;
import com.cll.parse.Parse;
import com.cll.preparework.Person;
import com.cll.preparework.PrepareWork;

import java.util.Arrays;
import java.util.List;

public class Test {


    public static void main(String[] args) {


        //读取并解析数据集
        Parse p = new Parse("src/input.txt");
        p.parse();
        PrepareWork pWork = p.getPrepareWork();
        PopEA popEA = new PopEA(pWork, 64, 7, 0.4);
        //初始化种群
        List<Person> pop = popEA.initPop();
        List<Person> parents;
        List<Person> child;
        for (int i = 0; i < 1000; i++) {
            //采用二进制锦标赛机制从种群中筛选用于产生子代的父代
            parents = popEA.binTournament(pop, 2);
            for (Person person : parents) {
                System.out.println("binTournament  " + person.getFitness());
            }
            //对父代采用交叉操作产生子代
            child = popEA.crossover(parents);
            //对子代进行变异操作
            popEA.mutation(child);
            //采用精英策略从子代和种群中选择下一代种群
            pop = popEA.select(pop, child);
        }
        System.out.println(pop.get(0));
        double[][] res = pop.get(0).getMatrix();
        for (int i = 0; i < res.length; i++) {
            System.out.println(Arrays.toString(res[i]));
        }

        popEA.dispatch(pop.get(0), "F:/experimentResult/res.txt");


//		int [] s=new int [10];
//		for (int i = 0; i < 10; i++) {
//			s[i]=i+1;
//		}
//		ArrayUtils.reverse(s);
//		for (int i = 0; i < s.length; i++) {
//			System.out.print(s[i]+"  ");
        //}


        //System.out.println(new Fraction(9, 9).getValue());
//		Parse p = new Parse("input.txt");
//		p.parse();
//-----------------------------------------------		
        //TaskManeger tManeger=p.getTaskManeger();
//		List<Task> list=tManeger.tasks;
//		for (Task task : list) {
//			System.out.println(task);
//		}
        //EmployeeManager eManeger=p.getEmployeeManeger();
//		List<Employee> eml=eManeger.employees;
//		for (Employee employee : eml) {
//			System.out.println(employee);
//		}
        //Graph g=p.getGraph();
//------------------------------		
//		Algorithm algorithm=new DE(7);
//		
//		PrepareWork pWork=p.getPrepareWork();
//		Evaluate evaluate=new Evaluate(pWork);
//		evaluate.evaluate(algorithm.getMatrix(pWork.getPrepareMatrix()),7);
        //------------------------------------
//		Graph g=p.getGraph();
//		List<Arc> liArcs=g.arcs;
//		for (Arc arc : liArcs) {
//			System.out.println(arc);
//		}

//		System.out.println(p.getTaskManeger().getSize());
//		Graph g=p.getGraph();
//		List<Task> list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(0);
//		System.out.println("���º�-----0------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(1);
//		System.out.println("���º�-----1------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(18);
//		System.out.println("���º�------18-----");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(3);
//		System.out.println("���º�-----3------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(2);
//		System.out.println("���º�----2-------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(4);
//		System.out.println("���º�-----4------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(5);
//		System.out.println("���º�-----5------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(15);
//		System.out.println("���º�-----15------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(6);
//		System.out.println("���º�-----6------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(13);
//		System.out.println("���º�-----13------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(8);
//		System.out.println("���º�-----8------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(9);
//		System.out.println("���º�-----9------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(7);
//		System.out.println("���º�-----7------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(11);
//		System.out.println("���º�-----11------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(12);
//		System.out.println("���º�-----12------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(16);
//		System.out.println("���º�-----16------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(19);
//		System.out.println("���º�-----19------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(14);
//		System.out.println("���º�----14-------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(10);
//		System.out.println("���º�----10-------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(17);
//		System.out.println("���º�-----17------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}
//		g.update(3);
//		System.out.println("���º�----3-------");
//		list=g.getAvailableTasks();
//		for (Task task : list) {
//			System.out.println("����������id��"+task.getId());
//		}

    }
}
