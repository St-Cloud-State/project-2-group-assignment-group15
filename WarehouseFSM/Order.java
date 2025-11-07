import java.io.*;
import java.util.*;
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String clientId;
    private Wishlist items = new Wishlist();
    private WaitlistItem waitlistItem;
    private double orderTotal;
    private static final String ORDER_STRING = "O";
    private static int orderNum = 1;
  
    // Constructor for normal orders
    public Order(Client client) {
        clientId = client.getId();
        Wishlist wishlist = client.getWishlist();
        Iterator<WishlistItem> iterator = wishlist.getWishlistIterator();
        while (iterator.hasNext()) {
            WishlistItem item = iterator.next();
            items.addProduct(item);
            orderTotal += item.getProduct().getPrice() * item.getQuantity();
        }   
        orderId = ORDER_STRING + orderNum++;
    }
    // Constructor for waitlist orders
    public Order(Client client, WaitlistItem waitlistItem) {
        clientId = client.getId();
        this.waitlistItem = waitlistItem;
        orderTotal += waitlistItem.getProduct().getPrice() * waitlistItem.getQuantity();
        orderId = ORDER_STRING + orderNum++;
    }
    public String getOrderId() {
        return orderId;
    }

    public String getClientId() {
        return clientId;
    }

    public Wishlist getItems() {
        return items;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Order ID: %-5s   Client ID: %-5s   Total: $%-8.2f%n", 
            orderId, clientId, orderTotal));
        sb.append("------------------------------------------------------------\n");
        sb.append(String.format("%-5s %-20s %-10s %-10s%n", 
            "ID", "PRODUCT NAME", "PRICE", "QUANTITY"));
        sb.append("------------------------------------------------------------\n");

        if (items.isEmpty()) {
            // If it's a waitlist order, display the waitlist item
            Product p = waitlistItem.getProduct();
            sb.append(String.format(
                "%-5s %-20s $%-9.2f %-10d%n",
                p.getId(),
                p.getName(),
                p.getPrice(),
                waitlistItem.getQuantity()
            ));
        }
        else {
            // If it's a normal order, display the wishlist items
            Iterator<WishlistItem> iterator = items.getWishlistIterator();
            while (iterator.hasNext()) {
                WishlistItem item = iterator.next();
                Product p = item.getProduct();
                sb.append(String.format(
                    "%-5s %-20s $%-9.2f %-10d%n",
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    item.getQuantity()
                ));
            }
        }

        sb.append("------------------------------------------------------------\n");
        return sb.toString();
    }

}
