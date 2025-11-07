import java.io.*;
import java.util.*;
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private String transactionId;
    private String clientId;
    private String date;
    private float amount;
    private double startingBalance;
    private double endingBalance;
    private static final String TRANSACTION_STRING = "T";
    private static int transactionNum = 1;

    public Transaction(String clientId, String date,  float amount, double startingBalance, double endingBalance) {
        this.clientId = clientId;
        this.date = date; 
        this.amount = amount;
        this.startingBalance = startingBalance;
        this.endingBalance = endingBalance;
        this.transactionId = TRANSACTION_STRING + transactionNum++;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getClientId() {
        return clientId;
    }

    public float getAmount() {
        return amount;
    }
    
    public String toString() {
        return String.format(
            "%-5s %-12s %-8s $%-10.2f $%-10.2f $%-10.2f",
            transactionId,
            date,
            clientId,
            amount,
            startingBalance,
            endingBalance
        );
    }

}