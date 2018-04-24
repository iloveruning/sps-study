package com.cll;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.moeaframework.Analyzer;
import org.moeaframework.Executor;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }



    @org.junit.Test
    public void test2() {
        String problem = "UF1";
        String[] algorithms = { "NSGAII", "SPEA2", "IBEA" };

        //setup the experiment
        Executor executor = new Executor()
                .withProblem(problem)
                .withMaxEvaluations(10000);

        Analyzer analyzer = new Analyzer()
                .withProblem(problem)
                .includeHypervolume()
                .showStatisticalSignificance();

        //run each algorithm for 50 seeds
        for (String algorithm : algorithms) {
            analyzer.addAll(algorithm,
                    executor.withAlgorithm(algorithm).runSeeds(50));
        }

        //print the results
        analyzer.printAnalysis();
    }


}
