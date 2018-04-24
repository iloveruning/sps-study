
package com.cll;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.problem.AbstractProblem;

/**
 * This class is used for ... 
 * @author: ChenLiangLiang
 * @date: 2017��8��16�� ����5:49:12
 * @version: 1.0
 *
 */
public class MyProblem extends AbstractProblem{

	/**
	 * 
	 */
	public MyProblem() {
		super(3, 2, 0);
	}

	/* (non-Javadoc)
	 * @see org.moeaframework.core.Problem#evaluate(org.moeaframework.core.Solution)
	 */
	@Override
	public void evaluate(Solution solution) {
		double v1 = EncodingUtils.getReal(solution.getVariable(0));
		double v2 = 1L-v1;
		double v3 = EncodingUtils.getReal(solution.getVariable(1));
		double v4 = EncodingUtils.getReal(solution.getVariable(2));
		
		double t1=200*v1/(8*v3*0.6);
		double t2=200*v2/(8*v4*0.8);
		
		//System.out.println("t1:"+t1+" t2:"+t2);
		double f1 = Math.max(t1, t2);
		double f2 = v3*t1*6000/30+v4*t2*10000/30;
		

		solution.setObjective(0, f1);
		solution.setObjective(1, f2);
		
	}

	/* (non-Javadoc)
	 * @see org.moeaframework.core.Problem#newSolution()
	 */
	@Override
	public Solution newSolution() {
		Solution solution = new Solution(3, 2, 0);
		RealVariable v1=EncodingUtils.newReal(0.0, 1.0);
		RealVariable v3=EncodingUtils.newReal(1.0, 1.5);
		RealVariable v4=EncodingUtils.newReal(1.0, 1.5);
		solution.setVariable(0, v1);
		solution.setVariable(1, v3);
		solution.setVariable(2, v4);

		return solution;
	}

}
