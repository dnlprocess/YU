package yu.edu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import edu.yu.introtoalgs.OctopusCountI;
import edu.yu.introtoalgs.OctopusCountI.ArmColor;
import edu.yu.introtoalgs.OctopusCountI.ArmTexture;
import edu.yu.introtoalgs.OctopusCount;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test(timeout=500)
    public void trivialAreIdentical () {
        final OctopusCountI oc = new OctopusCount ();
        int observationCounter = 0;
        
        final ArmColor[] colors1 = {ArmColor.GRAY, ArmColor.GRAY, ArmColor.GRAY, ArmColor.RED, ArmColor.RED, ArmColor.RED, ArmColor.BLACK, ArmColor.BLACK };
        final int[] lengthInCM1 = { 1,2,3,4,5,6,7,8};
        final ArmTexture[] textures1 = {ArmTexture.SMOOTH, ArmTexture.SMOOTH, ArmTexture.SMOOTH, ArmTexture.SLIMY, ArmTexture.SLIMY, ArmTexture.SLIMY, ArmTexture.STICKY, ArmTexture.STICKY};
        observationCounter++;
        
        addObservation(observationCounter, oc, colors1, lengthInCM1, textures1);
        addObservation(observationCounter, oc, colors1, lengthInCM1, textures1);
        final int count = oc.countThem() ;
        assertTrue(String.format("count: %2d, true: %2d", count, observationCounter), count == observationCounter);
    }
    private static void addObservation (int observationCounter,
                                        final OctopusCountI oc,
                                        final ArmColor[] colors,
                                        final int[] lengthInCM ,
                                        final ArmTexture[] textures )
                                            {
        //logger.info ("colors = {}" , Arrays.toString(colors));
        //logger.info ("lengthInCM = {}" , Arrays.toString(lengthInCM));
        //logger.info ("textures = {}" , Arrays.toString(textures));
        try {
            oc.addObservation(observationCounter, colors, lengthInCM, textures);
        }
        catch ( IllegalArgumentException iae ) {
            throw iae;
        }
        catch ( Exception e ) {
            //final String msg;
            //logger.error(msg, e);
            //throw new RuntimeException(msg, e);
        }
    }
}
