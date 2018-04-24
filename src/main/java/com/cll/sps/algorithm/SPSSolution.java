/*
package com.cll.sps.algorithm;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;

import java.util.HashMap;

*/
/**
 * @author chenliangliang
 * @date 2018/2/12
 *//*

public class SPSSolution extends AbstractGenericSolution<Double,SPSProblem> implements DoubleSolution {


    public SPSSolution(SPSProblem problem) {
        super(problem);
        this.initializeObjectiveValues();
        this.initializeVariables();
    }

    public SPSSolution(SPSSolution s) {
        super(s.problem);
        int i;
        for(i = 0; i < this.problem.getNumberOfVariables(); ++i) {
            this.setVariableValue(i, s.getVariableValue(i));
        }

        for(i = 0; i < this.problem.getNumberOfObjectives(); ++i) {
            this.setObjective(i, s.getObjective(i));
        }

        this.attributes = new HashMap<>(s.attributes);
    }

    */
/**
     * 初始化变量
     *//*

    private void initializeVariables() {
        for (int i = 0; i < problem.getNumberOfVariables(); i++) {
            this.setVariableValue(i,randomGenerator.nextInt(0,problem.getK()));
        }
    }


    @Override
    public void setVariableValue(int i, Double aDouble) {

    }

    @Override
    public String getVariableValueString(int i) {
        return this.getVariableValue(i).toString();
    }

    @Override
    public Solution<Integer> copy() {
        return new SPSSolution(this);
    }


    @Override
    public Double getLowerBound(int i) {
        return null;
    }

    @Override
    public Double getUpperBound(int i) {
        return null;
    }
}
*/
