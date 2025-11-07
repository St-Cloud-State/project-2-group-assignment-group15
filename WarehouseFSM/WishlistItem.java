import java.io.*;

public class WishlistItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private Product product; // declare variables
    private int quantity;

    public WishlistItem(Product product, int quantity) { // Constructor
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() { // Product getter
        return this.product;
    }
    
    public int getQuantity() { // Quantity getter
        return this.quantity;
    }

    public void setQuantity(int amount) { // Quantity setter
        this.quantity = amount;
    }

    public String toString() {
    return String.format(
        "%-5s %-20s $%-10.2f %-10d",
        product.getId(),
        product.getName(),
        product.getPrice(),
        quantity
    );
}

}
