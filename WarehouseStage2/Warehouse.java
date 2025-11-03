import java.util.*;
import java.io.*;

class Warehouse implements Serializable { // Warehouse class
    private static final long serialVersionUID = 1L;
    private ClientList clientList;
    private ProductList inventory;
    private static int lastProductId = 0;

    private static Warehouse warehouse;

    Warehouse() { // Constructor
        clientList = ClientList.instance();
        inventory = ProductList.instance();
    }

    public static Warehouse instance() {
        if (warehouse == null) {
            ClientIdServer.instance();
            return(warehouse = new Warehouse());
        } else {
            return warehouse;
        }
    }
    
    public Client addClient(String name, String address, String phone) { // Add client to clientlist
        Client c = new Client(name, address, phone);    
        clientList.addClient(c);
        return c;
    }

    public Product addProduct(String name, int quantity, double price) { // Add product to inventory
        Product p = new Product("P" + lastProductId++, name, quantity, price);
        inventory.addProduct(p);
        return p;
    }

    public Client verifyClient(String clientId) { // Verify client is in the warehouse
        Client client = clientList.getClientById(clientId);
        return client;
    }

    public Product verifyProduct(String productId) { // Verify product is in the warehouse
        Product p = inventory.search(productId);
        return p;
    }

    public void receivePayment(Client client, float amount, String date) {
        float clientBalance = client.getBalance();
        // if amount is greater than balance add credit else reduce balance
        if (amount > clientBalance) {
            float difference = (float) (amount - clientBalance);
            // Add credit to client account
            client.setCredit(difference + client.getCredit());
            client.setBalance(0);
        } else {
            float difference = (float) (clientBalance - amount);
            client.setBalance(difference);
        }

        // Record the transaction
        Transaction transaction = new Transaction(client.getId(), date, amount, clientBalance, client.getBalance());
        client.addTransaction(transaction);

        System.out.printf("Payment of $%.2f received from %s %s\n",
                amount, client.getId(),client.getName());
    } 
    
    public void receiveShipment(String productId, int quantity) {
    
        Product product = inventory.search(productId);
        
        if(product == null) {
            System.out.println("ERROR: Product not found");
            return;
        }
        
        processShipment(product, quantity);
        displayShipmentConfirmation(product, quantity);
    }
    
    private void processShipment(Product product, int quantity) {
        int quantityLeft = quantity;
        Waitlist waitlist = product.getWaitlist();
        
        Iterator<WaitlistItem> iterator = waitlist.getWaitlistIterator();
        
        while(iterator.hasNext() && quantityLeft > 0) {
            WaitlistItem item = iterator.next();
            
            if(quantityLeft >= item.getQuantity()) {
                completeWaitlistOrder(item);
                quantityLeft -= item.getQuantity();
                iterator.remove();
            } else {
                break;
            }
        }
        product.addStock(quantityLeft);
    }
    
    private void completeWaitlistOrder(WaitlistItem item) {
        Client client = clientList.getClientById(item.getClientId());
        Product product = item.getProduct();
        
        if (client == null || product == null) {
            System.out.println("Error: Client or Product not found");
            return;
        }
        
        Order order = new Order(client, item);
        client.addOrder(order);
        
        // deduct the order total from clients credit if possible, else add to balance
        if ((client.getBalance() + (float)order.getOrderTotal()) >= client.getCredit()) { // If there is a higher total than credit then deduct credit from total
            client.setBalance(client.getBalance() + (float)order.getOrderTotal() - client.getCredit());
            // set credit to 0
            client.setCredit(0);
        } else { // If there is higher credit than total, then just deduct total from credit
            client.setCredit(client.getCredit() - ( client.getBalance() + (float)order.getOrderTotal()));
            // set balance to 0
            client.setBalance(0);
          }
        
        System.out.println("\nFulfilled order for " + client.getName() + " from waitlist.\n");
        System.out.println(order.toString());
    }
    
    private void displayShipmentConfirmation(Product product, int quantity) {
        System.out.println("\n=== Shipment Received ===");
        System.out.println("Product: " + product.getName());
        System.out.println("Quantity received: " + quantity);
        System.out.println("Current stock: " + product.getStock());
        
        int waitlistCount = 0;
        Iterator<WaitlistItem> iterator = product.getWaitlist().getWaitlistIterator();
        while (iterator.hasNext()) {
            iterator.next();
            waitlistCount++;
        }
        
        System.out.println("Remaining waitlist items: " + waitlistCount);
        System.out.println("========================\n");
    }
    

    public WishlistItem addProductToWishlist(String clientId, String productId, int quantity) {
        
        // Verifiy client and product exist
        Client client = verifyClient(clientId);
        Product product = verifyProduct(productId);
        if (client == null) {
            System.out.println("Client not found");
            return null;
        }
        if (product == null) {
            System.out.println("Product not found");
            return null;
        }
       
        // Create new wishlist item
        WishlistItem item = new WishlistItem(product, quantity);
        client.addToWishlist(item);
        return item;
    
    }
    
    public void removeProductFromWishlist(String clientId, WishlistItem item) {
        Client client = verifyClient(clientId);
        if (client == null) {
            System.out.println("Client not found");
            return;
        }
        Wishlist wishlist = client.getWishlist();
        wishlist.removeProduct(item);
    }
    
