package edu.yu.introtoalgs;

public class Linear extends BigOMeasurable {

    private int n;
    
    public Linear() {
    }
    
    public void setup(int n) {
        this.n = n;
    }
    
    // Perform a linear operation
    public void execute() {
        int sum=0;
        for (int i = 0; i < n; i++) {
            sum += i;
        }
        sum-=1;
    }
}

