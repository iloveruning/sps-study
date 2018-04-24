package com.cll.sps;

import com.cll.sps.algorithm.NSGAIIPlus;
import com.cll.sps.algorithm.NSGAIIPlusBuilder;
import com.cll.sps.algorithm.SPSProblem;
import com.cll.sps.entity.EmployeeManager;
import com.cll.sps.entity.Graph;
import com.cll.sps.entity.TaskManager;
import com.cll.sps.evaluate.Evaluator;
import com.cll.sps.parse.Load;
import org.testng.annotations.Test;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.SimpleRandomMutation;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;

import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/2/12
 */
public class JmetalTest {

    @Test
    public void test1(){

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

        Evaluator evaluator=new Evaluator(10,em,graph);


        SPSProblem problem;

        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
        String referenceParetoFront = "";
        //定义优化问题
        problem = new SPSProblem(evaluator);
        //配置SBX交叉算子
        double crossoverProbability = 1.0;
        double crossoverDistributionIndex = 20.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);
        //配置变异算子
        double mutationProbability = 1.0 / problem.getNumberOfVariables();
        //  double mutationDistributionIndex = 20.0 ;
        mutation = new SimpleRandomMutation(mutationProbability);
        //配置选择算子
        /*selection = new BinaryTournamentSelection<DoubleSolution>(
                new RankingAndCrowdingDistanceComparator<DoubleSolution>());*/
        //将组件注册到algorithm
        /*algorithm = new NSGAIIBuilder<DoubleSolution>(problem, crossover, mutation)
                .setSelectionOperator(selection)
                .setMaxEvaluations(10000)
                .setPopulationSize(100)
                .build();*/
        NSGAIIPlus<DoubleSolution> algorithm;
        algorithm= new NSGAIIPlusBuilder<>(problem,crossover,mutation)
                .setWeight(0.01,0.2,0.1)
                .setInitPopulationSize(1000)
                .setFinalPopulationSize(100)
                .build();


        //用AlgorithmRunner运行算法
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
        //获取结果集
        List<DoubleSolution> population = algorithm.getResult();
        long computingTime = algorithmRunner.getComputingTime();

        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
        //将全部种群打印到文件中
        System.out.println(population);
        AbstractAlgorithmRunner.printFinalSolutionSet(population);
        if (!referenceParetoFront.equals("")){
            System.out.println(referenceParetoFront);
        }
    }
}