    public void updateWishlistItemQuantity(String clientId, WishlistItem item, int quantity) {
        String productId = item.getProduct().getId();
        
        Client client = verifyClient(clientId);
        if (client == null) {
            System.out.println("Client not found");
            return;
        }
        Wishlist wishlist = client.getWishlist();
        wishlist.updateQuantity(productId, quantity);
    }

    public Order processClientOrder(String clientId) {
        Client client = verifyClient(clientId);
        if (client == null) {
            System.out.println("Client not found");
            return null;
        }
        Wishlist wishlist = client.getWishlist();
        Iterator<WishlistItem> iterator = wishlist.getWishlistIterator();
        while (iterator.hasNext()) {
            WishlistItem item = iterator.next();
            Product product = item.getProduct();
            int requestedQuantity = item.getQuantity();
            int remainingQuantity = requestedQuantity - product.getStock();
            if (remainingQuantity > 0) {
                System.out.println("Insufficient stock for product: " + product.getName());
                
                // if no stock is available, remove the item from the order
                if(product.getStock() == 0) {
                    iterator.remove();
                    System.out.println("No stock available, removing item from order and adding entire quantity to waitlist.\n");
                }
                else {
                    System.out.println("Creating partial order for available stock: " + product.getStock());
                    // update the wishlist item quantity to available stock
                    item.setQuantity(product.getStock());
                    System.out.println("\nCreating waitlist item for remaining quantity: " + remainingQuantity);
                }
                
                // create a waitlist item for the remaining quantity and add it to the product's waitlist
                WaitlistItem waitlistItem = new WaitlistItem(product, clientId, remainingQuantity);
                product.addToWaitlist(waitlistItem);
                // reduce product stock to zero
                product.setStock(0);

                System.out.println("Added to waitlist:\n");
                System.out.printf("%-5s %-5s %-20s %-10s %-10s%n",
                    "CID", "PID", "PRODUCT NAME", "PRICE", "QUANTITY");
                System.out.println("================================================================");
                System.out.println(waitlistItem.toString());
                System.out.println("================================================================");
            }
            else {
                // Sufficient stock, reduce product stock
                product.setStock(product.getStock() - requestedQuantity);
            }
        }

        // Dont create an order if wishlist is empty -- all items were waitlisted
        if (wishlist.isEmpty()) {
            System.out.println("All items were waitlisted, no order created.");
            return null;
        }
        else {
          Order order = new Order(client);
          client.addOrder(order);
          // Clear the client's wishlist after processing the order
          client.getWishlist().clear();

          // deduct the order total from clients credit if possible, else add to balance
          if ((client.getBalance() + (float)order.getOrderTotal()) >= client.getCredit()) { // If there is a higher total than credit then deduct credit from total
                client.setBalance(client.getBalance() + (float)order.getOrderTotal() - client.getCredit());
                // set credit to 0
                client.setCredit(0);
          } else { // If there is higher credit than total, then just deduct total from credit
                client.setCredit(client.getCredit() - ( client.getBalance() + (float)order.getOrderTotal() ));
                // set balance to 0
                client.setBalance(0);
          }
          return order;
        }
    }
    
    public Iterator<Product> displayProducts() {
        return inventory.getProducts();
    }

    public Iterator<Client> displayClients() {
        return clientList.getClients();
    }

    public ClientList getClients() {
        return clientList;
    }

    public Iterator<WishlistItem> getClientWishlist(String clientId) {
        Client client = verifyClient(clientId);
        if (client == null) {
            System.out.println("Client not found");
            return null;
        }
        Wishlist wishlist = client.getWishlist();
        return wishlist.getWishlistIterator();
    }

    public Iterator<Order> getClientOrders(Client client) {
        if (client == null) {
            System.out.println("Client not found");
            return null;
        }
        return client.getOrders();
    }

    public Iterator<Transaction> getClientTransactions(Client client) {
        if (client == null) {
            System.out.println("Client not found");
            return null;
        }
        return client.getTransactions();
    }  
    
    public static Warehouse retrieve() {
        try {
          FileInputStream file = new FileInputStream("WarehouseData");
          ObjectInputStream input = new ObjectInputStream(file);
          warehouse = (Warehouse) input.readObject();
          ClientIdServer.retrieve(input);
          return warehouse;
        } catch(IOException ioe) {
          ioe.printStackTrace();
          return null;
        } catch(ClassNotFoundException cnfe) {
          cnfe.printStackTrace();
          return null;
        }
    }
    
    public static  boolean save() {
        try {
          FileOutputStream file = new FileOutputStream("WarehouseData");
          ObjectOutputStream output = new ObjectOutputStream(file);
          output.writeObject(warehouse);
          output.writeObject(ClientIdServer.instance());
          return true;
        } catch(IOException ioe) {
          ioe.printStackTrace();
          return false;
        }
    }
    
    private void writeObject(java.io.ObjectOutputStream output) {
        try {
          output.defaultWriteObject();
          output.writeObject(warehouse);
        } catch(IOException ioe) {
          System.out.println(ioe);
        }
    }
    
    private void readObject(java.io.ObjectInputStream input) {
        try {
          input.defaultReadObject();
          if (warehouse == null) {
            warehouse = (Warehouse) input.readObject();
          } else {
            input.readObject();
          }
        } catch(IOException ioe) {
          ioe.printStackTrace();
        } catch(Exception e) {
          e.printStackTrace();
        }
    }
    
    public String toString() {
        return inventory + "\n" + clientList;
    }
}
