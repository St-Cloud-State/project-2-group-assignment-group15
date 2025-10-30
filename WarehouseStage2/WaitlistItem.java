import java.util.*;
import java.io.*;

public class WaitlistItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private Product product;
    private String clientId;
    private int quantity;

    public WaitlistItem(Product product, String clientId, int quantity) { // Constructor
        this.product = product;
        this.clientId = clientId;
        this.quantity = quantity;
    }

    public Product getProduct() { // Product getter
        return this.product;
    }
    public String getClientId() {
	    return this.clientId;
    }
    
    public int getQuantity() { // Quantity getter
        return this.quantity;
    }

    public void setQuantity(int amount) { // Quantity setter
	   this.quantity = amount;
    }
    public void reduceQuantity(int amount) {
	    this.quantity -= amount;
    }


    @Override
    public String toString() {
    	return String.format("%-5s %-5s %-20s $%-10.2f %-10d",
        clientId,
        product.getId(),
        product.getName(),
        product.getPrice(),
        quantity
    );
}

}
