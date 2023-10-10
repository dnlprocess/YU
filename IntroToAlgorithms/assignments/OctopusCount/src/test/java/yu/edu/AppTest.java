package yu.edu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private static final int SIZE = OctopusCountI.N_ARMS;
    private static List<ArmColor> COLORS = Arrays.asList(ArmColor.values());
    private static List<ArmTexture> TEXTURES = Arrays.asList(ArmTexture.values());
    
    private static final Random RANDOM = new Random();

    public static ArmColor randomColor()  {
        return COLORS.get(RANDOM.nextInt(2));
    }

    public static ArmTexture randomTexture()  {
        return TEXTURES.get(RANDOM.nextInt(3));
    }
    public static int randomLength() {
        return new Random().nextInt(35)+1;
    }

    /**
     * Rigorous Test :-)
     */
    @Test(timeout=500)
    public void trivialAreIdentical () {
        long startTime = System.nanoTime();

        final OctopusCountI oc = new OctopusCount ();
        int observationCounter = 0;
        
        ArmColor[] colors1 = {ArmColor.GRAY, ArmColor.GRAY, ArmColor.GRAY, ArmColor.RED, ArmColor.RED, ArmColor.RED, ArmColor.BLACK, ArmColor.BLACK };
        int[] lengthInCM1 = { 1,2,3,4,5,6,7,8};
        ArmTexture[] textures1 = {ArmTexture.SMOOTH, ArmTexture.SMOOTH, ArmTexture.SMOOTH, ArmTexture.SLIMY, ArmTexture.SLIMY, ArmTexture.SLIMY, ArmTexture.STICKY, ArmTexture.STICKY};
        observationCounter++;
    
        List<Integer> indices = createIndices();
        addObservation(observationCounter, oc, colors1, lengthInCM1, textures1);
        addObservation(observationCounter, oc, colors1, lengthInCM1, textures1);
        addObservation(observationCounter, oc, shuffle(indices, colors1), shuffleInt(indices, lengthInCM1), shuffle(indices, textures1));
        final int count = oc.countThem() ;
        assertTrue(String.format("count: %2d, true: %2d", count, observationCounter), count == observationCounter);
    
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);
    }
    
    @Test
    public void mainTest () {
        int n = 2;

        System.out.println("begin");

        for (int i=1; i<20; i++) {
            n = n*2;
            int time = nTest(n);
            System.out.println(time);
        }
    }

    
    private int nTest (int n) {
        OctopusCountI oc = new OctopusCount ();

        ArmColor[][] colors = new ArmColor[n][8];
        int[][] lengthsInCM = new int[n][8];
        ArmTexture[][] textures = new ArmTexture[n][8];

        int observationCounter = 0;
        
        for (int j=0; j<n;j++){
            if ((j%3==0 || j%7==0)&& j>1) {
                List<Integer> indices = createIndices();
                colors[j] = shuffle(indices, colors[j-1]);
                textures[j] = shuffle(indices, textures[j-1]);
                lengthsInCM[j] = shuffleInt(indices, lengthsInCM[j-1]);
                continue;
            }

            for (int i = 0; i < 8; i++) {
                colors[j][i] = randomColor();
                textures[j][i] = randomTexture();
                lengthsInCM[j][i] = randomLength();
            }
            observationCounter++;
        }

        long startTime = System.nanoTime();

        for (int i=0; i<n;i++) {
            addObservation(i, oc, colors[i], lengthsInCM[i], textures[i]);
        }

        int count = oc.countThem() ;
        assertTrue(String.format("count: %2d, true: %2d, n: %d", count, observationCounter, n), count == observationCounter);


        long endTime = System.nanoTime();
        int duration = (int) (endTime - startTime);
        return duration;
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

    public static List<Integer> createIndices() {
        List<Integer> indices = new ArrayList<>();

        for (int i = 0; i <= 7; i++) {
            indices.add(i);
        }
        
        Collections.shuffle(indices, new Random());
        return indices;
    }

    public static <T> T[] shuffle(List<Integer> indices, T[] array) {
        T[] originalArray = Arrays.copyOf(array, array.length);
        IntStream.range(0, array.length)
            .forEach(i -> originalArray[i] = array[indices.get(i)]);

        return originalArray;
    }

    public static int[] shuffleInt(List<Integer> indices, int[] array) {
        int[] originalArray = Arrays.copyOf(array, array.length);
        IntStream.range(0, array.length)
            .forEach(i -> originalArray[i] = array[indices.get(i)]);

        return originalArray;
    }
}
