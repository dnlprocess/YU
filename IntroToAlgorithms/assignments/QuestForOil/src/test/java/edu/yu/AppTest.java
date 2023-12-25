package edu.yu;

import edu.yu.introtoalgs.*;


import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
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
public class AppTest 
{
    private final Random rand = new Random();
    private static final Logger logger = LogManager.getLogger(AppTest.class);
    private final SoftAssertion softAssertion = new SoftAssertion();

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();
    /**
     * Rigorous Test :-)
     */
    @Test
    public void tests() {
        String[] map1 = {
            "SSSSSSSSSS",//10
            "SSUUUUUUUS",
            "SSSUSSSSUS",
            "SSSUSSSSUU",
            "SUUUSSSSSU",
            "UUSSSSSSSU",
            "USSSSSSSUU",
            "UUSSSSSUUS",
            "SUUSSSSSUU",
            "SSUUUUUUUS",
        };
        int num = 37;

        demo(map1, 5,5, num);
    }
    public void demo(String[] maps, int row, int column, int num) {
        final SoftAssertion softAssert = new SoftAssertion();
        try {
            final char[][] map = new char[maps.length][maps[0].length()];
            for (int i = 0; i<maps.length; i++) {
                setRowAtATime(i, maps[i], map);
            }
            logMap(map);
            final QuestForOilBase qfo = new QuestForOil(map);
            final int retval = qfo.nContiguous(row, column);
            System.out.println(retval);
            softAssert.assertTrue(retval== num , "Mismatch on nContiguous");
        }
        catch (Exception e) {
        logger . error ("Problem" , e) ;
        softAssert . fail ("Unexpected exception: "+e. toString () ) ;
        }
        finally {
            softAssert.assertAll();
        }
    }

    private void setRowAtATime(int row, String vals, char[][] map) {
        char[] chars = vals.toCharArray();
        map[row] = chars;
    }

    private void logMap(char[][] map) {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        for (int column=0; column<map[0].length; column++) {
          sb.append(column);
          sb.append(' ');
        }
        sb.append(System.lineSeparator());
  
        for (char[] rowArray : map) {
          for (char c: rowArray) {
            sb.append(c);
            sb.append(' ');
          }
          sb.append(System.lineSeparator());
        }
        System.out.println(sb.toString());
    }



    private class SoftAssertion {

        private final List<String> softAssertions = new ArrayList<>();
    
        public void assertTrue(boolean condition, String message) {
            if (!condition) {
                softAssertions.add(message);
            }
        }

        public void fail(String message) {
            softAssertions.add(message);
        }
    
        public void assertAll() {
            if (!softAssertions.isEmpty()) {
                Assert.fail("Soft assertions failed: " + softAssertions.toString());
            }
        }
    }
}