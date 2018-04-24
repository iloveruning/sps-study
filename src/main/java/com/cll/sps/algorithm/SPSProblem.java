package com.cll.sps.algorithm;

import com.cll.sps.evaluate.Evaluator;
import org.apache.commons.math3.fraction.Fraction;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenliangliang
 * @date 2018/2/12
 */
public class SPSProblem extends AbstractDoubleProblem {


    private Evaluator evaluator;

    public SPSProblem(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.setNumberOfObjectives(3);
        this.setNumberOfVariables(evaluator.getEmployeeSize() * evaluator.getTaskSize());
        this.setName("SPS Problem");

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        for (int i = 0, l = getNumberOfVariables(); i < l; i++) {
            lowerLimit.add(0.0);
            upperLimit.add(1.0);
        }

        this.setLowerLimit(lowerLimit);
        this.setUpperLimit(upperLimit);
    }

    public int getEmpSize() {
        return evaluator.getEmployeeSize();
    }


    public int getTaskSize() {
        return evaluator.getTaskSize();
    }


    public int getK() {
        return evaluator.getK();
    }


    /**
     * 评价解的优劣
     */
    @Override
    public void evaluate(DoubleSolution s) {
        int m = getEmpSize();
        int n = getTaskSize();
        int[][] matrix = new int[m][n];

        Fraction f;
        int k = getK();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                f = new Fraction(s.getVariableValue(i * m + j));
                matrix[i][j] = f.multiply(k).intValue();
            }
        }

        Map<String, Double> res = evaluator.evaluate(matrix);

        s.setObjective(0, res.get("cost"));
        s.setObjective(1, res.get("time"));
        s.setObjective(2, res.get("satisfaction"));
    }


}
