package com.cll.sps.algorithm;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmBuilder;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import java.util.List;

/**
 * @author chenliangliang
 * @date 2018/4/8
 */
public class NSGAIIPlusBuilder<S extends Solution<?>> implements AlgorithmBuilder<NSGAIIPlus<S>> {


    /**
     * NSGAIIBuilder class
     */
    private final Problem<S> problem;
    private int maxEvaluations;
    private CrossoverOperator<S> crossoverOperator;
    private MutationOperator<S> mutationOperator;
    private SelectionOperator<List<S>, S> selectionOperator;
    private SolutionListEvaluator<S> evaluator;
    private int initPopulationSize;
    private int finalPopulationSize;
    private double[] weight;

    /**
     * NSGAIIBuilder constructor
     */
    public NSGAIIPlusBuilder(Problem<S> problem, CrossoverOperator<S> crossoverOperator,
                             MutationOperator<S> mutationOperator) {
        this.problem = problem;
        maxEvaluations = 25000;
        this.initPopulationSize=1000;
        this.finalPopulationSize = 100;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        selectionOperator = new BinaryTournamentSelection<S>(new RankingAndCrowdingDistanceComparator<S>());
        evaluator = new SequentialSolutionListEvaluator<S>();

    }

    public NSGAIIPlusBuilder<S> setMaxEvaluations(int maxEvaluations) {
        if (maxEvaluations < 0) {
            throw new JMetalException("maxEvaluations is negative: " + maxEvaluations);
        }
        this.maxEvaluations = maxEvaluations;

        return this;
    }

    public NSGAIIPlusBuilder<S> setInitPopulationSize(int initPopulationSize) {
        if (initPopulationSize < 0) {
            throw new JMetalException("initPopulationSize size is negative: " + initPopulationSize);
        }

        this.initPopulationSize = initPopulationSize;

        return this;
    }

    public NSGAIIPlusBuilder<S> setWeight(double... weight) {
        if (weight.length != problem.getNumberOfObjectives()) {
            throw new JMetalException("weight length is negative: " + weight.length);
        }

        this.weight = weight;

        return this;
    }

    public NSGAIIPlusBuilder<S> setFinalPopulationSize(int finalPopulationSize) {
        if (finalPopulationSize < 0) {
            throw new JMetalException("initPopulationSize size is negative: " + initPopulationSize);
        }

        this.finalPopulationSize = finalPopulationSize;

        return this;
    }

    public NSGAIIPlusBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
        if (selectionOperator == null) {
            throw new JMetalException("selectionOperator is null");
        }
        this.selectionOperator = selectionOperator;

        return this;
    }

    public NSGAIIPlusBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
        if (evaluator == null) {
            throw new JMetalException("evaluator is null");
        }
        this.evaluator = evaluator;

        return this;
    }


    @Override
    public NSGAIIPlus<S> build() {
        return new NSGAIIPlus<>(problem, maxEvaluations, initPopulationSize, finalPopulationSize, crossoverOperator,
                mutationOperator, selectionOperator, evaluator,weight);
    }
}
