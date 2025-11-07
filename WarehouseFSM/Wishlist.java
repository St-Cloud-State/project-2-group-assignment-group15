import java.util.*;
import java.io.*;

public class Wishlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<WishlistItem> wishlist = new LinkedList<>(); // Declare variable

    public Wishlist() { // Constructor
       
    }

    public void addProduct(WishlistItem item) { // Add a product to wishlist
        Iterator<WishlistItem> iterator = this.wishlist.iterator();
        while (iterator.hasNext()) {
            WishlistItem candidate = iterator.next();
            if (candidate.equals(item)) {
                candidate.setQuantity(candidate.getQuantity() + item.getQuantity());
                return;
            }
        }
        this.wishlist.add(item);
    }

    public void removeProduct(WishlistItem item) { // Remove a product from the wishlist
        this.wishlist.remove(item);
    }

    public void updateQuantity(String productId, int quantity) { // Update the quantity of a wishlistItem
        ListIterator<WishlistItem> iterator = this.wishlist.listIterator(); // iterator
        WishlistItem candidate;
        Product candidateProduct;
        String candidateId;

        while (iterator.hasNext()) { // Iterate through the list
            candidate = iterator.next();
            candidateProduct = candidate.getProduct();
            candidateId = candidateProduct.getId();
            if (candidateId.equals(productId)) {
                candidate.setQuantity(quantity);
            }
        }
    }

    public Iterator<WishlistItem> getWishlistIterator() {
        return this.wishlist.iterator();
    }


    public boolean isEmpty() {
        return this.wishlist.isEmpty();
    }

    public void clear() {
        this.wishlist.clear();
    }   
}