package edu.yu.introtoalgs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.yu.introtoalgs.EQIQBase;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    private final static double DELTA = 0.001;
    private static final Logger logger = LogManager.getLogger(AppTest.class);
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {   
        //SoftAssert softAssert = new SoftAssert();

        final int totalQuestions=2;
        final int nCandidates=2;
        final double[] eqSuccessRate={10.0, 20.0};
        final double[] iqSuccessRate={40.0,40.0};
        final int nepotismIndex=1;
        /*logger.error(totalQuestions,
                eqSuccessRate,
                iqSuccessRate,
                nepotismIndex);*/
        final EQIQBase eqiq = new EQIQ(totalQuestions,
                                    eqSuccessRate,
                                    iqSuccessRate,
                                    nepotismIndex);
       //logResults(eqiq);
       try {
            assertTrue("Nepotism should have succeeded", eqiq.canNepotismSucceed());
            assertEquals("getNumberEQQuestions()", 2.0, eqiq.getNumberEQQuestions(), DELTA);
            assertEquals("getNumberIQQuestions()", 0.0, eqiq.getNumberIQQuestions(), DELTA);
            assertEquals("getNumberOfSecondsSuccess()", 360, eqiq.getNumberOfSecondsSuccess(), DELTA);

            // Continue with the rest of your test logic
        } catch (AssertionError e) {
            fail("One or more assertions failed: " + e.getMessage());
        }
    }
}
