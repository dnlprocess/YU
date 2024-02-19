package edu.yu.da;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class WaterPressure extends WaterPressureBase {

    private class UnionFind {
        private Map<String, String> parent;
        private Map<String, Integer> rank;

        public UnionFind(HashSet<String> pumps) {
            this.parent = new HashMap<>();
            this.rank = new HashMap<>();
            for (String pump: pumps) {
                parent.put(pump, pump);
                rank.put(pump, 0);
            }
        }

        private String find(String u) {
            if (parent.get(u).equals(u)) {
                return u;
            }

            parent.put(u, find(parent.get(u)));
            return parent.get(u);
        }

        public void union(String u, String w) {
            String parentU = find(u);
            String parentW = find(w);

            if (parentU.equals(parentW)) {
                return;
            }

            if (rank.get(parentU) < rank.get(parentW)) {
                parent.put(parentU, parentW);
            } else if (rank.get(parentU) > rank.get(parentW)) {
                parent.put(parentW, parentU);
            } else {
                parent.put(parentU, parentW);
                rank.put(parentW, rank.get(parentW) + 1);
            }
        }

        public boolean isConnected(String u, String w) {
            return find(u).equals(find(w));
        }
    }

    private class Channel implements Comparable<Channel> {
        String start;
        String dest;
        double blockage;
        boolean input;
        double dist;
  
        public Channel(String start, String dest, double blockage) {
            this.start = start;
            this.dest = dest;
            this.blockage = blockage;
            this.input = false;
        }

        public void input() {
            this.input = true;
        }
  
        public void dist(double dist) {
          this.dist = dist;
        }
  
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Channel otherChannel = (Channel) obj;
            return dest.equals(otherChannel.dest);
        }

        @Override
        public int compareTo(Channel otherChannel) {
            return Double.compare(this.blockage, otherChannel.blockage);
        }
  
    }

    double minAmount;

    HashMap<String, List<Channel>> dag;
    HashSet<String> inputPumps;
    HashSet<String> pumps;
    List<Channel> channels;

    public WaterPressure(String initialInputPump) {
        super(initialInputPump);
        this.minAmount = Double.MIN_VALUE;
        
        this.dag = new HashMap<>();
        this.dag.put(initialInputPump, new ArrayList<>());

        this.inputPumps = new HashSet<>();
        this.inputPumps.add(initialInputPump);

        this.pumps = new HashSet<>();
        this.pumps.add(initialInputPump);

        this.channels = new ArrayList<>();

    }

    //remember an input pump by definition receives water so there can be a blockage between 
    public void addSecondInputPump(String secondInputPump) {
        validate(secondInputPump);
        if (!this.pumps.contains(secondInputPump) || this.inputPumps.contains(secondInputPump)) {
            throw new IllegalArgumentException();
        }
        if (this.inputPumps.size()>1) {
            return;
        }

        this.inputPumps.add(secondInputPump);
    }

    // Maybe use binary search to add
    // whit is min if not connected - retrun -1.0;
    public void addBlockage(String v, String w, double blockage) {
        if (this.minAmount != Double.MIN_VALUE) {
            throw new IllegalStateException();
        }
        validate(v);
        validate(w);
        validate(blockage);

        Channel channel = new Channel(v, w, blockage);
        this.dag.computeIfAbsent(v, k -> new ArrayList<Channel>());

        if (v.equals(w) || dag.get(v).contains(channel)) {
            throw new IllegalArgumentException();
        }

        this.dag.get(v).add(channel);
        this.channels.add(channel);
        this.pumps.add(v);
        this.pumps.add(w);
    }


    public double minAmount() {
        // TODO Auto-generated method stub

        solve();
        System.out.printf("FMin: %.2f", minAmount);
        return this.minAmount;
    }
    
    // This is DAG, so currently thinking should work to do BFS from each input pump - stopping if reach another input pump, and keeping track of the highest blockage.
    // This is O(V+E), so clearly something is wrong.
    // Need to have one visited array, or else will have too many repeats
    // MST type algorithm works if connected but if second input is in disconnected graph subset, difficult
    // Keep adding edges until connected to at least one input
        // Never add edges that direct INTO input pump
    private void solve() {
        Collections.sort(this.channels);
        Stack<Channel> mst = new Stack<>();
        UnionFind uf = new UnionFind(this.pumps);

        for (int i=0; i< channels.size() && mst.size() < this.pumps.size()-1; i++) {
            //System.out.println("gggg");
            Channel channel = this.channels.get(i);
            String start = channel.start;
            String dest = channel.dest;

            if (inputPumps.contains(dest)) continue;

            if (!uf.isConnected(start, dest)) {
                uf.union(start, dest);     // merge v and w components
                mst.add(channel); // add edge e to mst
                this.minAmount = channel.blockage;
                //System.out.printf("Min: %.2f", minAmount);
            }
            // else: it is not part of MST
        }

        check(uf);
    }

    private void check(UnionFind uf) {
        boolean connected;
        for (String pump: this.pumps) {
            connected = false;
            for (String inputPump: this.inputPumps) {
                if (uf.isConnected(pump, inputPump)) connected = true;
            }
            //if (!connected) System.out.printf("Pump: %s", pump);
            if (!connected) this.minAmount = -1.0;
        }
        //System.out.printf("Mxcin: %.2f", minAmount);
       
        if (this.channels.isEmpty()) {
            this.minAmount = 0.0;
        }
    }

    private void validate(String s) {
        if (s.length() == 0) {
            throw new IllegalArgumentException();
        }
    }
    private void validate(double i) {
        if (i <= 0) {
            throw new IllegalArgumentException();
        }
    }
    
}
