package yu.edu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        return COLORS.get(RANDOM.nextInt(SIZE));
    }

    public static ArmTexture randomTexture()  {
        return TEXTURES.get(RANDOM.nextInt(SIZE));
    }
    public static int randomLength() {
        return new Random().nextInt(35);
    }

    /**
     * Rigorous Test :-)
     */
    @Test(timeout=500)
    public void trivialAreIdentical () {
        long startTime = System.nanoTime();

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
    
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration);
    }

    @Test
    private void mainTest () {
        int n = 8;

        for (int i=1; i<15; i++) {
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
        
        while (n>0){
            n--;
            if (n%3==0) {
                colors[n] = shuffle(colors[n-1]);
                textures[n] = shuffle(textures[n-1]);
                lengthsInCM[n] = shuffleInt(lengthsInCM[n-1]);
                continue;
            }

            for (int i = 0; i < 8; i++) {
                colors[n][i] = randomColor();
                textures[n][i] = randomTexture();
                lengthsInCM[n][i] = randomLength();
            }
            observationCounter++;
        }

        long startTime = System.nanoTime();

        for (int i=0; i<n;i++) {
            addObservation(i, oc, colors[i], lengthsInCM[i], textures[i]);
        }

        int count = oc.countThem() ;
        assertTrue(String.format("count: %2d, true: %2d", count, observationCounter), count == observationCounter);


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

    public static <T> T[] shuffle(T[] array) {
        List<Integer> indices = IntStream.range(0, array.length).boxed().toList();
        Collections.shuffle(indices, new Random());

        T[] originalArray = Arrays.copyOf(array, array.length);
        IntStream.range(0, array.length)
            .forEach(i -> originalArray[i] = array[indices.get(i)]);

        return originalArray;
    }

    public static int[] shuffleInt(int[] array) {
        List<Integer> indices = IntStream.range(0, array.length).boxed().toList();
        Collections.shuffle(indices, new Random());

        int[] originalArray = Arrays.copyOf(array, array.length);
        IntStream.range(0, array.length)
            .forEach(i -> originalArray[i] = array[indices.get(i)]);

        return originalArray;
    }
}
