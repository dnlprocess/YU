package edu.yu.introtoalgs;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import edu.yu.introtoalgs.OctopusCountI;

public class OctopusCount implements OctopusCountI{
    private int count = 0;
    private Map<Observation, Integer> observations;

    /*
     * Do not care about order/consecutive
     * Use hashcode
     * Create map which hold specific arm orientation + number of arms fulfilling that?
     * Use regular hashmap, but create object to put within
     * maybe to make faster turn each arm into hashcode
     * from there set?
     * Convert each to a single list where you have each value together
     */

    private class Observation {
        Map<String, Integer> map;

        

        @Override
        public int hashCode() {
            
        }
    }


    public OctopusCount() {
        this.observations = new HashMap<Observation, Integer>();
    }

    /*
     * Make sure not negative
     * make sure all 8 long
     * make sure follow enum but that should take care of itself
     */
    public void addObservation(int observationId,
                             ArmColor[] colors,
                             int[] lengthInCM,
                             ArmTexture[] textures) throws IllegalArgumentException {
        if (observationId < 0 || colors.length != N_ARMS || lengthInCM.length != N_ARMS || textures.length != N_ARMS)  {
            throw new IllegalArgumentException();
        }
        for (int i=0; i<N_ARMS;i++) {
            if (colors[i] == null || lengthInCM[i] <= 0 || textures[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        ArrayList<String> list = new ArrayList<>();/*stream from multiple list to single list
        turn into set 
        Use enum and switch case
        */

        HashMap<String, Integer> map = new HashMap<>();
    }

    public int countThem() {
        return count;
    }
}
