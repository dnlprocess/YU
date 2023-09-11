package edu.yu.introtoalgs;

import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.yu.introtoalgs.OctopusCountI;

public class OctopusCount implements OctopusCountI{
    private int count;
    private /*Map<Observation, Integer>*/ HashSet<HashMap<Long, Integer>> observations;


    public OctopusCount() {
        this.observations = new HashSet<HashMap<Long, Integer>>()/*HashMap<Observation, Integer>()*/;
        this.count = 0;
    }

    /*
     * Make sure not negative
     * make sure all 8 long
     * make sure follow enum but that should take care of itself
     * make sure one of enum values
     */
    public void addObservation(int observationId,
                             ArmColor[] colors,
                             int[] lengthInCM,
                             ArmTexture[] textures) throws IllegalArgumentException {
        
        if (observationId < 0 || colors.length != N_ARMS || lengthInCM.length != N_ARMS || textures.length != N_ARMS)  {
            throw new IllegalArgumentException();
        }

        HashMap<Long, Integer> map = new HashMap<>();

        System.out.println("start");
        for (int i=0; i<N_ARMS;i++) {
            if (colors[i] == null || lengthInCM[i] <= 0 || textures[i] == null) {
                throw new IllegalArgumentException();
            }

            Long armLong = generateUniqueId(textures[i], colors[i], lengthInCM[i]);
            //System.out.println(textures[i].name()+colors[i].name()+lengthInCM[i]);
    
            int entries = map.get(armLong) == null? 0: map.get(armLong);
            map.put(armLong, entries+1);
        }

        if (!observations.contains(map)) {
            count++;
        }
        observations.add(map);
        //int observed = observations.get(map) == null? 0: observations.get(map);
        //observations.put(map, observed+1);

    }

    public int countThem() {
        return count;
    }

    private long generateUniqueId(ArmTexture texture, ArmColor color, int lengthInCM) {
        long uniqueId = ((long) texture.ordinal() << 48) |
                        ((long) color.ordinal() << 32) |
                        (lengthInCM & 0xFFFFFFFFL);
        return uniqueId;
    }
}
    /*
     * Do not care about order/consecutive
     * Use hashcode
     * Create map which hold specific arm orientation + number of arms fulfilling that?
     * Use regular hashmap, but create object to put within
     * maybe to make faster turn each arm into hashcode
     * from there set?
     * Convert each to a single list where you have each value together
     */

    /*private class Observation {
        Map<String, Integer> map;

        @Override
        public int hashCode() {
            return map.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Observation otherObs = (Observation) obj;
            if (otherObs.hashCode() != this.hashCode()) {
                return false;
            }

            return this.map.equals(otherObs.map);
        }
    }*/

