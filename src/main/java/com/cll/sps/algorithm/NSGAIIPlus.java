package com.cll.sps.algorithm;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import java.util.ArrayList;
import java.util.List;


/**
 * @author chenliangliang
 * @date 2018/2/8
 */
public class NSGAIIPlus<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, List<S>> {

    protected final int maxEvaluations;
    protected final SolutionListEvaluator<S> evaluator;
    protected int evaluations;
    //初始种群个数
    protected int initPopulationSize;
    //最终种群个数
    protected int finalPopulationSize;
    private final double rate;
    //个人偏好权重
    private double[] weight;
    //外部存档
    protected NonDominatedSolutionListArchive<S> archive;

    public NSGAIIPlus(Problem<S> problem, int maxEvaluations,
                      int initPopulationSize, int finalPopulationSize, CrossoverOperator<S> crossoverOperator,
                      MutationOperator<S> mutationOperator, SelectionOperator<List<S>, S> selectionOperator,
                      SolutionListEvaluator<S> evaluator, double[] weight) {
        super(problem);
        this.maxEvaluations = maxEvaluations;
        //设置种群大小
        this.setMaxPopulationSize(finalPopulationSize);
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        this.selectionOperator = selectionOperator;
        this.evaluator = evaluator;


        this.initPopulationSize = initPopulationSize;
        this.finalPopulationSize = finalPopulationSize;
        this.rate = (initPopulationSize - finalPopulationSize) / maxEvaluations;
        this.archive = new NonDominatedSolutionListArchive<>();
        this.weight = weight;
    }

    @Override
    protected void initProgress() {
        this.evaluations = this.getMaxPopulationSize();
    }

    @Override
    protected void updateProgress() {
        this.evaluations += this.getMaxPopulationSize();
    }

    @Override
    protected boolean isStoppingConditionReached() {
        return this.evaluations >= this.maxEvaluations;
    }

    @Override
    protected List<S> evaluatePopulation(List<S> population) {
        population = this.evaluator.evaluate(population, this.getProblem());
        return population;
    }

    @Override
    protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
        List<S> jointPopulation = new ArrayList();
        jointPopulation.addAll(population);
        jointPopulation.addAll(offspringPopulation);
        jointPopulation.addAll(archive.getSolutionList());
        RankingAndCrowdingSelection<S> rankingAndCrowdingSelection = new RankingAndCrowdingSelection(this.getMaxPopulationSize());
        List<S> newPopulation = rankingAndCrowdingSelection.execute(jointPopulation);
        newPopulation.sort((s1, s2) -> {
            double f1 = 0.0D, f2 = f1;
            for (int i = 0, l = s1.getNumberOfObjectives(); i < l; i++) {
                f1 += s1.getObjective(i) * weight[i];
                f2 += s2.getObjective(i) * weight[i];
            }

            return Double.compare(f1, f2);
        });
        archive.add(newPopulation.get(0));
        return newPopulation;
    }

    @Override
    public int getMaxPopulationSize() {
        int newPopulationSize = (int) (this.initPopulationSize - this.evaluations * rate);
        return newPopulationSize < this.finalPopulationSize ? finalPopulationSize : newPopulationSize;
    }

    @Override
    public List<S> getResult() {
        return this.getNonDominatedSolutions(this.getPopulation());
    }


    public List<S> getArchiveResult() {
        return this.archive.getSolutionList();
    }

    @Override
    public String getName() {
        return "INSGA-II";
    }


    protected List<S> getNonDominatedSolutions(List<S> solutionList) {
        return SolutionListUtils.getNondominatedSolutions(solutionList);
    }

    @Override
    public String getDescription() {
        return "Nondominated Sorting Genetic Algorithm version II";
    }
}
