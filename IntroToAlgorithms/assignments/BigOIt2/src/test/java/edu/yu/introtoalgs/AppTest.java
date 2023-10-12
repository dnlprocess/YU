package edu.yu.introtoalgs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        BigOIt2 algTester = new BigOIt2();

        try {
            BigOMeasurable toBeTested = (BigOMeasurable) Class.forName("edu.yu.introtoalgs.DoubleLoop").getConstructor().newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        double ratio = algTester.doublingRatio("edu.yu.introtoalgs.DoubleLoop", 60000);
        assertEquals(4.0, ratio, 0.1);
    }

    @Test
    public void shouldAnswerWithTrue2() {
        BigOIt2 algTester = new BigOIt2();

        double ratio = algTester.doublingRatio("edu.yu.introtoalgs.SumOfSquares", 60000);
        assertEquals(4.0, ratio, 0.1);
    }
    @Test
    public void shouldAnswerWithTrue3() {
        BigOIt2 algTester = new BigOIt2();

        double ratio = algTester.doublingRatio("edu.yu.introtoalgs.Linear", 60000);
        assertEquals(1.0, ratio, 0.1);
    }
}
