package com.cll.sps;

import com.cll.MyProblem;
import org.moeaframework.Executor;
import org.moeaframework.analysis.plot.Plot;
import org.moeaframework.core.NondominatedPopulation;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        NondominatedPopulation result = new Executor()
                .withProblemClass(MyProblem.class)
                .withAlgorithm("SPEA2")
                .withMaxEvaluations(1000)
                .run();

//				 for (Solution solution : result) {
//				     System.out.printf("%.5f => %.5f, %.5f\n",
//				     EncodingUtils.getReal(solution.getVariable(0)),
//				     solution.getObjective(0),
//				     solution.getObjective(1));
//				 }

        new Plot()
                .add("NSGAII", result)
                .show();
    }
}
