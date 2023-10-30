package edu.yu.introtoalgs;

public class SumOfSquares extends BigOMeasurable{
    private long result;
    private int n;

    public SumOfSquares() {
        result = 0;
        n = 0;
    }

    public void setup(int n) {
        this.n = n;
    }

    public void execute() {
        result = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                result += i * j;
            }
        }
    }
}