package com.cll.sps.experiment;

import com.cll.sps.algorithm.NSGAIIPlusBuilder;
import com.cll.sps.algorithm.SPSProblem;
import com.cll.sps.entity.EmployeeManager;
import com.cll.sps.entity.Graph;
import com.cll.sps.entity.TaskManager;
import com.cll.sps.evaluate.Evaluator;
import com.cll.sps.parse.Load;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/4/7
 */
public class SPSStudy {

    private static final int INDEPENDENT_RUNS = 1 ;

    public static void main(String[] args) throws IOException {
//    if (args.length != 1) {
//      throw new JMetalException("Missing argument: experimentBaseDirectory") ;
//    }
//    String experimentBaseDirectory = args[0] ;

        Load load = new Load();
        load.parse("T20-E15-S5.conf");
        TaskManager tm = load.getTaskManager();
        Graph graph = load.getGraph();
        EmployeeManager em=load.getEmployeeManager();
        System.out.println("================");
        graph.setTaskManager(tm);
        Evaluator evaluator=new Evaluator(10,em,graph);

        String experimentBaseDirectory = "F:/experimentResult";

        List<ExperimentProblem<DoubleSolution>> problemList = Arrays.asList(new ExperimentProblem<>(new SPSProblem(evaluator)));


        List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithmList =
                configureAlgorithmList(problemList);

        List<String> referenceFrontFileNames = Arrays.asList("sps.pf");

        Experiment<DoubleSolution, List<DoubleSolution>> experiment =
                new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SPSStudy")
                        .setAlgorithmList(algorithmList)
                        .setProblemList(problemList)
                        .setReferenceFrontDirectory("pareto_fronts")
                        .setReferenceFrontFileNames(referenceFrontFileNames)
                        .setExperimentBaseDirectory(experimentBaseDirectory)
                        .setOutputParetoFrontFileName("FUN")
                        .setOutputParetoSetFileName("VAR")
                        .setIndicatorList(Arrays.asList(
                                new Epsilon<DoubleSolution>(), new Spread<DoubleSolution>(), new GenerationalDistance<DoubleSolution>(),
                                new PISAHypervolume<DoubleSolution>(),
                                new InvertedGenerationalDistance<DoubleSolution>(),
                                new InvertedGenerationalDistancePlus<DoubleSolution>()))
                        .setIndependentRuns(INDEPENDENT_RUNS)
                        .setNumberOfCores(4)
                        .build();

        new ExecuteAlgorithms<>(experiment).run();
        new ComputeQualityIndicators<>(experiment).run() ;
        new GenerateLatexTablesWithStatistics(experiment).run() ;
        new GenerateWilcoxonTestTablesWithR<>(experiment).run() ;
        new GenerateFriedmanTestTables<>(experiment).run();
        new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).setDisplayNotch().run() ;
    }

    /**
     * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of a
     * {@link ExperimentAlgorithm}, which is a decorator for class {@link Algorithm}.
     *
     * @param problemList
     * @return
     */
    static List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> configureAlgorithmList(
            List<ExperimentProblem<DoubleSolution>> problemList) {
        List<ExperimentAlgorithm<DoubleSolution, List<DoubleSolution>>> algorithms = new ArrayList<>();


        for (int i = 0; i < problemList.size(); i++) {
            Algorithm<List<DoubleSolution>> algorithm = new NSGAIIBuilder<DoubleSolution>(
                    problemList.get(i).getProblem(),
                    new SBXCrossover(1.0, 20.0),
                    new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 20.0))
                    .build();
            algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
        }

        for (int i = 0; i < problemList.size(); i++) {
            Algorithm<List<DoubleSolution>> algorithm = new NSGAIIPlusBuilder<DoubleSolution>(
                    problemList.get(i).getProblem(),
                    new SBXCrossover(1.0, 20.0),
                    new PolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 20.0))
                    .setInitPopulationSize(500)
                    .setWeight(0.01,0.2,0.1)
                    .build();
            algorithms.add(new ExperimentAlgorithm<>(algorithm, problemList.get(i).getTag()));
        }

        return algorithms ;
    }
}
