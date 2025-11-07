import java.util.Iterator;

public class wishlistTester {
    public static void main(String[] s) {
        Client client1 = new Client("Chris", "14951 Waco St", "6128390057");
        Product product1 = new Product("P1","Plate", 10, 20.00);

        WishlistItem item = new WishlistItem(product1, 2);
        client1.addToWishlist(item);
        Wishlist wishlist = client1.getWishlist();

        Iterator<WishlistItem> it = wishlist.getWishlistIterator();

        while (it.hasNext()) {
            WishlistItem wi = it.next();
            int quantity = wi.getQuantity();
            Product p = wi.getProduct();
            System.out.println("Product: " + p.getName() + " Quantity: " + quantity);
            System.out.println(wi.toString());
        }
    }}