package edu.yu;

import edu.yu.da.*;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Assert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

/**
 * Unit test for simple App.
 */
public class ThereAndBackAgainTest
{
    private final Random rand = new Random();
    private static final Logger logger = LogManager.getLogger(ThereAndBackAgainTest.class);
    private static final SoftAssertion softAssertion = new SoftAssertion();

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();
    /**
     * Rigorous Test :-)
     */
    @Test
    public void demo() {
        double COST_DELTA = 1.0;
        final String startVertex = "a" ;
        final ThereAndBackAgainBase taba = new ThereAndBackAgain(startVertex);
        taba.addEdge(startVertex, "b", 1.0);
        taba.addEdge("b", "c", 2.0);
        taba.doIt();
        SoftAssertion.assertTrue(taba.goalVertex() == null, "goalVertex");
        softAssertion.assertTrue(Math.abs(taba.goalCost() - 0.0) <= COST_DELTA, "goalCost");
        softAssertion.assertTrue(taba.getOneLongestPath().equals(Collections.<String>emptyList()),
                                "getOneLongestPath" );
        softAssertion.assertTrue(taba.getOtherLongestPath().equals(Collections.<String>emptyList()),
                                "getOtherLongestPath");
    }
    

    



    private static class SoftAssertion {

        private static final List<String> softAssertions = new ArrayList<>();
    
        public static void assertTrue(boolean condition, String message) {
            if (!condition) {
                softAssertions.add(message);
            }
        }

        public static void fail(String message) {
            softAssertions.add(message);
        }
    
        public static void assertAll() {
            if (!softAssertions.isEmpty()) {
                Assert.fail("Soft assertions failed: " + softAssertions.toString());
            }
        }
    }
}