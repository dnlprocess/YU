package edu.yu.da;

import static org.junit.Assert.assertTrue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.Test;
import org.junit.Assert;

/**
 * Unit test for simple App.
 */
public class WaterPressureTest 
{
    /**
     * Rigorous Test :-)
     */
    private static final Logger logger = LogManager.getLogger(WaterPressureTest.class);

    @Test
    public void testWaterPressureBase() {
        final String initialInputPump = "PumpA";
        final String secondInputPump = "PumpB";
        final String[] V = {"PumpA", "PumpB"};
        final String[] W = {"PumpB", "PumpC"};
        final double[] weight = {1.0, 2.0};
        final double expectedMinAmount = 2.0;

        logger.info("Creating WaterPressureBase({})", initialInputPump);
        final WaterPressureBase wp = new WaterPressure(initialInputPump);

        for (int i = 0; i < V.length; i++) {
            logger.info("Invoking wp.addBlockage({}, {}, {})", V[i], W[i], weight[i]);
            wp.addBlockage(V[i], W[i], weight[i]);
        }

        logger.info("Invoking wp.addSecondInputPump({})", secondInputPump);
        wp.addSecondInputPump(secondInputPump);

        logger.info("Invoking wp.minAmount()");
        final double actualMinAmount = wp.minAmount();
        logger.info("Received {}", actualMinAmount);

        // Use TestNG SoftAssert for multiple assertions
        Assert.assertEquals(expectedMinAmount, actualMinAmount, 0.001);
    }


    @Test
    public void testSinglePumpStation() {
        final String initialInputPump = "InitialPump";

        logger.info("Creating WaterPressureBase({})", initialInputPump);
        final WaterPressureBase wp = new WaterPressure(initialInputPump);

        logger.info("Invoking wp.minAmount()");
        final double actualMinAmount = wp.minAmount();
        logger.info("Received {}", actualMinAmount);

        // TestNG assertion
        Assert.assertEquals(actualMinAmount, 0.0, 0.001);
    }


    @Test
    public void testWaterPressureBase1() {
        final String initialInputPump = "PumpA";
        final String secondInputPump = "PumpB";
        final String[] V = {"PumpA", "PumpD"};
        final String[] W = {"PumpB", "PumpE"};
        final double[] weight = {1.0, 2.0};
        final double expectedMinAmount = -1.0;

        logger.info("Creating WaterPressureBase({})", initialInputPump);
        final WaterPressureBase wp = new WaterPressure(initialInputPump);

        for (int i = 0; i < V.length; i++) {
            logger.info("Invoking wp.addBlockage({}, {}, {})", V[i], W[i], weight[i]);
            wp.addBlockage(V[i], W[i], weight[i]);
        }

        logger.info("Invoking wp.addSecondInputPump({})", secondInputPump);
        wp.addSecondInputPump(secondInputPump);

        logger.info("Invoking wp.minAmount()");
        final double actualMinAmount = wp.minAmount();
        logger.info("Received {}", actualMinAmount);

        // Use TestNG SoftAssert for multiple assertions
        Assert.assertEquals(expectedMinAmount, actualMinAmount, 0.001);
    }
}
