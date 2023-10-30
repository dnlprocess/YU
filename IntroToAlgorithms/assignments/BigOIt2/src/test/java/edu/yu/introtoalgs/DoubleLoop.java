package edu.yu.introtoalgs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DoubleLoop extends BigOMeasurable{
        int[][] data;
        List<Integer> list;
        Random rand = new Random();


        public DoubleLoop() {
            this.list = new ArrayList<>();
        }

        public void setup (int n) {
            this.data = new int[n][n];
            for (int i = 0; i < this.data.length; i++) {
                for (int j = 0; j < this.data[i].length; j++) {
                    data[i][j] = rand.nextInt();
                }
            }
        }

        public void execute() {
            for (int i=0; i<this.data.length; i++) {
                for (int j=0; j<this.data[i].length; j++) {
                    if (this.data[i][j] > this.data[j][i]) {
                        list.add(this.data[i][j]);
                    }
                }
            }
        }
    }