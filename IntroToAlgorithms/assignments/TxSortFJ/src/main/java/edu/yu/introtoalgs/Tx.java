package edu.yu.introtoalgs;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import edu.yu.introtoalgs.Account;
import edu.yu.introtoalgs.TxBase;

public class Tx extends TxBase {

    private Account sender;
    private Account receiver;
    private int amount;
    private LocalDateTime time;
    private final int txId;
    private static AtomicInteger nextTxId = new AtomicInteger(1);

    public Tx(Account sender, Account receiver, int amount) {
        super(sender, receiver, amount);
        if (sender==null || receiver==null || amount<=0) {
            throw new IllegalArgumentException();
        }

        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.time = LocalDateTime.now();
        this.txId = nextTxId.getAndIncrement();
    }
    
    public Account receiver() {
        return this.receiver;
    }

    public Account sender() {
        return this.sender;
    }

    public int amount() {
        return this.amount;
    }

    /** Returns a unique non-negative identifier.
     */
    public long id() {
        return this.txId;
    }

    /** Returns the time that the Tx was created or null.
     */
    public LocalDateTime time() {
        return this.time;
    }

    /** Returns the time that the Tx was created or null.
     */
    public void setTimeToNull() {
        this.time = null;
    }

    @Override
     public int compareTo(TxBase other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        return (int) ((int) this.id()-other.id());
        
		/*if (this.time == null && other.time() == null) {
            return 0;
        } else if (this.time == null) {
            return -1;
        } else if (other.time() == null) {
            return 1;
        }

        return this.time.compareTo(other.time());*/
    }
}
