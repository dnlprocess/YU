package edu.yu.introtoalgs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.yu.introtoalgs.WordLayout;

public class WordLayout extends WordLayoutBase {

    private class WordTree {
        private class Node {
            private Node left;
            private Node right;
            public int rows, columns;
            public int rowOffset, colOffset;

            public Node(int nRows, int nColumns, int rowOffset, int colOffset) {
                this.rows = nRows;
                this.columns = nColumns;
                this.rowOffset = rowOffset;
                this.colOffset = colOffset;
            }
        }

        private Node root;
        private Grid grid;
        private Map<String, List<LocationBase>> wordLocations;

        public WordTree(final int nRows, final int nColumns, Grid grid, Map<String, List<LocationBase>> wordLocations) {
            this.root = new Node(nRows, nColumns, 0, 0);
            this.grid = grid;
            this.wordLocations = wordLocations;
        }

        public boolean put(String word) {
            return put(word, root);
        }

        public boolean put(String word, Node node) {
            if (node == null) {
                return false;
            }

            if ((node.rows >= word.length() || node.rows >= word.length()) && (node.rows >0 && node.columns>0)) {
                addWordTree(word, node);
                return true;
            }

            if (put(word, node.left) || put(word, node.right)){
                return true;
            }

            return false;
        }

        private void addWordTree(String word, Node node) {
            char[] chars = word.toCharArray();
            List<LocationBase> locs = new ArrayList<>();

            int[] remainders = {node.rows - chars.length, node.columns - chars.length};
            int orientation = (Math.max(remainders[0], remainders[1]) == node.rows - chars.length) ? 0 : 1;
            int offsetRow = node.rowOffset;
            int offsetCol = node.colOffset;

            int remainRow = orientation==1? node.rows - 1: remainders[0];
            int remainCol = orientation==1? remainders[1]: node.columns - 1;


            if (orientation==1) {//word is layed horizontally
                for (int i=0; i<chars.length; i++) {
                    this.grid.grid[offsetRow+i][offsetCol+1]=chars[i];
                    locs.add(new LocationBase(offsetRow+i, offsetCol+1));
                }

                offsetRow += 1;
                offsetCol += chars.length;

                node.left = new Node(remainRow, chars.length, offsetRow, offsetCol);
                node.right = new Node(node.rows, remainCol, offsetRow, offsetCol);
            }
            else {//orientation==0, vertical over rows
                for (int i=0; i<chars.length; i++) {
                    this.grid.grid[offsetRow+1][offsetCol+i]=chars[i];
                    locs.add(new LocationBase(offsetRow+1, offsetCol+i));
                }

                offsetRow += chars.length;
                offsetCol += 1;

                node.left = new Node(chars.length, remainCol, offsetRow, offsetCol);
                node.right = new Node(remainRow, node.columns, offsetRow, offsetCol);
            }

            this.wordLocations.put(word, locs);

            //case 1: horizontally fits
                //perfectly fits: create left subchild, right with 0,0
                //more room: create left and right subchildren
            //case 2: vertically fits
                //perfectly fits: create left subchild, right with 0,0
                //more room: create left and right subchildren
            //exception that is impossible
        }
    }

    private Grid wordsGrid;
    private Map<String, List<LocationBase>> wordLocations;
    private WordTree wordTree;

    public WordLayout (final int nRows, final int nColumns, final List<String> words) {
        super(nRows, nColumns, words);
        if (nRows <= 0 || nColumns <=0 || words == null || words.isEmpty()) {
            throw new IllegalArgumentException();
        } 

        this.wordsGrid = new Grid(nRows, nColumns);
        this.wordLocations = new HashMap<>();
        this.wordTree = new WordTree(nRows, nColumns, this.wordsGrid, this.wordLocations);

        List<String> entries = new ArrayList<>(words);
        Collections.sort(entries, (a, b)->Integer.compare(a.length(), b.length()));


        for (String word: entries) {
            if (word == null) {
                throw new IllegalArgumentException();
            }
            if (!this.wordTree.put(word)) {
                throw new IllegalArgumentException();
            }//if addword of tree is false throw IllegalArg
        }
    }
      
    public List<LocationBase> locations(final String word) {
        return wordLocations.get(word);
    }
    
    public Grid getGrid() {
        return this.wordsGrid;
    }

    /*private void addWord(String word, int[][] locations) {
        List<LocationBase> locationsList = new ArrayList<>();

            for (int i=0; i<locations.length; i++) {
                //do stuff, add location. (x,y) where prioritize y order
                locationsList.add(new LocationBase(locations[i][0], locations[i][1]));
            }

            this.wordLocations.put(word, locationsList);
    }*/
}
